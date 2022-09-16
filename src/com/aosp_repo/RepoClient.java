package com.aosp_repo;

import com.aosp_repo.cfg.ConfigParser;
import com.aosp_repo.cfg.Configuration;
import com.aosp_repo.utils.FileUtil;
import com.aosp_repo.utils.RuntimeEnvironment;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class RepoClient {

    public static void initialize() throws IOException {
        System.out.println("Copying out \"configs/aosp.cfg\" to \".client/aosp.cfg\"");
        ResourceManager.copyOut("res/configs/aosp.cfg", new File(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/aosp.cfg"));

        if (new File(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/credentials.cfg").exists()) {
            System.out.println("Moving \"" + RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/credentials.cfg\" to the client folder");
            FileUtil.moveFile(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/credentials.cfg", RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/credentials.cfg");
        } else {
            System.out.println("Copying out \"configs/credentials.cfg\" to \"client/credentials.cfg\"");
            ResourceManager.copyOut("res/configs/credentials.cfg", new File(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/credentials.cfg"));
        }

        System.out.println("Repo Client initialized successfully!");
        System.exit(0);
    }

    public static void addDevice(String brand, String deviceCodename) throws IOException {
        if (RepoClient.class.getResource("res/devices/" + brand + "/" + deviceCodename + ".cfg") == null) {
            System.err.println("A file in res/devices/" + brand + "/" + deviceCodename + ".cfg does not exist yet.");
            System.err.println("You can download the vendor and/or chipsets from the official sources.");
            System.exit(1);
        }

        List<Configuration> configs = ConfigParser.loadFromResource("res/devices/" + brand + "/" + deviceCodename + ".cfg");

        System.out.println("** Device Information **");
        for (Configuration cfg : configs)
            System.out.println(cfg.getName() + ": " + cfg.getValue());

        Scanner sc = new Scanner(System.in);
        System.out.print("Are you sure that you want to include this device to your build? (Y/n) ");
        String choice = sc.next();

        if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("Yes")) {
            ResourceManager.copyOut("res/devices/" + brand + "/" + deviceCodename + ".cfg", new File(RuntimeEnvironment.USER_HOME.getPath() + "/.client/devices/" + brand + "-" + deviceCodename + ".cfg"));
            System.out.println("Device added successfully!");
        } else
            System.exit(0);
    }

}
