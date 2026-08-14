package com.radolyn.ayugram.utils.filters;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes5.dex */
public class HashablePattern {
    private final UUID id;
    private final ReversiblePattern pattern;

    public HashablePattern(UUID uuid, ReversiblePattern reversiblePattern) {
        this.id = uuid;
        this.pattern = reversiblePattern;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.id, ((HashablePattern) obj).id);
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public UUID getId() {
        return this.id;
    }

    public Pattern getPattern() {
        return this.pattern.pattern();
    }

    public boolean isReversed() {
        return this.pattern.reversed();
    }
}
