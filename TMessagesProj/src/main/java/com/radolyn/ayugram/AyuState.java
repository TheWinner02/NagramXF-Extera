package com.radolyn.ayugram;

import android.content.SharedPreferences;
import android.util.LongSparseArray;
import com.exteragram.messenger.backup.PreferencesUtils;
import com.radolyn.ayugram.utils.ThreadSafeLongSparseArray;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: loaded from: classes.dex */
public class AyuState {
    private static final AyuStateVariable allowDeleteDialogs;
    private static final AyuStateVariable automaticallyScheduled;
    private static boolean configLoaded;
    public static SharedPreferences.Editor editor;
    private static final AyuStateVariable hideSelection;
    public static SharedPreferences preferences;
    private static final LongSparseArray<ArrayList<Integer>> messageDeletePermitted = new LongSparseArray<>();
    private static final AtomicInteger fcmCounter = new AtomicInteger(0);
    private static final ThreadSafeLongSparseArray<Integer> terminatedSessions = new ThreadSafeLongSparseArray<>();
    private static final Object sync = new Object();

    static {
        automaticallyScheduled = new AyuStateVariable();
        allowDeleteDialogs = new AyuStateVariable();
        hideSelection = new AyuStateVariable();
        loadConfig();
    }

    public static void loadConfig() {
        synchronized (sync) {
            try {
                if (configLoaded) {
                    return;
                }
                SharedPreferences preferences2 = org.telegram.messenger.ApplicationLoader.applicationContext.getSharedPreferences("ayustate", 0);
                preferences = preferences2;
                editor = preferences2.edit();
                configLoaded = true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void resetGhostMode() {
        AyuStateVariable ayuStateVariable = automaticallyScheduled;
        ayuStateVariable.val = false;
        ayuStateVariable.resetAfter = -1;
    }

    public static void setAllowReadPacket(boolean allow, int accountId) {
        automaticallyScheduled.val = allow;
    }

    public static boolean isDeletePermitted(long dialogId, Integer msgId) {
        if (msgId == null) {
            return false;
        }
        return isDeleteMessagePermitted(dialogId, msgId);
    }

    public static boolean getAllowReadMessage(long j) {
        return AyuGhostConfig.isSendReadMessagePackets(j);
    }

    public static boolean getAllowReadSecretMedia() {
        return !AyuConfig.saveDeletedMessages;
    }

    public static void setAutomaticallyScheduled(boolean z, int i) {
        AyuStateVariable ayuStateVariable = automaticallyScheduled;
        ayuStateVariable.val = z;
        ayuStateVariable.resetAfter = i;
    }

    public static boolean getAutomaticallyScheduled() {
        return automaticallyScheduled.process();
    }

    public static void setAllowDeleteDialogs(boolean z, int i) {
        AyuStateVariable ayuStateVariable = allowDeleteDialogs;
        ayuStateVariable.val = z;
        ayuStateVariable.resetAfter = i;
    }

    public static boolean getAllowDeleteDialogs() {
        return allowDeleteDialogs.process();
    }

    public static void setHideSelection(boolean z, int i) {
        AyuStateVariable ayuStateVariable = hideSelection;
        ayuStateVariable.val = z;
        ayuStateVariable.resetAfter = i;
    }

    public static boolean getHideSelection() {
        return hideSelection.process();
    }

    public static void permitDeleteMessage(long j, int i) {
        LongSparseArray<ArrayList<Integer>> longSparseArray = messageDeletePermitted;
        ArrayList<Integer> arrayList = longSparseArray.get(j);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            longSparseArray.put(j, arrayList);
        }
        arrayList.add(Integer.valueOf(i));
    }

    public static boolean isDeleteMessagePermitted(long j, int i) {
        ArrayList<Integer> arrayList = messageDeletePermitted.get(j);
        if (arrayList == null) {
            return false;
        }
        return arrayList.contains(Integer.valueOf(i));
    }

    public static void messageDeleted(long j, int i) {
        ArrayList<Integer> arrayList = messageDeletePermitted.get(j);
        if (arrayList == null) {
            return;
        }
        arrayList.remove(Integer.valueOf(i));
    }

    public static void incrementFcmCounter() {
        fcmCounter.incrementAndGet();
    }

    public static int getFcmCounter() {
        return fcmCounter.get();
    }

    public static void resetSessionTerminated(int i) {
        terminatedSessions.remove(i);
    }

    public static void setSessionTerminated(int i, long j) {
        try {
            if (false) {
                UserConfig.getInstance(i).clearConfig();
                MessagesController.getInstance(i).performLogout(0);
            } else if (UserConfig.getInstance(i).isClientActivated()) {
                terminatedSessions.append(j, Integer.valueOf(i));
                TLRPC.User currentUser = UserConfig.getInstance(i).getCurrentUser();
                final Serializable name = currentUser != null ? ContactsController.formatName(currentUser) : Long.valueOf(j);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuState$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AyuState.$r8$lambda$yWmeMrRdhK3AOBdQfcnuXYwK7Vo(name);
                    }
                });
            }
        } catch (Throwable unused) {
        }
    }

    public static /* synthetic */ void $r8$lambda$yWmeMrRdhK3AOBdQfcnuXYwK7Vo(Serializable serializable) {
        try {
            if (LaunchActivity.isActive && LaunchActivity.getSafeLastFragment() != null) {
                BulletinFactory.global().createErrorBulletin(LocaleController.formatString(R.string.ErrorOccurred, serializable)).show();
            }
        } catch (Throwable unused) {
        }
    }

    public static boolean isSessionTerminated(int i, long j) {
        Integer num = terminatedSessions.get(j);
        return num != null && num.intValue() == i;
    }

    public static void setMessageBurned(int i, long j, int i2) {
        if (AyuConfig.saveDeletedMessages) {
            long clientUserId = UserConfig.getInstance(i).getClientUserId();
            editor.putBoolean("messageBurned_" + clientUserId + "_" + j + "_" + i2, true);
            editor.apply();
        }
    }

    public static boolean isMessageBurned(int i, long j, int i2) {
        if (!AyuConfig.saveDeletedMessages) {
            return false;
        }
        long clientUserId = UserConfig.getInstance(i).getClientUserId();
        return preferences.getBoolean("messageBurned_" + clientUserId + "_" + j + "_" + i2, false);
    }

    private static class AyuStateVariable {
        public int resetAfter;
        private final Object sync;
        public boolean val;

        private AyuStateVariable() {
            this.sync = new Object();
        }

        public boolean process() {
            synchronized (this.sync) {
                try {
                    int i = this.resetAfter;
                    if (i == -1) {
                        return this.val;
                    }
                    int i2 = i - 1;
                    this.resetAfter = i2;
                    boolean z = this.val;
                    if (i2 <= 0) {
                        this.val = false;
                        this.resetAfter = 0;
                    }
                    return z;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }
}
