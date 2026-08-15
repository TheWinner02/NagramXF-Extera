package com.exteragram.messenger.plugins.utils;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_bots;
import org.telegram.ui.ActionBar.BaseFragment;

/* JADX INFO: loaded from: classes.dex */
public final class MenuContextBuilder {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final HashMap<String, Object> contextData;

    public /* synthetic */ MenuContextBuilder(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    @JvmStatic
    public static final MenuContextBuilder create() {
        return INSTANCE.create();
    }

    @JvmStatic
    public static final MenuContextBuilder from(BaseFragment baseFragment) {
        return INSTANCE.from(baseFragment);
    }

    private MenuContextBuilder() {
        this.contextData = new HashMap<>();
    }

    public final MenuContextBuilder withAccount(int account) {
        this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-102144928925231L), Integer.valueOf(account));
        return this;
    }

    public final MenuContextBuilder withContext(Context context) {
        if (context != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-102248008140335L), context);
        }
        return this;
    }

    public final MenuContextBuilder withEncryptedChat(TLRPC.EncryptedChat encryptedChat) {
        if (encryptedChat != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-102213648401967L), encryptedChat);
        }
        return this;
    }

    public final MenuContextBuilder withChat(TLRPC.Chat chat) {
        if (chat != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101724022130223L), chat);
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101822806378031L), Long.valueOf(chat.id));
        }
        return this;
    }

    public final MenuContextBuilder withChatFull(TLRPC.ChatFull chatFull) {
        if (chatFull != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101775561737775L), chatFull);
        }
        return this;
    }

    public final MenuContextBuilder withUser(TLRPC.User user) {
        if (user != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101891525854767L), user);
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101904410756655L), Long.valueOf(user.id));
        }
        return this;
    }

    public final MenuContextBuilder withUserFull(TLRPC.UserFull userFull) {
        if (userFull != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101874345985583L), userFull);
        }
        return this;
    }

    public final MenuContextBuilder withBotInfo(TL_bots.BotInfo botInfo) {
        if (botInfo != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101973130233391L), botInfo);
        }
        return this;
    }

    public final MenuContextBuilder withDialogId(long dialogId) {
        this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-101938770495023L), Long.valueOf(dialogId));
        return this;
    }

    public final MenuContextBuilder withMessage(MessageObject message) {
        if (message != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-100332452726319L), message);
        }
        return this;
    }

    public final MenuContextBuilder withGroupedMessage(MessageObject.GroupedMessages groupedMessages) {
        if (groupedMessages != null) {
            this.contextData.put(Deobfuscator$exteraGramDev$TMessagesProj.getString(-100435531941423L), groupedMessages);
        }
        return this;
    }

    public final MenuContextBuilder withCustom(String key, Object value) {
        if (key != null && value != null) {
            this.contextData.put(key, value);
        }
        return this;
    }

    public final Map<String, Object> build() {
        return this.contextData;
    }

    /* JADX INFO: loaded from: classes4.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final MenuContextBuilder create() {
            return new MenuContextBuilder(null);
        }

        @JvmStatic
        public final MenuContextBuilder from(BaseFragment fragment) {
            if (fragment == null) {
                return create();
            }
            return create().withCustom(Deobfuscator$exteraGramDev$TMessagesProj.getString(-102673209902639L), fragment).withAccount(fragment.getCurrentAccount()).withContext(fragment.getParentActivity());
        }
    }
}
