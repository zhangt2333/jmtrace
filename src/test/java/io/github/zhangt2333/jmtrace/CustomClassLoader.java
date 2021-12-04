package io.github.zhangt2333.jmtrace;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * for reproducing the bug because classloader shadows the @see sun.misc.Launcher.AppClassLoader
 * @author zhangt2333
 */
public class CustomClassLoader extends URLClassLoader {

    private static final ClassLoader EXT_CLASS_LOADER = ClassLoader.getSystemClassLoader().getParent();;

    public static CustomClassLoader make() {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        try {
            Field ucp = systemClassLoader.getClass().getDeclaredField("ucp");
            ucp.setAccessible(true);
            Object urlClassPath = ucp.get(systemClassLoader);
            URL[] urls = (URL[]) urlClassPath.getClass().getDeclaredMethod("getURLs").invoke(urlClassPath);
            return new CustomClassLoader(urls, systemClassLoader);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected CustomClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (MemoryTraceLogUtils.class.getName().equals(name)) {
            return EXT_CLASS_LOADER.loadClass(name);
        }
        if (A.class.getName().equals(name)) {
            return loadClass(name, true);
        }
        return super.loadClass(name, true);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                long t1 = System.nanoTime();
                c = findClass(name);
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}
