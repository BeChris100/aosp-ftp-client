package com.aosp_repo.credentials;

import com.aosp_repo.ResourceManager;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.util.Scanner;

public class Credentials {

    public static void obtainCredentials() {
        Scanner sc = new Scanner(System.in);
        String username, password, url, aospRootDir;
        int port = 21;

        System.out.println("Please notice that after giving out the credentials, this Client will check if the FTP Server is online or not");
        System.out.println();
        System.out.println("Connection to the FTP Server (URL) (separate the port using the : character; default port is 21)");
        System.out.print("> ");
        String givenUrl = sc.nextLine();

        if (givenUrl.contains(":")) {
            String[] splits = givenUrl.split(":", 2);
            url = splits[0];

            try {
                port = Integer.parseInt(splits[1]);

                if (splits[1].contains("-") || port == 0 || port >= 65536)
                    throw new NumberFormatException("Port Number is out of range. Port Range is from 1 to 65535, given " + port);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.exit(1);
                return;
            }
        } else
            url = givenUrl;

        System.out.println();
        System.out.println("Username to the FTP Server");
        System.out.print("> ");
        username = sc.nextLine();

        System.out.println();
        System.out.println("Password to the FTP Server");
        System.out.print("> ");
        password = sc.nextLine();

        System.out.println();
        System.out.println("Directory to the AOSP Source Code (if root, enter '.')");
        System.out.print("> ");
        aospRootDir = sc.nextLine();

        System.out.println();
        System.out.println("Checking connection to \"" + url + "\" on port " + port + " with credentials \"" + username + "\" : \"" + password + "\"");

        try {
            FTPClient client = new FTPClient();
            client.addProtocolCommandListener(new ProtocolCommandListener() {
                @Override
                public void protocolCommandSent(ProtocolCommandEvent protocolCommandEvent) {
                    System.out.println("Sending out information to \"" + url + "\"");
                }

                @Override
                public void protocolReplyReceived(ProtocolCommandEvent protocolCommandEvent) {
                    // Please someone test this out, I don't have an FTP Server
                    System.out.println("Reply Code: " + protocolCommandEvent.getReplyCode());
                    System.out.println("Message: " + protocolCommandEvent.getMessage());
                }
            });
            client.connect(url, port);
            client.login(username, password);
        } catch (IOException e) {
            System.err.println("Could not connect to \"" + url + "\"!");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
