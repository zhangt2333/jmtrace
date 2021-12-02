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

    private static final String[] EXCLUDING_SYSTEM_PACKAGE = {
        "com.sun",
        "sun.reflect",
    };

    private static final String[] EXCLUDING_DEBUG_PACKAGE = {
        "org.gradle",
        "org.junit",
        "com.esotericsoftware",
        "worker.org.gradle",
        "org.slf4j",
        "net.rubygrapefruit",
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
        for (String s : EXCLUDING_SYSTEM_PACKAGE) {
            if (className.startsWith(s)) {
                return true;
            }
        }
        // excluding gradle and junit when debug is true
        if (debug) {
            for (String s : EXCLUDING_DEBUG_PACKAGE) {
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
                    printAllBytecode(codeAttribute);
                }

                for (CodeIterator it = codeAttribute.iterator(); it.hasNext();) {
                    int address = it.next();
                    int opcode = it.byteAt(address);
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
                        case Opcode.CASTORE:
                        case Opcode.SASTORE:
                        case Opcode.IASTORE:
                        case Opcode.FASTORE:
                        case Opcode.AASTORE:
                            handle32xastore(it, constPool, address, opcode);
                            break;
                        case Opcode.DASTORE:
                        case Opcode.LASTORE:
                            handle64xastore(it, constPool, address, opcode);
                            break;
                        case Opcode.BALOAD:
                        case Opcode.CALOAD:
                        case Opcode.SALOAD:
                        case Opcode.IALOAD:
                        case Opcode.FALOAD:
                        case Opcode.AALOAD:
                            handle32xaload(it, constPool, address, opcode);
                            break;
                        case Opcode.LALOAD:
                        case Opcode.DALOAD:
                            handle64xaload(it, constPool, address, opcode);
                            break;
                    }
                }
                if (debug) {
                    System.out.println("[debug] -------- after manipulating --------");
                    printAllBytecode(codeAttribute);
                }
                codeAttribute.computeMaxStack();
            }
            return ctClass.toBytecode();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("frozen")) {
                try {
                    return classPool.get(className).toBytecode();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printAllBytecode(CodeAttribute codeAttribute) throws BadBytecode {
        for (CodeIterator it = codeAttribute.iterator(); it.hasNext();) {
            int address = it.next();
            int opcode = it.byteAt(address);
            if (Opcode.GOTO == opcode || Opcode.IF_ICMPLT == opcode) {
                int operand = it.s16bitAt(address + 1);
                System.out.printf("[debug] %d %s %d\n", address, Mnemonic.OPCODE[opcode], operand);
            } else {
                System.out.printf("[debug] %d %s\n", address, Mnemonic.OPCODE[opcode]);
            }
        }
    }


    /* for getstatic/putstatic/getfield/putfield */

    private String handleBytecodeForField(Bytecode bytecode, CodeIterator it, ConstPool constPool, int address) {
        int operand = it.s16bitAt(address + 1);
        String fieldName = constPool.getFieldrefName(operand);
        String fieldClassName = constPool.getFieldrefClassName(operand);
        String fieldType = constPool.getFieldrefType(operand);
        fieldType = fieldType.length() > 1 ? "Ljava/lang/Object;" : fieldType;
        if ("J".equals(fieldType) || "D".equals(fieldType)) {
            bytecode.add(Opcode.DUP2);
        } else {
            bytecode.add(Opcode.DUP);
        }
        bytecode.addLdc(fieldClassName);
        bytecode.addLdc(fieldName);
        return fieldType;
    }

    private void handlePutfield(CodeIterator it, ConstPool constPool, int address) throws BadBytecode {
        Bytecode bytecode = new Bytecode(constPool);
        String fieldType = handleBytecodeForField(bytecode, it, constPool, address);
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
            "traceFieldWrite", String.format("(%sLjava/lang/String;Ljava/lang/String;)V", fieldType));
        it.insert(address, bytecode.get());
    }

    private void handleGetfield(CodeIterator it, ConstPool constPool, int address) throws BadBytecode {
        Bytecode bytecode = new Bytecode(constPool);
        String fieldType = handleBytecodeForField(bytecode, it, constPool, address);
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
            "traceFieldRead", String.format("(%sLjava/lang/String;Ljava/lang/String;)V", fieldType));
        it.insert(bytecode.get());
    }

    /* for *astore/*aload */

    private String opcodeToDescriptor(int opcode) {
        char x = Character.toUpperCase(Mnemonic.OPCODE[opcode].charAt(0));
        if (x == 'L') {
            return "J";
        }
        if (x == 'A') {
            return "Ljava/lang/Object;";
        }
        return String.valueOf(x);
    }

    private void handle32xastore(CodeIterator it, ConstPool constPool, int address, int opcode) throws BadBytecode {
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
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
                "traceArrayWrite", String.format("(Ljava/lang/Object;I%s)V", opcodeToDescriptor(opcode)));
        // ..., index, arr, value(4byte)
        bytecode.add(Opcode.DUP2_X1);
        // ..., arr, value(4byte), index, arr, value(4byte)
        bytecode.add(Opcode.POP2);
        // ..., arr, value(4byte), index
        bytecode.add(Opcode.SWAP);
        // ..., arr, index, value(4byte)
        it.insert(address, bytecode.get());
    }

    private void handle64xastore(CodeIterator it, ConstPool constPool, int address, int opcode) throws BadBytecode {
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
                "traceArrayWrite", String.format("(%sI)V", opcodeToDescriptor(opcode)));
        // ..., arr, value(8byte), index
        bytecode.add(Opcode.DUP_X2);
        // ..., arr, index, value(8byte), index
        bytecode.add(Opcode.POP);
        // ..., arr, index, value(8byte)
        it.insert(address, bytecode.get());
    }

    private void handle32xaload(CodeIterator it, ConstPool constPool, int address, int opcode) throws BadBytecode {
        Bytecode bytecode = new Bytecode(constPool);
        // ..., arr, index
        bytecode.add(Opcode.DUP2);
        // ..., arr, index, arr, index
        bytecode.add(Opcode.DUP2);
        // ..., arr, index, arr, index, arr, index
        bytecode.add(opcode);
        // ..., arr, index, arr, index, value(4Byte)
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
                "traceArrayRead", String.format("(Ljava/lang/Object;I%s)V", opcodeToDescriptor(opcode)));
        // ..., arr, index
        it.insert(address, bytecode.get());
    }

    private void handle64xaload(CodeIterator it, ConstPool constPool, int address, int opcode) throws BadBytecode {
        Bytecode bytecode = new Bytecode(constPool);
        // ..., arr, index
        bytecode.add(Opcode.DUP2);
        // ..., arr, index, arr, index
        bytecode.add(Opcode.DUP2);
        // ..., arr, index, arr, index, arr, index
        bytecode.add(opcode);
        // ..., arr, index, arr, index, value(8Byte)
        bytecode.addInvokestatic(MemoryTraceLogUtils.class.getName(),
                "traceArrayRead", String.format("(Ljava/lang/Object;I%s)V", opcodeToDescriptor(opcode)));
        // ..., arr, index
        it.insert(address, bytecode.get());
    }
}
