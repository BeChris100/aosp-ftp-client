package com.aosp_repo.cfg;

import com.aosp_repo.ResourceManager;
import com.aosp_repo.utils.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser {

    private static String removeStartSpaces(String line) {
        String results = line;

        if (results.startsWith(" "))
            results = results.replaceFirst(" ", "");

        if (results.startsWith("\t"))
            results = results.replaceFirst("\t", "");

        if (results.startsWith(" ") || results.startsWith("\t"))
            removeStartSpaces(results);

        return results;
    }

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

            if (line.startsWith("#"))
                continue;

            String[] opts;

            if (line.startsWith(" ") || line.startsWith("\t")) {
                String overwrittenLine = removeStartSpaces(line);
                opts = overwrittenLine.split(" = ", 2);
            } else
                opts = line.split(" = ", 2);

            Configuration config = new Configuration(opts[0], opts[1]);
            results.add(config);
        }

        return results;
    }

}
