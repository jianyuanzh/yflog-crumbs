package com.yflog.sync.tool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by root on 6/11/16.
 */
public class FutureDemo {

    public void futureAsLatch() throws ExecutionException, InterruptedException {
        FutureTask<Long> futureTask = new FutureTask<Long>(new Callable<Long>() {
            public Long call() throws Exception {
                Thread.sleep(5000);
                return System.currentTimeMillis();
            }
        });


        new Thread(futureTask).start();

        System.out.println("Start get future result");
        long start = System.currentTimeMillis();
        long result = futureTask.get();
        long end = System.currentTimeMillis();
        System.out.println(String.format("Finish get future result - result=%d, timeElapsed=%d ms", result, end - start));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new FutureDemo().futureAsLatch();
    }

}
