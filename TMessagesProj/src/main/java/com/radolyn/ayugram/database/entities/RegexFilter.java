package com.radolyn.ayugram.database.entities;

import java.util.UUID;

/* JADX INFO: loaded from: classes5.dex */
public class RegexFilter {
    public boolean caseInsensitive;
    public Long dialogId;
    public boolean enabled;
    public UUID id = UUID.randomUUID();
    public boolean reversed;
    public String text;
}
