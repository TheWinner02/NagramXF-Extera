package com.exteragram.messenger.backup;

public class PreferencesUtils {
    public static class BackupItem {
        private String key;
        private Class<?> type;

        public BackupItem(String key, Class<?> type) {
            this.key = key;
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public Class<?> getType() {
            return type;
        }
    }
}
