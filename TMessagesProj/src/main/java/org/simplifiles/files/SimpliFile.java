package org.simplifiles.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SimpliFile {
    private final File file;

    public SimpliFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public boolean delete() {
        return file != null && file.delete();
    }

    public boolean deleteRecursively() {
        if (file == null || !file.exists()) return true;
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    new SimpliFile(child).deleteRecursively();
                }
            }
        }
        return file.delete();
    }

    public SimpliFile create() {
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        return this;
    }

    public boolean clean() {
        if (file != null && file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    new SimpliFile(child).deleteRecursively();
                }
            }
        }
        return true;
    }

    public boolean touch() {
        try {
            if (file != null) {
                if (!file.exists()) {
                    File parent = file.getParentFile();
                    if (parent != null) parent.mkdirs();
                    file.createNewFile();
                }
                file.setLastModified(System.currentTimeMillis());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean copyTo(File dest, OverwritePolicy policy) {
        try {
            if (file == null || dest == null) return false;
            if (dest.exists() && policy == OverwritePolicy.ERROR) return false;
            File parent = dest.getParentFile();
            if (parent != null) parent.mkdirs();
            FileInputStream in = new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(dest);
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean moveTo(File dest, OverwritePolicy policy) {
        if (file == null || dest == null) return false;
        if (dest.exists() && policy == OverwritePolicy.REPLACE) {
            new SimpliFile(dest).deleteRecursively();
        }
        File parent = dest.getParentFile();
        if (parent != null) parent.mkdirs();
        boolean renamed = file.renameTo(dest);
        if (!renamed) {
            boolean copied = copyTo(dest, policy);
            if (copied) deleteRecursively();
            return copied;
        }
        return true;
    }

    public static String readText$default(SimpliFile simpliFile, long maxBytes, Object charset, int mask, Object defaultVal) {
        try {
            if (simpliFile == null || simpliFile.file == null || !simpliFile.file.exists()) return "";
            FileInputStream fis = new FileInputStream(simpliFile.file);
            byte[] data = new byte[(int) Math.min(maxBytes, simpliFile.file.length())];
            int read = fis.read(data);
            fis.close();
            return new String(data, 0, Math.max(0, read), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean writeFrom$default(SimpliFile simpliFile, InputStream inputStream, long offset, int mask, Object defaultVal) {
        try {
            if (simpliFile == null || simpliFile.file == null || inputStream == null) return false;
            File parent = simpliFile.file.getParentFile();
            if (parent != null) parent.mkdirs();
            FileOutputStream fos = new FileOutputStream(simpliFile.file);
            byte[] buf = new byte[8192];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean writeFromAtomic$default(SimpliFile simpliFile, InputStream inputStream, long offset, int mask, Object defaultVal) {
        return writeFrom$default(simpliFile, inputStream, offset, mask, defaultVal);
    }
}
