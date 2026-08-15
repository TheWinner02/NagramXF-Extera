package com.exteragram.messenger.plugins;

import com.chaquo.python.internal.Common;
import com.google.android.gms.cast.MediaTrack;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

/* JADX INFO: loaded from: classes.dex */
public final class Plugin {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private String appVersion;
    private String author;
    private transient PluginsController.PluginsEngine cachedEngine;
    private String description;
    private String engine;
    private volatile Throwable error;
    private final String id;
    private int index;
    private volatile boolean isEnabled;
    private volatile boolean isNotResponding;
    private final String name;
    private String pack;
    private List<String> requirements;
    private String sdkVersion;
    private String version;

    public Plugin(String str, String str2) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-64469475804719L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-64490950641199L);
        this.id = str;
        this.name = str2;
        this.appVersion = Deobfuscator$exteraGramDev$TMessagesProj.getString(-64572555019823L);
        this.sdkVersion = Deobfuscator$exteraGramDev$TMessagesProj.getString(-64551080183343L) + PythonPluginsEngine.INSTANCE.getSDK_VERSION();
        this.version = Deobfuscator$exteraGramDev$TMessagesProj.getString(-64555375150639L);
        this.description = "No description provided.";
        this.author = "Unknown author";
        this.index = -1;
        this.requirements = new ArrayList();
    }

    public final PluginsController.PluginsEngine getCachedEngine() {
        return this.cachedEngine;
    }

    public final void setCachedEngine(PluginsController.PluginsEngine pluginsEngine) {
        this.cachedEngine = pluginsEngine;
    }

    public final String getId() {
        return this.id;
    }

    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public final String getEngine() {
        return this.engine;
    }

    public final void setEngine(String engine) {
        this.engine = engine;
    }

    public final String getAuthor() {
        return this.author;
    }

    public final void setAuthor(String author) {
        this.author = author;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isEnabled() {
        return !hasError() && this.isEnabled;
    }

    public final void setEnabled(boolean enabled) {
        this.isEnabled = enabled && !hasError();
    }

    public final Throwable getError() {
        return this.error;
    }

    public final void setError(Throwable error) {
        this.error = error;
        if (hasError()) {
            this.isEnabled = false;
        }
    }

    public final boolean hasError() {
        return this.error != null;
    }

    public final String getPack() {
        return this.pack;
    }

    public final int getIndex() {
        return this.index;
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(String version) {
        this.version = version;
    }

    public final List<String> getRequirements() {
        return this.requirements;
    }

    public final String getAppVersion() {
        return this.appVersion;
    }

    public final void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public final String getSdkVersion() {
        return this.sdkVersion;
    }

    public final void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public final void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    /* JADX INFO: renamed from: isNotResponding, reason: from getter */
    public final boolean getIsNotResponding() {
        return this.isNotResponding;
    }

    public final void setNotResponding(boolean notResponding) {
        this.isNotResponding = notResponding;
    }

    public final String getIcon() {
        if (this.pack == null || this.index < 0) {
            return null;
        }
        return this.pack + '/' + this.index;
    }

    public final void setIcon(String link) {
        int iLastIndexOf$default;
        if (INSTANCE.isIconValid(link) && (iLastIndexOf$default = link.lastIndexOf('/')) != -1) {
            String strSubstring = link.substring(0, iLastIndexOf$default);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-62992007054895L);
            this.pack = strSubstring;
            String strSubstring2 = link.substring(iLastIndexOf$default + 1);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-63065021498927L);
            this.index = Integer.parseInt(strSubstring2);
        }
    }

    public boolean equals(Object other) {
        if (other instanceof Plugin) {
            return Intrinsics.areEqual(((Plugin) other).id, this.id);
        }
        return this == other;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean isIconValid(String input) {
            return input != null && new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-100663165208111L)).matches(input);
        }
    }
}
