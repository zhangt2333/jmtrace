package io.github.zhangt2333.jmtrace;

import java.lang.instrument.Instrumentation;

/**
 * premain entry of agent
 * @author zhangt2333
 */
public class MemoryTraceAgent {
    public static void premain(String arg, Instrumentation instrumentation) {
        System.out.println("Javaagent premain running, arg:" + arg);
        boolean debug = "debug=true".equals(arg);
        instrumentation.addTransformer(new BytecodeTransformer(debug));
    }
}
