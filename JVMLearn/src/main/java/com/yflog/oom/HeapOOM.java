package com.yflog.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rooter on 2016/3/15.
 * To make heap OOM easier to be observed, limit the heap size by:
 *  -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {

    public static void main(String[] args) {
        List<HeapOOM> oomObjs = new ArrayList<HeapOOM>();
        while (true) {
            oomObjs.add(new HeapOOM());
        }
    }
}
