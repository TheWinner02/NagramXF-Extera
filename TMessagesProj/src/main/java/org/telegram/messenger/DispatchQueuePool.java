package org.telegram.messenger;

import java.util.LinkedList;

public class DispatchQueuePool {
    private final int maxCount;
    private final LinkedList<DispatchQueue> queues = new LinkedList<>();

    public DispatchQueuePool(int count) {
        this.maxCount = count;
    }

    public synchronized DispatchQueue getNextQueue() {
        if (queues.isEmpty()) {
            return new DispatchQueue("DispatchQueuePool_" + System.currentTimeMillis());
        }
        return queues.removeFirst();
    }

    public synchronized void execute(Runnable runnable) {
        DispatchQueue queue = getNextQueue();
        queue.postRunnable(() -> {
            runnable.run();
            synchronized (DispatchQueuePool.this) {
                if (queues.size() < maxCount) {
                    queues.add(queue);
                } else {
                    queue.recycle();
                }
            }
        });
    }
}
