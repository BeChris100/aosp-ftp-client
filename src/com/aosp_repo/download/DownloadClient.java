package com.aosp_repo.download;

import com.aosp_repo.cfg.ConfigParser;
import com.aosp_repo.cfg.Configuration;
import com.aosp_repo.utils.DateUtil;
import com.aosp_repo.utils.io.FileUtil;
import com.aosp_repo.utils.RuntimeEnvironment;
import com.aosp_repo.utils.Utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadClient {

    public static File CACHE_FILE = new File("");

    private final List<Configuration> configs = new ArrayList<>();

    public void writeToCache() {
        try {
            if (RuntimeEnvironment.isLinux()) {
                if (RuntimeEnvironment.USER_NAME.equals("root"))
                    CACHE_FILE = new File("/var/cache/repo-client_" + Utility.fromArray(DateUtil.getDate(), "") + "_" + Utility.fromArray(DateUtil.getTime(true), "") + ".file");
                else
                    CACHE_FILE = new File(RuntimeEnvironment.USER_HOME.getPath() + "/.cache/repo-client_" + Utility.fromArray(DateUtil.getDate(), "") + "_" + Utility.fromArray(DateUtil.getTime(true), "") + ".file");

                FileUtil.write(CACHE_FILE.getPath(), ConfigParser.fromConfigurationList(configs, System.lineSeparator()).toCharArray());
            }
            // Windows and macOS Support will not be included for now
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyWorkingDirectory(File workingDir) {
        boolean changed = false;

        for (int i = 0; i < configs.size(); i++) {
            if (configs.get(i).name().equals("WorkingDir")) {
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
