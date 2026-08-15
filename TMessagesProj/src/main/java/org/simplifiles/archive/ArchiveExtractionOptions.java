package org.simplifiles.archive;

public class ArchiveExtractionOptions {
    public static final ArchiveExtractionOptions INSTANCE = new ArchiveExtractionOptions();

    public ArchiveExtractionOptions builder() { return this; }
    public ArchiveExtractionOptions targetPolicy(ExtractionTargetPolicy policy) { return this; }
    public ArchiveExtractionOptions build() { return this; }
}
