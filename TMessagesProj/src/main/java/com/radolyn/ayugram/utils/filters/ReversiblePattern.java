package com.radolyn.ayugram.utils.filters;

import java.util.Objects;
import java.util.regex.Pattern;

public final class ReversiblePattern {
    private final Pattern pattern;
    private final boolean reversed;

    public ReversiblePattern(Pattern pattern, boolean z) {
        this.pattern = pattern;
        this.reversed = z;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof ReversiblePattern)) {
            return false;
        }
        ReversiblePattern reversiblePattern = (ReversiblePattern) obj;
        return this.reversed == reversiblePattern.reversed && Objects.equals(this.pattern, reversiblePattern.pattern);
    }

    public final int hashCode() {
        return Objects.hash(pattern, reversed);
    }

    public Pattern pattern() {
        return this.pattern;
    }

    public boolean reversed() {
        return this.reversed;
    }

    public final String toString() {
        return "ReversiblePattern[pattern=" + pattern + ", reversed=" + reversed + "]";
    }
}
