package com.aosp_repo.utils;

import java.lang.management.ManagementFactory;
import java.util.List;

public class RuntimeEnvironment {

    public static final String RUNNING_OS_NAME = System.getProperty("os.name");
    public static final String RUNNING_OS_ARCH = System.getProperty("os.arch");
    public static final String RUNNING_OS_VERSION = System.getProperty("os.version");

    public static int countRuntimeModifications() {
        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();

        int n = 0;

        for (String arg : args) {
            if (arg.startsWith("-Dos.name=") ||
                    arg.startsWith("-Dos.arch=") ||
                    arg.startsWith("-Dos.version="))
                n++;
        }

        return n;
    }

    public static boolean isWindows() {
        return RUNNING_OS_NAME.toLowerCase().contains("win");
    }

    public static boolean isMac() {
        return RUNNING_OS_NAME.toLowerCase().contains("mac");
    }

    public static boolean isLinux() {
        return RUNNING_OS_NAME.toLowerCase().contains("nux") ||
                RUNNING_OS_NAME.toLowerCase().contains("aix") ||
                RUNNING_OS_NAME.toLowerCase().contains("nix");
    }

    public static String getOperatingSystemName() {
        return RUNNING_OS_NAME;
    }

    public static String getOperatingSystemArchitecture() {
        return RUNNING_OS_ARCH;
    }

    public static String getOperatingSystemVersion() {
        return RUNNING_OS_VERSION;
    }

}
