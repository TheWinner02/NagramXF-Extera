package com.radolyn.ayugram.utils.filters;

import java.util.regex.Pattern;

public class ReversiblePattern {
    private final Pattern pattern;
    private final boolean reversed;

    public ReversiblePattern(Pattern pattern, boolean reversed) {
        this.pattern = pattern;
        this.reversed = reversed;
    }

    public Pattern pattern() {
        return pattern;
    }

    public boolean reversed() {
        return reversed;
    }
}
