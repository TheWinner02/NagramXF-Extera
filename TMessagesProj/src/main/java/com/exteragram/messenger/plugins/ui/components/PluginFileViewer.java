package com.exteragram.messenger.plugins.ui.components;

import android.app.Activity;
import android.content.DialogInterface;
import com.exteragram.messenger.utils.MarkdownUtils;
import java.io.File;
import kotlin.Metadata;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_iv;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;

/* JADX INFO: loaded from: classes4.dex */
@SourceDebugExtension({"SMAP\nPluginFileViewer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PluginFileViewer.kt\ncom/exteragram/messenger/plugins/ui/components/PluginFileViewer\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,127:1\n1#2:128\n*E\n"})
public final class PluginFileViewer {
    public static final PluginFileViewer INSTANCE = new PluginFileViewer();
    private static final int MAX_BLOCK_LENGTH = 8192;
    private static final int MAX_FILE_SIZE = 524288;

    private PluginFileViewer() {
    }

    public static /* synthetic */ boolean open$default(PluginFileViewer pluginFileViewer, BaseFragment baseFragment, File file, String str, int i, Object obj) {
        if ((i & 4) != 0) {
            str = null;
        }
        return pluginFileViewer.open(baseFragment, file, str);
    }

    public final boolean open(final BaseFragment fragment, final File file, final String fileName) {
        Activity parentActivity;
        if (fragment == null || (parentActivity = fragment.getParentActivity()) == null || file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        if (file.length() > 524288) {
            BulletinFactory.of(fragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.ImportFileTooLarge)).show();
            return false;
        }
        final AlertDialog alertDialog = new AlertDialog(parentActivity, 3, fragment.getResourceProvider());
        alertDialog.setCanceledOnTouchOutside(false);
        final boolean[] zArr = {false};
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginFileViewer$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                PluginFileViewer.m1360$r8$lambda$V5c36KhCJGXK1eWQobTq6ixb5g(zArr, dialogInterface);
            }
        });
        alertDialog.showDelayed(150L);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.PluginFileViewer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PluginFileViewer.$r8$lambda$qqdbMSxrLs0kkr2wrRKExk_Q7bY(file, fileName, alertDialog, zArr, fragment);
            }
        });
        return true;
    }

    /* JADX INFO: renamed from: $r8$lambda$V5c36KhC-JGXK1eWQobTq6ixb5g, reason: not valid java name */
    public static void m1360$r8$lambda$V5c36KhCJGXK1eWQobTq6ixb5g(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    public static void $r8$lambda$qqdbMSxrLs0kkr2wrRKExk_Q7bY(File file, String str, final AlertDialog alertDialog, final boolean[] zArr, final BaseFragment baseFragment) {
        MessageObject tempObj;
        try {
            PluginFileViewer pluginFileViewer = INSTANCE;
            tempObj = pluginFileViewer.createMessageObject(file, pluginFileViewer.normalizeFileName(file, str));
        } catch (Throwable th) {
            FileLog.e(th);
            tempObj = null;
        }
        final MessageObject messageObjectCreateMessageObject = tempObj;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.PluginFileViewer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                PluginFileViewer.open$lambda$1$0(alertDialog, zArr, messageObjectCreateMessageObject, baseFragment);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void open$lambda$1$0(AlertDialog alertDialog, boolean[] zArr, MessageObject messageObject, BaseFragment baseFragment) {
        try {
            alertDialog.dismiss();
        } catch (Throwable unused) {
        }
        if (zArr[0]) {
            return;
        }
        if (messageObject != null) {
            baseFragment.createArticleViewer(false).open(messageObject);
        } else {
            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.ErrorOccurred)).show();
        }
    }

    private final MessageObject createMessageObject(File file, String fileName) {
        String text = FilesKt.readText(file, Charsets.UTF_8);
        TL_iv.TL_page tL_page = new TL_iv.TL_page();
        tL_page.local = file;
        tL_page.url = fileName;
        MarkdownUtils.appendPreformattedBlocks(tL_page.blocks, text, "python", 8192);
        TLRPC.TL_webPage tL_webPage = new TLRPC.TL_webPage();
        tL_webPage.id = file.getAbsolutePath().hashCode();
        tL_webPage.url = fileName;
        tL_webPage.display_url = fileName;
        tL_webPage.title = fileName;
        tL_webPage.flags |= 1028;
        tL_webPage.cached_page = tL_page;
        long j = UserConfig.getInstance(UserConfig.selectedAccount).clientUserId;
        TLRPC.TL_message tL_message = new TLRPC.TL_message();
        tL_message.id = 0;
        tL_message.date = (int) (System.currentTimeMillis() / 1000);
        tL_message.message = fileName;
        tL_message.out = true;
        TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
        tL_peerUser.user_id = j;
        tL_message.peer_id = tL_peerUser;
        TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
        tL_peerUser2.user_id = j;
        tL_message.from_id = tL_peerUser2;
        TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage = new TLRPC.TL_messageMediaWebPage();
        tL_messageMediaWebPage.webpage = tL_webPage;
        tL_message.media = tL_messageMediaWebPage;
        return new MessageObject(UserConfig.selectedAccount, tL_message, false, true);
    }

    /* JADX WARN: Code duplicated, block: B:8:0x000c  */
    private final String normalizeFileName(File file, String fileName) {
        if (fileName == null) {
            fileName = file.getName();
        } else {
            if (StringsKt.isBlank(fileName)) {
                fileName = null;
            }
            if (fileName == null) {
                fileName = file.getName();
            }
        }
        if (StringsKt.endsWith(fileName, ".plugin", true)) {
            return fileName;
        }
        return fileName + ".plugin";
    }
}
