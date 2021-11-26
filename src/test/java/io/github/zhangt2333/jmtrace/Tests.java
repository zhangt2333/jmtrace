package io.github.zhangt2333.jmtrace;

import org.junit.Test;


/**
 * JUnit Test cases for jmtrace
 * @author zhangt2333
 */
public class Tests {

    public static String s;

    @Test
    public void getStatic() {
        String ss = s;
    }

    @Test
    public void putStatic() {
        s = "aaa";
    }

    @Test
    public void getField() {
        String f = new A().f;
    }

    @Test
    public void putField() {
        new A().f = "field2";
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

}

class A {
    public String f;

    public byte[] bytes;
    public boolean[] booleans;
    public char[] chars;
    public short[] shorts;
    public int[] ints;
    public float[] floats;
    public double[] doubles;
    public long[] longs;

    public Byte[] bytess;
    public Boolean[] booleanss;
    public Character[] charss;
    public Short[] shortss;
    public Integer[] intss;
    public Float[] floatss;
    public Double[] doubless;
    public Long[] longss;
    public Object[] objects;
    public A[] as;
}