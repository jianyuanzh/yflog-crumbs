package com.yflog.sync.tool;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Vincent on 6/11/16.
 * A demo of using CountDownLatch. Here CountDownLatch is used to impl as:
 * 1. Starting gate,
 * 2. Ending gate
 */
public class LatchDemo {
    public long taskTimeCalc(int nThread, final Runnable task) throws InterruptedException {
        final CountDownLatch startingGate = new CountDownLatch(1);
        final CountDownLatch endingGate = new CountDownLatch(nThread);

        for (int i = 0; i < nThread; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        startingGate.await();
                        try {
                            task.run();
                        }
                        finally {
                            endingGate.countDown();
                        }
                    }
                    catch (InterruptedException ignored) {
                    }
                }
            };
            thread.start();
        }
        long start = System.currentTimeMillis();
        startingGate.countDown();
        endingGate.await();
        long end = System.currentTimeMillis();
        return end - start;
    }
    public static void main(String[] args) throws InterruptedException {
        final Random random = new Random();
        Runnable task = new Runnable() {
            public void run() {
                long intervalInSec = 1000 * ( Math.abs(random.nextInt()) % 5);
                try {
                    Thread.sleep(intervalInSec);
                }
                catch (InterruptedException ignored) {
                }
            }
        };

        long interval = new LatchDemo().taskTimeCalc(10, task);
        System.out.println("Time elapsed " + interval/1000 + "s" ) ;
    }
}
