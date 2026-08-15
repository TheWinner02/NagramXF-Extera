package org.simplifiles.archive.security;

public class SecurityPolicy {
    public static final SecurityPolicy DEFAULT = new SecurityPolicy();
    public static final SecurityPolicy INSTANCE = DEFAULT;

    public SecurityPolicy builder() { return this; }
    public SecurityPolicy maxEntries(long v) { return this; }
    public SecurityPolicy maxTotalUncompressedSize(long v) { return this; }
    public SecurityPolicy maxSingleFileSize(long v) { return this; }
    public SecurityPolicy maxCompressionRatio(double v) { return this; }
    public SecurityPolicy build() { return this; }
}
