/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.zhangt2333.jmtrace;

/**
 * a file from @link https://github.com/gradle/gradle/blob/master/subprojects/base-services/src/main/java/org/gradle/api/JavaVersion.java
 * edited by zhangt2333 and streamlined for fixing bug
 * <br>
 * <br>
 */
public enum JavaVersion {
    VERSION_1_1, VERSION_1_2, VERSION_1_3, VERSION_1_4,
    VERSION_1_5, VERSION_1_6, VERSION_1_7, VERSION_1_8,
    VERSION_1_9, VERSION_1_10,
    /**
     * Java 11 major version.
     *
     * @since 4.7
     */
    VERSION_11,

    /**
     * Java 12 major version.
     *
     * @since 5.0
     */
    VERSION_12,

    /**
     * Java 13 major version.
     *
     * @since 6.0
     */
    VERSION_13,

    /**
     * Java 14 major version.
     *
     * @since 6.3
     */
    VERSION_14,

    /**
     * Java 15 major version.
     *
     * @since 6.3
     */
    VERSION_15,

    /**
     * Java 16 major version.
     *
     * @since 6.3
     */
    VERSION_16,

    /**
     * Java 17 major version.
     *
     * @since 6.3
     */
    VERSION_17,

    /**
     * Java 18 major version.
     * Not officially supported by Gradle. Use at your own risk.
     *
     * @since 7.0
     */
    // @Incubating
    VERSION_18,

    /**
     * Java 19 major version.
     * Not officially supported by Gradle. Use at your own risk.
     *
     * @since 7.0
     */
    // @Incubating
    VERSION_19,

    /**
     * Java 20 major version.
     * Not officially supported by Gradle. Use at your own risk.
     *
     * @since 7.0
     */
    // @Incubating
    VERSION_20,

    /**
     * Higher version of Java.
     * @since 4.7
     */
    VERSION_HIGHER;
    // Since Java 9, version should be X instead of 1.X
    // However, to keep backward compatibility, we change from 11
    private static final int FIRST_MAJOR_VERSION_ORDINAL = 10;
    private final String versionName;

    JavaVersion() {
        this.versionName = ordinal() >= FIRST_MAJOR_VERSION_ORDINAL ? getMajorVersion() : "1." + getMajorVersion();
    }

    @Override
    public String toString() {
        return versionName;
    }

    public String getMajorVersion() {
        return String.valueOf(ordinal() + 1);
    }

    /**
     * this method was private originally <br>
     * edited by zhangt2333
     */
    public static JavaVersion getVersionForMajor(int major) {
        return major >= values().length ? JavaVersion.VERSION_HIGHER : values()[major - 1];
    }
}