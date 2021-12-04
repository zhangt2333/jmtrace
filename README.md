# jmtrace

Trace memory access in Java programs.

## Build
We use `Gradle` as our build tool.
It is easy to build by
```shell
chmod +x ./gradlew
./gradlew package
```

## Usage

The usage is same to the `java` command. For example, if you have a JAR package `Hello.jar` and want to trace its memory accesses, you can type the following command:
```shell
chmod +x ./jmtrace
./jmtrace -jar Hello.jar
```

and memory access logs will be printed to `stdout` in the format:
```
R(ead)|W(rite) <threadId> <ObjectId> <ObjectType.fieldName|ClassName.staticFiledName|ArrayType[index]>
```

## Test with JUnit

run:

```shell
chmod +x ./gradlew
./gradlew test
```

then you can see the report in `build/reports/tests/test/classes/io.github.zhangt2333.jmtrace.Tests.html`





Note that `./jmtrace` is in fact a shell script.