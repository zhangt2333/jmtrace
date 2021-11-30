package io.github.zhangt2333.jmtrace;

/**
 * some utils for logging memory trace
 * @author zhangt2333
 */
public class MemoryTraceLogUtils {

    private static void traceFieldRead1(int hashCode, String clzName, String fieldName) {
        System.out.printf(
            "R %d %016x %s.%s\n",
            Thread.currentThread().getId(),
            hashCode,
            clzName,
            fieldName
        );
    }

    public static void traceFieldRead(byte o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldRead(boolean o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldRead(char o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldRead(short o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldRead(int o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldRead(float o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldRead(Object o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldRead(long o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldRead(double o, String clzName, String fieldName) {
        traceFieldRead1(System.identityHashCode(o), clzName, fieldName);
    }

    private static void traceFieldWrite1(int hashCode, String clzName, String fieldName) {
        System.out.printf(
            "W %d %016x %s.%s\n",
            Thread.currentThread().getId(),
                hashCode,
            clzName,
            fieldName
        );
    }

    public static void traceFieldWrite(byte o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldWrite(boolean o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldWrite(char o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldWrite(short o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldWrite(int o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldWrite(float o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldWrite(Object o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldWrite(long o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }

    public static void traceFieldWrite(double o, String clzName, String fieldName) {
        traceFieldWrite1(System.identityHashCode(o), clzName, fieldName);
    }



    private static void traceArrayWrite1(Object array, int index, int hashCode) {
        System.out.printf(
            "W %d %016x %s[%d]\n",
            Thread.currentThread().getId(),
            hashCode,
            array.getClass().getComponentType().getCanonicalName(),
            index
        );
    }

    public static void traceArrayWrite(Object array, int index, byte value) {
        traceArrayWrite1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayWrite(Object array, int index, char value) {
        traceArrayWrite1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayWrite(Object array, int index, short value) {
        traceArrayWrite1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayWrite(Object array, int index, int value) {
        traceArrayWrite1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayWrite(Object array, int index, float value) {
        traceArrayWrite1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayWrite(Object array, int index, Object value) {
        traceArrayWrite1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayWrite(long value, int index) {
        System.out.printf(
            "W %d %016x long[%d]\n",
            Thread.currentThread().getId(),
            System.identityHashCode(value),
            index
        );
    }

    public static void traceArrayWrite(double value, int index) {
        System.out.printf(
            "W %d %016x double[%d]\n",
            Thread.currentThread().getId(),
            System.identityHashCode(value),
            index
        );
    }

    private static void traceArrayRead1(Object array, int index, int hashCode) {
        System.out.printf(
            "R %d %016x %s[%d]\n",
            Thread.currentThread().getId(),
            hashCode,
            array.getClass().getComponentType().getCanonicalName(),
            index
        );
    }

    public static void traceArrayRead(Object array, int index, byte value) {
        traceArrayRead1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayRead(Object array, int index, char value) {
        traceArrayRead1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayRead(Object array, int index, short value) {
        traceArrayRead1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayRead(Object array, int index, int value) {
        traceArrayRead1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayRead(Object array, int index, float value) {
        traceArrayRead1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayRead(Object array, int index, Object value) {
        traceArrayRead1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayRead(Object array, int index, long value) {
        traceArrayRead1(array, index, System.identityHashCode(value));
    }

    public static void traceArrayRead(Object array, int index, double value) {
        traceArrayRead1(array, index, System.identityHashCode(value));
    }
}
