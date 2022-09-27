package com.aosp_repo.cfg;

import com.aosp_repo.ResourceManager;
import com.aosp_repo.utils.Utility;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser {

    public static List<Configuration> loadFromResource(String resourceFile) throws IOException {
        InputStream is = ResourceManager.openStream(resourceFile);
        StringBuilder read = new StringBuilder();
        int data;

        while ((data = is.read()) != -1)
            read.append((char) data);

        is.close();

        String contents = read.toString();
        String[] lines = contents.split(Utility.getLineSeparator(contents));

        List<Configuration> results = new ArrayList<>();

        for (String line : lines) {
            if (line.isEmpty())
                continue;

            if (line.startsWith(" ") || line.startsWith("\t"))
                line = Utility.removeStartSpaces(line);

            if (line.startsWith("#"))
                continue;

            String[] opts;

            if (line.startsWith(" ") || line.startsWith("\t")) {
                String overwrittenLine = Utility.removeStartSpaces(line);
                opts = overwrittenLine.split(" = ", 2);
            } else
                opts = line.split(" = ", 2);

            Configuration config = new Configuration(opts[0], opts[1]);
            results.add(config);
        }

        return results;
    }

    public static List<Configuration> loadFromFile(File file) throws IOException {
        if (!file.exists())
            throw new FileNotFoundException("File at \"" + file.getPath() + "\" not found");

        if (!file.canRead())
            throw new AccessDeniedException("Current user cannot access \"" + file.getPath() + "\"");

        if (file.isDirectory())
            throw new IllegalStateException("\"" + file.getPath() + "\" is a directory");

        FileInputStream fis = new FileInputStream(file);
        StringBuilder str = new StringBuilder();
        int data;

        while ((data = fis.read()) != -1)
            str.append((char) data);

        fis.close();

        String contents = str.toString();
        String[] lines = contents.split(Utility.getLineSeparator(contents));

        List<Configuration> results = new ArrayList<>();

        for (String line : lines) {
            if (line.isEmpty())
                continue;

            if (line.startsWith(" ") || line.startsWith("\t"))
                line = Utility.removeStartSpaces(line);

            if (line.startsWith("#"))
                continue;

            String[] opts;

            if (line.startsWith(" ") || line.startsWith("\t")) {
                String overwrittenLine = Utility.removeStartSpaces(line);
                opts = overwrittenLine.split(" = ", 2);
            } else
                opts = line.split(" = ", 2);

            Configuration config = new Configuration(opts[0], opts[1]);
            results.add(config);
        }

        return results;
    }

    public static String fromConfigurationList(List<Configuration> configurationList, String spliterator) {
        if (configurationList == null)
            throw new NullPointerException("Configuration List has been passed as null");

        if (configurationList.size() == 0)
            return "";

        StringBuilder str = new StringBuilder();

        for (Configuration cfg : configurationList)
            str.append(cfg.name()).append(" = ").append(cfg.value()).append(spliterator);

        return str.substring(0, str.toString().length() - spliterator.length());
    }
}
