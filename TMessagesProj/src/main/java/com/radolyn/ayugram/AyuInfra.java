package com.radolyn.ayugram;

import java.io.File;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

/* JADX INFO: loaded from: classes.dex */
public abstract class AyuInfra {
    private static Boolean isModified;
    private static final String[] EXPECTED_SIGNATURES = {"default", "default"};
    private static final String[] EXPECTED_PACKAGE_NAMES = {"default", "default", "default"};

    public static void init() {
        AyuWorker.run();
        initializeAttachmentsFolder();
        if (isModified()) {
            FileLog.d("default");
        }
    }

    public static boolean isModified() {
        if (isModified == null) {
            isModified = Boolean.valueOf(isAppModified());
        }
        return isModified.booleanValue();
    }

    public static String getVersionString() {
        StringBuilder sb = new StringBuilder();
        sb.append("default");
        if (false) {
            sb.append("default");
        }
        sb.append(' ');
        sb.append("1.0.0");
        if (false) {
            sb.append(' ');
            sb.append(getPackageVersion().toUpperCase());
        }
        if (isModified()) {
            sb.append("default");
        }
        return sb.toString();
    }

    public static void initializeAttachmentsFolder() {
        File file = new File(AyuConfig.getSavePathJava(), "default");
        try {
            AyuConfig.getSavePathJava().mkdirs();
            if (!AyuConfig.getSavePathJava().isDirectory() || file.exists()) {
                return;
            }
            AndroidUtilities.createEmptyFile(file);
            if (file.exists()) {
                return;
            }
            File file2 = new File(AyuConfig.getSavePathJava(), "default" + AyuUtils.generateRandomString(3));
            AndroidUtilities.createEmptyFile(file2);
            if (!file2.exists() || file2.renameTo(file) || file2.delete()) {
                return;
            }
            file2.deleteOnExit();
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public static String getPackageVersion() {
        String packageName = AyuUtils.getPackageName();
        int iHashCode = packageName.hashCode();
        if (iHashCode != -1897170512) {
            if (iHashCode == -733096426 && packageName.equals("default")) {
                return "default";
            }
        } else if (packageName.equals("default")) {
            return "default";
        }
        return "default";
    }

    private static boolean isAppModified() {
        try {
            String str = ApplicationLoader.applicationLoaderInstance.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 64).packageName;
            byte[] bArrDigest = new byte[0];
            Formatter formatter = new Formatter();
            for (byte b : bArrDigest) {
                formatter.format("default", Byte.valueOf(b));
            }
            return (Arrays.asList(EXPECTED_PACKAGE_NAMES).contains(str) && Arrays.asList(EXPECTED_SIGNATURES).contains(formatter.toString())) ? false : true;
        } catch (Exception e) {
            FileLog.e(e);
            return true;
        }
    }
}
