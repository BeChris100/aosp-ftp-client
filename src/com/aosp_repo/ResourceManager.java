package com.aosp_repo;

import com.aosp_repo.utils.FileUtil;

import java.io.*;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

public class ResourceManager {

    public static boolean exists(String resPath) {
        return ResourceManager.class.getResource(resPath) != null;
    }

    public static String getPath(String resPath) {
        if (ResourceManager.class.getResource(resPath) == null)
            throw new RuntimeException("Resource \"" + resPath + "\" does not exist");

        return Objects.requireNonNull(ResourceManager.class.getResource(resPath)).getPath();
    }

    public static URL getResourceUrl(String resPath) {
        if (ResourceManager.class.getResource(resPath) == null)
            throw new RuntimeException("Resource \"" + resPath + "\" does not exist");

        return ResourceManager.class.getResource(resPath);
    }

    public static InputStream openStream(String resPath) {
        if (ResourceManager.class.getResource(resPath) == null)
            throw new RuntimeException("Resource \"" + resPath + "\" does not exist");

        return ResourceManager.class.getResourceAsStream(resPath);
    }

    public static void copyOut(String resPath, File outFile) throws IOException {
        if (ResourceManager.class.getResource(resPath) == null)
            throw new RuntimeException("Resource \"" + resPath + "\" does not exist");

        if (!outFile.exists())
            FileUtil.createFile(outFile.getPath(), true);

        if (outFile.exists() && !outFile.canWrite())
            throw new AccessDeniedException("Could not overwrite \"" + outFile.getPath() + "\"");

        InputStream input = openStream(resPath);
        FileOutputStream output = new FileOutputStream(outFile, false);

        int data;
        while ((data = input.read()) != -1)
            output.write(data);

        input.close();
        output.close();
    }

}
