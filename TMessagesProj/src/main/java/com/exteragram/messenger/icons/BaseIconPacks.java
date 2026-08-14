package com.exteragram.messenger.icons;

import android.content.res.Resources;

public final class BaseIconPacks {
    public static final BaseIconPacks INSTANCE = new BaseIconPacks();

    private BaseIconPacks() {
    }

    public int getConversionRemix(Resources res, int iconId) {
        try {
            String entryName = res.getResourceEntryName(iconId);
            String remixEntryName = entryName + "_remix";
            int remixId = res.getIdentifier(remixEntryName, "drawable", "nu.gpu.nagram");
            if (remixId != 0) {
                return remixId;
            }
        } catch (Exception ignored) {
        }
        return iconId;
    }
}
