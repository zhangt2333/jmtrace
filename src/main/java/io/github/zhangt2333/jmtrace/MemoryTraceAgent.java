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
        judgeJDK8();
        addUrlToExtClassLoader();
        boolean debug = "debug=true".equals(arg);
        boolean test = "test=true".equals(arg);
        if (debug) {
            System.out.println("[debug] Javaagent premain running.");
        }
        instrumentation.addTransformer(new BytecodeTransformer(debug, test));
    }

    private static void judgeJDK8() {
        if (getJavaVersion() != 8) {
            System.err.println("Run failed, you should use Java 8, please make sure 'java -version' in your terminal will output 'version 1.8.*'!");
            System.exit(-1);
        }
    }

    private static void addUrlToExtClassLoader() {
        try {
            ClassLoader extClassLoader = ClassLoader.getSystemClassLoader().getParent();
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", java.net.URL.class);
            addURL.setAccessible(true);
            addURL.invoke(extClassLoader, MemoryTraceAgent.class.getProtectionDomain().getCodeSource().getLocation());
            extClassLoader.loadClass("io.github.zhangt2333.jmtrace.MemoryTraceLogUtils"); // to store System.out in advance
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if(version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if(dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }
}
