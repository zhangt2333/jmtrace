package io.github.zhangt2333.jmtrace;

import java.lang.instrument.Instrumentation;

/**
 * premain entry of agent
 * @author zhangt2333
 */
public class MemoryTraceAgent {
    public static void premain(String arg, Instrumentation instrumentation) {
        boolean debug = "debug=true".equals(arg);
        if (debug) {
            System.out.println("[debug] Javaagent premain running.");
        }
        instrumentation.addTransformer(new BytecodeTransformer(debug));
    }
}
