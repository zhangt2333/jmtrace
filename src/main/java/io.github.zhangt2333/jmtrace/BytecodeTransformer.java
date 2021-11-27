package io.github.zhangt2333.jmtrace;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Mnemonic;
import javassist.bytecode.Opcode;

import java.io.ByteArrayInputStream;
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
        // excluding special class in this project
        if (MemoryTraceLogUtils.class.getName().equals(className)) {
            return true;
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
            ConstPool constPool = classFile.getConstPool();
            for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
                if (ctBehavior.isEmpty()) {
                    continue;
                }
                MethodInfo methodInfo = ctBehavior.getMethodInfo();
                CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
                if (debug) {
                    System.out.printf("[debug]   [%s#%s%s]\n", ctClass.getName(), methodInfo.getName(),
                            methodInfo.getDescriptor());
                }
                for (CodeIterator it = codeAttribute.iterator(); it.hasNext();) {
                    int address = it.next();
                    int opcode = it.byteAt(address);
                    if (debug) {
                        System.out.printf("[debug] %d %s\n", address, Mnemonic.OPCODE[opcode]);
                    }
                    switch (opcode) {
                        case Opcode.GETSTATIC:
                        case Opcode.GETFIELD:
                            handleGetfield(it, constPool, address);
                            break;
                        case Opcode.PUTSTATIC:
                        case Opcode.PUTFIELD:
                            handlePutfield(it, constPool, address);
                            break;
                        case Opcode.BASTORE:
                            handle32xastore(it, constPool, address, "B");
                            break;
                        case Opcode.CASTORE:
                            handle32xastore(it, constPool, address, "C");
                            break;
                        case Opcode.SASTORE:
                            handle32xastore(it, constPool, address, "S");
                            break;
                        case Opcode.IASTORE:
                            handle32xastore(it, constPool, address, "I");
                            break;
                        case Opcode.FASTORE:
                            handle32xastore(it, constPool, address, "F");
                            break;
                        case Opcode.AASTORE:
                            handle32xastore(it, constPool, address, "Ljava/lang/Object;");
                            break;
                        case Opcode.DASTORE:
                            handle64xastore(it, constPool, address, 'D');
                            break;
                        case Opcode.LASTORE:
                            handle64xastore(it, constPool, address, 'J');
                            break;
                        case Opcode.BALOAD:
                            handle32xaload(it, constPool, address, "B");
                            break;
                        case Opcode.CALOAD:
                            handle32xaload(it, constPool, address, "C");
                            break;
                        case Opcode.SALOAD:
                            handle32xaload(it, constPool, address, "S");
                            break;
                        case Opcode.IALOAD:
                            handle32xaload(it, constPool, address, "I");
                            break;
                        case Opcode.FALOAD:
                            handle32xaload(it, constPool, address, "F");
                            break;
                        case Opcode.AALOAD:
                            handle32xaload(it, constPool, address, "Ljava/lang/Object;");
                            break;
                        case Opcode.LALOAD:
                            handle64xaload(it, constPool, address, "J");
                            break;
                        case Opcode.DALOAD:
                            handle64xaload(it, constPool, address, "D");
                            break;
                    }
                }
                if (debug) {
                    System.out.println("[debug] -------- after manipulating --------");
                    for (CodeIterator it = codeAttribute.iterator(); it.hasNext();) {
                        int address = it.next();
                        int opcode = it.byteAt(address);
                        System.out.printf("[debug] %d %s\n", address, Mnemonic.OPCODE[opcode]);
                    }
                }
                codeAttribute.computeMaxStack();
            }
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* for getstatic/putstatic/getfield/putfield */

    private Bytecode getBytecodeForField(CodeIterator it, ConstPool constPool, int address) {
        int operand = it.s16bitAt(address + 1);
        String fieldName = constPool.getFieldrefName(operand);
        String fieldClassName = constPool.getFieldrefClassName(operand);
        Bytecode bytecode = new Bytecode(constPool);
        bytecode.addOpcode(Opcode.DUP);
        bytecode.addLdc(fieldClassName);
        bytecode.addLdc(fieldName);
        return bytecode;
    }

    private void handlePutfield(CodeIterator it, ConstPool constPool, int address) throws BadBytecode {
        Bytecode bytecode = getBytecodeForField(it, constPool, address);
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
            "traceFieldWrite", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V");
        it.insert(address, bytecode.get());
    }

    private void handleGetfield(CodeIterator it, ConstPool constPool, int address) throws BadBytecode {
        Bytecode bytecode = getBytecodeForField(it, constPool, address);
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
            "traceFieldRead", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V");
        it.insert(bytecode.get());
    }

    /* for *astore/*aload */

    private Bytecode getBytecodeFor32Array(CodeIterator it, ConstPool constPool, int address) {
        Bytecode bytecode = new Bytecode(constPool);
        // ..., arr, index, value(4byte)
        bytecode.add(Opcode.DUP2_X1);
        // ..., index, value(4byte), arr, index, value(4byte)
        bytecode.add(Opcode.POP);
        // ..., index, value(4byte), arr, index
        bytecode.add(Opcode.DUP2_X1);
        // ..., index, arr, index, value(4byte), arr, index
        bytecode.add(Opcode.POP);
        // ..., index, arr, index, value(4byte), arr
        bytecode.add(Opcode.DUP2_X1);
        // ..., index, arr, value(4byte), arr, index, value(4byte), arr
        bytecode.add(Opcode.POP);
        // ..., index, arr, value(4byte), arr, index, value(4byte)
        return bytecode;
    }

    private void bytecodeEndFor32xastore(Bytecode bytecode) {
        // ..., index, arr, value(4byte)
        bytecode.add(Opcode.DUP2_X1);
        // ..., arr, value(4byte), index, arr, value(4byte)
        bytecode.add(Opcode.POP2);
        // ..., arr, value(4byte), index
        bytecode.add(Opcode.SWAP);
        // ..., arr, index, value(4byte)
    }

    private void handle32xastore(CodeIterator it, ConstPool constPool, int address, String x) throws BadBytecode {
        Bytecode bytecode = getBytecodeFor32Array(it, constPool, address);
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
                "traceArrayWrite", String.format("(Ljava/lang/Object;I%s)V", x));
        // ..., index, arr, value(4byte)
        bytecodeEndFor32xastore(bytecode);
        it.insert(address, bytecode.get());
    }

    private void handle64xastore(CodeIterator it, ConstPool constPool, int address, char x) throws BadBytecode {
        Bytecode bytecode = new Bytecode(constPool);
        // ..., arr, index, value(8byte)
        bytecode.add(Opcode.DUP2_X1);
        // ..., arr, value(8byte), index, value(8byte)
        bytecode.add(Opcode.DUP2_X1);
        // ..., arr, value(8byte), value(8byte), index, value(8byte)
        bytecode.add(Opcode.POP2);
        // ..., arr, value(8byte), value(8byte), index
        bytecode.add(Opcode.DUP_X2);
        // ..., arr, value(8byte), index, value(8byte), index
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
                "traceArrayWrite", String.format("(%CI)V", x));
        // ..., arr, value(8byte), index
        bytecode.add(Opcode.DUP_X2);
        // ..., arr, index, value(8byte), index
        bytecode.add(Opcode.POP);
        // ..., arr, index, value(8byte)
        it.insert(address, bytecode.get());
    }

    private void handle32xaload(CodeIterator it, ConstPool constPool, int address, String x) throws BadBytecode {
        Bytecode bytecode = new Bytecode(constPool);
        // ..., arr, index
        bytecode.add(Opcode.DUP2);
        // ..., arr, index, arr, index
        it.insert(address, bytecode.get());
        // ..., arr, index, value(4Byte)
        bytecode = new Bytecode(constPool);
        bytecode.add(Opcode.DUP_X2);
        // ..., value(4Byte), arr, index, value(4Byte)
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
                "traceArrayRead", String.format("(Ljava/lang/Object;I%s)V", x));
        it.insert(bytecode.get());
    }

    private void handle64xaload(CodeIterator it, ConstPool constPool, int address, String x) throws BadBytecode {
        Bytecode bytecode = new Bytecode(constPool);
        // ..., arr, index
        bytecode.add(Opcode.DUP2);
        // ..., arr, index, arr, index
        it.insert(address, bytecode.get());
        // ..., arr, index, value(8Byte)
        bytecode = new Bytecode(constPool);
        bytecode.add(Opcode.DUP2_X2);
        // ..., value(8Byte), arr, index, value(8Byte)
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
                "traceArrayRead", String.format("(Ljava/lang/Object;I%s)V", x));
        it.insert(bytecode.get());
    }
}
