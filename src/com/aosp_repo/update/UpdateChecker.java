package com.aosp_repo.update;

import com.aosp_repo.RepoClient;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.URL;

/*
This will be worked on, when the first release or pre-release rolls out
 */
public class UpdateChecker {

    private static String requestConnection(String urlLine) throws Exception {
        // Code copied from https://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java (rewrote some code)
        URL url = new URL(urlLine);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream is = conn.getInputStream();
        StringBuilder result = new StringBuilder();
        int data;

        while ((data = is.read()) != -1)
            result.append((char) data);

        return result.toString();
    }

    public static boolean hasUpdates() throws Exception {
        return false;
    }

    public static void downloadLatestUpdate() throws Exception {
    }

}
