package io.github.zhangt2333.jmtrace;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author zhangt2333
 */
public class TemperingSystemOut {
    int f;
    public TemperingSystemOut() {
        PrintStream tempOut = System.out;
        PrintStream tempErr = System.err;
        System.setOut(new PrintStream(new ByteArrayOutputStream(), false));
        System.setErr(new PrintStream(new ByteArrayOutputStream(), false));
        this.f = 10;
        System.setOut(tempOut);
        System.setErr(tempErr);
    }
}


