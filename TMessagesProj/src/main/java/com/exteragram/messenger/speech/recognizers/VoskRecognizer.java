package com.exteragram.messenger.speech.recognizers;

import android.text.TextUtils;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.speech.VoiceRecognitionController;
import com.exteragram.messenger.speech.utils.FormatConverter;
import com.exteragram.messenger.utils.network.ExteraHttpClient;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.android.RecognitionListener;
import org.vosk.android.SpeechStreamService;

public class VoskRecognizer implements VoiceRecognitionController.RecognitionProvider, AutoCloseable {
    private static final Gson gson = new Gson();
    private final OkHttpClient client = ExteraHttpClient.INSTANCE.getClient();
    private final File modelsDir = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), "Vosk Models");
    private final Map<String, Model> loadedModels = new ConcurrentHashMap<>();
    private final List<VoiceRecognitionController.RecognitionModel> models = new ArrayList<VoiceRecognitionController.RecognitionModel>() {{
        add(new VoiceRecognitionController.RecognitionModel("ca", "https://alphacephei.com/vosk/models/vosk-model-small-ca-0.4.zip", 43405881L));
        add(new VoiceRecognitionController.RecognitionModel("cs", "https://alphacephei.com/vosk/models/vosk-model-small-cs-0.4-rhasspy.zip", 46088666L));
        add(new VoiceRecognitionController.RecognitionModel("de", "https://alphacephei.com/vosk/models/vosk-model-small-de-0.15.zip", 46499967L));
        add(new VoiceRecognitionController.RecognitionModel("en", "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip", 41205931L));
        add(new VoiceRecognitionController.RecognitionModel("eo", "https://alphacephei.com/vosk/models/vosk-model-small-eo-0.42.zip", 43839401L));
        add(new VoiceRecognitionController.RecognitionModel("es", "https://alphacephei.com/vosk/models/vosk-model-small-es-0.42.zip", 39817833L));
        add(new VoiceRecognitionController.RecognitionModel("fa", "https://alphacephei.com/vosk/models/vosk-model-small-fa-0.5.zip", 62153530L));
        add(new VoiceRecognitionController.RecognitionModel("fr", "https://alphacephei.com/vosk/models/vosk-model-small-fr-0.22.zip", 42233323L));
        add(new VoiceRecognitionController.RecognitionModel("gu", "https://alphacephei.com/vosk/models/vosk-model-small-gu-0.42.zip", 108054987L));
        add(new VoiceRecognitionController.RecognitionModel("hi", "https://alphacephei.com/vosk/models/vosk-model-small-hi-0.22.zip", 44458845L));
        add(new VoiceRecognitionController.RecognitionModel("it", "https://alphacephei.com/vosk/models/vosk-model-small-it-0.22.zip", 49665141L));
        add(new VoiceRecognitionController.RecognitionModel("ja", "https://alphacephei.com/vosk/models/vosk-model-small-ja-0.22.zip", 49704573L));
        add(new VoiceRecognitionController.RecognitionModel("kk", "https://alphacephei.com/vosk/models/vosk-model-small-kz-0.15.zip", 43739114L));
        add(new VoiceRecognitionController.RecognitionModel("ko", "https://alphacephei.com/vosk/models/vosk-model-small-ko-0.22.zip", 86914329L));
        add(new VoiceRecognitionController.RecognitionModel("nl", "https://alphacephei.com/vosk/models/vosk-model-small-nl-0.22.zip", 40441176L));
        add(new VoiceRecognitionController.RecognitionModel("pl", "https://alphacephei.com/vosk/models/vosk-model-small-pl-0.22.zip", 52979372L));
        add(new VoiceRecognitionController.RecognitionModel("pt", "https://alphacephei.com/vosk/models/vosk-model-small-pt-0.3.zip", 32453112L));
        add(new VoiceRecognitionController.RecognitionModel("ru", "https://alphacephei.com/vosk/models/vosk-model-small-ru-0.22.zip", 46236750L));
        add(new VoiceRecognitionController.RecognitionModel("tg", "https://alphacephei.com/vosk/models/vosk-model-small-tg-0.22.zip", 51879043L));
        add(new VoiceRecognitionController.RecognitionModel("tr", "https://alphacephei.com/vosk/models/vosk-model-small-tr-0.3.zip", 36855784L));
        add(new VoiceRecognitionController.RecognitionModel("uk", "https://alphacephei.com/vosk/models/vosk-model-small-uk-v3-nano.zip", 77622640L));
        add(new VoiceRecognitionController.RecognitionModel("uz", "https://alphacephei.com/vosk/models/vosk-model-small-uz-0.22.zip", 51061189L));
        add(new VoiceRecognitionController.RecognitionModel("vi", "https://alphacephei.com/vosk/models/vosk-model-small-vn-0.4.zip", 33656337L));
        add(new VoiceRecognitionController.RecognitionModel("zh", "https://alphacephei.com/vosk/models/vosk-model-small-cn-0.22.zip", 43898754L));
    }};

    private void unpackZip(String zipFile, String targetDirectory) throws IOException {
        File targetDir = new File(targetDirectory);
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
        byte[] buffer = new byte[1024];
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        String rootDir = null;
        while (nextEntry != null) {
            String name = nextEntry.getName();
            if (rootDir == null) {
                rootDir = name;
            }
            if (name.startsWith(rootDir)) {
                name = name.substring(rootDir.length());
            }
            File file = new File(targetDir, name);
            if (nextEntry.isDirectory()) {
                file.mkdirs();
            } else {
                new File(file.getParent()).mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while (true) {
                    int read = zipInputStream.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, read);
                }
                fileOutputStream.close();
            }
            zipInputStream.closeEntry();
            nextEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    @Override
    public List<VoiceRecognitionController.RecognitionModel> listAvailableModels() {
        return this.models;
    }

    @Override
    public List<VoiceRecognitionController.RecognitionModel> listDownloadedModels() {
        List<VoiceRecognitionController.RecognitionModel> downloaded = new ArrayList<>();
        for (VoiceRecognitionController.RecognitionModel model : this.models) {
            File file = new File(this.modelsDir, model.getLanguage());
            if (file.exists() && !new File(file, "model.zip").exists() && !isDirectoryEmpty(file)) {
                downloaded.add(model);
            }
        }
        return downloaded;
    }

    private boolean isDirectoryEmpty(File file) {
        String[] list = file.list();
        return list == null || list.length == 0;
    }

    @Override
    public void downloadModel(final String language, VoiceRecognitionController.DownloadModelCallback downloadModelCallback) {
        VoiceRecognitionController.RecognitionModel recognitionModel = null;
        for (VoiceRecognitionController.RecognitionModel model : this.models) {
            if (model.getLanguage().equals(language)) {
                recognitionModel = model;
                break;
            }
        }
        if (recognitionModel == null) {
            downloadModelCallback.onError(new IllegalArgumentException("Model not found: " + language));
            return;
        }

        File file = new File(this.modelsDir, recognitionModel.getLanguage());
        if (new File(file, "model.zip").exists()) {
            try {
                deleteDirectory(file);
            } catch (IOException e) {
                downloadModelCallback.onError(new IOException("Failed to delete existing model directory", e));
                return;
            }
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            Response response = this.client.newCall(new Request.Builder().url(recognitionModel.getUrl()).build()).execute();
            if (!response.isSuccessful()) {
                FileLog.e("Failed to download: " + response);
            }
            File file2 = new File(file, "model.zip");
            try (InputStream inputStream = response.body().byteStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(file2)) {
                long jContentLength = response.body().contentLength();
                byte[] bArr = new byte[4096];
                int read;
                while ((read = inputStream.read(bArr)) != -1) {
                    fileOutputStream.write(bArr, 0, read);
                    if (jContentLength > 0) {
                        downloadModelCallback.onProgress((float) file2.length() / jContentLength);
                    }
                }
            }
            unpackZip(file2.getAbsolutePath(), file.getAbsolutePath());
            try {
                if (!file2.delete()) {
                    file2.deleteOnExit();
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            downloadModelCallback.onCompleted();
        } catch (Exception e3) {
            downloadModelCallback.onError(e3);
        }
    }

    private void deleteDirectory(File file) throws IOException {
        File[] files = file.listFiles();
        if (file.isDirectory() && files != null) {
            for (File child : files) {
                deleteDirectory(child);
            }
        }
        if (!file.delete()) {
            FileLog.e("Failed to delete file or directory: " + file.getAbsolutePath());
        }
    }

    @Override
    public void recognize(String path, final String language, final VoiceRecognitionController.RecognitionCallback recognitionCallback) {
        VoiceRecognitionController.RecognitionModel supportedModel = null;
        for (VoiceRecognitionController.RecognitionModel model : this.models) {
            if (model.getLanguage().equals(language)) {
                supportedModel = model;
                break;
            }
        }
        if (supportedModel == null) {
            recognitionCallback.onLanguageNotSupported(language);
            return;
        }

        boolean downloaded = false;
        for (VoiceRecognitionController.RecognitionModel model : listDownloadedModels()) {
            if (model.getLanguage().equals(language)) {
                downloaded = true;
                break;
            }
        }
        if (!downloaded) {
            recognitionCallback.onLanguageNotDownloaded(language);
            return;
        }

        try {
            if (!this.loadedModels.containsKey(language)) {
                FileLog.d("Loading model: " + language);
                this.loadedModels.put(language, new Model(new File(this.modelsDir, language).getAbsolutePath()));
                FileLog.d("Model loaded: " + language);
            }
            Model model = this.loadedModels.get(language);
            InputStream pcmStream = FormatConverter.extractAndConvertToPcm(path, false);
            int sampleRate = FormatConverter.getSampleRate(path);
            FileLog.d("Recognizing: " + path);
            float f = (float) sampleRate;
            final Recognizer recognizer = new Recognizer(model, f);
            new SpeechStreamService(recognizer, pcmStream, f).start(new RecognitionListener() {
                @Override
                public void onPartialResult(String result) {
                }

                @Override
                public void onTimeout() {
                }

                @Override
                public void onResult(String result) {
                    FileLog.d("Result: " + result);
                    if (TextUtils.isEmpty(result)) {
                        return;
                    }
                    try {
                        Map<?, ?> map = gson.fromJson(result, Map.class);
                        String text = (String) map.get("text");
                        if (!TextUtils.isEmpty(text)) {
                            recognitionCallback.onChunk(text);
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }

                @Override
                public void onFinalResult(String result) {
                    FileLog.d("Final result: " + result);
                    try {
                        Map<?, ?> map = gson.fromJson(result, Map.class);
                        String text = (String) map.get("text");
                        recognitionCallback.onCompleted(text != null ? text : "");
                    } catch (Exception e) {
                        recognitionCallback.onCompleted("");
                    }
                    recognizer.close();
                }

                @Override
                public void onError(Exception exc) {
                    FileLog.e("Failed to recognize", exc);
                    recognitionCallback.onError(exc);
                    recognizer.close();
                }
            });
        } catch (IOException e) {
            FileLog.e("Failed to recognize", e);
            recognitionCallback.onError(e);
        }
    }

    @Override
    public void unloadModels() {
        for (Model model : this.loadedModels.values()) {
            try {
                model.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        this.loadedModels.clear();
    }

    @Override
    public void close() {
        unloadModels();
    }
}
