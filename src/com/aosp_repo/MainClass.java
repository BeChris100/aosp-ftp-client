package com.aosp_repo;

import com.aosp_repo.download.DownloadClient;
import com.aosp_repo.utils.FileUtil;
import com.aosp_repo.utils.RuntimeEnvironment;
import com.aosp_repo.utils.Utility;

import java.io.File;
import java.io.IOException;

public class MainClass {

    private static void printHelp() {
        System.out.println("AOSP Repo Client (version 1.0)");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("--init                                    Initializes an empty AOSP Configuration for this client");
        System.out.println("--device-add Brand Codename               Adds a new device to your AOSP Build");
        System.out.println("--device-sync Brand Codename              Downloads the files to the current working directory");
        System.out.println("--device-set Brand Codename               Applies a device to the \"vendor\" folder");
        System.out.println("--device-remove Brand Codename            Removes the device from the client configuration and possibly removing the Vendor Folder.");
        System.out.println("--get-code / -gC                          Downloads the AOSP Source Code from the FTP Server");
        System.out.println("--credentials                             Requests fields for ");
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
                    if (FileUtil.exists(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/devices/" + args[1] + "/" + args[2] + ".cfg")) {
                        System.err.println("Device Codename \"" + args[2] + "\" already exists!");
                        System.exit(1);
                    }
                    try {
                        RepoClient.addDevice(args[1], args[2]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "--device-remove" -> {
                    if (args.length != 3) {
                        System.err.println("Usage:");
                        System.err.println("java -jar repo-client.jar --device-remove [Brand] [Codename]");
                        System.exit(1);
                    }
                }
                case "--get-code", "-gC" -> {
                    DownloadClient client = new DownloadClient();
                    client.applyCredentials(new File(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/credentials.cfg"));
                    client.applyWorkingDirectory(RuntimeEnvironment.WORKING_DIRECTORY);
                    client.writeToCache();
                    client.startDownload();
                }
                case "--help", "-h" -> printHelp();
                default -> {
                    System.err.println("Unknown Command!");
                    System.err.println("Command provided: " + Utility.fromArray(args, " "));
                    System.exit(1);
                }
            }
        } else
            printHelp();
    }

}
