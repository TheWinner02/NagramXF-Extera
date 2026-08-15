package com.exteragram.messenger.plugins.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import com.exteragram.messenger.plugins.Plugin;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.pip.PipController;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nPluginsWatchdog.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PluginsWatchdog.kt\ncom/exteragram/messenger/plugins/utils/PluginsWatchdog\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 SharedPreferences.kt\nandroidx/core/content/SharedPreferencesKt\n*L\n1#1,224:1\n2792#2,3:225\n2792#2,3:228\n41#3,12:231\n*S KotlinDebug\n*F\n+ 1 PluginsWatchdog.kt\ncom/exteragram/messenger/plugins/utils/PluginsWatchdog\n*L\n86#1:225,3\n129#1:228,3\n159#1:231,12\n*E\n"})
public final class PluginsWatchdog {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final PluginsController controller;
    private final ConcurrentHashMap<Thread, ExecutionInfo> executingPlugins;
    private final ConcurrentHashMap<Thread, ExecutionInfo> frozenExecutions;
    private final ConcurrentHashMap<Thread, ScheduledFuture<?>> scheduledChecks;
    private ScheduledExecutorService scheduler;

    @JvmStatic
    public static final void showNotRespondingAlert(Plugin plugin) {
        INSTANCE.showNotRespondingAlert(plugin);
    }

    public PluginsWatchdog(PluginsController pluginsController) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-102771994150447L);
        this.controller = pluginsController;
        this.executingPlugins = new ConcurrentHashMap<>();
        this.frozenExecutions = new ConcurrentHashMap<>();
        this.scheduledChecks = new ConcurrentHashMap<>();
    }

    public static final /* data */ class ExecutionInfo {
        private final String pluginId;

        public static /* synthetic */ ExecutionInfo copy$default(ExecutionInfo executionInfo, String str, int i, Object obj) {
            if ((i & 1) != 0) {
                str = executionInfo.pluginId;
            }
            return executionInfo.copy(str);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getPluginId() {
            return this.pluginId;
        }

        public final ExecutionInfo copy(String pluginId) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-102011784939055L);
            return new ExecutionInfo(pluginId);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            return (other instanceof ExecutionInfo) && Intrinsics.areEqual(this.pluginId, ((ExecutionInfo) other).pluginId);
        }

        public int hashCode() {
            return this.pluginId.hashCode();
        }

        public String toString() {
            return Deobfuscator$exteraGramDev$TMessagesProj.getString(-102110569186863L) + this.pluginId + ')';
        }

        public ExecutionInfo(String str) {
            this.pluginId = str;
        }
    }

    public final void start() {
        ScheduledExecutorService scheduledExecutorService = this.scheduler;
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
            this.scheduler = scheduledThreadPoolExecutor;
        }
    }

    public final void stop() {
        for (ScheduledFuture<?> scheduledFuture : this.scheduledChecks.values()) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-102759109248559L);
            scheduledFuture.cancel(false);
        }
        this.scheduledChecks.clear();
        ScheduledExecutorService scheduledExecutorService = this.scheduler;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
        this.scheduler = null;
        for (ExecutionInfo executionInfo : this.frozenExecutions.values()) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-102321022584367L);
            Plugin plugin = this.controller.getPlugins().get(executionInfo.getPluginId());
            if (plugin != null) {
                plugin.setNotResponding(false);
            }
        }
        this.frozenExecutions.clear();
        this.executingPlugins.clear();
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0076  */
    public final void onPluginExecutionStarted(final String pluginId) {
        Plugin plugin;
        if (pluginId == null) {
            return;
        }
        final Thread threadCurrentThread = Thread.currentThread();
        final ExecutionInfo executionInfo = new ExecutionInfo(pluginId);
        this.executingPlugins.put(threadCurrentThread, executionInfo);
        ExecutionInfo executionInfoRemove = this.frozenExecutions.remove(threadCurrentThread);
        if (executionInfoRemove != null) {
            boolean zAreEqual = Intrinsics.areEqual(executionInfoRemove.getPluginId(), pluginId);
            ConcurrentHashMap<Thread, ExecutionInfo> concurrentHashMap = this.frozenExecutions;
            if (zAreEqual) {
                concurrentHashMap.put(threadCurrentThread, executionInfo);
            } else {
                Collection<ExecutionInfo> collectionValues = concurrentHashMap.values();
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-102363972257327L);
                Collection<ExecutionInfo> collection = collectionValues;
                if (collection.isEmpty()) {
                    plugin = this.controller.getPlugins().get(executionInfoRemove.getPluginId());
                    if (plugin != null) {
                        plugin.setNotResponding(false);
                    }
                    NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(NotificationCenter.pluginIsNotResponding, new Object[0]);
                } else {
                    Iterator<ExecutionInfo> it = collection.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (Intrinsics.areEqual(((ExecutionInfo) it.next()).getPluginId(), executionInfoRemove.getPluginId())) {
                            }
                        } else {
                            plugin = this.controller.getPlugins().get(executionInfoRemove.getPluginId());
                            if (plugin != null) {
                                plugin.setNotResponding(false);
                            }
                            NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(NotificationCenter.pluginIsNotResponding, new Object[0]);
                        }
                    }
                }
            }
        }
        ScheduledFuture<?> scheduledFutureRemove = this.scheduledChecks.remove(threadCurrentThread);
        if (scheduledFutureRemove != null) {
            scheduledFutureRemove.cancel(false);
        }
        ScheduledExecutorService scheduledExecutorService = this.scheduler;
        if (scheduledExecutorService == null) {
            return;
        }
        try {
            this.scheduledChecks.put(threadCurrentThread, scheduledExecutorService.schedule(new Runnable() { // from class: com.exteragram.messenger.plugins.utils.PluginsWatchdog$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PluginsWatchdog.this.executingPlugins.remove(threadCurrentThread, executionInfo);
                }
            }, 5L, TimeUnit.SECONDS));
        } catch (RejectedExecutionException unused) {
            this.executingPlugins.remove(threadCurrentThread, executionInfo);
        }
    }

    public static void $r8$lambda$aEUKVuMeoYLYYeSPkSgPbWxPjUk(final PluginsWatchdog pluginsWatchdog, final Thread thread, final ExecutionInfo executionInfo, final String str) {
        try {
            final Ref.BooleanRef booleanRef = new Ref.BooleanRef();
            ConcurrentHashMap<Thread, ExecutionInfo> concurrentHashMap = pluginsWatchdog.executingPlugins;
            final Function2 function2 = new Function2() { // from class: com.exteragram.messenger.plugins.utils.PluginsWatchdog$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return PluginsWatchdog.onPluginExecutionStarted$lambda$1$0(executionInfo, pluginsWatchdog, thread, str, booleanRef, (Thread) obj, (PluginsWatchdog.ExecutionInfo) obj2);
                }
            };
            concurrentHashMap.computeIfPresent(thread, new BiFunction() { // from class: com.exteragram.messenger.plugins.utils.PluginsWatchdog$$ExternalSyntheticLambda2
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return PluginsWatchdog.onPluginExecutionStarted$lambda$1$1(function2, obj, obj2);
                }
            });
            if (booleanRef.element) {
                NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(NotificationCenter.pluginIsNotResponding, new Object[0]);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ExecutionInfo onPluginExecutionStarted$lambda$1$1(Function2 function2, Object obj, Object obj2) {
        return (ExecutionInfo) function2.invoke(obj, obj2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ExecutionInfo onPluginExecutionStarted$lambda$1$0(ExecutionInfo executionInfo, PluginsWatchdog pluginsWatchdog, Thread thread, String str, Ref.BooleanRef booleanRef, Thread thread2, ExecutionInfo executionInfo2) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-102935202907695L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-102999627417135L);
        if (executionInfo2 == executionInfo) {
            pluginsWatchdog.frozenExecutions.put(thread, executionInfo);
            Plugin plugin = pluginsWatchdog.controller.getPlugins().get(str);
            if (plugin != null && !plugin.getIsNotResponding()) {
                plugin.setNotResponding(true);
                booleanRef.element = true;
            }
        }
        return executionInfo2;
    }

    public final void onPluginExecutionFinished(String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-102441281668655L);
        Thread threadCurrentThread = Thread.currentThread();
        ExecutionInfo executionInfo = this.executingPlugins.get(threadCurrentThread);
        if (executionInfo != null && Intrinsics.areEqual(executionInfo.getPluginId(), pluginId) && this.executingPlugins.remove(threadCurrentThread, executionInfo)) {
            ScheduledFuture<?> scheduledFutureRemove = this.scheduledChecks.remove(threadCurrentThread);
            if (scheduledFutureRemove != null) {
                scheduledFutureRemove.cancel(false);
            }
            ExecutionInfo executionInfoRemove = this.frozenExecutions.remove(threadCurrentThread);
            if (executionInfoRemove != null) {
                Collection<ExecutionInfo> collectionValues = this.frozenExecutions.values();
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-102402626962991L);
                Collection<ExecutionInfo> collection = collectionValues;
                if (!collection.isEmpty()) {
                    Iterator<ExecutionInfo> it = collection.iterator();
                    while (it.hasNext()) {
                        if (Intrinsics.areEqual(((ExecutionInfo) it.next()).getPluginId(), executionInfoRemove.getPluginId())) {
                            return;
                        }
                    }
                }
                Plugin plugin = this.controller.getPlugins().get(executionInfoRemove.getPluginId());
                if (plugin != null) {
                    plugin.setNotResponding(false);
                }
                NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(NotificationCenter.pluginIsNotResponding, new Object[0]);
            }
        }
    }

    public final void forceDisablePlugin(String pluginId, Activity activity) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-102479936374319L);
        disablePluginPref(pluginId);
        restartApp(activity);
    }

    public final void forceDeletePlugin(String pluginId, Activity activity) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-103145656305199L);
        disablePluginPref(pluginId);
        PluginsController.INSTANCE.setPluginPinned(pluginId, false);
        this.controller.cleanupPlugin(pluginId);
        try {
            PipController.INSTANCE.uninstallDependencies(pluginId);
        } catch (Exception e) {
            FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-103107001599535L) + pluginId, e);
        }
        this.controller.clearPluginSettingsPreferences(pluginId, true);
        new File(this.controller.getPluginsDir(), pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-102849303561775L)).delete();
        this.controller.getPlugins().remove(pluginId);
        this.controller.notifyPluginsChanged();
        restartApp(activity);
    }

    private final void disablePluginPref(String pluginId) {
        SharedPreferences.Editor editorEdit = this.controller.getPreferences().edit();
        editorEdit.putBoolean(Deobfuscator$exteraGramDev$TMessagesProj.getString(-102866483430959L) + pluginId, false);
        editorEdit.apply();
    }

    private final void restartApp(final Activity activity) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.utils.PluginsWatchdog$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PluginsWatchdog.m1385$r8$lambda$C6oS2ADJb9prdSSfDfoeRfN2sA(activity);
            }
        }, 200L);
    }

    /* JADX INFO: renamed from: $r8$lambda$C6oS2ADJb9prd-SSfDfoeRfN2sA, reason: not valid java name */
    public static void m1385$r8$lambda$C6oS2ADJb9prdSSfDfoeRfN2sA(Activity activity) {
        if (activity != null) {
            PackageManager packageManager = activity.getPackageManager();
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-103051167024687L);
            Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(activity.getPackageName());
            activity.finishAffinity();
            if (launchIntentForPackage != null) {
                activity.startActivity(launchIntentForPackage);
            }
        }
        System.exit(0);
        throw new RuntimeException(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101492093896239L));
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final void showNotRespondingAlert(final Plugin plugin) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-101105546839599L);
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (safeLastFragment == null) {
                return;
            }
            final Activity parentActivity = safeLastFragment.getParentActivity();
            AlertDialog alertDialogCreate = new AlertDialog.Builder(parentActivity, safeLastFragment.getResourceProvider()).setTitle(plugin.getName() + " is not responding").setItems(new String[]{LocaleController.getString(R.string.WaitMore), LocaleController.getString(R.string.Disable), LocaleController.getString(R.string.Delete)}, new int[]{R.drawable.msg_recent, R.drawable.msg_block, R.drawable.msg_delete}, new DialogInterface.OnClickListener() { // from class: com.exteragram.messenger.plugins.utils.PluginsWatchdog$Companion$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PluginsWatchdog.Companion.$r8$lambda$YVUu_yybweI20Wq2RsPMfENiUXc(plugin, parentActivity, dialogInterface, i);
                }
            }).create();
            alertDialogCreate.show();
            alertDialogCreate.setItemColor(alertDialogCreate.getItemsCount() - 1, Theme.getColor(Theme.key_text_RedBold), Theme.getColor(Theme.key_text_RedRegular));
        }

        public static void $r8$lambda$YVUu_yybweI20Wq2RsPMfENiUXc(Plugin plugin, Activity activity, DialogInterface dialogInterface, int i) {
            if (i == 1) {
                PluginsController.INSTANCE.getInstance().getWatchdog().forceDisablePlugin(plugin.getId(), activity);
            } else {
                if (i != 2) {
                    return;
                }
                PluginsController.INSTANCE.getInstance().getWatchdog().forceDeletePlugin(plugin.getId(), activity);
            }
        }
    }
}
