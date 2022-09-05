package com.aosp_repo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

public class ResourceManager {

    public static boolean exists(String resPath) {
        return ResourceManager.class.getResource(resPath) != null;
    }

    public static String getPath(String resPath) {
        return Objects.requireNonNull(ResourceManager.class.getResource(resPath)).getPath();
    }

    public static InputStream openStream(String resPath) {
        return ResourceManager.class.getResourceAsStream(resPath);
    }

    public static void copyOut(String resPath, File outFile) throws IOException {
        if (outFile.exists() && !outFile.canWrite())
            throw new AccessDeniedException("Could not rewrite \"" + outFile.getPath() + "\"");

        InputStream input = openStream(resPath);
        FileOutputStream output = new FileOutputStream(outFile, false);

        int data;
        while ((data = input.read()) != -1)
            output.write(data);

        input.close();
        output.close();
    }

}
