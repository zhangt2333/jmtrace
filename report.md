# The report of PA2—jmtrace

In this PA, I use [Javassist](https://www.javassist.org/) and *Javaagent Technology* to manipulate Java Bytecode.

This core of project contains 3 files:

* `io.github.zhangt2333.jmtrace.MemoryTraceAgent`
    * It is the entry of Javaagent. It will create the `BytecodeTransformer` and pass it to the `java.lang.instrument.Instrumentation` object.
    * Another process is exposing the URL of jmtrace to `sun.misc.Launcher.ExtClassLoader`. Because In my unit tests, I found a bug when a User-defined class loader shadows `sun.misc.Launcher.AppClassLoader` so that our class `MemoryTraceLogUtils` will not be loaded. You can reproduce this bug in my test case `io.github.zhangt2333.jmtrace.Tests#bug2` without this process.
* `io.github.zhangt2333.jmtrace.BytecodeTransformer`
    * It is the class that does bytecode manipulation.
    * It will mask some Java libraries like `java.lang`, `com.sun`, etc., by checking the class loader (is a bootstrap class loader or `sun.misc.Launcher.ExtClassLoader`) and the class name.
    * And it will travel all the bytecode and handle some like `getstatic`/`putstatic`/`getfield`/`putfield`/`*aload`/`*astore` to insert some bytecode to call the logging methods in `MemoryTraceLogUtils`.
* `io.github.zhangt2333.jmtrace.MemoryTraceLogUtils`
    * There are some logging methods.
    * A particular action in this class is to save the real `System.out` object to write logs correctly. Because in the testing, I found a bug that `org.gradle.internal.io.LinePerThreadBufferingOutputStream` will temper the `System.out` so that `java.lang.StackOverflowError` will occur when our logging method is called.

Something interesting/attractive in this project:

* In this project, 48% code is test cases.
* Using [Gradle](https://gradle.org/) to do building, almost fully automatic for artifact building, Javaagent injection, and unit testing. 

