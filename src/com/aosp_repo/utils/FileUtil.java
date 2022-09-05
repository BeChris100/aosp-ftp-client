package com.aosp_repo.utils;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static final File USER_DIR = new File(System.getProperty("user.dir"));

    public static boolean exists(String path) {
        return new File(path).exists();
    }

    public static String readFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists())
            throw new FileNotFoundException("The file \"" + filePath + "\" does not exist");

        if (file.isDirectory())
            throw new IllegalStateException("The current path assigned to \"" + filePath + "\" is a directory!");

        if (!file.canRead())
            throw new AccessDeniedException("\"" + filePath + "\" is a read-protected file or the current user does not have access to it");

        FileInputStream fis = new FileInputStream(file);
        StringBuilder str = new StringBuilder();
        int data;

        while ((data = fis.read()) != -1)
            str.append((char) data);

        fis.close();
        return str.toString();
    }

    public static void writeFile(String filePath, String contents) throws IOException {
        File file = new File(filePath);

        if (file.exists() && file.isDirectory())
            throw new IllegalStateException("The current path assigned to \"" + filePath + "\" is a directory");

        if (file.exists() && !file.canWrite())
            throw new AccessDeniedException("\"" + filePath + "\" is a write-protected file or the current user does not have access to it");

        if (!file.canWrite())
            throw new AccessDeniedException("The current directory/file is a write-protected file or the current user does not have access to it");

        FileWriter writer = new FileWriter(filePath, false);
        writer.write(contents);
        writer.close();
    }

    public static int getLastSeparator(String path) {
        int lastSep;

        if (path.contains("/") && path.contains("\\")) {
            path = path.replaceAll("\\\\", "/");
            lastSep = path.lastIndexOf('/');
        } else if (path.contains("\\"))
            lastSep = path.lastIndexOf('\\');
        else
            lastSep = path.lastIndexOf('/');

        if (path.substring(0, lastSep + 1).length() != 0)
            lastSep = path.length() - 1;

        return lastSep;
    }

    public static List<String> listDirectory(String dirPath) throws IOException {
        List<String> data = new ArrayList<>();
        File dir = new File(dirPath);

        if (!dir.exists())
            throw new FileNotFoundException("The current directory at \"" + dirPath + "\" does not exist");

        if (dir.isFile()) {
            data.add(dirPath);
            return data;
        }

        if (!dir.canRead())
            throw new AccessDeniedException("The current directory/file is a read-protected place or the current user does not have access to it");

        File[] files = dir.listFiles();
        if (files == null)
            return data;

        for (File file : files)
            data.add(file.getPath());

        return data;
    }

    public static void delete(String path) throws IOException {
        File f = new File(path);

        if (!f.exists())
            return;

        if (f.isFile()) {
            if (!f.delete())
                throw new IOException("Could not delete \"" + path + "\"");

            return;
        }

        if (listDirectory(path).size() == 0) {
            if (!f.delete())
                throw new IOException("Could not delete \"" + path + "\"");

            return;
        }

        File[] files = f.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            BasicFileAttributes fileAttr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            if (fileAttr.isRegularFile()) {
                if (file.delete())
                    throw new IOException("Could not delete \"" + file.getPath() + "\"");
            }

            if (fileAttr.isSymbolicLink()) {
                if (file.delete())
                    throw new IOException("Could not delete \"" + file.getPath() + "\"");
            }

            if (fileAttr.isDirectory())
                delete(file.getPath());
        }

        if (!f.delete())
            throw new IOException("Could not delete \"" + path + "\"");
    }

    public static void mkdirs(String path) throws IOException {
        File dir = new File(path.substring(0, getLastSeparator(path) + 1));

        if (dir.exists())
            throw new FileAlreadyExistsException("\"" + path + "\" already exists");

        if (!dir.mkdirs())
            throw new IOException("Could not make new folders at \"" + path + "\"");
    }

    public static void rewrite(String oldLine, String newLine, File file) throws IOException {
        if (!file.exists())
            throw new FileNotFoundException("File at \"" + file.getPath() + "\" not found");

        if (file.isDirectory())
            throw new IllegalStateException("\"" + file.getPath() + "\" is a directory");

        writeFile(file.getPath(), readFile(file.getPath()).replace(oldLine, newLine));
    }
}
