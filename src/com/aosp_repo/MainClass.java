package com.aosp_repo;

import com.aosp_repo.utils.FileUtil;
import com.aosp_repo.utils.RuntimeEnvironment;
import com.aosp_repo.utils.Utility;

import java.io.IOException;

public class MainClass {

    private static void printHelp() {
        System.out.println("AOSP Repo Client (version 1.0)");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("--init\t\t\t\t\t\t\t\t\t\tInitializes an empty AOSP Configuration for this client");
        System.out.println("--device-add Brand Codename\t\t\t\t\tAdds a new device to your AOSP Build");
        System.out.println("--device-sync Brand Codename\t\t\t\tDownloads the files to the current working directory");
        System.out.println("--device-set Brand Codename\t\t\t\t\tApplies a device to the \"vendor\" folder");
    }

    public static void main(String[] args) {
        if (args.length != 0) {
            switch (args[0]) {
                case "--init" -> {
                    try {
                        RepoClient.initialize();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "--device-add" -> {
                    if (args.length != 3) {
                        System.err.println("Usage:");
                        System.err.println("java -jar AospRepoClient.jar --device-add [Brand] [Codename]");
                        System.exit(1);
                    }
                    if (FileUtil.exists(RuntimeEnvironment.USER_HOME.getPath() + "/.client/devices/" + args[1] + "/" + args[2] + ".cfg")) {
                        System.err.println("Device Codename \"" + args[2] + "\" already exists!");
                        System.exit(1);
                    }
                    try {
                        RepoClient.addDevice(args[1], args[2]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "--help", "-h" -> printHelp();
                default -> {
                    System.err.println("Unknown Command!");
                    System.err.println("Command provided: " + Utility.fromArray(args, " "));
                }
            }
        } else
            printHelp();
    }

}
