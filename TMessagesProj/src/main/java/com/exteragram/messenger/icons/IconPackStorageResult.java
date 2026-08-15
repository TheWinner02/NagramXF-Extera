package com.exteragram.messenger.icons;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes4.dex */
public abstract class IconPackStorageResult<T> {
    public /* synthetic */ IconPackStorageResult(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public static final /* data */ class Success<T> extends IconPackStorageResult<T> {
        private final T value;

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            return (other instanceof Success) && Intrinsics.areEqual(this.value, ((Success) other).value);
        }

        public int hashCode() {
            T t = this.value;
            if (t == null) {
                return 0;
            }
            return t.hashCode();
        }

        public String toString() {
            return "Success(value=" + this.value + ')';
        }

        public Success(T t) {
            super(null);
            this.value = t;
        }

        public final T getValue() {
            return this.value;
        }
    }

    private IconPackStorageResult() {
    }

    public static final /* data */ class Failure extends IconPackStorageResult {
        private final IconPackStorageError error;

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            return (other instanceof Failure) && this.error == ((Failure) other).error;
        }

        public int hashCode() {
            return this.error.hashCode();
        }

        public String toString() {
            return "Failure(error=" + this.error + ')';
        }

        public Failure(IconPackStorageError iconPackStorageError) {
            super(null);
            this.error = iconPackStorageError;
        }

        public final IconPackStorageError getError() {
            return this.error;
        }
    }
}
