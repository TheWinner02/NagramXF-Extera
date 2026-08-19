package com.radolyn.ayugram.controllers;

import android.util.LruCache;
import com.radolyn.ayugram.utils.filters.HashablePattern;
import com.radolyn.ayugram.utils.filters.ReversiblePattern;
import java.util.ArrayList;
import java.util.HashSet;
import org.telegram.messenger.MessageObject;

public class AyuFilterCacheController {
    private final LruCache<String, Boolean> filterCache = new LruCache<>(1000);
    private final ArrayList<HashablePattern> sharedPatterns = new ArrayList<>();

    public Boolean isFiltered(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        if (messageObject == null) return null;
        String key = messageObject.getDialogId() + "_" + messageObject.getId();
        return filterCache.get(key);
    }

    public void putFiltered(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages, boolean isFiltered) {
        if (messageObject != null) {
            String key = messageObject.getDialogId() + "_" + messageObject.getId();
            filterCache.put(key, isFiltered);
        }
    }

    public void invalidate(MessageObject messageObject) {
        if (messageObject != null) {
            String key = messageObject.getDialogId() + "_" + messageObject.getId();
            filterCache.remove(key);
        }
    }

    public ArrayList<ReversiblePattern> getPatternsByDialogId(long dialogId) {
        return null;
    }

    public HashSet<HashablePattern> getExclusionsByDialogId(long dialogId) {
        return null;
    }

    public ArrayList<HashablePattern> getSharedPatterns() {
        return sharedPatterns;
    }
}
