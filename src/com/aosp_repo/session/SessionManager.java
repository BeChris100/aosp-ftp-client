package com.aosp_repo.session;

import com.aosp_repo.cfg.ConfigParser;
import com.aosp_repo.cfg.Configuration;
import com.aosp_repo.credentials.Credentials;
import com.aosp_repo.credentials.CredentialsData;
import com.aosp_repo.utils.FileUtil;
import com.aosp_repo.utils.RuntimeEnvironment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    /*
    Sneak Peek on the Version 2.0

    What Session Manager does:
    -> List all Sessions from the .cache directory (/home/[username]/.cache/repo-ftp-client/sessions) on multiple AOSP Clients
    -> Prompt the user in the primary InputStream if there are more sessions than one
     */

    public static List<Session> getSessions() throws IOException {
        List<String> stringSessions;

        if (RuntimeEnvironment.isLinux())
            stringSessions = FileUtil.listDirectory(RuntimeEnvironment.USER_HOME.getPath() + "/.cache/repo-ftp-client/sessions", true, false);
        else
            stringSessions = FileUtil.listDirectory(RuntimeEnvironment.USER_HOME.getPath() + "\\AppData\\Roaming\\repo-ftp-client\\sessions", true, false);

        List<File> sessionFiles = new ArrayList<>();
        for (String strSession : stringSessions)
            sessionFiles.add(new File(strSession));

        if (sessionFiles.size() == 0)
            return new ArrayList<>();

        List<Session> sessions = new ArrayList<>();

        File appliedAospDir = null;
        CredentialsData appliedCredentialsData = null;

        for (File sessionFile : sessionFiles) {
            List<Configuration> credentialsConfig = ConfigParser.loadFromFile(sessionFile);

            for (Configuration cfg : credentialsConfig) {
                if (cfg.getName().equals("CredentialsData")) {
                    if (appliedCredentialsData == null)
                        appliedCredentialsData = CredentialsData.initializeFromFile(new File(cfg.getValue()));
                }
                if (cfg.getName().equals("AospDir")) {
                    if (appliedAospDir == null)
                        appliedAospDir = new File(cfg.getValue());
                }
            }

            sessions.add(new Session(appliedAospDir, appliedCredentialsData));
        }

        return sessions;
    }

}
