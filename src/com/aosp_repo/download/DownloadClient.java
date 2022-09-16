package com.aosp_repo.download;

import com.aosp_repo.cfg.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadClient {

    private final List<Configuration> configs = new ArrayList<>();

    public void writeToCache() {
    }

    public void applyWorkingDirectory(File workingDir) {
        boolean changed = false;

        for (int i = 0; i < configs.size(); i++) {
            if (configs.get(i).getName().equals("WorkingDir")) {
                configs.set(i, new Configuration("WorkingDir", workingDir.getPath()));
                changed = true;
                break;
            }
        }

        if (!changed)
            configs.add(new Configuration("WorkingDir", workingDir.getPath()));
    }

    public void applyCredentials(File credentialsFile) {
    }

    public void startDownload() {
    }
}
