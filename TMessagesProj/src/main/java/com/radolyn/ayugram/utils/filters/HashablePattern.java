package com.radolyn.ayugram.utils.filters;

import java.util.Objects;
import java.util.regex.Pattern;

public class HashablePattern {
    private final Pattern pattern;
    private final boolean reversed;
    private final String rawPattern;

    public HashablePattern(String regex, boolean reversed) {
        this.rawPattern = regex;
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        this.reversed = reversed;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public boolean isReversed() {
        return reversed;
    }

    public String getRawPattern() {
        return rawPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashablePattern)) return false;
        HashablePattern that = (HashablePattern) o;
        return reversed == that.reversed && Objects.equals(rawPattern, that.rawPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawPattern, reversed);
    }
}
