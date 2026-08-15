package com.exteragram.messenger.plugins.ui.components;

import com.exteragram.messenger.plugins.pip.PipController;
import kotlin.Metadata;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

/* JADX INFO: loaded from: classes4.dex */
@SourceDebugExtension({"SMAP\nInstallPluginBottomSheet.kt\nKotlin\n*S Kotlin\n*F\n+ 1 InstallPluginBottomSheet.kt\ncom/exteragram/messenger/plugins/ui/components/InstallPluginBottomSheet$buttonView$1$1$4\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,749:1\n1#2:750\n*E\n"})
public final class InstallPluginBottomSheet$buttonView$1$1$4 implements PipController.InstallerDelegate {
    final /* synthetic */ InstallPluginBottomSheet this$0;

    public InstallPluginBottomSheet$buttonView$1$1$4(InstallPluginBottomSheet installPluginBottomSheet) {
        this.this$0 = installPluginBottomSheet;
    }

    @Override // com.exteragram.messenger.plugins.pip.PipController.InstallerDelegate
    public void onProgress(final String text) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-123679894947375L);
        final InstallPluginBottomSheet installPluginBottomSheet = this.this$0;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$buttonView$1$1$4$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                InstallPluginBottomSheet$buttonView$1$1$4.m1357$r8$lambda$vIvpOtqwK24iEREBsNrPTCFMg(installPluginBottomSheet, text);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$vIvpOtqwK24-iE-REBsNrPTCFMg, reason: not valid java name */
    public static void m1357$r8$lambda$vIvpOtqwK24iEREBsNrPTCFMg(InstallPluginBottomSheet installPluginBottomSheet, String str) {
        Runnable runnable = installPluginBottomSheet.delayedLoadingRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        installPluginBottomSheet.delayedLoadingRunnable = null;
        if (installPluginBottomSheet.cancellationRequested) {
            return;
        }
        installPluginBottomSheet.button.setLoading(false);
        installPluginBottomSheet.button.setText(LocaleController.getString(R.string.Cancel), true);
        installPluginBottomSheet.button.setSubText(str, true);
    }

    @Override // com.exteragram.messenger.plugins.pip.PipController.InstallerDelegate
    public boolean isCancelled() {
        return this.this$0.cancellationRequested;
    }
}
