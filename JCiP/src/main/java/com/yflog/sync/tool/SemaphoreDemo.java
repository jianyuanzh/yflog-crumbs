package com.yflog.sync.tool;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by root on 6/11/16.
 */
public class SemaphoreDemo {
    public static class BoundedArrayList<T> {
        private final List<T> _inner;
        private final Semaphore _semaphore;

        public BoundedArrayList(int size) {
            _inner = new ArrayList<T>();
            _semaphore = new Semaphore(size);
        }

        public boolean add(T t) throws InterruptedException {
            _semaphore.acquire();
            boolean added = false;
            try {
                added = _inner.add(t);
            }
            finally {
                if (!added)
                    _semaphore.release();
            }
            return added;
        }

        public boolean remove(T t) {
            boolean removed = _inner.remove(t);
            if (removed)
                _semaphore.release();
            return removed;
        }
    }
}
