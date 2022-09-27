package com.aosp_repo;

import com.aosp_repo.cfg.ConfigParser;
import com.aosp_repo.cfg.Configuration;
import com.aosp_repo.download.DownloadClient;
import com.aosp_repo.update.UpdateData;
import com.aosp_repo.update.UpdateFile;
import com.aosp_repo.update.UpdateManager;
import com.aosp_repo.utils.RuntimeEnvironment;
import com.aosp_repo.utils.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class MainClass {

    private static final String[] COMMANDS = {
            "init",
            "i",
            "device-add",
            "dA",
            "device-add-local",
            "dAL",
            "device-sync",
            "dSy",
            "device-set",
            "dS",
            "device-remove",
            "dR",
            "get-code",
            "gC",
            "credentials",
            "c",
            "template",
            "t",
            "help",
            "h",
            "session-add",
            "sA",
            "session-set",
            "sS",
            "session-remove",
            "sR"
    };

    private static void printHelp() {
        System.out.println("AOSP Repo Client (version " + RepoClient.Build.VERSION_CODE + ")");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("--init / -i                                            Initializes an empty AOSP Configuration for this client");
        System.out.println("--device-add / -dA Brand Codename                      Adds a new device to your AOSP Build");
        System.out.println("--device-add-local / -dAL Configuration                Adds a new device to your AOSP Build");
        System.out.println("--device-sync / -dSy Brand Codename                    Downloads the files to the current working directory");
        System.out.println("--device-set / -dS Brand Codename                      Applies a device to the \"vendor\" folder");
        System.out.println("--device-remove / -dR Brand Codename                   Removes the device from the client configuration and possibly removing the Vendor Folder.");
        System.out.println("--get-code / -gC                                       Downloads the AOSP Source Code from the FTP Server");
        System.out.println("--credentials / -c                                     Requests fields for the credentials");
        System.out.println("--template / -t [TemplateFile]|list (OutputFile)       Reads out a template (OutputFile is optional)");
        System.out.println("--updates / -u                                         Check for Updates");
        System.out.println("--help / -h [Command]                                  Lists the Help Page or reads");
        // No Help Page on Session Management for now
    }

    private static void printHelp(@NotNull String command) {
        System.out.println("AOSP Repo Client (version 1.0)");
        System.out.println();

        if (command.isEmpty()) {
            System.err.println("Command empty");
            System.exit(1);
            return;
        }

        if (command.equalsIgnoreCase("sA") || command.equalsIgnoreCase("session-add") ||
                command.equalsIgnoreCase("sS") || command.equalsIgnoreCase("session-set") ||
                command.equalsIgnoreCase("sR") || command.equalsIgnoreCase("session-remove")) {
            System.err.println("Session Management coming soon");
            System.exit(1);
            return;
        }

        boolean found = false;
        for (String findCommand : COMMANDS) {
            if (findCommand.equals(command)) {
                found = true;
                break;
            }
        }

        if (!found) {
            System.err.println("Command \"" + command + "\" not found");
            System.exit(1);
        }
    }

    private static void parseArgs(String @NotNull [] args) {
        switch (args[0]) {
            case "--session-add", "-sA", "--session-set", "-sS", "--session-remove", "-sR" -> {
                System.err.println("Session Management is coming to Version 2.0");
                System.exit(1);
            }
            case "--init", "-i" -> {
                try {
                    RepoClient.initialize();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case "--updates", "-u" -> {
                try {
                    List<UpdateData> updatesData = UpdateManager.listUpdates();
                    for (UpdateData updateData : updatesData) {
                        System.out.println("Tag: " + updateData.tag());
                        System.out.println("Pre-Release: " + updateData.preRelease());

                        for (UpdateFile updateFile : updateData.updateFiles()) {
                            System.out.println("File.Name: " + updateFile.name());
                            System.out.println("File.DownloadURL: " + updateFile.browserDownloadUrl());
                            System.out.println("File.Size: " + updateFile.size());
                        }

                        System.out.println();
                    }
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);

                    if (sw.toString().contains("UnknownHostException")) {
                        System.err.println("Could not establish a valid connection to \"https://api.github.com\"");
                        System.err.println("Check, if your Internet Connection is online and can receive GET-Requests");
                    } else
                        e.printStackTrace();
                }
            }
            case "--device-add", "-dA" -> {
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
            case "--device-add-local", "-dAL" -> {
                if (args.length != 2) {
                    System.err.println("Usage:");
                    System.err.println("java -jar AospRepoClient.jar --device-add-local [ConfigFile]");
                    System.exit(1);
                    return;
                }

                if (!new File(args[1]).exists()) {
                    System.err.println("Could not find \"" + args[1] + "\"");
                    System.exit(1);
                    return;
                }

                try {
                    List<Configuration> cfg = ConfigParser.loadFromFile(new File(args[1]));

                    System.out.println("You are currently loading this device:");
                    for (Configuration co : cfg)
                        System.out.println(co.name() + " = " + co.value());

                    System.out.println();
                    System.out.print("Is this information correct? (Y/n)  ");
                    Scanner userIn = new Scanner(System.in);
                    String userChoice = userIn.next();

                    if (userChoice.equalsIgnoreCase("Y") || userChoice.equalsIgnoreCase("Yes"))
                        FileUtil.copyFile(args[1], RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/devices/local/" + args[1] + ".cfg");
                    else {
                        System.out.println("Aborted");
                        System.exit(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case "--device-remove", "-dR" -> {
                if (args.length != 3) {
                    System.err.println("Usage:");
                    System.err.println("java -jar repo-client.jar --device-remove [Brand] [Codename]");
                    System.exit(1);
                    return;
                }

                try {
                    if (FileUtil.exists(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/devices/" + args[1] + "/" + args[2] + ".cfg"))
                        FileUtil.delete(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/devices/" + args[1] + "/" + args[2] + ".cfg");

                    if (FileUtil.exists(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/devices/" + args[1] + "/" + args[2] + "-images.zip"))
                        FileUtil.delete(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/devices/" + args[1] + "/" + args[2] + "-images.zip");

                    if (FileUtil.exists(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/vendor/google_devices/" + args[2]))
                        FileUtil.delete(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/vendor/google_devices/" + args[2]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case "--template", "-t" -> {
                System.err.println("Usage:");
                System.err.println("java -jar repo-client.jar --template [TemplateFile]|list (Optional:OutputFile)");
                System.err.println();
                System.err.println("Reads out a template and if the OutputFile is given, then writes directly to an file instead of printing it out");

                try {
                    InputStream is;

                    if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("templates.list"))
                        is = ResourceManager.openStream("res/templates/templates.list");
                    else {
                        if (args[1].endsWith(".cfg"))
                            is = ResourceManager.openStream("res/templates/" + args[1]);
                        else
                            is = ResourceManager.openStream("res/templates/" + args[1] + ".cfg");
                    }

                    StringBuilder str = new StringBuilder();
                    int data;

                    while ((data = is.read()) != -1)
                        str.append((char) data);

                    is.close();

                    if (args.length == 3)
                        FileUtil.write(args[2], str.toString().toCharArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case "--get-code", "-gC" -> {
                DownloadClient client = new DownloadClient();
                client.applyCredentials(new File(RuntimeEnvironment.WORKING_DIRECTORY.getPath() + "/.client/credentials.cfg"));
                client.applyWorkingDirectory(RuntimeEnvironment.WORKING_DIRECTORY);
                client.writeToCache();
                client.startDownload();
            }
            case "--help", "-h", "?" -> {
                if (args.length == 1)
                    printHelp();
                else
                    printHelp(args[1]);
            }
            default -> {
                boolean found = false;

                for (String command : COMMANDS) {
                    if (command.equalsIgnoreCase(args[0])) {
                        found = true;
                        break;
                    }
                }

                if (found)
                    System.err.println("Command \"" + args[0] + "\" not implemented yet");
                else {
                    System.err.println("Unknown Command!");
                    System.err.println("Command provided: " + args[0]);
                }

                System.exit(1);
            }
        }
    }

    public static void main(String @NotNull [] args) {
        if (RepoClient.Build.BUILD_TYPE == RepoClient.Build.BuildType.DEVELOPMENT) {
            System.err.println("Development builds are unstable builds, which should not be used by regular people.");
            System.err.println("If you know, how to debug Java Code, then you may go for it.");
        } else if (RepoClient.Build.BUILD_TYPE == RepoClient.Build.BuildType.PRE_RELEASE) {
            System.err.println("Pre-Release builds may or may not come with bugs.");
            System.err.println("If a bug or something happens, you should create a bug report / issue on GitHub.");
        }

        if (args.length != 0)
            parseArgs(args);
        else
            printHelp();
    }

}
