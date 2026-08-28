package com.chaquo.python.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import com.chaquo.python.Python;
import com.chaquo.python.internal.Common;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import okhttp3.internal.url._UrlKt;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.FileLog;

/* JADX INFO: loaded from: classes.dex */
public class AndroidPlatform extends Python.Platform {
    public static String ABI;
    private AssetManager am;
    private JSONObject buildJson;
    public Application mContext;
    private SharedPreferences sp;
    private static final String[] OBSOLETE_FILES = {"app.zip", "requirements.zip", "chaquopy.mp3", "stdlib.mp3", "chaquopy.zip", "lib-dynload", "stdlib.zip", "bootstrap.zip", "stdlib-common.zip", "ticket.txt"};
    private static final String[] OBSOLETE_CACHE = {"AssetFinder"};

    public native void redirectStdioToLogcat();

    public AndroidPlatform(Context context) {
        FileLog.d("[AndroidPlatform] constructor: initializing AndroidPlatform...");
        Application application = (Application) context.getApplicationContext();
        this.mContext = application;
        this.sp = application.getSharedPreferences(Common.ASSET_DIR, 0);
        this.am = this.mContext.getAssets();
        try {
            this.buildJson = new JSONObject(streamToString(this.am.open("chaquopy/build.json")));
            FileLog.d("[AndroidPlatform] build.json loaded successfully. python_version=" + this.buildJson.optString("python_version"));
            loadNativeLibs();
            FileLog.d("[AndroidPlatform] loadNativeLibs finished successfully.");
            try {
                redirectStdioToLogcat();
                FileLog.d("[AndroidPlatform] redirectStdioToLogcat initialized.");
            } catch (Throwable t) {
                FileLog.e("[AndroidPlatform] redirectStdioToLogcat error", t);
            }
            for (String str : Build.SUPPORTED_ABIS) {
                try {
                    InputStream testStream = this.am.open("chaquopy/" + Common.assetZip(Common.ASSET_STDLIB, str));
                    testStream.close();
                    ABI = str;
                    FileLog.d("[AndroidPlatform] found supported ABI asset for: " + ABI);
                    break;
                } catch (IOException unused) {
                }
            }
            if (ABI != null) {
                return;
            }
            throw new RuntimeException("None of this device's ABIs " + Arrays.toString(Build.SUPPORTED_ABIS) + " are supported by this app.");
        } catch (Throwable e) {
            FileLog.e("[AndroidPlatform] constructor failed", e);
            throw new RuntimeException(e);
        }
    }

    public Application getApplication() {
        return this.mContext;
    }

    @Override // com.chaquo.python.Python.Platform
    public String getPath() {
        FileLog.d("[AndroidPlatform] getPath() called, ABI=" + ABI);
        String str = this.mContext.getFilesDir() + "/chaquopy";
        ArrayList<String> pathList = new ArrayList<>(Arrays.asList(
            Common.assetZip(Common.ASSET_STDLIB, Common.ABI_COMMON),
            Common.assetZip(Common.ASSET_BOOTSTRAP),
            "bootstrap-native/" + ABI
        ));
        String strConcat = _UrlKt.FRAGMENT_ENCODE_SET;
        for (int i = 0; i < pathList.size(); i++) {
            strConcat = strConcat + str + "/" + pathList.get(i);
            if (i < pathList.size() - 1) {
                strConcat = strConcat.concat(":");
            }
        }
        ArrayList<String> extractList = new ArrayList<>(pathList);
        Collections.addAll(extractList,
            Common.ASSET_CACERT,
            Common.assetZip(Common.ASSET_APP),
            Common.assetZip(Common.ASSET_REQUIREMENTS, Common.ABI_COMMON),
            Common.assetZip(Common.ASSET_REQUIREMENTS, ABI),
            Common.assetZip(Common.ASSET_STDLIB, ABI)
        );
        try {
            deleteObsolete(this.mContext.getFilesDir(), OBSOLETE_FILES);
            deleteObsolete(this.mContext.getCacheDir(), OBSOLETE_CACHE);
            FileLog.d("[AndroidPlatform] getPath: extracting assets for " + extractList);
            extractAssets(extractList);
            FileLog.d("[AndroidPlatform] getPath: extractAssets completed successfully! Python path=" + strConcat);
            return strConcat;
        } catch (Throwable e) {
            FileLog.e("[AndroidPlatform] getPath failed", e);
            throw new RuntimeException(e);
        }
    }

    private void deleteObsolete(File file, String[] strArr) {
        if (file == null || !file.exists()) {
            return;
        }
        for (String str : strArr) {
            deleteRecursive(new File(file, "chaquopy/" + str.replace("<abi>", ABI != null ? ABI : "")));
        }
    }

    @Override // com.chaquo.python.Python.Platform
    public void onStart(Python python) {
        try {
            FileLog.d("[AndroidPlatform] onStart: initializing java.android module...");
            python.getModule("java.android").callAttr("initialize", this.mContext, this.buildJson, new String[]{Common.ASSET_APP, Common.ASSET_REQUIREMENTS, "stdlib-" + ABI});
            FileLog.d("[AndroidPlatform] onStart: java.android module initialized successfully.");
        } catch (Throwable t) {
            FileLog.e("[AndroidPlatform] onStart failed", t);
            throw t;
        }
    }

    private void extractAssets(List<String> list) throws JSONException, IOException {
        JSONObject jSONObject = this.buildJson.getJSONObject("assets");
        HashSet<String> hashSet = new HashSet<>(list);
        HashSet<String> hashSet2 = new HashSet<>();
        SharedPreferences.Editor editorEdit = this.sp.edit();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            for (String next2 : list) {
                if (next.equals(next2) || next.startsWith(next2 + "/")) {
                    extractAsset(jSONObject, editorEdit, next);
                    hashSet.remove(next2);
                    if (next.startsWith(next2 + "/")) {
                        hashSet2.add(next2);
                    }
                    break;
                }
            }
        }
        if (!hashSet.isEmpty()) {
            FileLog.e("[AndroidPlatform] extractAssets: missing assets in build.json: " + hashSet);
            throw new RuntimeException("Missing assets in build.json: " + hashSet);
        }
        for (String str : hashSet2) {
            cleanExtractedDir(str, jSONObject);
        }
        editorEdit.apply();
    }

    private void extractAsset(JSONObject jSONObject, SharedPreferences.Editor editor, String str) throws JSONException, IOException {
        String str2 = "chaquopy/" + str;
        File file = new File(this.mContext.getFilesDir(), str2);
        String str3 = "asset." + str;
        String string = jSONObject.getString(str);
        if (file.exists() && this.sp.getString(str3, _UrlKt.FRAGMENT_ENCODE_SET).equals(string)) {
            return;
        }
        file.delete();
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        File file2 = new File(parentFile, file.getName() + ".tmp");
        file2.delete();
        try (InputStream inputStreamOpen = this.am.open(str2);
             FileOutputStream fileOutputStream = new FileOutputStream(file2)) {
            transferStream(inputStreamOpen, fileOutputStream);
        }
        if (!file2.renameTo(file)) {
            file.delete();
            if (!file2.renameTo(file)) {
                try (InputStream in = new FileInputStream(file2);
                     OutputStream out = new FileOutputStream(file)) {
                    transferStream(in, out);
                }
                file2.delete();
            }
        }
        editor.putString(str3, string);
    }

    private void cleanExtractedDir(String str, JSONObject jSONObject) {
        File file = new File(this.mContext.getFilesDir(), "chaquopy/" + str);
        String[] list = file.list();
        if (list == null) {
            return;
        }
        for (String str2 : list) {
            File file2 = new File(file, str2);
            if (file2.isDirectory()) {
                cleanExtractedDir(str + "/" + str2, jSONObject);
            } else {
                if (!jSONObject.has(str + "/" + str2)) {
                    file2.delete();
                }
            }
        }
    }

    private void deleteRecursive(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles != null) {
            for (File file2 : fileArrListFiles) {
                deleteRecursive(file2);
            }
        }
        file.delete();
    }

    private void transferStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1048576];
        int i;
        while ((i = inputStream.read(bArr)) != -1) {
            outputStream.write(bArr, 0, i);
        }
    }

    private String streamToString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    private void loadNativeLibs() {
        for (String str : Arrays.asList(Common.ASSET_DIR, "python")) {
            System.loadLibrary("crypto_" + str);
            System.loadLibrary("ssl_" + str);
            System.loadLibrary("sqlite3_" + str);
        }
        try {
            System.loadLibrary("python" + this.buildJson.getString("python_version"));
        } catch (Exception unused) {
            System.loadLibrary("python3.11");
        }
        System.loadLibrary("chaquopy_java");
    }
}
