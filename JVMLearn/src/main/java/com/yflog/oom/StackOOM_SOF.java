package com.yflog.oom;

/**
 * Created by rooter on 2016/3/15.
 * Limit the stack size by following JVM attr:
 *  -Xss128k
 */
public class StackOOM_SOF {

    private long stackDepth = 0;
    public void recurisivelyCall() {
        stackDepth++;
        recurisivelyCall();
    }

    public static void main(String[] args) {
        StackOOM_SOF sof = new StackOOM_SOF();
        try {
            sof.recurisivelyCall();
        }
        catch (Throwable e) {
            System.out.println("Stack depth: " + sof.stackDepth);
            throw e;
        }
    }
}
