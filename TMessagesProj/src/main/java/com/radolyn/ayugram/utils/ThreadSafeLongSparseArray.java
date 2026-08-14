package com.radolyn.ayugram.utils;

import android.util.LongSparseArray;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes.dex */
public class ThreadSafeLongSparseArray<E> {
    private final LongSparseArray<E> array = new LongSparseArray<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void put(long j, E e) {
        this.lock.writeLock().lock();
        try {
            this.array.put(j, e);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public E get(long j) {
        this.lock.readLock().lock();
        try {
            return this.array.get(j);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public E get(long j, E e) {
        this.lock.readLock().lock();
        try {
            return this.array.get(j, e);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public int size() {
        this.lock.readLock().lock();
        try {
            return this.array.size();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void remove(long j) {
        this.lock.writeLock().lock();
        try {
            this.array.remove(j);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void clear() {
        this.lock.writeLock().lock();
        try {
            this.array.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void removeAt(int i) {
        this.lock.writeLock().lock();
        try {
            this.array.removeAt(i);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public long keyAt(int i) {
        this.lock.readLock().lock();
        try {
            return this.array.keyAt(i);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public E valueAt(int i) {
        this.lock.readLock().lock();
        try {
            return this.array.valueAt(i);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void setValueAt(int i, E e) {
        this.lock.writeLock().lock();
        try {
            this.array.setValueAt(i, e);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public int indexOfKey(long j) {
        this.lock.readLock().lock();
        try {
            return this.array.indexOfKey(j);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public int indexOfValue(E e) {
        this.lock.readLock().lock();
        try {
            return this.array.indexOfValue(e);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void append(long j, E e) {
        this.lock.writeLock().lock();
        try {
            this.array.append(j, e);
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
