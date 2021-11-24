package io.github.zhangt2333.jmtrace;

import org.junit.Test;


/**
 * JUnit Test cases
 * @author zhangt2333
 */
public class Tests {

    @Test
    public void simple() {
        int a = 1;
        a = 10;
        int b = a;
    }

    public static String s = "aa";
    @Test
    public void getStatic() {
        String ss = s;
    }

    public static final String sFinal = "aa";

    @Test
    public void getStaticFinal() {
        String ss = sFinal;
    }

    @Test
    public void call() {
        io.github.zhangt2333.jmtrace.MemoryTraceLogUtils.traceStaticRead();
    }
}
