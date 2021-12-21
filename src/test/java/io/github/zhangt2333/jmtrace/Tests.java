package io.github.zhangt2333.jmtrace;

import org.junit.Test;

import java.lang.reflect.Constructor;

/**
 * JUnit Test cases for jmtrace
 * @author zhangt2333
 */
public class Tests {

    public static String aStaticString;
    public static byte aStaticByte;
    public static boolean aStaticBoolean;
    public static char aStaticChar;
    public static short aStaticShort;
    public static int aStaticInt;
    public static float aStaticFloat;
    public static double aStaticDouble;
    public static long aStaticLong;

    @Test
    public void getStatic() {
        String aString = aStaticString;
        byte aByte = aStaticByte;
        boolean aBoolean = aStaticBoolean;
        char aChar = aStaticChar;
        short aShort = aStaticShort;
        int anInt = aStaticInt;
        float aFloat = aStaticFloat;
        double aDouble = aStaticDouble;
        long aLong = aStaticLong;
    }

    @Test
    public void putStatic() {
        aStaticString= "ss";
        aStaticByte = 5;
        aStaticBoolean = true;
        aStaticChar = 'c';
        aStaticShort = 6;
        aStaticInt = 100;
        aStaticFloat = 0.3f;
        aStaticDouble = 0.34;
        aStaticLong = 3L;
    }

    @Test
    public void getField() {
        A a = new A();
        String aString = a.aString;
        byte aByte = a.aByte;
        boolean aBoolean = a.aBoolean;
        char aChar = a.aChar;
        short aShort = a.aShort;
        int anInt = a.anInt;
        float aFloat = a.aFloat;
        double aDouble = a.aDouble;
        long aLong = a.aLong;
    }

    @Test
    public void putField() {
        A a = new A();
        a.aString = "ss";
        a.aByte = 5;
        a.aBoolean = true;
        a.aChar = 'c';
        a.aShort = 6;
        a.anInt = 100;
        a.aFloat = 0.3f;
        a.aDouble = 0.34;
        a.aLong = 3L;
    }

    @Test
    public void byteArrayStore() {
        A a = new A();

        a.bytes = new byte[2];
        a.bytes[0] = 5;
        a.bytes[1] = 6;
    }

    @Test
    public void byteArrayLoad() {
        A a = new A();

        a.bytes = new byte[2];
        byte b = a.bytes[0];
        b = a.bytes[1];
    }

    @Test
    public void booleanArrayStore() {
        A a = new A();

        a.booleans = new boolean[2];
        a.booleans[0] = true;
        a.booleans[1] = false;
    }

    @Test
    public void booleanArrayLoad() {
        A a = new A();

        a.booleans = new boolean[2];
        boolean b = a.booleans[0];
        b = a.booleans[1];
    }

    @Test
    public void charArrayStore() {
        A a = new A();

        a.chars = new char[2];
        a.chars[0] = 'c';
        a.chars[1] = 'b';
    }

    @Test
    public void charArrayLoad() {
        A a = new A();

        a.chars = new char[2];
        char c = a.chars[0];
        c = a.chars[1];
    }

    @Test
    public void shortArrayStore() {
        A a = new A();

        a.shorts = new short[2];
        a.shorts[0] = 10;
        a.shorts[1] = 20;
    }

    @Test
    public void shortArrayLoad() {
        A a = new A();

        a.shorts = new short[2];
        short s = a.shorts[0];
        s = a.shorts[1];
    }

    @Test
    public void intArrayStore() {
        A a = new A();

        a.ints = new int[2];
        a.ints[0] = 10;
        a.ints[1] = 20;
    }

    @Test
    public void intArrayLoad() {
        A a = new A();

        a.ints = new int[2];
        int i = a.ints[0];
        i = a.ints[1];
    }

    @Test
    public void floatArrayStore() {
        A a = new A();

        a.floats = new float[2];
        a.floats[0] = 0.3f;
        a.floats[1] = 0.4f;
    }

    @Test
    public void floatArrayLoad() {
        A a = new A();

        a.floats = new float[2];
        float f = a.floats[0];
        f = a.floats[1];
    }

    @Test
    public void longArrayStore() {
        A a = new A();

        a.longs = new long[2];
        a.longs[0] = 20L;
        a.longs[1] = 21L;
    }

    @Test
    public void longArrayLoad() {
        A a = new A();

        a.longs = new long[2];
        long l = a.longs[0];
        l = a.longs[1];
    }

    @Test
    public void doubleArrayStore() {
        A a = new A();

        a.doubles = new double[2];
        a.doubles[0] = 0.33;
        a.doubles[1] = 0.44;
    }

    @Test
    public void doubleArrayLoad() {
        A a = new A();

        a.doubles = new double[2];
        double d = a.doubles[0];
        d = a.doubles[1];
    }

    @Test
    public void objectArrayStore() {
        A a = new A();

        a.bytess = new Byte[2];
        a.bytess[0] = 5;
        a.bytess[1] = 6;

        a.booleanss = new Boolean[2];
        a.booleanss[0] = true;
        a.booleanss[1] = false;

        a.charss = new Character[2];
        a.charss[0] = 'c';
        a.charss[1] = 'b';

        a.shortss = new Short[2];
        a.shortss[0] = 10;
        a.shortss[1] = 20;

        a.intss = new Integer[2];
        a.intss[0] = 10;
        a.intss[1] = 20;

        a.floatss = new Float[2];
        a.floatss[0] = 0.3f;
        a.floatss[1] = 0.4f;

        a.longss = new Long[2];
        a.longss[0] = 20L;
        a.longss[1] = 21L;

        a.doubless = new Double[2];
        a.doubless[0] = 0.33;
        a.doubless[1] = 0.44;

        a.objects = new Object[2];
        a.objects[0] = new A();
        a.objects[1] = new A();

        a.as = new A[2];
        a.as[0] = new A();
        a.as[1] = new A();
    }

    @Test
    public void innerClass() {
        new InnerClass();
    }

    @Test
    public void temperingSystemOut() {
        new TemperingSystemOut();
    }

    @Test
    public void objectArrayLoad() {
        A a = new A();

        a.bytess = new Byte[2];
        Byte b = a.bytess[0];
        b = a.bytess[1];

        a.booleanss = new Boolean[2];
        Boolean bool = a.booleanss[0];
        bool = a.booleanss[1];

        a.charss = new Character[2];
        Character c = a.charss[0];
        c = a.charss[1];

        a.shortss = new Short[2];
        Short s = a.shortss[0];
        s = a.shortss[1];

        a.intss = new Integer[2];
        Integer i = a.intss[0];
        i = a.intss[1];

        a.floatss = new Float[2];
        Float f = a.floatss[0];
        f = a.floatss[1];

        a.longss = new Long[2];
        Long l = a.longss[0];
        l = a.longss[1];

        a.doubless = new Double[2];
        Double d = a.doubless[0];
        d = a.doubless[1];

        a.objects = new Object[2];
        Object o = a.objects[0];
        o = a.objects[1];

        a.as = new A[2];
        A a1 = a.as[0];
        a1 = a.as[1];
    }

    /**
     * A bug triggered by @see worker.org.gradle.api.JavaVersion#getVersionForMajor
     * <br>
     * this reason of the bug is that Javassist has not edited the operand of goto instruction when we call `CodeIterator::insert` twice
     * <br>
     * <pre>
     * Instruction type does not match stack map
     * Exception Details:
     *   Location:
     *     io/github/zhangt2333/jmtrace/JavaVersion.getVersionForMajor(I)Lio/github/zhangt2333/jmtrace/JavaVersion; @30: dup_x2
     *   Reason:
     *     Current frame's stack size doesn't match stackmap.
     *   Current Frame:
     *     bci: @30
     *     flags: { }
     *     locals: { integer }
     *     stack: { '[Lio/github/zhangt2333/jmtrace/JavaVersion;', integer, 'io/github/zhangt2333/jmtrace/JavaVersion' }
     *   Stackmap Frame:
     *     bci: @30
     *     flags: { }
     *     locals: { integer }
     *     stack: { 'io/github/zhangt2333/jmtrace/JavaVersion' }
     *   Bytecode:
     *     0x0000000: 1ab8 0010 bea1 0011 b200 1159 12a6 123b
     *     0x0000010: b800 b0a7 000b b800 101a 0464 5c32 5bb8
     *     0x0000020: 00b8 b0
     *   Stackmap Table:
     *     same_frame(@22)
     *     same_locals_1_stack_item_frame(@30,Object[#103])
     * </pre>
     */
    @Test
    public void bug1() {
        JavaVersion.getVersionForMajor(1);
    }

    /**
     * A bug triggered by @see worker.org.gradle.process.internal.worker.GradleWorkerMain#run line68
     * <br>
     * because the @see worker.org.gradle.internal.classloader.FilteringClassLoader line91 shadows the @see sun.misc.Launcher.AppClassLoader
     * which can load our class @see io.github.zhangt2333.jmtrace.MemoryTraceLogUtils.
     * <br>
     * The call stack is as follows: <br>
     * <pre>
     * loadClass:408, ClassLoader (java.lang) [2]
     * loadClass:352, ClassLoader (java.lang)
     * loadClass:91, FilteringClassLoader (worker.org.gradle.internal.classloader)
     * loadClass:406, ClassLoader (java.lang) [1]
     * loadClass:352, ClassLoader (java.lang)
     * <init>:75, SystemApplicationClassLoaderWorker (org.gradle.process.internal.worker.child)
     * newInstance0:-1, NativeConstructorAccessorImpl (sun.reflect)
     * newInstance:62, NativeConstructorAccessorImpl (sun.reflect)
     * newInstance:45, DelegatingConstructorAccessorImpl (sun.reflect)
     * newInstance:423, Constructor (java.lang.reflect)
     * run:68, GradleWorkerMain (worker.org.gradle.process.internal.worker)
     * main:74, GradleWorkerMain (worker.org.gradle.process.internal.worker)
     * </pre>
     * <br>
     * @see io.github.zhangt2333.jmtrace.CustomClassLoader for reproducing the bug
     */
    @Test
    public void bug2() throws Exception{
        Class<?> aClass = CustomClassLoader.make().loadClass(A.class.getName());
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(String.class);
        declaredConstructor.setAccessible(true);
        declaredConstructor.newInstance("aa");
    }
}