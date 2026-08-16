package com.exteragram.messenger.plugins;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import androidx.core.content.FileProvider;
import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.plugins.hooks.PluginsHooks;
import com.exteragram.messenger.plugins.models.CustomSetting;
import com.exteragram.messenger.plugins.models.DividerSetting;
import com.exteragram.messenger.plugins.models.EditTextSetting;
import com.exteragram.messenger.plugins.models.HeaderSetting;
import com.exteragram.messenger.plugins.models.InputSetting;
import com.exteragram.messenger.plugins.models.SelectorSetting;
import com.exteragram.messenger.plugins.models.SettingItem;
import com.exteragram.messenger.plugins.models.SwitchSetting;
import com.exteragram.messenger.plugins.models.TextSetting;
import com.exteragram.messenger.plugins.pip.PipController;
import com.exteragram.messenger.plugins.ui.PluginSettingsActivity;
import com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet;
import com.exteragram.messenger.plugins.ui.components.PluginFileViewer;
import com.exteragram.messenger.plugins.utils.PyObjectUtils;
import com.exteragram.messenger.utils.AppUtils;
import com.exteragram.messenger.utils.network.RemoteUtils;
import com.exteragram.messenger.utils.text.LocaleUtils;
import com.google.android.gms.cast.MediaError;
import com.sun.jna.Callback;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlin.time.DurationKt;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.simplifiles.SimpliFiles;
import org.simplifiles.archive.ArchiveExtractionOptions;
import org.simplifiles.archive.ExtractionTargetPolicy;
import org.simplifiles.archive.security.SecurityPolicy;
import org.simplifiles.files.OverwritePolicy;
import org.simplifiles.files.SimpliFile;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nPythonPluginsEngine.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PythonPluginsEngine.kt\ncom/exteragram/messenger/plugins/PythonPluginsEngine\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 SharedPreferences.kt\nandroidx/core/content/SharedPreferencesKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,2775:1\n1#2:2776\n41#3,12:2777\n41#3,12:2795\n41#3,12:2810\n41#3,12:2822\n41#3,12:2834\n1300#4,2:2789\n1315#4,4:2791\n777#4:2807\n873#4,2:2808\n1586#4:2846\n1661#4,3:2847\n296#4,2:2850\n1586#4:2852\n1661#4,3:2853\n777#4:2856\n873#4,2:2857\n1834#4,4:2859\n37#5,2:2863\n*S KotlinDebug\n*F\n+ 1 PythonPluginsEngine.kt\ncom/exteragram/messenger/plugins/PythonPluginsEngine\n*L\n502#1:2777,12\n792#1:2795,12\n1039#1:2810,12\n1044#1:2822,12\n1059#1:2834,12\n752#1:2789,2\n752#1:2791,4\n814#1:2807\n814#1:2808,2\n1281#1:2846\n1281#1:2847,3\n1282#1:2850,2\n1400#1:2852\n1400#1:2853,3\n1401#1:2856\n1401#1:2857,2\n669#1:2859,4\n2093#1:2863,2\n*E\n"})
public final class PythonPluginsEngine implements PluginsController.PluginsEngine {
    private static final long MAX_SDK_VERSION_BYTES = 65536;
    private static boolean SDK_BETA;
    private static File SDK_DIR;
    private static String SDK_VERSION;
    private static volatile boolean sdkInitialized;
    private volatile PyObject basePluginClass;
    private PyObject debuggerListener;
    private PyObject devServerClass;
    private volatile Python python;
    private static final String SAFE_MODE_ENABLE_ERROR = Deobfuscator$exteraGramDev$TMessagesProj.getString(-104601650218543L);

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Pattern VERSION_PATTERN = Pattern.compile(Deobfuscator$exteraGramDev$TMessagesProj.getString(-105374744331823L));
    private static final String[] SDK_REQUIRED_MODULES = {Deobfuscator$exteraGramDev$TMessagesProj.getString(-105542248056367L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-105039736882735L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-105022557013551L)};
    private static final SecurityPolicy SDK_ARCHIVE_POLICY = SecurityPolicy.INSTANCE.builder().maxEntries(100000).maxTotalUncompressedSize(2147483648L).maxSingleFileSize(536870912).maxCompressionRatio(500.0d).build();
    private static final ArchiveExtractionOptions SDK_ARCHIVE_OPTIONS = ArchiveExtractionOptions.INSTANCE.builder().targetPolicy(ExtractionTargetPolicy.REPLACE).build();
    private final ConcurrentHashMap<String, PyObject> pluginInstances = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> settingsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap.KeySetView<String, Boolean> dependencyPaths = ConcurrentHashMap.newKeySet();

    public interface PyMethodCaller<T> {
        PyObject call(PyObject instance, T value);
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public boolean canOpenInExternalApp() {
        return true;
    }

    @SourceDebugExtension({"SMAP\nPythonPluginsEngine.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PythonPluginsEngine.kt\ncom/exteragram/messenger/plugins/PythonPluginsEngine$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,2775:1\n1#2:2776\n*E\n"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String getSDK_VERSION() {
            return PythonPluginsEngine.SDK_VERSION;
        }

        public final void setSDK_VERSION(String str) {
            PythonPluginsEngine.SDK_VERSION = str;
        }

        public final boolean getSDK_BETA() {
            return PythonPluginsEngine.SDK_BETA;
        }

        public final void setSDK_BETA(boolean z) {
            PythonPluginsEngine.SDK_BETA = z;
        }

        private final boolean sdkModuleExists(File sdkDir, String moduleName) {
            if (new File(sdkDir, moduleName + Deobfuscator$exteraGramDev$TMessagesProj.getString(-131763023398447L)).exists()) {
                return true;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(moduleName);
            sb.append(Deobfuscator$exteraGramDev$TMessagesProj.getString(-131848922744367L));
            return new File(sdkDir, sb.toString()).exists();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void deleteFileIfExists(File file) {
            if (file != null && file.exists()) {
                file.delete();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean isSdkDirValid(File sdkDir) {
            if (sdkDir == null || !sdkDir.isDirectory()) {
                return false;
            }
            for (String str : PythonPluginsEngine.SDK_REQUIRED_MODULES) {
                if (!sdkModuleExists(sdkDir, str)) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String canonicalPathOrNull(String path) {
            if (path == null || path.length() == 0) {
                return null;
            }
            try {
                return new File(path).getCanonicalPath();
            } catch (Throwable unused) {
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Set<String> topLevelModuleNames(File dir) {
            File[] fileArrListFiles;
            if (dir == null || (fileArrListFiles = dir.listFiles()) == null) {
                return SetsKt.emptySet();
            }
            HashSet hashSet = new HashSet();
            for (File file : fileArrListFiles) {
                String name = file.getName();
                if (file.isDirectory()) {
                    if (!name.equals("__pycache__") && !name.endsWith(".dist-info") && !name.endsWith(".egg-info")) {
                        hashSet.add(name);
                    }
                } else if (name.endsWith(".py") || name.endsWith(".pyc") || name.endsWith(".so")) {
                    int dotIdx = name.indexOf('.');
                    hashSet.add(dotIdx != -1 ? name.substring(0, dotIdx) : name);
                }
            }
            return hashSet;
        }
    }

    public final ConcurrentHashMap<String, PyObject> getPluginInstances() {
        return this.pluginInstances;
    }

    public final PyObject getDebuggerListener() {
        return this.debuggerListener;
    }

    public final void setDebuggerListener(PyObject pyObject) {
        this.debuggerListener = pyObject;
    }

    public final PyObject getBasePluginClass() {
        return this.basePluginClass;
    }

    public final void setBasePluginClass(PyObject pyObject) {
        this.basePluginClass = pyObject;
    }

    private final PluginsController getPluginsController() {
        return PluginsController.INSTANCE.getInstance();
    }

    private final synchronized Python getPython() {
        if (this.python == null) {
            initPython();
            if (this.python == null) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-137183272125999L));
                return null;
            }
        }
        return this.python;
    }

    private final void initPython() {
        try {
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "initPython: starting Python.start...");
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(ApplicationLoader.applicationContext));
            }
            this.python = Python.getInstance();
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "initPython: Python.start completed! python=" + this.python);
        } catch (Exception e) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "initPython error: " + e.getMessage(), e);
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-137402315458095L), e);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public boolean isPlugin(File file, MessageObject messageObject) {
        if (file != null) {
            String name = file.getName().toLowerCase(Locale.ROOT);
            if (name.endsWith(".plugin") || name.endsWith(".py")) {
                return true;
            }
        }
        if (messageObject != null && messageObject.getDocumentName() != null) {
            String docName = messageObject.getDocumentName().toLowerCase(Locale.ROOT);
            if (docName.endsWith(".plugin") || docName.endsWith(".py")) {
                return true;
            }
        }
        return false;
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public boolean isEngineAvailable() {
        return this.python != null && sdkInitialized && this.basePluginClass != null;
    }

    private final synchronized PyObject requireBasePluginClass() {
        try {
            if (ExteraConfig.getPluginsSafeMode()) {
                throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-135555479520815L));
            }
            if (!sdkInitialized) {
                android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "requireBasePluginClass: sdkInitialized is false, attempting initSdk()...");
                initSdk();
            }
            if (!sdkInitialized) {
                throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-135778817820207L));
            }
            PyObject pyObject = this.basePluginClass;
            if (pyObject != null) {
                return pyObject;
            }
            Python python = getPython();
            if (python == null) {
                throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-135310666384943L));
            }
            try {
                PyObject pyObject2 = (PyObject) python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-135473875142191L)).get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-136075170563631L));
                if (pyObject2 != null) {
                    this.basePluginClass = pyObject2;
                    return pyObject2;
                }
                throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-136199724615215L));
            } catch (Throwable th) {
                android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "requireBasePluginClass getModule error: " + th.getMessage(), th);
                throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-135856127231535L), th);
            }
        } catch (Throwable th2) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "requireBasePluginClass exception: " + th2.getMessage(), th2);
            throw new RuntimeException(th2);
        }
    }

    private final void installSdkArchive(File archiveFile, boolean fromApk) {
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "installSdkArchive: archiveFile=" + (archiveFile != null ? archiveFile.getAbsolutePath() : "null") + ", exists=" + (archiveFile != null && archiveFile.exists()));
        File file = SDK_DIR;
        if (file == null || archiveFile == null || !archiveFile.exists()) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "installSdkArchive: archiveFile or SDK_DIR is null/non-existent!");
            return;
        }
        File file2 = new File(file.getParentFile(), "sdk_temp");
        try {
            Updater.INSTANCE.setBuildFromApk(fromApk);
            if (file2.exists()) {
                file2.delete();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "installSdkArchive: Unzipping SDK archive to " + file.getAbsolutePath());
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(archiveFile))) {
                ZipEntry entry;
                byte[] buffer = new byte[8192];
                while ((entry = zis.getNextEntry()) != null) {
                    File outFile = new File(file, entry.getName());
                    if (entry.isDirectory()) {
                        outFile.mkdirs();
                    } else {
                        File parent = outFile.getParentFile();
                        if (parent != null && !parent.exists()) {
                            parent.mkdirs();
                        }
                        try (FileOutputStream fos = new FileOutputStream(outFile)) {
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                    }
                    zis.closeEntry();
                }
            }
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "installSdkArchive: Successfully extracted SDK archive to " + file.getAbsolutePath());
        } catch (Exception e) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "installSdkArchive error: " + e.getMessage(), e);
            FileLog.e(e);
        }
    }

    private final boolean initSdk() {
        String str;
        Boolean bool;
        sdkInitialized = false;
        Python python = getPython();
        if (python == null) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "initSdk: getPython() is null!");
            return false;
        }
        if (SDK_DIR == null) {
            File file = new File(new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-134258399397423L)), Deobfuscator$exteraGramDev$TMessagesProj.getString(-134219744691759L));
            SDK_DIR = file;
            if (!file.exists()) {
                File file2 = SDK_DIR;
                if (file2 == null) {
                    return false;
                }
                SimpliFiles.directory(file2).create();
            }
        }
        File file3 = SDK_DIR;
        if (file3 == null) {
            return false;
        }
        Updater.Companion companion = Updater.INSTANCE;
        File fileRequestSdkFromApkFile = companion.requestSdkFromApkFile();
        File pythonSdkUpdateFile = companion.getPythonSdkUpdateFile();
        File pythonCurrentSdkFile = companion.getPythonCurrentSdkFile();
        boolean zExists = fileRequestSdkFromApkFile.exists();
        if (zExists) {
            SimpliFiles.directory(file3).clean();
            INSTANCE.deleteFileIfExists(fileRequestSdkFromApkFile);
        }
        boolean z = true;
        if (!zExists && pythonSdkUpdateFile.exists()) {
            try {
                SimpliFiles.file(pythonSdkUpdateFile).copyTo(pythonCurrentSdkFile, OverwritePolicy.REPLACE);
                installSdkArchive(pythonCurrentSdkFile, false);
            } catch (Exception e) {
                FileLog.e(e);
                zExists = true;
            }
        }
        File file4 = new File(file3, "v.txt");
        if (file4.exists()) {
            String string = SimpliFile.readText$default(SimpliFiles.file(file4), 65536L, null, 2, null).trim();
            boolean zEndsWith$default = string.endsWith("-dev");
            int pipeIdx = string.indexOf('|');
            String strSubstringBefore$default = pipeIdx != -1 ? string.substring(0, pipeIdx) : string;
            try (InputStream inputStreamOpen = ApplicationLoader.applicationContext.getAssets().open("plugins_pysdk/v.txt");
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamOpen, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                String string2 = sb.toString().trim();
                try {
                    if (AppUtils.compareVersions(zEndsWith$default ? "dev" : "release", string2, strSubstringBefore$default)) {
                        zExists = true;
                    }
                } catch (NumberFormatException e2) {
                    FileLog.e(e2);
                }
            } catch (IOException e3) {
                FileLog.e(e3);
                return false;
            }
        }
        if (zExists || !INSTANCE.isSdkDirValid(file3)) {
            if (!zExists) {
                FileLog.w(Deobfuscator$exteraGramDev$TMessagesProj.getString(-133914802013743L));
            }
            SimpliFiles.directory(file3).clean();
            try {
                InputStream inputStreamSdkFromApk = Updater.INSTANCE.sdkFromApk();
                try {
                    SimpliFile.writeFromAtomic$default(SimpliFiles.file(pythonCurrentSdkFile), inputStreamSdkFromApk, 0L, 2, null);
                    CloseableKt.closeFinally(inputStreamSdkFromApk, null);
                    installSdkArchive(pythonCurrentSdkFile, true);
                } catch (Throwable th5) {
                    try {
                        throw th5;
                    } catch (Throwable th6) {
                        CloseableKt.closeFinally(inputStreamSdkFromApk, th5);
                        throw th6;
                    }
                }
            } catch (IOException e4) {
                FileLog.e(e4);
                return false;
            }
        }
        Updater.INSTANCE.deleteSdkUpdateFile();
        PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-133837492602415L));
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-133854672471599L);
        PyObject pyObject = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-132261239604783L));
        if (pyObject != null) {
            pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132222584899119L), file3.getAbsolutePath());
        }
        try {
            PyObject module2 = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132244059735599L));
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-132308484245039L);
            PyObject pyObjectCallAttr = module2.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132364318819887L), new Object[0]);
            if (pyObjectCallAttr == null || !pyObjectCallAttr.toBoolean()) {
                z = false;
            }
            sdkInitialized = z;
            PyObject pyObject2 = (PyObject) module2.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-132475987969583L));
            if (pyObject2 == null || (str = (String) pyObject2.toJava(String.class)) == null) {
                str = SDK_VERSION;
            }
            SDK_VERSION = str;
            PyObject pyObject3 = (PyObject) module2.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-131977771763247L));
            SDK_BETA = (pyObject3 == null || (bool = (Boolean) pyObject3.toJava(Boolean.TYPE)) == null) ? SDK_BETA : bool.booleanValue();
            if (this.basePluginClass == null && sdkInitialized && !ExteraConfig.getPluginsSafeMode()) {
                try {
                    requireBasePluginClass();
                } catch (Exception e5) {
                    FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132948434372143L), e5);
                }
            }
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "initSdk: completed. sdkInitialized=" + sdkInitialized + ", SDK_VERSION=" + SDK_VERSION);
            return sdkInitialized;
        } catch (Throwable th7) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "initSdk error: " + th7.getMessage(), th7);
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-131956296926767L), th7);
            try {
                Updater.INSTANCE.restoreSdkFromApk();
            } catch (Throwable th8) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132205405029935L), th8);
            }
            return false;
        }
    }

    private final void stopAndUnloadSdk() {
        Python python = this.python;
        if (python == null) {
            return;
        }
        this.basePluginClass = null;
        PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132536117511727L));
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-132553297380911L);
        PyObject pyObject = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-132626311824943L));
        if (pyObject != null) {
            try {
                PyObject pyObjectCallAttr = pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132591952086575L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-132677851432495L));
                if (pyObjectCallAttr != null) {
                    pyObjectCallAttr.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132656376596015L), new Object[0]);
                }
            } catch (Throwable th) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-132703621236271L), th);
            }
        }
        sdkInitialized = false;
        File file = SDK_DIR;
        PyObject pyObject2 = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-113616786572847L));
        if (file != null && pyObject2 != null && pyObject2.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113698390951471L), file.getAbsolutePath()).toBoolean()) {
            pyObject2.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113694095984175L), file.getAbsolutePath());
        }
        if (file == null || pyObject == null) {
            return;
        }
        removeModulesRecursive(pyObject, file, Deobfuscator$exteraGramDev$TMessagesProj.getString(-113784290297391L));
    }

    private final void removeModulesRecursive(PyObject sysModules, File file, String prefix) {
        if (Intrinsics.areEqual(prefix, Deobfuscator$exteraGramDev$TMessagesProj.getString(-113797175199279L))) {
            prefix = Deobfuscator$exteraGramDev$TMessagesProj.getString(-113294664025647L);
        }
        if (file.isDirectory()) {
            if (sysModules.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113307548927535L), prefix + file.getName()).toBoolean()) {
                sysModules.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113354793567791L), prefix + file.getName());
            }
        }
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles != null) {
            for (File file2 : fileArrListFiles) {
                removeModulesRecursive(sysModules, file2, file.getName() + '.');
            }
        }
        String name = file.getName();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-113371973436975L);
        String strSubstringBefore$default = name.indexOf('.') != -1 ? name.substring(0, name.indexOf('.')) : name;
        if (file.isDirectory()) {
            return;
        }
        if (sysModules.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113436397946415L), prefix + strSubstringBefore$default).toBoolean()) {
            sysModules.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113414923109935L), prefix + strSubstringBefore$default);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void init(Runnable callback) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-113500822455855L);
        if (getPython() == null) {
            callback.run();
            return;
        }
        try {
            if (!initSdk()) {
                callback.run();
                return;
            }
            if (!ExteraConfig.getPluginsSafeMode()) {
                final Updater.Companion companion = Updater.INSTANCE;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        companion.checkUpdates();
                    }
                }, 5000L);
            }
            Python python = this.python;
            if (python == null) {
                callback.run();
                return;
            }
            if (!ExteraConfig.getPluginsSafeMode()) {
                try {
                    PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114273916569135L));
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-114342636045871L);
                    Object java = module.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113865894676015L), getPluginsController().getPluginsDir().getAbsolutePath(), getPluginsController().getPreferences().getAll()).toJava(String[].class);
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-113810060101167L);
                    String[] strArr = (String[]) java;
                    if (!(strArr.length == 0)) {
                        SharedPreferences.Editor editorEdit = getPluginsController().getPreferences().edit();
                        for (String str : strArr) {
                            editorEdit.remove(str);
                        }
                        editorEdit.apply();
                        FileLog.d(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113930319185455L) + strArr.length + Deobfuscator$exteraGramDev$TMessagesProj.getString(-113973268858415L));
                    }
                } catch (PyException e) {
                    FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-112543044748847L), e);
                }
            }
            PipController.INSTANCE.cleanup();
            loadPlugins(callback);
            checkDevServer();
        } catch (Throwable th) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113479347619375L), th);
            callback.run();
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void checkDevServer() {
        if (ExteraConfig.getPluginsSafeMode()) {
            return;
        }
        if (ExteraConfig.getPluginsDevMode()) {
            runDevServer();
        } else {
            stopDevServer();
        }
    }

    private final void runDevServer() {
        Python python;
        if (ExteraConfig.getPluginsSafeMode() || (python = getPython()) == null) {
            return;
        }
        if (this.devServerClass != null) {
            stopDevServer();
        }
        try {
            PyObject pyObject = (PyObject) python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-112663303833135L)).get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-112152202724911L));
            this.devServerClass = pyObject;
            if (pyObject == null) {
                return;
            }
            if (pyObject != null) {
                pyObject.callAttrThrows(Deobfuscator$exteraGramDev$TMessagesProj.getString(-112263871874607L), new Object[0]);
            }
            FileLog.d(Deobfuscator$exteraGramDev$TMessagesProj.getString(-112328296384047L));
        } catch (Throwable th) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113011196184111L), th);
            this.devServerClass = null;
        }
    }

    private final void stopDevServer() {
        PyObject pyObject = this.devServerClass;
        if (pyObject == null) {
            return;
        }
        try {
            pyObject.callAttrThrows(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113148635137583L), new Object[0]);
            FileLog.d(Deobfuscator$exteraGramDev$TMessagesProj.getString(-113131455268399L));
            this.devServerClass = null;
        } catch (Throwable th) {
            try {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-112732023309871L), th);
            } finally {
                this.devServerClass = null;
            }
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void shutdown(Runnable callback) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-112843692459567L);
        if (getPython() == null) {
            callback.run();
            return;
        }
        try {
            stopDevServer();
            Iterator it = new ArrayList(this.pluginInstances.keySet()).iterator();
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-112942476707375L);
            while (it.hasNext()) {
                unloadPlugin((String) it.next());
            }
            PyObject pyObject = this.debuggerListener;
            if (pyObject != null) {
                pyObject.close();
            }
            this.debuggerListener = null;
            this.pluginInstances.clear();
            synchronized (this) {
                removePluginPathsFromSysPath();
                stopAndUnloadSdk();
                this.python = null;
                sdkInitialized = false;
                Unit unit = Unit.INSTANCE;
            }
            FileLog.d(Deobfuscator$exteraGramDev$TMessagesProj.getString(-112933886772783L));
        } catch (Exception e) {
            FileLog.e(e);
        }
        callback.run();
    }

    public final void loadPlugins(final Runnable callback) {
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "PythonPluginsEngine.loadPlugins called!");
        new Thread(new Runnable() {
            @Override
            public final void run() {
                android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "PythonPluginsEngine.loadPlugins executing on background thread...");
                PythonPluginsEngine.m1313$r8$lambda$2QnMYM5kpMDhr3l1kM17QcThy8(PythonPluginsEngine.this, callback);
            }
        }, "PluginsInitThread").start();
    }

    public static void m1313$r8$lambda$2QnMYM5kpMDhr3l1kM17QcThy8(PythonPluginsEngine pythonPluginsEngine, Runnable runnable) {
        PluginsController.PluginValidationResult pluginValidationResultValidatePluginFromFile;
        Plugin plugin;
        Python python = pythonPluginsEngine.getPython();
        if (python == null) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "loadPlugins: Python is NULL!");
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
                return;
            }
            return;
        }
        try {
            PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-92000216172079L));
            PyObject pyObject = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-92021691008559L));
            String absolutePath = pythonPluginsEngine.getPluginsController().getPluginsDir().getAbsolutePath();
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPlugins: pluginsDir=" + absolutePath);
            if (!ExteraConfig.getPluginsSafeMode() && pyObject != null && !pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-92034575910447L), absolutePath).toBoolean()) {
                pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-90449732978223L), absolutePath);
            }
            File[] fileArrListFiles = pythonPluginsEngine.getPluginsController().getPluginsDir().listFiles(new FilenameFilter() {
                @Override
                public final boolean accept(File file, String str) {
                    return PythonPluginsEngine.loadPlugins$lambda$0$0(file, str);
                }
            });
            if (fileArrListFiles == null) {
                android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPlugins: listFiles returned null!");
                pythonPluginsEngine.getPluginsController().notifyPluginsChanged();
                if (runnable != null) {
                    AndroidUtilities.runOnUIThread(runnable);
                    return;
                }
                return;
            }
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPlugins: found " + fileArrListFiles.length + " .py plugin files on disk.");
            int i = 0;
            for (File file : fileArrListFiles) {
                String name = file.getName();
                String strSubstring = name.endsWith(".py") ? name.substring(0, name.length() - 3) : name;
                android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPlugins: loading file " + file.getAbsolutePath() + " (id=" + strSubstring + ")");
                try {
                    String absolutePath2 = file.getAbsolutePath();
                    pluginValidationResultValidatePluginFromFile = pythonPluginsEngine.validatePluginFromFile(absolutePath2);
                    try {
                        if (pluginValidationResultValidatePluginFromFile.getError() != null) {
                            throw new Exception(pluginValidationResultValidatePluginFromFile.getError());
                        }
                        String absolutePath3 = file.getAbsolutePath();
                        pythonPluginsEngine.loadPlugin(strSubstring, absolutePath3, pluginValidationResultValidatePluginFromFile.getPlugin());
                        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPlugins: successfully loaded " + strSubstring);
                    } catch (Throwable th) {
                        android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "loadPlugins error for " + file.getName() + ": " + th.getMessage(), th);
                        FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-91051028399663L) + file.getName() + Deobfuscator$exteraGramDev$TMessagesProj.getString(-91072503236143L) + th.getMessage(), th);
                        Plugin plugin2 = pluginValidationResultValidatePluginFromFile != null ? pluginValidationResultValidatePluginFromFile.getPlugin() : null;
                        if (plugin2 != null) {
                            plugin = new Plugin(strSubstring, plugin2.getName());
                            plugin.setAuthor(plugin2.getAuthor());
                            plugin.setDescription(plugin2.getDescription());
                            plugin.setIcon(plugin2.getIcon());
                            plugin.setVersion(plugin2.getVersion());
                            plugin.setAppVersion(plugin2.getAppVersion());
                            plugin.setSdkVersion(plugin2.getSdkVersion());
                            plugin.setRequirements(plugin2.getRequirements());
                            plugin.setEngine(plugin2.getEngine());
                        } else {
                            plugin = new Plugin(strSubstring, strSubstring);
                            plugin.setAuthor("Unknown author");
                            plugin.setVersion(Deobfuscator$exteraGramDev$TMessagesProj.getString(-91128337810991L));
                            plugin.setEngine(Deobfuscator$exteraGramDev$TMessagesProj.getString(-91145517680175L));
                        }
                        plugin.setError(th);
                        plugin.setEnabled(false);
                        pythonPluginsEngine.getPluginsController().getPlugins().put(strSubstring, plugin);
                    }
                } catch (Throwable unused) {
                    pluginValidationResultValidatePluginFromFile = null;
                }
            }
            pythonPluginsEngine.getPluginsController().notifyPluginsChanged();
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPlugins completed. Total loaded plugins in memory: " + pythonPluginsEngine.getPluginsController().getPlugins().size());
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
            }
        } catch (PyException e) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "loadPlugins PyException: " + e.getMessage(), e);
            FileLog.e(e);
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean loadPlugins$lambda$0$0(File file, String str) {
        String lowerCase = str.toLowerCase(Locale.ROOT);
        return lowerCase.endsWith(".py");
    }

    public final void loadPlugin(String pluginId, String filePath) throws Exception {
        loadPlugin(pluginId, filePath, null, null);
    }

    public final void loadPlugin(String pluginId, String filePath, Plugin metadata) throws Exception {
        loadPlugin(pluginId, filePath, metadata, null);
    }

    public final void loadPlugin(String pluginId, String filePath, Plugin metadata, PipController.InstallerDelegate delegate) throws Exception {
        boolean z = getPluginsController().getPreferences().getBoolean("plugins_enabled_" + pluginId, false);
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new Exception("File not found: " + filePath);
        }
        if (metadata == null) {
            PluginsController.PluginValidationResult pluginValidationResultValidatePluginFromFile = validatePluginFromFile(filePath);
            if (pluginValidationResultValidatePluginFromFile.getError() != null) {
                throw new Exception(pluginValidationResultValidatePluginFromFile.getError());
            }
            metadata = pluginValidationResultValidatePluginFromFile.getPlugin();
        }
        if (metadata == null) {
            return;
        }
        if (!Intrinsics.areEqual(pluginId, metadata.getId())) {
            throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-112010468804143L) + pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-112135022855727L) + metadata.getId() + Deobfuscator$exteraGramDev$TMessagesProj.getString(-111645396583983L));
        }
        if (this.pluginInstances.containsKey(pluginId)) {
            unloadPlugin(pluginId);
        }
        metadata.setEnabled(false);
        metadata.setError(null);
        getPluginsController().getPlugins().put(pluginId, metadata);
        if (ExteraConfig.getPluginsSafeMode()) {
            return;
        }
        if (z) {
            createPluginInstance(pluginId, filePath, metadata, delegate);
            setPluginEnabled(pluginId, true, null);
        } else if (delegate != null) {
            installPluginDependencies(pluginId, metadata, delegate);
        }
    }

    private final void installPluginDependencies(String pluginId, Plugin pluginMetadata, PipController.InstallerDelegate delegate) throws Exception {
        PyObject module;
        List<String> requirements = pluginMetadata.getRequirements();
        List<String> list = requirements;
        if (list == null || list.isEmpty()) {
            return;
        }
        List<String> listInstallDependencies = PipController.INSTANCE.installDependencies(requirements, pluginId, delegate);
        List<String> list2 = listInstallDependencies;
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(list2, 10)), 16));
        for (Object obj : list2) {
            linkedHashMap.put(obj, INSTANCE.topLevelModuleNames(new File((String) obj)));
        }
        Set set = INSTANCE.topLevelModuleNames(SDK_DIR);
        for (Object entryObj : linkedHashMap.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            String str = (String) entry.getKey();
            Set setIntersect = CollectionsKt.intersect((Set) entry.getValue(), set);
            if (!setIntersect.isEmpty()) {
                throw new Exception("Module conflict in " + new File(str).getName() + " for plugin " + pluginId + ": " + setIntersect.toString());
            }
        }
        for (Object entryObj2 : linkedHashMap.entrySet()) {
            Map.Entry entry2 = (Map.Entry) entryObj2;
            String str2 = (String) entry2.getKey();
            Set<String> set2 = (Set<String>) entry2.getValue();
            String name = new File(str2).getName();
            disableShadowedPlugins(pluginId, name, set2);
        }
        Python python = getPython();
        PyObject pyObject = (python == null || (module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-110434215806511L))) == null) ? null : (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-110451395675695L));
        if (pyObject == null) {
            return;
        }
        int size = listInstallDependencies.size();
        while (true) {
            size--;
            if (-1 >= size) {
                return;
            }
            String str3 = listInstallDependencies.get(size);
            removeFromSysPath(pyObject, str3);
            pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-110000424109615L), 0, str3);
            this.dependencyPaths.add(str3);
        }
    }

    private final void disableShadowedPlugins(String pluginId, String dependencyName, Set<String> providedModules) {
        for (String str : providedModules) {
            if (!Intrinsics.areEqual(str, pluginId)) {
                if (new File(getPluginsController().getPluginsDir(), str + Deobfuscator$exteraGramDev$TMessagesProj.getString(-109953179469359L)).exists()) {
                    FileLog.w(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109970359338543L) + str + Deobfuscator$exteraGramDev$TMessagesProj.getString(-110129273128495L) + dependencyName + Deobfuscator$exteraGramDev$TMessagesProj.getString(-110116388226607L) + pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-110215172474415L));
                    unloadPlugin(str);
                    SharedPreferences.Editor editorEdit = getPluginsController().getPreferences().edit();
                    StringBuilder sb = new StringBuilder();
                    sb.append(Deobfuscator$exteraGramDev$TMessagesProj.getString(-110803582993967L));
                    sb.append(str);
                    editorEdit.putBoolean(sb.toString(), false);
                    editorEdit.apply();
                    Plugin plugin = getPluginsController().getPlugins().get(str);
                    if (plugin != null) {
                        plugin.setError(new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-110872302470703L) + str + Deobfuscator$exteraGramDev$TMessagesProj.getString(-110923842078255L) + dependencyName + Deobfuscator$exteraGramDev$TMessagesProj.getString(-110537295021615L) + pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-110623194367535L)));
                    }
                }
            }
        }
    }

    private final void removePluginDependencies(String pluginId) {
        PipController.INSTANCE.uninstallDependencies(pluginId);
        pruneDependencyPaths();
    }

    private final void pruneDependencyPaths() {
        PyObject module;
        if (this.dependencyPaths.isEmpty()) {
            return;
        }
        Set<String> setActiveLibraryPaths = PipController.INSTANCE.activeLibraryPaths();
        ConcurrentHashMap.KeySetView<String, Boolean> keySetView = this.dependencyPaths;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-110773518222895L);
        ArrayList arrayList = new ArrayList();
        for (Object obj : keySetView) {
            if (!setActiveLibraryPaths.contains((String) obj)) {
                arrayList.add(obj);
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        try {
            Python python = this.python;
            PyObject module2 = python != null ? python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109192970257967L)) : null;
            PyObject pyObject = module2 != null ? (PyObject) module2.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-109141430650415L)) : null;
            PyObject pyObject2 = module2 != null ? (PyObject) module2.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-109154315552303L)) : null;
            PyObject pyObject3 = module2 != null ? (PyObject) module2.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-109257394767407L)) : null;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj2 = arrayList.get(i);
                i++;
                String str = (String) obj2;
                if (pyObject != null) {
                    removeFromSysPath(pyObject, str);
                }
                if (pyObject3 != null) {
                    pyObject3.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109274574636591L), str, null);
                }
                if (pyObject2 != null) {
                    removeModulesUnderPath(pyObject2, str);
                }
                this.dependencyPaths.remove(str);
            }
            Python python2 = this.python;
            if (python2 == null || (module = python2.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109291754505775L))) == null) {
                return;
            }
            module.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109334704178735L), new Object[0]);
        } catch (PyException e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-108862257776175L), e);
        }
    }

    private final void removeModulesUnderPath(PyObject sysModules, String path) {
        String string;
        PyObject pyObjectCallAttr;
        Object objM2315constructorimpl;
        String string2;
        List<PyObject> listAsList;
        PyObject pyObject;
        String strCanonicalPathOrNull;
        Python python = this.python;
        if (python == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        String str = File.separator;
        sb.append(str);
        String string3 = sb.toString();
        String strCanonicalPathOrNull2 = INSTANCE.canonicalPathOrNull(path);
        String str2 = strCanonicalPathOrNull2 != null ? strCanonicalPathOrNull2 + str : string3;
        ArrayList arrayList = new ArrayList();
        for (PyObject pyObject2 : python.getBuiltins().callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109708366333487L), sysModules).asList()) {
            if (pyObject2 != null && (string = pyObject2.toString()) != null && (pyObjectCallAttr = sysModules.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109789970712111L), string)) != null) {
                PyObject pyObject3 = (PyObject) pyObjectCallAttr.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-109807150581295L));
                if (pyObject3 == null || (string2 = pyObject3.toString()) == null) {
                    try {
                        PyObject pyObject4 = (PyObject) pyObjectCallAttr.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-109854395221551L));
                        string2 = (pyObject4 == null || pyObject4.asList() == null || pyObject4.asList().isEmpty()) ? null : pyObject4.asList().get(0).toString();
                    } catch (Throwable unused) {
                        string2 = null;
                    }
                    if (string2 == null) {
                    }
                }
                if ((string2 != null && string2.startsWith(string3)) || ((strCanonicalPathOrNull = INSTANCE.canonicalPathOrNull(string2)) != null && strCanonicalPathOrNull.startsWith(str2))) {
                    arrayList.add(string);
                }
            }
        }
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-109815740515887L);
        for (Object obj : arrayList) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-109944589534767L);
            sysModules.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109437783393839L), (String) obj, null);
        }
        if (arrayList.isEmpty()) {
            return;
        }
        FileLog.d(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109454963263023L) + arrayList.size() + Deobfuscator$exteraGramDev$TMessagesProj.getString(-109429193459247L) + new File(path).getName());
    }

    private final void removePluginPathsFromSysPath() {
        PyObject module;
        PyObject pyObject;
        Python python = this.python;
        if (python == null || (module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-109618172020271L))) == null || (pyObject = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-109635351889455L))) == null) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.dependencyPaths);
        arrayList.add(getPluginsController().getPluginsDir().getAbsolutePath());
        Iterator it = arrayList.iterator();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-108084868695599L);
        while (it.hasNext()) {
            removeFromSysPath(pyObject, (String) it.next());
        }
        this.dependencyPaths.clear();
    }

    private final void removeFromSysPath(PyObject sysPath, String path) {
        while (sysPath.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-108144998237743L), path).toBoolean()) {
            sysPath.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-108123523401263L), path);
        }
    }

    private final void createPluginInstance(String pluginId, String filePath, Plugin pluginMetadata, PipController.InstallerDelegate delegate) throws Exception {
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance START for pluginId=" + pluginId + ", filePath=" + filePath);
        PyObject module;
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance: calling requireBasePluginClass...");
        PyObject pyObjectRequireBasePluginClass = requireBasePluginClass();
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance: pyObjectRequireBasePluginClass=" + pyObjectRequireBasePluginClass);

        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance: installing dependencies for " + pluginId + "...");
        installPluginDependencies(pluginId, pluginMetadata, delegate);
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance: dependencies installed. Refreshing import caches...");
        refreshImportCaches(pluginId, new File(filePath).getParentFile());
        try {
            Python python = getPython();
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance: getPython()=" + python);
            if (python == null || (module = python.getModule(pluginId)) == null) {
                throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-108230897583663L) + pluginId);
            }
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance: Python module loaded=" + module);
            PyObject pyObject = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-107895890134575L));
            String string = pyObject != null ? pyObject.toString() : null;
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance: module __file__=" + string);
            Companion companion = INSTANCE;
            if (!Intrinsics.areEqual(companion.canonicalPathOrNull(string), companion.canonicalPathOrNull(filePath))) {
                android.util.Log.w("NAGRAM_PLUGIN_DEBUG", "createPluginInstance path mismatch: string=" + string + " vs filePath=" + filePath + ". Continuing anyway...");
            }
            PyObject pyObjectCallAttr = pyObjectRequireBasePluginClass.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-108565905032751L), module);
            if (pyObjectCallAttr == null) {
                throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-106981062100527L) + pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-107144270857775L));
            }
            PyObject pyObjectCall = pyObjectCallAttr.call(new Object[0]);
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance: plugin instance created=" + pyObjectCall);
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-106912342623791L), (Object) pluginMetadata.getId());
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-106865097983535L), (Object) pluginMetadata.getName());
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-106877982885423L), (Object) pluginMetadata.getDescription());
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107479278306863L), (Object) pluginMetadata.getAuthor());
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107586652489263L), (Object) pluginMetadata.getVersion());
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107552292750895L), (Object) pluginMetadata.getIcon());
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107565177652783L), (Object) pluginMetadata.getAppVersion());
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107616717260335L), (Object) pluginMetadata.getSdkVersion());
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107736976344623L), (Object) pluginMetadata.getRequirements());
            String string2 = Deobfuscator$exteraGramDev$TMessagesProj.getString(-107251645040175L);
            Boolean bool = Boolean.FALSE;
            pyObjectCall.put(string2, (Object) bool);
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107217285301807L), (Object) bool);
            pyObjectCall.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107337544386095L), (Object) null);
            this.pluginInstances.put(pluginId, pyObjectCall);
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "createPluginInstance SUCCESS for " + pluginId);
        } catch (PyException e) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "createPluginInstance PyException: " + e.getMessage(), e);
            throw new Exception(Deobfuscator$exteraGramDev$TMessagesProj.getString(-107814285755951L) + e.getMessage(), e);
        } catch (Throwable th) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "createPluginInstance Throwable: " + th.getMessage(), th);
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0096 A[Catch: all -> 0x0060, PyException -> 0x0063, TryCatch #6 {PyException -> 0x0063, blocks: (B:8:0x0031, B:10:0x003b, B:13:0x0054, B:14:0x0058, B:22:0x0082, B:24:0x0088, B:25:0x0093, B:27:0x0096, B:29:0x00a6, B:30:0x00b4, B:32:0x00d0, B:35:0x00d7, B:37:0x00dd, B:39:0x00e1, B:45:0x0115, B:46:0x011a, B:42:0x010f), top: B:98:0x0031, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:29:0x00a6 A[Catch: all -> 0x0060, PyException -> 0x0063, TryCatch #6 {PyException -> 0x0063, blocks: (B:8:0x0031, B:10:0x003b, B:13:0x0054, B:14:0x0058, B:22:0x0082, B:24:0x0088, B:25:0x0093, B:27:0x0096, B:29:0x00a6, B:30:0x00b4, B:32:0x00d0, B:35:0x00d7, B:37:0x00dd, B:39:0x00e1, B:45:0x0115, B:46:0x011a, B:42:0x010f), top: B:98:0x0031, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:43:0x0112 A[EDGE_INSN: B:43:0x0112->B:44:0x0113 BREAK  A[LOOP:0: B:38:0x00df->B:42:0x010f]] */
    public final void unloadPlugin(String pluginId) {
        if (pluginId == null) {
            return;
        }
        this.settingsCache.remove(pluginId);
        PyObject pyObjectRemove = this.pluginInstances.remove(pluginId);
        Plugin plugin = getPluginsController().getPlugins().get(pluginId);
        if (plugin != null) {
            plugin.setEnabled(false);
        }
        try {
            if (pyObjectRemove != null) {
                try {
                    if (PyObjectUtils.getBoolean(pyObjectRemove, "enabled", false)) {
                        getPluginsController().getWatchdog().onPluginExecutionStarted(pluginId);
                        try {
                            pyObjectRemove.callAttr("on_unload", new Object[0]);
                        } catch (Throwable th) {
                            FileLog.e(th);
                        } finally {
                            getPluginsController().getWatchdog().onPluginExecutionFinished(pluginId);
                        }
                    }
                } catch (Throwable th) {
                    FileLog.e(th);
                }
                try {
                    pyObjectRemove.put("enabled", (Object) Boolean.FALSE);
                    pyObjectRemove.close();
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
            File file = new File(getPluginsController().getPluginsDir(), "wheels");
            if (file.exists() && file.isDirectory()) {
                File[] fileArrListFiles = file.listFiles();
                if (fileArrListFiles != null) {
                    for (File file2 : fileArrListFiles) {
                        if (file2.getName().startsWith(pluginId + ".")) {
                            INSTANCE.deleteFileIfExists(file2);
                        }
                    }
                }
            }
            refreshImportCaches(pluginId, getPluginsController().getPluginsDir());
            getPluginsController().cleanupPlugin(pluginId);
        } catch (Throwable th) {
            FileLog.e(th);
            getPluginsController().cleanupPlugin(pluginId);
        }
    }

    private final void refreshImportCaches(String pluginId, File moduleDir) {
        Python python = this.python;
        if (python == null) {
            return;
        }
        try {
            PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-106280982431279L));
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-106298162300463L);
            PyObject pyObject = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-106353996875311L));
            if (pyObject != null) {
                evictPluginModule(pyObject, pluginId, moduleDir);
            }
            PyObject pyObject2 = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-106319637136943L));
            if (moduleDir != null && pyObject2 != null) {
                pyObject2.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-122348455085615L), moduleDir.getAbsolutePath(), null);
            }
            python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-122434354431535L)).callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-122408584627759L), new Object[0]);
        } catch (PyException e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-122485894039087L) + pluginId, e);
        }
    }

    private final void evictPluginModule(PyObject sysModules, String pluginId, File moduleDir) {
        try {
            sysModules.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-123134434100783L), pluginId, null);
        } catch (Throwable ignored) {}
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void setPluginEnabled(String pluginId, boolean enabled, final Utilities.Callback<String> callback) {
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "PythonPluginsEngine.setPluginEnabled start: pluginId=" + pluginId + ", enabled=" + enabled);
        Plugin plugin;
        try {
            Plugin plugin2 = getPluginsController().getPlugins().get(pluginId);
            if (plugin2 == null) {
                android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled: plugin2 is null in plugins map for " + pluginId + ", checking disk...");
                File fileOnDisk = new File(getPluginsController().getPluginsDir(), pluginId + ".py");
                if (fileOnDisk.exists()) {
                    PluginsController.PluginValidationResult valRes = validatePluginFromFile(fileOnDisk.getAbsolutePath());
                    plugin2 = valRes.getPlugin();
                }
                if (plugin2 != null) {
                    getPluginsController().getPlugins().put(pluginId, plugin2);
                } else {
                    throw new Exception("Plugin metadata non trovata per " + pluginId);
                }
            }
            if (enabled && plugin2 != null) {
                plugin2.setError(null);
            }
            if (enabled && ExteraConfig.getPluginsSafeMode()) {
                android.util.Log.w("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled: Safe mode is enabled, cannot enable plugin " + pluginId);
                if (callback != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public final void run() {
                            callback.run("Safe mode enabled");
                        }
                    });
                    return;
                }
                return;
            }
            PyObject pyObject = this.pluginInstances.get(pluginId);
            if (enabled && pyObject == null) {
                android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled: creating plugin instance for " + pluginId + " at " + getPluginPath(pluginId));
                if (!sdkInitialized) {
                    android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled: sdkInitialized is false, calling initSdk()...");
                    initSdk();
                }
                createPluginInstance(pluginId, getPluginPath(pluginId), plugin2, null);
                pyObject = this.pluginInstances.get(pluginId);
                if (pyObject == null) {
                    throw new Exception("Impossibile creare l'istanza Python per " + pluginId);
                }
            }
            if (!enabled) {
                android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled: disabling plugin " + pluginId);
                SharedPreferences.Editor editorEdit = getPluginsController().getPreferences().edit();
                editorEdit.putBoolean("plugins_enabled_" + pluginId, false);
                editorEdit.apply();
                unloadPlugin(pluginId);
            } else {
                if (pyObject == null) {
                    throw new IllegalArgumentException("pyObject is null");
                }
                getPluginsController().cleanupPlugin(pluginId);
                getPluginsController().getWatchdog().onPluginExecutionStarted(pluginId);
                try {
                    android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled: calling on_enable for " + pluginId);
                    try {
                        if (pyObject.get("on_enable") != null) {
                            pyObject.callAttr("on_enable", new Object[0]);
                        }
                    } catch (Throwable thOnEnable) {
                        android.util.Log.w("NAGRAM_PLUGIN_DEBUG", "on_enable exception (ignored) for " + pluginId + ": " + thOnEnable.getMessage());
                    }
                    getPluginsController().getWatchdog().onPluginExecutionFinished(pluginId);
                    Boolean bool = Boolean.TRUE;
                    pyObject.put("is_enabled", (Object) bool);
                    pyObject.put("error", (Object) null);
                    plugin2.setError(null);
                    plugin2.setEnabled(true);
                    SharedPreferences.Editor editorEdit2 = getPluginsController().getPreferences().edit();
                    editorEdit2.putBoolean("plugins_enabled_" + pluginId, true);
                    editorEdit2.apply();
                    getPluginsController().loadPluginSettings(pluginId);
                    android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled: SUCCESS! Plugin " + pluginId + " is now ENABLED!");
                } catch (Throwable th) {
                    android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled execution failed: " + th.getMessage(), th);
                    getPluginsController().getWatchdog().onPluginExecutionFinished(pluginId);
                    throw th;
                }
            }
            getPluginsController().notifyPluginsChanged();
            if (callback != null) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public final void run() {
                        callback.run(null);
                    }
                });
            }
        } catch (Throwable th2) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "setPluginEnabled top catch error for " + pluginId + ": " + th2.getMessage(), th2);
            FileLog.e(th2);
            if (enabled && (plugin = getPluginsController().getPlugins().get(pluginId)) != null) {
                plugin.setError(th2);
            }
            SharedPreferences.Editor editorEdit3 = getPluginsController().getPreferences().edit();
            editorEdit3.putBoolean("plugins_enabled_" + pluginId, false);
            editorEdit3.apply();
            unloadPlugin(pluginId);
            if (callback != null) {
                final String errStr = AppUtils.stackTraceToString(th2);
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public final void run() {
                        callback.run(errStr);
                    }
                });
            }
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void deletePlugin(String pluginId, final Utilities.Callback<String> callback) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-121910368421423L);
        unloadPlugin(pluginId);
        try {
            removePluginDependencies(pluginId);
        } catch (Exception e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-122026332538415L) + pluginId, e);
        }
        INSTANCE.deleteFileIfExists(new File(getPluginsController().getPluginsDir(), pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-121571066005039L)));
        PluginsController.Companion companion = PluginsController.INSTANCE;
        if (companion.isPluginPinned(pluginId)) {
            companion.setPluginPinned(pluginId, false);
        }
        getPluginsController().clearPluginSettingsPreferences(pluginId, true);
        getPluginsController().getPlugins().remove(pluginId);
        getPluginsController().notifyPluginsChanged();
        if (callback != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    callback.run(null);
                }
            });
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public String getPluginPath(String id) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-121588245874223L);
        return getPluginsController().getPluginsDir().getAbsolutePath() + File.separator + id + Deobfuscator$exteraGramDev$TMessagesProj.getString(-121592540841519L);
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openInExternalApp(String id) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-121678440187439L);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        File file = new File(getPluginPath(id));
        if (file.exists()) {
            PluginFileViewer.INSTANCE.open(safeLastFragment, file, id + Deobfuscator$exteraGramDev$TMessagesProj.getString(-121699915023919L));
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void sharePlugin(String id) {
        Activity parentActivity;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-121665555285551L);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null || (parentActivity = safeLastFragment.getParentActivity()) == null) {
            return;
        }
        String pluginPath = getPluginPath(id);
        File file = new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-121738569729583L));
        SimpliFiles.directory(file).create();
        File file2 = new File(file, id + Deobfuscator$exteraGramDev$TMessagesProj.getString(-121768634500655L));
        try {
            SimpliFiles.file(pluginPath).copyTo(file2, OverwritePolicy.REPLACE);
            Uri uriForFile = FileProvider.getUriForFile(parentActivity, ApplicationLoader.getApplicationId() + Deobfuscator$exteraGramDev$TMessagesProj.getString(-121734274762287L), file2);
            Intent intent = new Intent(Deobfuscator$exteraGramDev$TMessagesProj.getString(-120127956993583L));
            intent.setFlags(1);
            intent.putExtra(Deobfuscator$exteraGramDev$TMessagesProj.getString(-120304050652719L), uriForFile);
            intent.setType(Deobfuscator$exteraGramDev$TMessagesProj.getString(-120355590260271L));
            safeLastFragment.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.ShareFile)), MediaError.DetailedErrorCode.SEGMENT_UNKNOWN);
            file2.deleteOnExit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public final void loadPluginFromFile(String filePath, Plugin pluginMetadata, Utilities.Callback<String> callback) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-119973338170927L);
        loadPluginFromFile(filePath, pluginMetadata, callback, null);
    }

    public final void loadPluginFromFile(final String filePath, final Plugin pluginMetadata, final Utilities.Callback<String> callback, final PipController.InstallerDelegate delegate) {
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPluginFromFile: launching dedicated PluginLoadThread for filePath=" + filePath);
        new Thread(new Runnable() {
            @Override
            public final void run() {
                android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPluginFromFile: executing loadPluginFromFileInternal...");
                loadPluginFromFileInternal(pluginMetadata, filePath, delegate, callback);
            }
        }, "PluginLoadThread-" + System.currentTimeMillis()).start();
    }

    public final void loadPluginFromFileInternal(Plugin plugin, String str, PipController.InstallerDelegate installerDelegate, final Utilities.Callback<String> callback) {
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPluginFromFileInternal: start plugin=" + plugin + ", str=" + str);
        if (plugin == null) {
            try {
                PluginsController.PluginValidationResult pluginValidationResultValidatePluginFromFile = validatePluginFromFile(str);
                if (pluginValidationResultValidatePluginFromFile.getError() != null) {
                    throw new Exception(pluginValidationResultValidatePluginFromFile.getError());
                }
                plugin = pluginValidationResultValidatePluginFromFile.getPlugin();
                if (plugin == null) {
                    throw new IllegalArgumentException("Plugin metadata is null");
                }
            } catch (Throwable th) {
                android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "loadPluginFromFileInternal validation error: " + th.getMessage(), th);
                FileLog.e(th);
                if (callback != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public final void run() {
                            callback.run(th.getMessage());
                        }
                    });
                }
                return;
            }
        }
        String id = plugin.getId();
        android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPluginFromFileInternal: id=" + id);
        File file = new File(getPluginsController().getPluginsDir(), id + ".py");
        File fileBackup = new File(getPluginsController().getPluginsDir(), id + ".bak");
        boolean zExists = file.exists();
        try {
            if (zExists) {
                unloadPlugin(id);
                INSTANCE.deleteFileIfExists(fileBackup);
                file.renameTo(fileBackup);
                removePluginDependencies(id);
            }
            SimpliFiles.file(new File(str)).copyTo(file, OverwritePolicy.REPLACE);
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPluginFromFileInternal: copied file, calling loadPlugin...");
            loadPlugin(id, file.getAbsolutePath(), plugin, installerDelegate);
            android.util.Log.d("NAGRAM_PLUGIN_DEBUG", "loadPluginFromFileInternal: loadPlugin completed successfully!");
            INSTANCE.deleteFileIfExists(fileBackup);
            getPluginsController().notifyPluginsChanged();
            if (callback != null) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public final void run() {
                        callback.run(null);
                    }
                });
            }
        } catch (Throwable th) {
            android.util.Log.e("NAGRAM_PLUGIN_DEBUG", "loadPluginFromFileInternal error: " + th.getMessage(), th);
            FileLog.e(th);
            if (zExists && fileBackup.exists()) {
                fileBackup.renameTo(file);
                try {
                    loadPlugin(id, file.getAbsolutePath());
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            getPluginsController().notifyPluginsChanged();
            if (callback != null) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public final void run() {
                        callback.run(th.getMessage());
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void loadPluginFromFile$lambda$0$1(Utilities.Callback callback, Throwable th) {
        callback.run(AppUtils.stackTraceToString(th));
    }

    private final String findModuleNameOwner(String pluginId) {
        Object obj;
        if (INSTANCE.topLevelModuleNames(SDK_DIR).contains(pluginId)) {
            return Deobfuscator$exteraGramDev$TMessagesProj.getString(-120050647582255L);
        }
        Iterator<String> it = PipController.INSTANCE.activeLibraryPaths().iterator();
        while (it.hasNext()) {
            File file = new File(it.next());
            if (INSTANCE.topLevelModuleNames(file).contains(pluginId)) {
                return Deobfuscator$exteraGramDev$TMessagesProj.getString(-120119367058991L) + file.getName();
            }
        }
        Python python = this.python;
        if (python == null) {
            return null;
        }
        try {
            PyObject pyObjectCallAttr = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-120780792022575L)).callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-120767907120687L), pluginId);
            if (pyObjectCallAttr == null) {
                return null;
            }
            PyObject pyObject = (PyObject) pyObjectCallAttr.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-120810856793647L));
            String string = pyObject != null ? pyObject.toString() : null;
            if (string != null) {
                Companion companion = INSTANCE;
                if (Intrinsics.areEqual(companion.canonicalPathOrNull(string), companion.canonicalPathOrNull(new File(getPluginsController().getPluginsDir(), pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-120918230976047L)).getAbsolutePath()))) {
                    return null;
                }
                return string;
            }
            PyObject pyObject2 = (PyObject) pyObjectCallAttr.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-120935410845231L));
            if (pyObject2 == null) {
                return Deobfuscator$exteraGramDev$TMessagesProj.getString(-120424309737007L);
            }
            String strCanonicalPathOrNull = INSTANCE.canonicalPathOrNull(new File(getPluginsController().getPluginsDir(), pluginId).getAbsolutePath());
            List<PyObject> listAsList = python.getBuiltins().callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-120587518494255L), pyObject2).asList();
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-120548863788591L);
            List<PyObject> list = listAsList;
            ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
            Iterator<PyObject> it2 = list.iterator();
            while (it2.hasNext()) {
                arrayList.add(((PyObject) it2.next()).toString());
            }
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                obj = arrayList.get(i);
                i++;
                if (!Intrinsics.areEqual(INSTANCE.canonicalPathOrNull((String) obj), strCanonicalPathOrNull)) {
                    return (String) obj;
                }
            }
            obj = null;
            return (String) obj;
        } catch (PyException e) {
            FileLog.w(Deobfuscator$exteraGramDev$TMessagesProj.getString(-120669122872879L) + pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-119032740333103L) + e.getMessage());
            return Deobfuscator$exteraGramDev$TMessagesProj.getString(-119097164842543L);
        } catch (Throwable th) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-119191654123055L) + pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-118865236608559L), th);
            return null;
        }
    }

    public final PluginsController.PluginValidationResult validatePluginFromFile(String filePath) {
        String string;
        String string2;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-118839466804783L);
        if (!new File(filePath).exists()) {
            return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-118938251052591L));
        }
        try {
            Map<String, String> pluginMetadata = parsePluginMetadata(filePath);
            String str = pluginMetadata.get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-118976905758255L));
            if (str == null) str = pluginMetadata.get("id");
            if (str == null) str = pluginMetadata.get("name");
            if (str == null) str = pluginMetadata.get("module");

            String str2 = pluginMetadata.get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-118981200725551L));
            if (str2 == null) str2 = pluginMetadata.get("name");
            if (str2 == null) str2 = pluginMetadata.get("title");
            if (str2 == null) str2 = str;
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                if (str != null) {
                    if (!new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-119384927651375L)).matches(str)) {
                        return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-119513776670255L));
                    }
                    String strFindModuleNameOwner = findModuleNameOwner(str);
                    if (strFindModuleNameOwner != null) {
                        return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-117881689097775L) + strFindModuleNameOwner + Deobfuscator$exteraGramDev$TMessagesProj.getString(-118689142949423L));
                    }
                    String str3 = pluginMetadata.get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-118281121056303L));
                    if (str3 != null) {
                        Matcher matcher = VERSION_PATTERN.matcher(str3);
                        if (!matcher.matches()) {
                            return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-118401380140591L) + str3);
                        }
                        String strGroup = matcher.group(1);
                        if (strGroup == null) {
                            return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-118414265042479L) + str3);
                        }
                        String strGroup2 = matcher.group(2);
                        if (strGroup2 != null && (string2 = StringsKt.trim((CharSequence) strGroup2).toString()) != null) {
                            if (!AppUtils.compareVersions(strGroup, BuildVars.BUILD_VERSION_STRING, string2)) {
                                return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-117014105703983L) + str3 + Deobfuscator$exteraGramDev$TMessagesProj.getString(-116597493876271L) + BuildVars.BUILD_VERSION_STRING);
                            }
                        }
                        return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-116932501325359L) + str3);
                    }
                    String str4 = pluginMetadata.get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-116674803287599L));
                    if (str4 != null) {
                        Matcher matcher2 = VERSION_PATTERN.matcher(str4);
                        if (!matcher2.matches()) {
                            return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-116726342895151L) + str4);
                        }
                        String strGroup3 = matcher2.group(1);
                        if (strGroup3 == null) {
                            return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-116807947273775L) + str4);
                        }
                        String strGroup4 = matcher2.group(2);
                        if (strGroup4 != null && (string = StringsKt.trim((CharSequence) strGroup4).toString()) != null) {
                            if (!AppUtils.compareVersions(strGroup3, SDK_VERSION, string)) {
                                return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-117469372237359L) + str4 + Deobfuscator$exteraGramDev$TMessagesProj.getString(-117602516223535L) + SDK_VERSION);
                            }
                        }
                        return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-117387767858735L) + str4);
                    }
                    if (str2 != null) {
                        Plugin plugin = new Plugin(str, str2);
                        plugin.setEngine(Deobfuscator$exteraGramDev$TMessagesProj.getString(-117297573545519L));
                        String string3 = pluginMetadata.get("author");
                        if (string3 == null) {
                            string3 = "Unknown author";
                        }
                        plugin.setAuthor(string3);
                        String string4 = pluginMetadata.get("description");
                        if (string4 == null) {
                            string4 = "No description";
                        }
                        plugin.setDescription(string4);
                        plugin.setIcon(pluginMetadata.get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-115759975253551L)));
                        String string5 = pluginMetadata.get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-115790040024623L));
                        if (string5 == null) {
                            string5 = Deobfuscator$exteraGramDev$TMessagesProj.getString(-115755680286255L);
                        }
                        plugin.setVersion(string5);
                        plugin.setAppVersion(str3);
                        plugin.setSdkVersion(str4);
                        String str5 = pluginMetadata.get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-115841579632175L));
                        if (str5 != null && str5.length() != 0) {
                            List<String> listSplit = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-115820104795695L)).split(str5, 0);
                            ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(listSplit, 10));
                            Iterator<String> it = listSplit.iterator();
                            while (it.hasNext()) {
                                arrayList.add(StringsKt.trim((CharSequence) it.next()).toString());
                            }
                            ArrayList arrayList2 = new ArrayList();
                            int size = arrayList.size();
                            int i = 0;
                            while (i < size) {
                                Object obj = arrayList.get(i);
                                i++;
                                if (((String) obj).length() > 0) {
                                    arrayList2.add(obj);
                                }
                            }
                            plugin.setRequirements(arrayList2);
                        }
                        plugin.setEnabled(getPluginsController().getPreferences().getBoolean(Deobfuscator$exteraGramDev$TMessagesProj.getString(-115970428651055L) + str, false));
                        return new PluginsController.PluginValidationResult(plugin, null);
                    }
                    throw new IllegalArgumentException(Deobfuscator$exteraGramDev$TMessagesProj.getString(-117130069820975L).toString());
                }
                throw new IllegalArgumentException(Deobfuscator$exteraGramDev$TMessagesProj.getString(-119354862880303L).toString());
            }
            return new PluginsController.PluginValidationResult(null, Deobfuscator$exteraGramDev$TMessagesProj.getString(-119629740787247L));
        } catch (PyException e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-115489392313903L) + filePath + Deobfuscator$exteraGramDev$TMessagesProj.getString(-115545226888751L) + e.getMessage(), e);
            return new PluginsController.PluginValidationResult(null, e.getMessage());
        } catch (Throwable th) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-115588176561711L) + filePath, th);
            return new PluginsController.PluginValidationResult(null, th.getMessage());
        }
    }

    public final List<SettingItem> parsePySettingDefinitions(List<? extends PyObject> pyDefinitionsList) throws Throwable {
        Object editTextSetting;
        String string;
        View view;
        Object customSetting;
        String string2;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-116292551198255L);
        ArrayList arrayList = new ArrayList(pyDefinitionsList.size());
        for (PyObject pyObject : pyDefinitionsList) {
            Object headerSetting = null;
            String string3 = PyObjectUtils.getString(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-116369860609583L), null);
            if (string3 == null) {
                FileLog.w(Deobfuscator$exteraGramDev$TMessagesProj.getString(-116468644857391L));
            } else {
                String string4 = PyObjectUtils.getString(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-116176587081263L), null);
                String string5 = PyObjectUtils.getString(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-116193766950447L), null);
                String string6 = PyObjectUtils.getString(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-116137932375599L), null);
                String string7 = PyObjectUtils.getString(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-116241011590703L), null);
                PyObject pyObject2 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-116271076361775L));
                PyObject pyObject3 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-114664758593071L));
                String string8 = PyObjectUtils.getString(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-114656168658479L), null);
                PyObject pyObject4 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-114694823364143L));
                PyObject pyObject5 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-114797902579247L));
                PyObject pyObject6 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-114776427742767L));
                switch (string3.hashCode()) {
                    case -1866021310:
                        if (string3.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114407060555311L))) {
                            String string9 = PyObjectUtils.getString(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-115214514406959L), null);
                            boolean z = PyObjectUtils.getBoolean(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-115227399308847L), false);
                            int i = PyObjectUtils.getInt(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-115201629505071L), 256);
                            String string10 = PyObjectUtils.getString(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-115257464079919L), null);
                            if (string4 != null && string9 != null) {
                                if (pyObject4 == null || (string = pyObject4.toString()) == null) {
                                    string = Deobfuscator$exteraGramDev$TMessagesProj.getString(-115270348981807L);
                                }
                                editTextSetting = new EditTextSetting(string4, string9, string, z, i, string10, pyObject2);
                                headerSetting = editTextSetting;
                                break;
                            }
                        }
                        break;
                    case -1349088399:
                        if (string3.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114484369966639L))) {
                            PyObject pyObject7 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-115321888589359L));
                            PyObject pyObject8 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-115420672837167L));
                            PyObject pyObject9 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-115433557739055L));
                            PyObject pyObject10 = (PyObject) pyObject.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-115399198000687L));
                            if (pyObject9 != null) {
                                CustomSetting.Factory factory = (CustomSetting.Factory) PyObjectUtils.toJavaCompat(pyObject9, CustomSetting.Factory.class);
                                if (factory != null) {
                                    customSetting = pyObject10 == null ? new CustomSetting((CustomSetting.Factory<?>) factory, pyObject5, pyObject6, pyObject3, string8) : new CustomSetting(factory, pyObject10, pyObject5, pyObject6, pyObject3, string8);
                                    headerSetting = customSetting;
                                }
                                break;
                            } else if (pyObject8 != null) {
                                UItem uItem = (UItem) PyObjectUtils.toJavaCompat(pyObject8, UItem.class);
                                if (uItem != null) {
                                    customSetting = new CustomSetting(uItem, pyObject5, pyObject6, pyObject3, string8);
                                    headerSetting = customSetting;
                                }
                                break;
                            } else if (pyObject7 != null && (view = (View) PyObjectUtils.toJavaCompat(pyObject7, View.class)) != null) {
                                customSetting = new CustomSetting(view, pyObject5, pyObject6, pyObject3, string8);
                                headerSetting = customSetting;
                                break;
                            }
                        }
                        break;
                    case -1221270899:
                        if (string3.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114437125326383L)) && string5 != null) {
                            headerSetting = new HeaderSetting(string5);
                        }
                        break;
                    case -889473228:
                        if (string3.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114604629050927L)) && string4 != null && string5 != null && pyObject4 != null) {
                            editTextSetting = new SwitchSetting(string4, string5, pyObject4.toBoolean(), string6, string7, pyObject2, pyObject3, string8);
                            headerSetting = editTextSetting;
                        }
                        break;
                    case 3556653:
                        if (string3.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114505844803119L))) {
                            boolean z2 = PyObjectUtils.getBoolean(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-115351953360431L), false);
                            boolean z3 = PyObjectUtils.getBoolean(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-115373428196911L), false);
                            if (string5 != null) {
                                headerSetting = new TextSetting(string5, string6, string7, z2, z3, pyObject5, pyObject6, pyObject3, string8);
                            }
                        }
                        break;
                    case 100358090:
                        if (string3.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114862327088687L)) && string4 != null && string5 != null) {
                            if (pyObject4 == null || (string2 = pyObject4.toString()) == null) {
                                string2 = Deobfuscator$exteraGramDev$TMessagesProj.getString(-114583154214447L);
                            }
                            editTextSetting = new InputSetting(string4, string5, string2, string6, string7, pyObject2, pyObject3, string8);
                            headerSetting = editTextSetting;
                        }
                        break;
                    case 1191572447:
                        if (string3.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114544499508783L))) {
                            String[] stringArray = PyObjectUtils.getStringArray(pyObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-114557384410671L), null);
                            if (string4 != null && string5 != null && stringArray != null && stringArray.length != 0 && pyObject4 != null) {
                                editTextSetting = new SelectorSetting(string4, string5, pyObject4.toInt(), stringArray, string7, pyObject2, pyObject3, string8);
                                headerSetting = editTextSetting;
                                break;
                            }
                        }
                        break;
                    case 1674318617:
                        if (string3.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114381290751535L))) {
                            headerSetting = new DividerSetting(string5);
                        }
                        break;
                }
                if (headerSetting != null) {
                    arrayList.add(headerSetting);
                }
            }
        }
        return arrayList;
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public List<SettingItem> loadPluginSettings(String id) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-114913866696239L);
        try {
            Plugin plugin = getPluginsController().getPlugins().get(id);
            PyObject pyObject = this.pluginInstances.get(id);
            if (getPluginsController().isPluginActive$TMessagesProj(plugin) && pyObject != null) {
                PyObject pyObjectCallAttr = pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-114918161663535L), new Object[0]);
                if (pyObjectCallAttr == null) {
                    return null;
                }
                List<PyObject> listAsList = pyObjectCallAttr.asList();
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-114986881140271L);
                if (listAsList.isEmpty()) {
                    return null;
                }
                try { return parsePySettingDefinitions(listAsList); } catch (Throwable th) { return new ArrayList<>(); }
            }
            getPluginsController().invalidatePluginSettings(id);
            return null;
        } catch (Exception e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-115038420747823L), e);
            return null;
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void executeOnAppEvent(String eventType) {
        Python python;
        PyObject module;
        PyObject pyObject;
        PluginsController pluginsController;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-95938701182511L);
        if (!sdkInitialized || ExteraConfig.getPluginsSafeMode() || (python = getPython()) == null || (module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-96050370332207L))) == null || (pyObject = (PyObject) module.get((Object) Deobfuscator$exteraGramDev$TMessagesProj.getString(-96033190463023L))) == null) {
            return;
        }
        PyObject pyObjectCall = pyObject.call(eventType);
        try {
            PyObject pyObject2 = this.debuggerListener;
            if (pyObject2 != null) {
                try {
                    pyObject2.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-96131974710831L), pyObjectCall);
                } catch (PyException e) {
                    FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-96196399220271L), e);
                }
            }
            for (Map.Entry<String, PyObject> entry : this.pluginInstances.entrySet()) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-95861391771183L);
                Map.Entry<String, PyObject> entry2 = entry;
                String key = entry2.getKey();
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-95904341444143L);
                String str = key;
                PyObject value = entry2.getValue();
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-96522816734767L);
                PyObject pyObject3 = value;
                if (getPluginsController().isPluginActive$TMessagesProj(str) && PyObjectUtils.getBoolean(pyObject3, Deobfuscator$exteraGramDev$TMessagesProj.getString(-96591536211503L), false) && PyObjectUtils.getString(pyObject3, Deobfuscator$exteraGramDev$TMessagesProj.getString(-96557176473135L), null) == null) {
                    getPluginsController().getWatchdog().onPluginExecutionStarted(str);
                    try {
                        try {
                            pyObject3.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-96686025492015L), pyObjectCall);
                            pluginsController = getPluginsController();
                        } catch (PyException e2) {
                            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-96733270132271L) + eventType + Deobfuscator$exteraGramDev$TMessagesProj.getString(-96222169024047L) + str, e2);
                            pluginsController = getPluginsController();
                        }
                        pluginsController.getWatchdog().onPluginExecutionFinished(str);
                    } catch (Throwable th) {
                        getPluginsController().getWatchdog().onPluginExecutionFinished(str);
                        throw th;
                    }
                }
            }
            Unit unit = Unit.INSTANCE;
            AutoCloseableKt.closeFinally(pyObjectCall, null);
        } catch (Throwable th2) {
            try {
                throw th2;
            } catch (Throwable th3) {
                AutoCloseableKt.closeFinally(pyObjectCall, th2);
                throw new RuntimeException(th3);
            }
        }
    }

    private final <T> PluginsController.HookResult<T> executeHook(PyObject pluginInstance, T initialValue, Class<T> valueClass, String pyResultKey, PyMethodCaller<T> caller, Utilities.Callback<PyException> errorLogger) throws Exception {
        PluginsController.HookResult<T> hookResult = new PluginsController.HookResult<>(initialValue, false, false);
        if (pluginInstance != null) {
            try {
                PyObject pyObjectCall = caller.call(pluginInstance, initialValue);
                if (pyObjectCall != null) {
                    try {
                        String string = PyObjectUtils.getString(pyObjectCall, Deobfuscator$exteraGramDev$TMessagesProj.getString(-96316658304559L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-96278003598895L));
                        if (string != null && string.endsWith("cancel")) {
                            hookResult = new PluginsController.HookResult<>(null, true, false);
                        } else {
                            if (string != null && (string.endsWith("modify") || string.endsWith("override"))) {
                                PyObject pyObject = (PyObject) pyObjectCall.get((Object) pyResultKey);
                                if (pyObject != null) {
                                    try {
                                        initialValue = (T) pyObject.toJava(valueClass);
                                        AutoCloseableKt.closeFinally(pyObject, null);
                                    } catch (Throwable th) {
                                        AutoCloseableKt.closeFinally(pyObject, th);
                                        throw th;
                                    }
                                }
                                if (string.endsWith("stop")) {
                                    hookResult = new PluginsController.HookResult<>(initialValue, false, true);
                                }
                            }
                            AutoCloseableKt.closeFinally(pyObjectCall, null);
                        }
                        AutoCloseableKt.closeFinally(pyObjectCall, null);
                        return hookResult;
                    } catch (Throwable th3) {
                        try {
                            throw th3;
                        } catch (Throwable th4) {
                            AutoCloseableKt.closeFinally(pyObjectCall, th3);
                            throw th4;
                        }
                    }
                }
            } catch (PyException e) {
                errorLogger.run(e);
            }
        }
        return new PluginsController.HookResult<>(initialValue, false, false);
    }

    private final <T> PluginsController.HookResult<T> executeHook(String pluginId, T initialValue, Class<T> valueClass, String pyResultKey, PyMethodCaller<T> caller, Utilities.Callback<PyException> errorLogger) {
        try { return executeHook(this.pluginInstances.get(pluginId), initialValue, valueClass, pyResultKey, caller, errorLogger); } catch (Exception e) { return new PluginsController.HookResult<>(initialValue, false, false); }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<TLObject> executePreRequestHook(final String requestName, final int account, TLObject request, final String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-94834894587439L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-94955153671727L);
        return executeHook(pluginId, request, (Class<TLObject>) TLObject.class, Deobfuscator$exteraGramDev$TMessagesProj.getString(-94933678835247L), (PyMethodCaller<TLObject>) new PyMethodCaller() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda23
            @Override // com.exteragram.messenger.plugins.PythonPluginsEngine.PyMethodCaller
            public final PyObject call(PyObject pyObject, Object obj) {
                return PythonPluginsEngine.$r8$lambda$3HuOhQN3SQ64XZXjHG3mX3zdIqY(requestName, account, pyObject, (TLObject) obj);
            }
        }, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda24
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-88443983250991L) + pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-88139040572975L) + requestName, (PyException) obj);
            }
        });
    }

    public static PyObject $r8$lambda$3HuOhQN3SQ64XZXjHG3mX3zdIqY(String str, int i, PyObject pyObject, TLObject tLObject) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-88332314101295L);
        return pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-88379558741551L), str, Integer.valueOf(i), tLObject);
    }

    /* JADX WARN: Code duplicated, block: B:39:0x00b5 A[Catch: all -> 0x005b, TRY_LEAVE, TryCatch #4 {all -> 0x005b, blocks: (B:8:0x0027, B:10:0x003b, B:12:0x004b, B:17:0x005e, B:20:0x0077, B:27:0x0082, B:30:0x009b, B:37:0x00a6, B:39:0x00b5, B:35:0x00a2, B:36:0x00a5, B:25:0x007e, B:26:0x0081, B:44:0x00cc, B:33:0x00a0, B:19:0x006f, B:23:0x007c, B:29:0x0093), top: B:61:0x0027, outer: #2, inners: #0, #3, #5, #6 }] */
    /* JADX WARN: Code duplicated, block: B:59:0x006f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:64:0x0093 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.Object, org.telegram.tgnet.TLObject] */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v10 */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v12 */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v14 */
    /* JADX WARN: Type inference failed for: r8v15 */
    /* JADX WARN: Type inference failed for: r8v16 */
    /* JADX WARN: Type inference failed for: r8v17 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Type inference failed for: r8v7, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r8v8 */
    /* JADX WARN: Type inference failed for: r8v9 */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.Object, org.telegram.tgnet.TLRPC$TL_error] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v10 */
    /* JADX WARN: Type inference failed for: r9v11 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v15 */
    /* JADX WARN: Type inference failed for: r9v16 */
    /* JADX WARN: Type inference failed for: r9v17 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v7, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r9v8 */
    /* JADX WARN: Type inference failed for: r9v9 */
    public final PluginsController.HookResult<PluginsHooks.PostRequestResult> executePostRequestHook(String requestName, int account, TLObject response, TLRPC.TL_error error, PyObject pluginInstance) throws Exception {
        TLObject resObj = response;
        TLRPC.TL_error errObj = error;
        if (pluginInstance != null) {
            try {
                PyObject pyObjectCallAttr = pluginInstance.callAttr("on_post_request", requestName, Integer.valueOf(account), response, error);
                if (pyObjectCallAttr != null) {
                    try {
                        String string = PyObjectUtils.getString(pyObjectCallAttr, "action", "continue");
                        if (string != null) {
                            if (string.endsWith("cancel")) {
                                AutoCloseableKt.closeFinally(pyObjectCallAttr, null);
                                return new PluginsController.HookResult<>(new PluginsHooks.PostRequestResult(null, null), true, false);
                            }
                            if (string.endsWith("modify") || string.endsWith("override")) {
                                PyObject pyObject = (PyObject) pyObjectCallAttr.get((Object) "response");
                                if (pyObject != null) {
                                    resObj = (TLObject) pyObject.toJava(TLObject.class);
                                    pyObject.close();
                                }
                                PyObject pyObject2 = (PyObject) pyObjectCallAttr.get((Object) "error");
                                if (pyObject2 != null) {
                                    errObj = (TLRPC.TL_error) pyObject2.toJava(TLRPC.TL_error.class);
                                    pyObject2.close();
                                }
                                boolean stop = string.endsWith("stop");
                                AutoCloseableKt.closeFinally(pyObjectCallAttr, null);
                                return new PluginsController.HookResult<>(new PluginsHooks.PostRequestResult(resObj, errObj), false, stop);
                            }
                        }
                        AutoCloseableKt.closeFinally(pyObjectCallAttr, null);
                    } catch (Throwable th) {
                        AutoCloseableKt.closeFinally(pyObjectCallAttr, th);
                        throw th;
                    }
                }
            } catch (PyException e) {
                FileLog.e(e);
            }
        }
        return new PluginsController.HookResult<>(new PluginsHooks.PostRequestResult(resObj, errObj), false, false);
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<PluginsHooks.PostRequestResult> executePostRequestHook(String requestName, int account, TLObject response, TLRPC.TL_error error, String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-95582218896943L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-95633758504495L);
        try { return executePostRequestHook(requestName, account, response, error, this.pluginInstances.get(pluginId)); } catch (Exception e) { return new PluginsController.HookResult<>(new PluginsHooks.PostRequestResult(response, error), false, false); }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<TLRPC.Update> executeUpdateHook(final String updateName, final int account, TLRPC.Update update, String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-95612283668015L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-95169902036527L);
        return executeHook(pluginId, update, (Class<TLRPC.Update>) TLRPC.Update.class, Deobfuscator$exteraGramDev$TMessagesProj.getString(-95217146676783L), (PyMethodCaller<TLRPC.Update>) new PyMethodCaller() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda3
            @Override // com.exteragram.messenger.plugins.PythonPluginsEngine.PyMethodCaller
            public final PyObject call(PyObject pyObject, Object obj) {
                return PythonPluginsEngine.$r8$lambda$gogZ8_uy9hbJzmvkveYPLtlEcFs(updateName, account, pyObject, (TLRPC.Update) obj);
            }
        }, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda4
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-88199170115119L) + updateName, (PyException) obj);
            }
        });
    }

    public static PyObject $r8$lambda$gogZ8_uy9hbJzmvkveYPLtlEcFs(String str, int i, PyObject pyObject, TLRPC.Update update) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-88164810376751L);
        return pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-88126155671087L), str, Integer.valueOf(i), update);
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<TLRPC.Updates> executeUpdatesHook(final String containerName, final int account, TLRPC.Updates updates, String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-95238621513263L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-95298751055407L);
        return executeHook(pluginId, updates, (Class<TLRPC.Updates>) TLRPC.Updates.class, Deobfuscator$exteraGramDev$TMessagesProj.getString(-95277276218927L), (PyMethodCaller<TLRPC.Updates>) new PyMethodCaller() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda9
            @Override // com.exteragram.messenger.plugins.PythonPluginsEngine.PyMethodCaller
            public final PyObject call(PyObject pyObject, Object obj) {
                return PythonPluginsEngine.$r8$lambda$66nPF4OSjZKgz4yasnWGOzZ1Egk(containerName, account, pyObject, (TLRPC.Updates) obj);
            }
        }, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda10
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-89010918934063L) + containerName, (PyException) obj);
            }
        });
    }

    public static PyObject $r8$lambda$66nPF4OSjZKgz4yasnWGOzZ1Egk(String str, int i, PyObject pyObject, TLRPC.Updates updates) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-88980854162991L);
        return pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-88942199457327L), str, Integer.valueOf(i), updates);
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public PluginsController.HookResult<SendMessagesHelper.SendMessageParams> executeSendMessageHook(final int account, SendMessagesHelper.SendMessageParams params, final String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-95380355434031L);
        return executeHook(pluginId, params, (Class<SendMessagesHelper.SendMessageParams>) SendMessagesHelper.SendMessageParams.class, Deobfuscator$exteraGramDev$TMessagesProj.getString(-95341700728367L), (PyMethodCaller<SendMessagesHelper.SendMessageParams>) new PyMethodCaller() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda18
            @Override // com.exteragram.messenger.plugins.PythonPluginsEngine.PyMethodCaller
            public final PyObject call(PyObject pyObject, Object obj) {
                return PythonPluginsEngine.$r8$lambda$_D6PxQm0hg98qBk_zHO3yV4BGHE(account, pyObject, (SendMessagesHelper.SendMessageParams) obj);
            }
        }, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda19
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-104777743877679L) + pluginId, (PyException) obj);
            }
        });
    }

    public static PyObject $r8$lambda$_D6PxQm0hg98qBk_zHO3yV4BGHE(int i, PyObject pyObject, SendMessagesHelper.SendMessageParams sendMessageParams) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-88705976256047L);
        return pyObject.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-88667321550383L), Integer.valueOf(i), sendMessageParams);
    }

    public final String fetchParameterValue(String filePath, String parameterName) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-93799807469103L);
        if (filePath == null) {
            return null;
        }
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                return parsePluginMetadata(filePath).get(parameterName);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public final Map<String, String> parsePluginMetadata(String filePath) {
        HashMap<String, String> map = new HashMap<>();
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    int readLines = 0;
                    while ((line = reader.readLine()) != null && readLines < 200) {
                        readLines++;
                        line = line.trim();
                        if (line.startsWith("#")) {
                            line = line.substring(1).trim();
                            int colonIdx = line.indexOf(':');
                            if (colonIdx > 0) {
                                String key = line.substring(0, colonIdx).trim().toLowerCase(Locale.ROOT);
                                String value = line.substring(colonIdx + 1).trim();
                                map.put(key, value);
                                if (key.startsWith("meta ")) {
                                    map.put(key.substring(5).trim(), value);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    FileLog.e("Error reading plugin header with Java: " + e.getMessage());
                }

                String filename = file.getName();
                if (filename.endsWith(".plugin")) {
                    filename = filename.substring(0, filename.length() - 7);
                } else if (filename.endsWith(".py")) {
                    filename = filename.substring(0, filename.length() - 3);
                }

                if (!map.containsKey("id")) map.put("id", filename);
                if (!map.containsKey("name")) map.put("name", filename);
                if (!map.containsKey("module")) map.put("module", filename);
                if (!map.containsKey("title")) map.put("title", filename);
            }
        }
        return map;
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public Object getPluginSetting(String pluginId, String key, Object defaultValue) {
        Object java;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-94499887138351L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-94066095441455L);
        ConcurrentHashMap<String, Object> concurrentHashMap = this.settingsCache.get(pluginId);
        if (concurrentHashMap != null && concurrentHashMap.containsKey(key)) {
            return concurrentHashMap.get(key);
        }
        Python python = getPython();
        if (python != null) {
            try {
                PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-94014555833903L));
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-94083275310639L);
                PyObject pyObjectCallAttr = module.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-94207829362223L), pluginId, key, defaultValue);
                if (pyObjectCallAttr != null) {
                    if (defaultValue instanceof Boolean) {
                        java = Boolean.valueOf(pyObjectCallAttr.toBoolean());
                    } else if (defaultValue instanceof Integer) {
                        java = Integer.valueOf(pyObjectCallAttr.toInt());
                    } else if (defaultValue instanceof String) {
                        java = pyObjectCallAttr.toString();
                        Deobfuscator$exteraGramDev$TMessagesProj.getString(-94259368969775L);
                    } else if (defaultValue instanceof Float) {
                        java = Float.valueOf(pyObjectCallAttr.toFloat());
                    } else if (defaultValue instanceof Long) {
                        java = Long.valueOf(pyObjectCallAttr.toLong());
                    } else if (defaultValue == null) {
                        java = pyObjectCallAttr.toJava(Object.class);
                        Deobfuscator$exteraGramDev$TMessagesProj.getString(-92670231070255L);
                    } else {
                        java = pyObjectCallAttr.toJava(defaultValue.getClass());
                        Deobfuscator$exteraGramDev$TMessagesProj.getString(-92653051201071L);
                    }
                    ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> concurrentHashMap2 = this.settingsCache;
                    final Function1 function1 = new Function1() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda25
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return PythonPluginsEngine.$r8$lambda$QERKwSD6yL4J8XJCKdmRa2I5DR8((String) obj);
                        }
                    };
                    ConcurrentHashMap<String, Object> concurrentHashMapComputeIfAbsent = concurrentHashMap2.computeIfAbsent(pluginId, new Function() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda26
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return PythonPluginsEngine.$r8$lambda$T5aUeMCGEOtaNiCJriFIVhbHSkA(function1, obj);
                        }
                    });
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-92704590808623L);
                    concurrentHashMapComputeIfAbsent.put(key, java);
                    return java;
                }
            } catch (PyException e) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-92803375056431L) + pluginId + '/' + key, e);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static ConcurrentHashMap $r8$lambda$QERKwSD6yL4J8XJCKdmRa2I5DR8(String str) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-104898002961967L);
        return new ConcurrentHashMap();
    }

    public static ConcurrentHashMap $r8$lambda$T5aUeMCGEOtaNiCJriFIVhbHSkA(Function1 function1, Object obj) {
        return (ConcurrentHashMap) function1.invoke(obj);
    }

    public static ConcurrentHashMap $r8$lambda$H3KBjR7kR4EblHmumAscDlVGoa4(Function1 function1, Object obj) {
        return (ConcurrentHashMap) function1.invoke(obj);
    }

    public static ConcurrentHashMap $r8$lambda$M02QGUTSwEr0Np6WtwhMk889TKc(String str) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-104971017405999L);
        return new ConcurrentHashMap();
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void setPluginSetting(String pluginId, String key, Object value) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-92382468261423L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-92481252509231L);
        ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> concurrentHashMap = this.settingsCache;
        final Function1 function1 = new Function1() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PythonPluginsEngine.$r8$lambda$M02QGUTSwEr0Np6WtwhMk889TKc((String) obj);
            }
        };
        ConcurrentHashMap<String, Object> concurrentHashMapComputeIfAbsent = concurrentHashMap.computeIfAbsent(pluginId, new Function() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return PythonPluginsEngine.$r8$lambda$H3KBjR7kR4EblHmumAscDlVGoa4(function1, obj);
            }
        });
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-92429712901679L);
        concurrentHashMapComputeIfAbsent.put(key, value);
        Python python = getPython();
        if (python == null) {
            return;
        }
        try {
            PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-92528497149487L));
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-92597216626223L);
            module.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-93202807014959L), pluginId, key, value);
        } catch (PyException e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-93254346622511L) + pluginId + '/' + key, e);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void clearPluginSettings(String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-93451915118127L);
        this.settingsCache.remove(pluginId);
        Python python = getPython();
        if (python == null) {
            return;
        }
        try {
            PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-92949403944495L));
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-93018123421231L);
            module.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-93005238519343L), pluginId);
        } catch (PyException e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-93078252963375L) + pluginId, e);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public Map<String, ?> getAllPluginSettings(String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-91648028853807L);
        Python python = getPython();
        if (python == null) {
            return null;
        }
        try {
            PyObject module = python.getModule(Deobfuscator$exteraGramDev$TMessagesProj.getString(-91626554017327L));
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-91695273494063L);
            PyObject pyObjectCallAttr = module.callAttr(Deobfuscator$exteraGramDev$TMessagesProj.getString(-91751108068911L), pluginId);
            if (pyObjectCallAttr != null) {
                HashMap map = new HashMap();
                Map<PyObject, PyObject> mapAsMap = pyObjectCallAttr.asMap();
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-91282956633647L);
                for (Map.Entry<PyObject, PyObject> entry : mapAsMap.entrySet()) {
                    PyObject key = entry.getKey();
                    PyObject value = entry.getValue();
                    if (key != null) {
                        map.put(key.toString(), value != null ? value.toJava(Object.class) : null);
                    }
                }
                this.settingsCache.put(pluginId, new ConcurrentHashMap<>(map));
                return map;
            }
        } catch (PyException e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-91390330816047L) + pluginId, e);
        }
        return null;
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void showInstallDialog(final BaseFragment fragment, final InstallPluginBottomSheet.PluginInstallParams params) {
        android.util.Log.d("PLUGIN_DEBUG", "PythonPluginsEngine.showInstallDialog: params.getFilePath()=" + (params != null ? params.getFilePath() : "null"));
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public final void run() {
                if (params == null || TextUtils.isEmpty(params.getFilePath())) {
                    android.util.Log.d("PLUGIN_DEBUG", "PythonPluginsEngine: params or filePath is empty!");
                    return;
                }
                File file = new File(params.getFilePath());
                android.util.Log.d("PLUGIN_DEBUG", "PythonPluginsEngine: file=" + file.getAbsolutePath() + ", exists=" + file.exists());
                final PluginsController.PluginValidationResult pluginValidationResultValidatePluginFromFile = validatePluginFromFile(params.getFilePath());
                android.util.Log.d("PLUGIN_DEBUG", "PythonPluginsEngine: validatePluginFromFile result plugin=" + pluginValidationResultValidatePluginFromFile.getPlugin() + ", error=" + pluginValidationResultValidatePluginFromFile.getError());
                if (pluginValidationResultValidatePluginFromFile.getPlugin() != null) {
                    android.util.Log.d("PLUGIN_DEBUG", "PythonPluginsEngine: Showing InstallPluginBottomSheet dialog!");
                    new InstallPluginBottomSheet(fragment, pluginValidationResultValidatePluginFromFile, params).show();
                } else {
                    String err = pluginValidationResultValidatePluginFromFile.getError();
                    android.util.Log.d("PLUGIN_DEBUG", "PythonPluginsEngine: Showing error bulletin: " + err);
                    BulletinFactory.of(fragment).createSimpleBulletin(R.raw.error, err != null ? err : "Error loading plugin").show();
                }
            }
        });
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openPluginSettings(String id, BaseFragment fragment) {
        Plugin plugin = getPluginsController().getPlugins().get(id);
        if (plugin != null) {
            openPluginSettings(plugin, fragment);
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openPluginSettings(final Plugin plugin, final BaseFragment fragment) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public final void run() {
                fragment.presentFragment(new PluginSettingsActivity(plugin));
            }
        });
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openPluginSetting(final Plugin plugin, final String linkAlias, final BaseFragment fragment) {
        PluginsController.INSTANCE.runOnPluginsQueue(new Runnable() {
            @Override
            public final void run() {
                openPluginSettingInternal(plugin, linkAlias, fragment);
            }
        });
    }

    public final void openPluginSettingInternal(Plugin plugin, String str, final BaseFragment baseFragment) {
        final PluginSettingsActivity settingsLinkPrefix;
        if (getPluginsController().isPluginActive$TMessagesProj(plugin)) {
            if (!str.contains("/")) {
                settingsLinkPrefix = new PluginSettingsActivity(plugin, str);
            } else {
                List<SettingItem> list = getPluginsController().getSettings().get(plugin.getId());
                if (list == null) {
                    return;
                }
                String[] strArr = str.split("/");
                int length = strArr.length - 1;
                List<SettingItem> pySettingDefinitions = list;
                TextSetting textSetting = null;
                for (int i = 0; i < length; i++) {
                    String str2 = strArr[i];
                    for (SettingItem next : pySettingDefinitions) {
                        if ((next instanceof TextSetting) && str2.equals(next.getLinkAlias())) {
                            textSetting = (TextSetting) next;
                            break;
                        }
                    }
                }
                if (textSetting == null) {
                    return;
                }
                PluginSettingsActivity pluginSettingsActivity = new PluginSettingsActivity(plugin, textSetting.getText(), pySettingDefinitions, textSetting.getCreateSubFragmentCallback(), strArr[strArr.length - 1]);
                settingsLinkPrefix = pluginSettingsActivity;
            }
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public final void run() {
                    baseFragment.presentFragment(settingsLinkPrefix);
                }
            });
        }
    }

    @Override // com.exteragram.messenger.plugins.PluginsController.PluginsEngine
    public void openPluginSetting(String pluginId, String linkAlias, BaseFragment fragment) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-91811237611055L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-91927201728047L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-91901431924271L);
        Plugin plugin = getPluginsController().getPlugins().get(pluginId);
        if (plugin != null) {
            openPluginSetting(plugin, linkAlias, fragment);
        }
    }

    public static final class Updater {
        private static int TAG;
        private static boolean isLoading;
        private static long lastCheckUpdateTime;
        private static boolean notifyWhenChangeStatus;
        private static int status;

        /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);
        private static final Pattern PYTHON_SDK_APP_VERSION_PATTERN = Pattern.compile(Deobfuscator$exteraGramDev$TMessagesProj.getString(-103433419114031L));
        private static final Pattern PYTHON_SDK_APP_VERSION_CODE_PATTERN = Pattern.compile(Deobfuscator$exteraGramDev$TMessagesProj.getString(-103622397675055L));
        private static final Runnable notifyRunnable = new Runnable() {
            @Override
            public final void run() {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pluginsPySdkInfoChanged);
            }
        };

        @JvmStatic
        public static final void checkUpdates() {
            INSTANCE.checkUpdates();
        }

        @JvmStatic
        public static final void checkUpdates(boolean z) {
            INSTANCE.checkUpdates(z);
        }

        @JvmStatic
        public static final void deleteSdkUpdateFile() {
            INSTANCE.deleteSdkUpdateFile();
        }

        @JvmStatic
        public static final File getPythonCurrentSdkFile() {
            return INSTANCE.getPythonCurrentSdkFile();
        }

        @JvmStatic
        public static final File getPythonSdkUpdateFile() {
            return INSTANCE.getPythonSdkUpdateFile();
        }

        @JvmStatic
        public static final CharSequence getStateString() {
            return INSTANCE.getStateString();
        }

        @JvmStatic
        public static final CharSequence getVersion() {
            return INSTANCE.getVersion();
        }

        @JvmStatic
        public static final String hashBytes(InputStream inputStream) {
            return INSTANCE.hashBytes(inputStream);
        }

        @JvmStatic
        public static final boolean isAppVersionCodeCompatible(String str, String str2) {
            return INSTANCE.isAppVersionCodeCompatible(str, str2);
        }

        @JvmStatic
        public static final boolean isAppVersionCompatible(String str, String str2) {
            return INSTANCE.isAppVersionCompatible(str, str2);
        }

        @JvmStatic
        public static final boolean isSdkFromApk() {
            return INSTANCE.isSdkFromApk();
        }

        @JvmStatic
        public static final boolean isSdkVersionNewer(String str, boolean z) {
            return INSTANCE.isSdkVersionNewer(str, z);
        }

        @JvmStatic
        public static final Companion.PythonSdkUpdateInfo parsePythonSdkUpdateResponse(TLRPC.messages_Messages messages_messages) {
            return INSTANCE.parsePythonSdkUpdateResponse(messages_messages);
        }

        @JvmStatic
        public static final File requestSdkFromApkFile() {
            return INSTANCE.requestSdkFromApkFile();
        }

        @JvmStatic
        public static final void restoreSdkFromApk() {
            INSTANCE.restoreSdkFromApk();
        }

        @JvmStatic
        public static final void savePythonSdkArchive(TLRPC.Message message, TLRPC.Document document) {
            INSTANCE.savePythonSdkArchive(message, document);
        }

        @JvmStatic
        public static final void savePythonSdkArchive(TLRPC.Message message, TLRPC.Document document, boolean z) {
            INSTANCE.savePythonSdkArchive(message, document, z);
        }

        @JvmStatic
        public static final InputStream sdkFromApk() {
            try { return INSTANCE.sdkFromApk(); } catch (Exception e) { return null; }
        }

        @JvmStatic
        public static final void setBuildFromApk(boolean z) {
            INSTANCE.setBuildFromApk(z);
        }

        @JvmStatic
        public static final void zipFolder(File file, File file2) {
            INSTANCE.zipFolder(file, file2);
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final int getStatus() {
                return Updater.status;
            }

            public final void setStatus(int i) {
                Updater.status = i;
            }

            public final boolean getNotifyWhenChangeStatus() {
                return Updater.notifyWhenChangeStatus;
            }

            public final void setNotifyWhenChangeStatus(boolean z) {
                Updater.notifyWhenChangeStatus = z;
            }

            @JvmStatic
            public final CharSequence getVersion() {
                String sdk_version = (ExteraConfig.getPluginsEngine() && PythonPluginsEngine.sdkInitialized) ? PythonPluginsEngine.INSTANCE.getSDK_VERSION() : null;
                boolean sdk_beta = PythonPluginsEngine.INSTANCE.getSDK_BETA();
                if (sdk_version == null && PythonPluginsEngine.SDK_DIR != null) {
                    File file = new File(PythonPluginsEngine.SDK_DIR, Deobfuscator$exteraGramDev$TMessagesProj.getString(-55188051478063L));
                    if (file.exists()) {
                        String string = StringsKt.trim((CharSequence) SimpliFile.readText$default(SimpliFiles.file(file), 65536L, null, 2, null)).toString();
                        sdk_beta = string.endsWith("-dev");
                        int pipeIdx = string.indexOf('|');
                        sdk_version = pipeIdx != -1 ? string.substring(0, pipeIdx) : string;
                    }
                }
                if (sdk_version == null) {
                    return "v.txt not found";
                }
                StringBuilder sb = new StringBuilder();
                sb.append(sdk_version);
                sb.append(sdk_beta ? "-dev" : "");
                return sb.toString();
            }

            @JvmStatic
            public final CharSequence getStateString() {
                int status = getStatus();
                if (status == 0) {
                    return getVersion();
                }
                if (status == 1) {
                    return "Checking for updates...";
                }
                if (status != 2) {
                    if (status == 3) {
                        return "Downloading update...";
                    }
                    if (status != 4) {
                        return null;
                    }
                    return "Restart plugin system to apply update";
                }
                return "Latest version installed (" + getVersion() + ")";
            }

            /* JADX INFO: loaded from: classes4.dex */
            public static final class PythonSdkUpdateInfo extends TLRPC.TL_help_appUpdate {
                private String abi;
                private String appVersion;
                private String appVersionCode;
                private String appVersionCodeOperator;
                private String appVersionOperator;
                private boolean available;
                private String channel;
                private TLRPC.Message message;

                public PythonSdkUpdateInfo() {
                    clear();
                }

                public final TLRPC.Message getMessage() {
                    return this.message;
                }

                public final void setMessage(TLRPC.Message message) {
                    this.message = message;
                }

                public final boolean getAvailable() {
                    return this.available;
                }

                public final void setAvailable(boolean z) {
                    this.available = z;
                }

                public final String getChannel() {
                    return this.channel;
                }

                public final void setChannel(String str) {
                    this.channel = str;
                }

                public final String getAppVersionOperator() {
                    return this.appVersionOperator;
                }

                public final void setAppVersionOperator(String str) {
                    this.appVersionOperator = str;
                }

                public final String getAppVersion() {
                    return this.appVersion;
                }

                public final void setAppVersion(String str) {
                    this.appVersion = str;
                }

                public final String getAppVersionCodeOperator() {
                    return this.appVersionCodeOperator;
                }

                public final void setAppVersionCodeOperator(String str) {
                    this.appVersionCodeOperator = str;
                }

                public final String getAppVersionCode() {
                    return this.appVersionCode;
                }

                public final void setAppVersionCode(String str) {
                    this.appVersionCode = str;
                }

                public final String getAbi() {
                    return this.abi;
                }

                public final void setAbi(String str) {
                    this.abi = str;
                }

                public final void clear() {
                    this.message = null;
                    this.available = false;
                    this.can_not_skip = false;
                    this.channel = null;
                    this.version = null;
                    this.appVersion = null;
                    this.appVersionOperator = null;
                    this.appVersionCode = null;
                    this.appVersionCodeOperator = null;
                    this.document = null;
                    this.abi = null;
                }

                public final boolean canInstall() {
                    String str = this.appVersion;
                    String str2 = this.appVersionOperator;
                    String str3 = this.appVersionCode;
                    String str4 = this.appVersionCodeOperator;
                    String str5 = this.version;
                    return (str == null || str2 == null || Updater.INSTANCE.isAppVersionCompatible(str2, str)) && (str3 == null || str4 == null || Updater.INSTANCE.isAppVersionCodeCompatible(str4, str3)) && ((str5 == null || Updater.INSTANCE.isSdkVersionNewer(str5, Intrinsics.areEqual(this.channel, Deobfuscator$exteraGramDev$TMessagesProj.getString(-63120856073775L)))) && this.document != null && Intrinsics.areEqual(this.abi, Build.SUPPORTED_ABIS[0]));
                }
            }

            @JvmStatic
            public final InputStream sdkFromApk() throws IOException {
                InputStream inputStreamOpen = ApplicationLoader.applicationContext.getAssets().open(Deobfuscator$exteraGramDev$TMessagesProj.getString(-55892426114607L) + Build.SUPPORTED_ABIS[0] + Deobfuscator$exteraGramDev$TMessagesProj.getString(-55965440558639L));
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-56064224806447L);
                return inputStreamOpen;
            }

            @JvmStatic
            public final boolean isSdkFromApk() {
                return new File(new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-56038455002671L)), Deobfuscator$exteraGramDev$TMessagesProj.getString(-55587483436591L)).exists() || requestSdkFromApkFile().exists();
            }

            @JvmStatic
            public final void setBuildFromApk(boolean fromApk) {
                File file = new File(new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-55608958273071L)), Deobfuscator$exteraGramDev$TMessagesProj.getString(-55707742520879L));
                if (file.exists() && !fromApk) {
                    PythonPluginsEngine.INSTANCE.deleteFileIfExists(file);
                }
                if (file.exists() || !fromApk) {
                    return;
                }
                touchFile(file);
            }

            @JvmStatic
            public final String hashBytes(InputStream inputStream) {
                int i;
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-55797936834095L);
                try {
                    try {
                        MessageDigest messageDigest = MessageDigest.getInstance(Deobfuscator$exteraGramDev$TMessagesProj.getString(-54200208999983L));
                        byte[] bArr = new byte[1048576];
                        while (true) {
                            int i2 = inputStream.read(bArr);
                            if (i2 <= 0) {
                                break;
                            }
                            messageDigest.update(bArr, 0, i2);
                        }
                        byte[] bArrDigest = messageDigest.digest();
                        StringBuilder sb = new StringBuilder(bArrDigest.length * 2);
                        for (byte b2 : bArrDigest) {
                            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                            String str = String.format(Deobfuscator$exteraGramDev$TMessagesProj.getString(-54157259327023L), Arrays.copyOf(new Object[]{Byte.valueOf(b2)}, 1));
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-54170144228911L);
                            sb.append(str);
                        }
                        String string = sb.toString();
                        Deobfuscator$exteraGramDev$TMessagesProj.getString(-54221683836463L);
                        CloseableKt.closeFinally(inputStream, null);
                        return string;
                    } catch (Throwable th) {
                        try {
                            throw th;
                        } catch (Throwable th2) {
                            CloseableKt.closeFinally(inputStream, th);
                            throw th2;
                        }
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                    return null;
                }
            }

            @JvmStatic
            public final File getPythonSdkUpdateFile() {
                return new File(new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-54350532855343L)), Deobfuscator$exteraGramDev$TMessagesProj.getString(-54397777495599L));
            }

            @JvmStatic
            public final File getPythonCurrentSdkFile() {
                return new File(new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-54419252332079L)), Deobfuscator$exteraGramDev$TMessagesProj.getString(-53916741158447L));
            }

            @JvmStatic
            public final File requestSdkFromApkFile() {
                return new File(new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-53886676387375L)), Deobfuscator$exteraGramDev$TMessagesProj.getString(-54002640504367L));
            }

            @JvmStatic
            public final void deleteSdkUpdateFile() {
                File pythonSdkUpdateFile = getPythonSdkUpdateFile();
                if (pythonSdkUpdateFile.exists()) {
                    PythonPluginsEngine.INSTANCE.deleteFileIfExists(pythonSdkUpdateFile);
                    updateStatus(0);
                }
            }

            @JvmStatic
            public final void checkUpdates() {
                checkUpdates(false);
            }

            @JvmStatic
            public final void checkUpdates(boolean force) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                if ((getStatus() != 1 || Math.abs(jCurrentTimeMillis - Updater.lastCheckUpdateTime) >= 6000) && getStatus() <= 2) {
                    if (!force) {
                        boolean z = Math.abs(jCurrentTimeMillis - ExteraConfig.getSdkUpdateScheduleTimestamp()) < 3600000L;
                        if (!ExteraConfig.getPluginsEngine() || ExteraConfig.getPluginsSafeMode() || z) {
                            return;
                        }
                    }
                    ExteraConfig.setSdkUpdateScheduleTimestamp(jCurrentTimeMillis);
                    updateStatus(1);
                    Updater.lastCheckUpdateTime = jCurrentTimeMillis;
                    RemoteUtils.searchMessages(Deobfuscator$exteraGramDev$TMessagesProj.getString(-54054180111919L), new TLRPC.TL_inputMessagesFilterDocument(), new Utilities.Callback2() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$Companion$$ExternalSyntheticLambda1
                        @Override // org.telegram.messenger.Utilities.Callback2
                        public final void run(Object obj, Object obj2) {
                            PythonPluginsEngine.Updater.Companion.m1321$r8$lambda$us7HlP4jQnbH6ikxQnBYzBZPGU((TLRPC.messages_Messages) obj, (TLRPC.TL_error) obj2);
                        }
                    }, 3000);
                }
            }

            /* JADX INFO: renamed from: $r8$lambda$us7HlP4jQnbH-6ikxQnBYzBZPGU, reason: not valid java name */
            public static void m1321$r8$lambda$us7HlP4jQnbH6ikxQnBYzBZPGU(TLRPC.messages_Messages messages_messages, TLRPC.TL_error tL_error) {
                Companion companion;
                final PythonSdkUpdateInfo pythonSdkUpdateResponse;
                boolean z = false;
                if (tL_error != null) {
                    FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-53409935017519L) + tL_error.text);
                } else if (messages_messages != null && (pythonSdkUpdateResponse = (companion = Updater.INSTANCE).parsePythonSdkUpdateResponse(messages_messages)) != null) {
                    if (!ExteraConfig.getPluginsPySdkAutoUpdate() && !pythonSdkUpdateResponse.can_not_skip) {
                        final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                        if (safeLastFragment != null) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$Companion$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    PythonPluginsEngine.Updater.Companion.checkUpdates$lambda$0$0(safeLastFragment, pythonSdkUpdateResponse);
                                }
                            });
                        }
                        z = true;
                    }
                    if (!z) {
                        try {
                            companion.savePythonSdkArchive(pythonSdkUpdateResponse.getMessage(), pythonSdkUpdateResponse.document);
                            z = true;
                        } catch (Exception e) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(Deobfuscator$exteraGramDev$TMessagesProj.getString(-53775007237679L));
                            sb.append(pythonSdkUpdateResponse.getChannel());
                            sb.append(Deobfuscator$exteraGramDev$TMessagesProj.getString(-53341215540783L));
                            TLRPC.Message message = pythonSdkUpdateResponse.getMessage();
                            sb.append(message != null ? Integer.valueOf(message.id) : null);
                            sb.append(')');
                            FileLog.e(sb.toString(), e);
                        }
                    }
                }
                if (z) {
                    return;
                }
                Updater.INSTANCE.updateStatus(2);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static final void checkUpdates$lambda$0$0(BaseFragment baseFragment, PythonSdkUpdateInfo pythonSdkUpdateInfo) {
                FileLog.d("Python SDK Update available: " + (pythonSdkUpdateInfo != null ? pythonSdkUpdateInfo.version : ""));
            }

            @JvmStatic
            public final void restoreSdkFromApk() {
                touchFile(requestSdkFromApkFile());
            }

            private final void touchFile(File file) {
                try {
                    file.createNewFile();
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final void updateStatus(int newStatus) {
                setStatus(newStatus);
                if (getNotifyWhenChangeStatus()) {
                    AndroidUtilities.cancelRunOnUIThread(Updater.notifyRunnable);
                    AndroidUtilities.runOnUIThread(Updater.notifyRunnable, newStatus == 1 ? 0L : 600L);
                }
            }

            @JvmStatic
            public final PythonSdkUpdateInfo parsePythonSdkUpdateResponse(TLRPC.messages_Messages res) {
                PythonSdkUpdateInfo pythonSdkUpdateInfo = new PythonSdkUpdateInfo();
                Iterator<TLRPC.Message> it = res.messages.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    TLRPC.Message next = it.next();
                    if (next instanceof TLRPC.TL_message) {
                        TLRPC.TL_message tL_message = (TLRPC.TL_message) next;
                        if (!TextUtils.isEmpty(tL_message.message) && (tL_message.media instanceof TLRPC.TL_messageMediaDocument)) {
                            String str = tL_message.message;
                            boolean zContains$default = str.contains("STABLE");
                            String str2 = tL_message.message;
                            boolean zContains$default2 = str2.contains("BETA");
                            if (zContains$default || zContains$default2) {
                                if (!zContains$default2 || ExteraConfig.getPluginsPySdkBetaVersions()) {
                                    StringBuilder sb = new StringBuilder();
                                    String str3 = tL_message.message;
                                    String[] lines = str3.split("\n");
                                    boolean z = false;
                                    for (String rawLine : lines) {
                                        String string = rawLine.trim();
                                        if (!TextUtils.isEmpty(string) || !z) {
                                            if (string.startsWith("---")) {
                                                pythonSdkUpdateInfo.setChannel(zContains$default2 ? "beta" : "stable");
                                                z = true;
                                            } else if (z) {
                                                Matcher matcher = Updater.PYTHON_SDK_APP_VERSION_PATTERN.matcher(string);
                                                if (!matcher.matches()) {
                                                    Matcher matcher2 = Updater.PYTHON_SDK_APP_VERSION_CODE_PATTERN.matcher(string);
                                                    if (matcher2.matches()) {
                                                        pythonSdkUpdateInfo.setAppVersionCodeOperator(matcher2.group(1));
                                                        String strGroup = matcher2.group(2);
                                                        pythonSdkUpdateInfo.setAppVersionCode(strGroup != null ? StringsKt.trim((CharSequence) strGroup).toString() : null);
                                                     } else {
                                                         String[] listSplit$default = string.split("=", 2);
                                                         if (listSplit$default.length == 2) {
                                                             String string2 = listSplit$default[0].trim();
                                                             String string3 = listSplit$default[1].trim();
                                                             int iHashCode = string2.hashCode();
                                                             if (iHashCode != -1085916422) {
                                                                 if (iHashCode != 96360) {
                                                                     if (iHashCode == 351608024 && string2.equals("version")) {
                                                                         pythonSdkUpdateInfo.version = string3;
                                                                     }
                                                                 } else if (string2.equals("abi")) {
                                                                     pythonSdkUpdateInfo.setAbi(string3);
                                                                 }
                                                             } else if (string2.equals("can_not_skip")) {
                                                                 pythonSdkUpdateInfo.can_not_skip = Boolean.parseBoolean(string3);
                                                             }
                                                         }
                                                     }
                                                 } else {
                                                     pythonSdkUpdateInfo.setAppVersionOperator(matcher.group(1));
                                                     String strGroup2 = matcher.group(2);
                                                     pythonSdkUpdateInfo.setAppVersion(strGroup2 != null ? strGroup2.trim() : null);
                                                 }
                                             } else {
                                                 sb.append(string);
                                                 sb.append("\n");
                                             }
                                         }
                                     }
                                     pythonSdkUpdateInfo.document = tL_message.media.document;
                                     if (!pythonSdkUpdateInfo.canInstall()) {
                                         pythonSdkUpdateInfo.clear();
                                     } else {
                                         pythonSdkUpdateInfo.text = sb.toString();
                                         ArrayList<TLRPC.MessageEntity> arrayList = new ArrayList<>();
                                         for (TLRPC.MessageEntity messageEntity : tL_message.entities) {
                                             if (!(messageEntity instanceof TLRPC.TL_messageEntityPre)) {
                                                 arrayList.add(messageEntity);
                                             }
                                         }
                                         pythonSdkUpdateInfo.entities = arrayList;
                                         pythonSdkUpdateInfo.setMessage(next);
                                         break;
                                     }
                                 }
                             }
                        }
                    }
                }
                if (pythonSdkUpdateInfo.getMessage() == null) {
                    return null;
                }
                pythonSdkUpdateInfo.setAvailable((pythonSdkUpdateInfo.document == null || TextUtils.isEmpty(pythonSdkUpdateInfo.version)) ? false : true);
                return pythonSdkUpdateInfo;
            }

            @JvmStatic
            public final boolean isSdkVersionNewer(String remoteVersion, boolean isBeta) {
                if (!ExteraConfig.getPluginsPySdkBetaVersions() && PythonPluginsEngine.INSTANCE.getSDK_BETA()) {
                    return !isBeta;
                }
                PythonPluginsEngine.Companion companion = PythonPluginsEngine.INSTANCE;
                if (companion.getSDK_VERSION() != null) {
                    return AppUtils.compareVersions("0.0.1", remoteVersion, companion.getSDK_VERSION());
                }
                return false;
            }

            @JvmStatic
            public final boolean isAppVersionCompatible(String operator, String targetVersion) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-53074927568431L);
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-53173711816239L);
                return AppUtils.compareVersions(operator, BuildVars.BUILD_VERSION_STRING, targetVersion);
            }

            @JvmStatic
            public final boolean isAppVersionCodeCompatible(String operator, String targetVersion) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-53233841358383L);
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-53212366521903L);
                return AppUtils.compareVersions(operator, String.valueOf(org.telegram.messenger.BuildConfig.VERSION_CODE), targetVersion);
            }

            @JvmStatic
            public final void zipFolder(File sourceDir, File zipFile) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-53272496064047L);
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-52834409399855L);
                SimpliFiles.directory(sourceDir).zipTo(zipFile, OverwritePolicy.REPLACE);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final void copyArchiveToPluginsDirectory(TLRPC.Document document, boolean autoRestartEngine) {
                File pythonSdkUpdateFile = getPythonSdkUpdateFile();
                try {
                    File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(document);
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-52800049661487L);
                    SimpliFiles.file(pathToAttach).copyTo(pythonSdkUpdateFile, OverwritePolicy.REPLACE);
                    if (autoRestartEngine) {
                        PluginsController.INSTANCE.getInstance().restart();
                    } else {
                        updateStatus(4);
                    }
                } catch (Exception e) {
                    FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-52950373516847L), e);
                    updateStatus(2);
                }
                Updater.isLoading = false;
            }

            @JvmStatic
            public final void savePythonSdkArchive(TLRPC.Message msg, TLRPC.Document document) {
                savePythonSdkArchive(msg, document, false);
            }

            @JvmStatic
            public final void savePythonSdkArchive(TLRPC.Message msg, final TLRPC.Document document, final boolean autoRestartEngine) {
                if (Updater.isLoading || msg == null || document == null) {
                    return;
                }
                MessageObject messageObject = new MessageObject(UserConfig.selectedAccount, msg, false, true);
                Updater.isLoading = true;
                updateStatus(3);
                if (!messageObject.mediaExists) {
                    Updater.TAG = DownloadController.getInstance(UserConfig.selectedAccount).generateObserverTag();
                    FileLoader.getInstance(UserConfig.selectedAccount).loadFile(document, messageObject, 1, 0);
                    DownloadController.getInstance(UserConfig.selectedAccount).addLoadingFileObserver(FileLoader.getAttachFileName(document), messageObject, new DownloadController.FileDownloadProgressListener() { // from class: com.exteragram.messenger.plugins.PythonPluginsEngine$Updater$Companion$savePythonSdkArchive$1
                        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                        public void onProgressDownload(String fileName, long downloadSize, long totalSize) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-65362829002287L);
                        }

                        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                        public void onProgressUpload(String fileName, long downloadSize, long totalSize, boolean isEncrypted) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-64929037305391L);
                        }

                        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                        public void onFailedDownload(String fileName, boolean canceled) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-65148080637487L);
                            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-65264044754479L));
                            PythonPluginsEngine.Updater.isLoading = false;
                            PythonPluginsEngine.Updater.INSTANCE.updateStatus(2);
                        }

                        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                        public void onSuccessDownload(String fileName) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-65401483707951L);
                            PythonPluginsEngine.Updater.INSTANCE.copyArchiveToPluginsDirectory(document, autoRestartEngine);
                        }

                        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
                        public int getObserverTag() {
                            return PythonPluginsEngine.Updater.TAG;
                        }
                    });
                    return;
                }
                copyArchiveToPluginsDirectory(document, autoRestartEngine);
            }
        }

        private Updater() {
        }
    }
}
