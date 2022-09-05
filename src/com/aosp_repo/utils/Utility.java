package com.aosp_repo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static String readInputStream(InputStream is, boolean closeStream) throws IOException {
        if (is == null)
            throw new NullPointerException("The current InputStream is null");

        StringBuilder str = new StringBuilder();
        int data;

        while ((data = is.read()) != -1)
            str.append((char) data);

        if (closeStream)
            is.close();

        return str.toString();
    }

    public static String getLineSeparatorFromContents(String contents, boolean named) {
        if (contents == null)
            return "";

        if (contents.isEmpty())
            return "";

        long r = 0;
        long n = 0;

        char[] contentChars = contents.toCharArray();
        for (char currentChar : contentChars) {
            if (currentChar == '\r')
                r++;

            if (currentChar == '\n')
                n++;
        }

        if (r == n) {
            if (named)
                return "CR LF";
            else
                return "\r\n";
        } else if (r >= 1 && n == 0) {
            if (named)
                return "CR";
            else
                return "\r";
        } else if (n >= 1 && r == 0) {
            if (named)
                return "LF";
            else
                return "\n";
        } else
            return "";
    }

    public static List<Environment> getEnvironments(String contents, char[] ignoredStatements) {
        return new ArrayList<>();
    }

}
