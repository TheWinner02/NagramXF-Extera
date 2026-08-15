package com.exteragram.messenger.plugins.utils;

import android.content.SharedPreferences;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nNativeCrashHandler.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NativeCrashHandler.kt\ncom/exteragram/messenger/plugins/utils/NativeCrashHandler\n+ 2 SharedPreferences.kt\nandroidx/core/content/SharedPreferencesKt\n*L\n1#1,48:1\n41#2,12:49\n*S KotlinDebug\n*F\n+ 1 NativeCrashHandler.kt\ncom/exteragram/messenger/plugins/utils/NativeCrashHandler\n*L\n39#1:49,12\n*E\n"})
public final class NativeCrashHandler {
    private static final String CRASH_FLAG_FILENAME = Deobfuscator$exteraGramDev$TMessagesProj.getString(-101028237428271L);
    public static final NativeCrashHandler INSTANCE = new NativeCrashHandler();

    @JvmStatic
    public static final native void init(String flagPath);

    private NativeCrashHandler() {
    }

    @JvmStatic
    public static final void checkAndHandleNativeCrash() {
        File file = new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-100504251418159L));
        if (file.exists()) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-100581560829487L));
            SharedPreferences.Editor editorEdit = ApplicationLoader.applicationContext.getSharedPreferences(Deobfuscator$exteraGramDev$TMessagesProj.getString(-100207898674735L), 0).edit();
            editorEdit.putBoolean(Deobfuscator$exteraGramDev$TMessagesProj.getString(-100276618151471L), true);
            editorEdit.apply();
            file.delete();
        }
    }

    @JvmStatic
    public static final String getCrashFlagPath() {
        String absolutePath = new File(ApplicationLoader.getFilesDirFixed(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-100938043115055L)).getAbsolutePath();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-101015352526383L);
        return absolutePath;
    }
}
