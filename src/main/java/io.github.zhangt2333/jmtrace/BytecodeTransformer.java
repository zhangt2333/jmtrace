package io.github.zhangt2333.jmtrace;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;


/**
 * class file transformer for byte code
 * @author zhangt2333
 */
public class BytecodeTransformer implements ClassFileTransformer {

    private static final Class<? extends ClassLoader> EXT_CLASS_LOADER_CLASS = ClassLoader.getSystemClassLoader()
                                                                                          .getParent()
                                                                                          .getClass();

    private boolean debug;

    private ClassPool classPool;

    private static final String[] DEBUG_EXCLUDING_PACKAGE = {
        "org.gradle",
        "org.junit",
        "com.sun",
        "com.esotericsoftware",
        "worker.org.gradle",
        "org.slf4j",
        "net.rubygrapefruit",
        "sun.reflect",
        "junit.framework",
        "org.apache",
    };

    public BytecodeTransformer(boolean debug) {
        this.debug = debug;
        this.classPool = ClassPool.getDefault();
        this.classPool.appendSystemPath();
    }

    private boolean isExcludeClass(ClassLoader loader, String className) {
        // excluding the system library
        if (loader == null || EXT_CLASS_LOADER_CLASS.isInstance(loader)) {
            return true;
        }
        // excluding gradle and junit when debug is true
        if (debug) {
            for (String s : DEBUG_EXCLUDING_PACKAGE) {
                if (className.startsWith(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace('/', '.');
        if (isExcludeClass(loader, className)) {
            return null;
        }

        try {
            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            ClassFile classFile = ctClass.getClassFile();
            for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
                if (ctBehavior.isEmpty()) {
                    continue;
                }
                MethodInfo methodInfo = ctBehavior.getMethodInfo();
                CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
                System.out.printf("  [%s#%s%s]\n", ctClass.getName(), methodInfo.getName(),
                        methodInfo.getDescriptor());
                for (CodeIterator it = codeAttribute.iterator(); it.hasNext();) {
                    int address = it.next();
                    int opcode = it.byteAt(address);
                    switch (opcode) {
                        case Opcode.GETSTATIC:
                            int operand = it.s16bitAt(address + 1);
                            String fieldrefName = classFile.getConstPool().getFieldrefName(operand);
                            String fieldrefType = classFile.getConstPool().getFieldrefType(operand);
                            String fieldrefClassName = classFile.getConstPool().getFieldrefClassName(operand);

                            System.out.printf("line:%d %d %s %d read:%s.%s %s\n",
                                    methodInfo.getLineNumber(address),
                                    opcode, Mnemonic.OPCODE[opcode], operand,
                                    fieldrefClassName, fieldrefName, fieldrefType);
                            break;
                        default:
                            System.out.printf("%d %s\n", opcode, Mnemonic.OPCODE[opcode]);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
