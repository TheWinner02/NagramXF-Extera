package com.exteragram.messenger.plugins.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.exteragram.messenger.plugins.Plugin;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.models.EditTextSetting;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.EditTextCell;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;

/* JADX INFO: loaded from: classes4.dex */
@SuppressLint({"ViewConstructor"})
@SourceDebugExtension({"SMAP\nPluginEditTextCell.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PluginEditTextCell.kt\ncom/exteragram/messenger/plugins/ui/components/PluginEditTextCell\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,257:1\n1#2:258\n37#3,2:259\n*S KotlinDebug\n*F\n+ 1 PluginEditTextCell.kt\ncom/exteragram/messenger/plugins/ui/components/PluginEditTextCell\n*L\n156#1:259,2\n*E\n"})
public final class PluginEditTextCell extends EditTextCell {
    private static final int SAVE_DEBOUNCE_MS = 750;
    private EditTextSetting currentSetting;
    private Runnable pendingSaveRunnable;
    private String pluginId;
    private final TextWatcher saveTextWatcher;
    private String valueToSave;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v1, types: [android.text.TextWatcher, com.exteragram.messenger.plugins.ui.components.PluginEditTextCell$saveTextWatcher$1] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public PluginEditTextCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, null, false, false, -1, resourcesProvider);
        TextWatcher r7 = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence newText, int start, int before, int count) {
                PluginEditTextCell.this.valueToSave = newText != null ? newText.toString() : "";
                PluginEditTextCell.this.scheduleSave();
            }
        };
        this.saveTextWatcher = r7;
        setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider));
        this.editText.addTextChangedListener(r7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void scheduleSave() {
        Runnable runnable = this.pendingSaveRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        EditTextSetting editTextSetting = this.currentSetting;
        final String key = editTextSetting != null ? editTextSetting.getKey() : null;
        final String str = this.valueToSave;
        final String str2 = this.pluginId;
        final EditTextSetting editTextSetting2 = this.currentSetting;
        if (key == null || str == null || str2 == null || editTextSetting2 == null) {
            return;
        }
        Runnable runnable2 = new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.PluginEditTextCell$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PluginEditTextCell.$r8$lambda$Ta83XVBXVLaC75iTOLwtEhLaxn4(PluginEditTextCell.this, editTextSetting2, str, str2, key);
            }
        };
        this.pendingSaveRunnable = runnable2;
        AndroidUtilities.runOnUIThread(runnable2, 750L);
    }

    public static void $r8$lambda$Ta83XVBXVLaC75iTOLwtEhLaxn4(PluginEditTextCell pluginEditTextCell, final EditTextSetting editTextSetting, final String str, final String str2, final String str3) {
        pluginEditTextCell.pendingSaveRunnable = null;
        PluginsController.INSTANCE.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.PluginEditTextCell$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                PluginEditTextCell.scheduleSave$lambda$0$0(editTextSetting, str, str2, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void scheduleSave$lambda$0$0(EditTextSetting editTextSetting, String str, String str2, String str3) {
        if (editTextSetting.getMaxLength() > 0 && str.length() > editTextSetting.getMaxLength()) {
            str = str.substring(0, editTextSetting.getMaxLength());
        }
        PluginsController.INSTANCE.getInstance().setPluginSettingAndTriggerOnChange(str2, str3, str, editTextSetting.getOnChangeCallback());
    }

    @Override // org.telegram.ui.Cells.EditTextCell
    public void onFocusChanged(boolean focused) {
        super.onFocusChanged(focused);
        if (focused) {
            return;
        }
        flushPendingSave();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        flushPendingSave();
    }

    private final void flushPendingSave() {
        Runnable runnable = this.pendingSaveRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            runnable.run();
            this.pendingSaveRunnable = null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:10:0x001a  */
    public final void bind(String pluginId, EditTextSetting setting) {
        boolean z;
        InputFilter[] inputFilterArr;
        EditTextSetting editTextSetting = this.currentSetting;
        if (editTextSetting == null) {
            z = false;
        } else {
            if (TextUtils.equals(editTextSetting != null ? editTextSetting.getKey() : null, setting.getKey())) {
                z = true;
            } else {
                z = false;
            }
        }
        if (!z) {
            flushPendingSave();
        }
        this.pluginId = pluginId;
        this.currentSetting = setting;
        setWatchersEnabled(false);
        this.editText.setSingleLine(!setting.getMultiline());
        int i = (setting.getMultiline() ? 131072 : 0) | 573441;
        if (this.editText.getInputType() != i) {
            this.editText.setInputType(i);
        }
        if (setting.getMaxLength() > 0) {
            setShowLimitWhenNear(setting.getMaxLength() / Math.min(setting.getMaxLength(), 4));
        } else {
            setShowLimitWhenNear(-1);
        }
        ArrayList arrayList = new ArrayList();
        InputFilter inputFilterCreateInputFilter = createInputFilter(setting.getMask());
        if (inputFilterCreateInputFilter != null) {
            arrayList.add(inputFilterCreateInputFilter);
        }
        if (setting.getMaxLength() > 0) {
            arrayList.add(new InputFilter.LengthFilter(setting.getMaxLength()));
        }
        EditTextCaption editTextCaption = this.editText;
        if (!arrayList.isEmpty()) {
            inputFilterArr = (InputFilter[]) arrayList.toArray(new InputFilter[0]);
        } else {
            inputFilterArr = new InputFilter[0];
        }
        editTextCaption.setFilters(inputFilterArr);
        String pluginSettingString = PluginsController.INSTANCE.getInstance().getPluginSettingString(pluginId, setting.getKey(), setting.getDefaultValue());
        if (!TextUtils.equals(pluginSettingString, String.valueOf(this.editText.getText()))) {
            if (this.editText.hasFocus() && z) {
                setWatchersEnabled(true);
                return;
            }
            setText(pluginSettingString);
        }
        this.valueToSave = pluginSettingString;
        if (setting.getHint() != null) {
            this.editText.setHint(setting.getHint());
        }
        setWatchersEnabled(true);
    }

    private final void setWatchersEnabled(boolean enabled) {
        this.editText.removeTextChangedListener(this.saveTextWatcher);
        if (enabled) {
            this.editText.addTextChangedListener(this.saveTextWatcher);
        }
    }

    private final InputFilter createInputFilter(String maskRegex) {
        if (TextUtils.isEmpty(maskRegex)) {
            return null;
        }
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(maskRegex);
        } catch (PatternSyntaxException e) {
            FileLog.e("Invalid mask for EditText: ".concat(maskRegex), e);
            return null;
        }
        final Pattern patternCompile = pattern;
        return new InputFilter() { // from class: com.exteragram.messenger.plugins.ui.components.PluginEditTextCell$$ExternalSyntheticLambda1
            @Override // android.text.InputFilter
            public final CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                return PluginEditTextCell.$r8$lambda$V7Y2RoWGJo4arI5W4MF0XsqMNDo(patternCompile, charSequence, i, i2, spanned, i3, i4);
            }
        };
    }

    public static CharSequence $r8$lambda$V7Y2RoWGJo4arI5W4MF0XsqMNDo(Pattern pattern, CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        Matcher matcher;
        StringBuilder sb = new StringBuilder(i2 - i);
        boolean z = true;
        while (i < i2) {
            char cCharAt = charSequence.charAt(i);
            if ((pattern == null || (matcher = pattern.matcher(String.valueOf(cCharAt))) == null) ? false : matcher.matches()) {
                sb.append(cCharAt);
            } else {
                z = false;
            }
            i++;
        }
        if (z) {
            return null;
        }
        return sb.toString();
    }

    public static final class Factory extends UItem.UItemFactory<PluginEditTextCell> {

        /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);

        @JvmStatic
        public static final UItem as(Plugin plugin, EditTextSetting editTextSetting) {
            return INSTANCE.as(plugin, editTextSetting);
        }

        public boolean getIsClickableValue() {
            return false;
        }

        @Override // org.telegram.ui.Components.UItem.UItemFactory
        public PluginEditTextCell createView(Context context, RecyclerListView listView, int currentAccount, int classGuid, Theme.ResourcesProvider resourcesProvider) {
            return new PluginEditTextCell(context, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.UItem.UItemFactory
        public void bindView(View view, UItem item, boolean divider, UniversalAdapter adapter, UniversalRecyclerView listView) {
            if ((view instanceof PluginEditTextCell) && (item.object instanceof EditTextSetting)) {
                PluginEditTextCell pluginEditTextCell = (PluginEditTextCell) view;
                pluginEditTextCell.bind("", (EditTextSetting) item.object);
                pluginEditTextCell.setDivider(divider);
            }
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            @JvmStatic
            public final UItem as(Plugin plugin, EditTextSetting setting) {
                UItem uItemOfFactory = UItem.ofFactory(Factory.class);
                uItemOfFactory.object = setting;
                return uItemOfFactory;
            }
        }

        static {
            UItem.UItemFactory.setup(new Factory());
        }
    }
}
