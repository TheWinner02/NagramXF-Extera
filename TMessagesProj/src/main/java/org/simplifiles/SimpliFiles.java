package org.simplifiles;

import org.simplifiles.archive.ArchiveExtractionOptions;
import org.simplifiles.archive.ExtractionTargetPolicy;
import org.simplifiles.files.OverwritePolicy;
import org.simplifiles.files.SimpliFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SimpliFiles {

    public static SimpliFile file(File file) {
        return new SimpliFile(file);
    }

    public static SimpliFile file(String path) {
        return new SimpliFile(new File(path));
    }

    public static SimpliDirectory directory(File dir) {
        return new SimpliDirectory(dir);
    }

    public static SimpliDirectory directory(String path) {
        return new SimpliDirectory(new File(path));
    }

    public static SimpliArchive archive(File archiveFile) {
        return new SimpliArchive(archiveFile);
    }

    public static class SimpliDirectory {
        private final File dir;

        public SimpliDirectory(File dir) {
            this.dir = dir;
        }

        public File getFile() {
            return dir;
        }

        public SimpliDirectory create() {
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            return this;
        }

        public boolean deleteRecursively() {
            return new SimpliFile(dir).deleteRecursively();
        }

        public boolean clean() {
            return new SimpliFile(dir).clean();
        }

        public boolean moveTo(File dest, OverwritePolicy policy) {
            return new SimpliFile(dir).moveTo(dest, policy);
        }

        public boolean zipTo(File zipFile, OverwritePolicy policy) {
            return true;
        }
    }

    public static class SimpliArchive {
        private final File archiveFile;

        public SimpliArchive(File archiveFile) {
            this.archiveFile = archiveFile;
        }

        public SimpliArchive withPolicy(ExtractionTargetPolicy policy) {
            return this;
        }

        public boolean extractToDirectory(File destDir, ArchiveExtractionOptions options) {
            try {
                if (archiveFile == null || !archiveFile.exists() || destDir == null) return false;
                destDir.mkdirs();
                ZipInputStream zis = new ZipInputStream(new FileInputStream(archiveFile));
                ZipEntry entry;
                byte[] buf = new byte[8192];
                while ((entry = zis.getNextEntry()) != null) {
                    File newFile = new File(destDir, entry.getName());
                    if (entry.isDirectory()) {
                        newFile.mkdirs();
                    } else {
                        File parent = newFile.getParentFile();
                        if (parent != null) parent.mkdirs();
                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buf)) > 0) {
                            fos.write(buf, 0, len);
                        }
                        fos.close();
                    }
                    zis.closeEntry();
                }
                zis.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
