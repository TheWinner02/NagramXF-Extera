package com.exteragram.messenger.plugins.hooks;

import de.robv.android.xposed.XC_MethodHook;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.FileLog;

public final class XposedHookRecord implements HookRecord {
    public static final Companion INSTANCE = new Companion();
    private static final Map<AutoCloseable, Integer> callbackReferences = Collections.synchronizedMap(new IdentityHashMap<>());
    private final AtomicBoolean cleanedUp = new AtomicBoolean();
    private final XC_MethodHook.Unhook unhookObject;

    public XposedHookRecord(XC_MethodHook.Unhook unhook) {
        this.unhookObject = unhook;
        XC_MethodHook callback = unhook != null ? unhook.getCallback() : null;
        INSTANCE.retainCallback(callback instanceof AutoCloseable ? (AutoCloseable) callback : null);
    }

    @Override
    public void cleanup() {
        if (this.cleanedUp.compareAndSet(false, true) && this.unhookObject != null) {
            try {
                this.unhookObject.unhook();
            } catch (Throwable th) {
                FileLog.e(th);
            } finally {
                XC_MethodHook callback = this.unhookObject.getCallback();
                try {
                    INSTANCE.releaseCallback(callback instanceof AutoCloseable ? (AutoCloseable) callback : null);
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
    }

    @Override
    public boolean matches(Object criteria) {
        return (criteria instanceof XC_MethodHook.Unhook) && this.unhookObject == criteria;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other != null && getClass() == other.getClass() && this.unhookObject == ((XposedHookRecord) other).unhookObject;
    }

    public int hashCode() {
        return this.unhookObject != null ? this.unhookObject.hashCode() : 0;
    }

    public static final class Companion {
        private Companion() {}

        /* JADX INFO: Access modifiers changed from: private */
        public final void retainCallback(AutoCloseable callback) {
            if (callback == null) {
                return;
            }
            synchronized (XposedHookRecord.callbackReferences) {
                Integer num = XposedHookRecord.callbackReferences.get(callback);
                XposedHookRecord.callbackReferences.put(callback, (num != null ? num : 0) + 1);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void releaseCallback(AutoCloseable callback) throws Exception {
            boolean close = false;
            if (callback == null) {
                return;
            }
            synchronized (XposedHookRecord.callbackReferences) {
                Integer num = XposedHookRecord.callbackReferences.get(callback);
                int count = num != null ? num : 0;
                if (count <= 1) {
                    XposedHookRecord.callbackReferences.remove(callback);
                    close = true;
                } else {
                    XposedHookRecord.callbackReferences.put(callback, count - 1);
                }
            }
            if (close) {
                callback.close();
            }
        }
    }
}
