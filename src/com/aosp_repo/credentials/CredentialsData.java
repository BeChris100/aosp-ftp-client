package com.aosp_repo.credentials;

import com.aosp_repo.cfg.ConfigParser;
import com.aosp_repo.cfg.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

public class CredentialsData {

    private final int port;
    private final String url, username, password, remoteAospDir;

    protected CredentialsData(String url, int port, String username, String password, String remoteAospDir) {
        this.url = url;
        this.port = port;
        this.username = username;
        this.password = password;
        this.remoteAospDir = remoteAospDir;
    }

    public static CredentialsData initializeFromFile(File file) throws IOException {
        if (file == null)
            throw new NullPointerException("File is null, cannot initialize Credentials Data");

        if (!file.exists())
            throw new FileNotFoundException("File at \"" + file.getPath() + "\" not found");

        if (!file.canRead())
            throw new AccessDeniedException("Current User cannot read \"" + file.getPath() + "\"");

        if (file.isDirectory())
            throw new IllegalStateException("\"" + file.getPath() + "\" is a directory");


        List<Configuration> configs = ConfigParser.loadFromFile(file);

        String username = "", password = "", url = "", remoteAospDir = "";
        int port = 21;

        for (Configuration config : configs) {
            switch (config.name()) {
                case "Username" -> username = config.value();
                case "Password" -> password = config.value();
                case "FtpUrlConnection" -> url = config.value();
                case "Port" -> port = Integer.parseInt(config.value());
                case "FtpAospRootDir" -> remoteAospDir = config.value();
            }
        }

        return new CredentialsData(url, port, username, password, remoteAospDir);
    }

}
