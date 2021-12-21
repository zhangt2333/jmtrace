package io.github.zhangt2333.jmtrace;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * premain entry of agent
 * @author zhangt2333
 */
public class MemoryTraceAgent {
    public static void premain(String arg, Instrumentation instrumentation) {
        addUrlToExtClassLoader();
        boolean debug = "debug=true".equals(arg);
        boolean test = "test=true".equals(arg);
        if (debug) {
            System.out.println("[debug] Javaagent premain running.");
        }
        instrumentation.addTransformer(new BytecodeTransformer(debug, test));
    }

    private static void addUrlToExtClassLoader() {
        try {
            ClassLoader extClassLoader = ClassLoader.getSystemClassLoader().getParent();
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", java.net.URL.class);
            addURL.setAccessible(true);
            addURL.invoke(extClassLoader, MemoryTraceAgent.class.getProtectionDomain().getCodeSource().getLocation());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
