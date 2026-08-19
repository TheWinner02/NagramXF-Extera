package com.exteragram.messenger.speech;

import android.text.TextUtils;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.speech.recognizers.VoskRecognizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.telegram.messenger.FileLog;

public class VoiceRecognitionController {
    private final Map<String, List<String>> chunkCache = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final AtomicLong lastRecognitionTime = new AtomicLong(System.currentTimeMillis());
    private final Map<String, RecognitionProvider> providers = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock providersLock = new ReentrantReadWriteLock();
    private final Map<String, RecognitionResult> resultCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> unloadTask;

    public interface DownloadModelCallback {
        void onCompleted();
        void onError(Exception exc);
        void onProgress(float f);
    }

    public interface RecognitionCallback {
        void onChunk(String str);
        void onCompleted(String str);
        void onError(Exception exc);
        void onLanguageNotDownloaded(String str);
        void onLanguageNotSupported(String str);
    }

    public interface RecognitionProvider {
        void downloadModel(String str, DownloadModelCallback downloadModelCallback);
        List<RecognitionModel> listAvailableModels();
        List<RecognitionModel> listDownloadedModels();
        void recognize(String str, String str2, RecognitionCallback recognitionCallback);
        void unloadModels();
    }

    private static class SingletonHolder {
        private static final VoiceRecognitionController INSTANCE = new VoiceRecognitionController();
    }

    private VoiceRecognitionController() {
        providers.put("vosk", new VoskRecognizer());
        startUnloadTask();
    }

    public static VoiceRecognitionController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static boolean isCustomRecognitionEnabled() {
        return !Objects.equals(ExteraConfig.recognitionLanguage, "none");
    }

    private void startUnloadTask() {
        this.unloadTask = this.scheduledExecutorService.scheduleWithFixedDelay(
                this::checkAndUnloadInactiveModels, 1L, 1L, TimeUnit.MINUTES);
    }

    private void updateLastRecognitionTime() {
        this.lastRecognitionTime.set(System.currentTimeMillis());
    }

    public void checkAndUnloadInactiveModels() {
        if (System.currentTimeMillis() - this.lastRecognitionTime.get() > 600000) {
            this.providersLock.writeLock().lock();
            try {
                for (RecognitionProvider provider : this.providers.values()) {
                    provider.unloadModels();
                }
                FileLog.d("Unloaded models due to inactivity");
            } finally {
                this.providersLock.writeLock().unlock();
            }
        }
    }

    public String key(Long l, int i) {
        return l + "_" + i;
    }

    public List<RecognitionModel> listAvailableModels(String providerName) {
        this.providersLock.readLock().lock();
        try {
            RecognitionProvider recognitionProvider = this.providers.get(providerName);
            if (recognitionProvider == null) {
                throw new IllegalArgumentException("Provider not found: " + providerName);
            }
            return recognitionProvider.listAvailableModels();
        } finally {
            this.providersLock.readLock().unlock();
        }
    }

    public List<RecognitionModel> listDownloadedModels(String providerName) {
        this.providersLock.readLock().lock();
        try {
            RecognitionProvider recognitionProvider = this.providers.get(providerName);
            if (recognitionProvider == null) {
                throw new IllegalArgumentException("Provider not found: " + providerName);
            }
            return recognitionProvider.listDownloadedModels();
        } finally {
            this.providersLock.readLock().unlock();
        }
    }

    public void downloadModel(String providerName, final String language, final DownloadModelCallback downloadModelCallback) {
        this.providersLock.readLock().lock();
        try {
            final RecognitionProvider recognitionProvider = this.providers.get(providerName);
            if (recognitionProvider == null) {
                throw new IllegalArgumentException("Provider not found: " + providerName);
            }
            this.executorService.submit(() -> {
                try {
                    recognitionProvider.downloadModel(language, downloadModelCallback);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            });
        } finally {
            this.providersLock.readLock().unlock();
        }
    }

    public void startRecognition(final String key, final String path, final String language, final String providerName, final RecognitionCallback recognitionCallback) {
        this.executorService.submit(() -> {
            this.providersLock.readLock().lock();
            try {
                RecognitionProvider recognitionProvider = this.providers.get(providerName);
                if (recognitionProvider == null) {
                    throw new IllegalArgumentException("Provider not found: " + providerName);
                }
                this.providersLock.readLock().unlock();
                updateLastRecognitionTime();
                List<String> chunks = new ArrayList<>();
                this.chunkCache.put(key, chunks);
                try {
                    recognitionProvider.recognize(path, language, new RecognitionCallback() {
                        @Override
                        public void onChunk(String text) {
                            if (!text.isEmpty()) {
                                chunks.add(text);
                            }
                            recognitionCallback.onChunk(TextUtils.join(" ", chunks));
                        }

                        @Override
                        public void onCompleted(String text) {
                            if (!text.isEmpty()) {
                                chunks.add(text);
                            }
                            String finalText = TextUtils.join(" ", chunks);
                            resultCache.put(key, new RecognitionResult(finalText));
                            chunkCache.remove(key);
                            recognitionCallback.onCompleted(finalText);
                        }

                        @Override
                        public void onError(Exception exc) {
                            recognitionCallback.onError(exc);
                        }

                        @Override
                        public void onLanguageNotDownloaded(String lang) {
                            recognitionCallback.onLanguageNotDownloaded(lang);
                        }

                        @Override
                        public void onLanguageNotSupported(String lang) {
                            recognitionCallback.onLanguageNotSupported(lang);
                        }
                    });
                } catch (Exception e) {
                    recognitionCallback.onError(e);
                }
            } catch (Throwable th) {
                this.providersLock.readLock().unlock();
                throw th;
            }
        });
    }

    public boolean isRecognizing(Long dialogId, int messageId) {
        return this.chunkCache.containsKey(key(dialogId, messageId));
    }

    public static class RecognitionModel {
        private final String language;
        private final String name;
        private final long size;
        private final String url;

        public RecognitionModel(String language, String url, long size) {
            String display = new Locale(language).getDisplayLanguage();
            if (TextUtils.isEmpty(display)) {
                display = "Language: " + language;
            }
            this.name = display;
            this.language = language;
            this.url = url;
            this.size = size;
        }

        public String getLanguage() {
            return this.language;
        }

        public String getUrl() {
            return this.url;
        }

        public long getSize() {
            return this.size;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class RecognitionResult {
        private final String text;
        private final long timestamp = System.currentTimeMillis();

        public RecognitionResult(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }
}
