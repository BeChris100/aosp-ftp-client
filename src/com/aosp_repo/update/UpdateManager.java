package com.aosp_repo.update;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpdateManager {

    private static @NotNull List<UpdateFile> getFiles(@NotNull JSONArray filesRoot) {
        List<UpdateFile> updateFiles = new ArrayList<>();

        for (int i = 0; i < filesRoot.length(); i++) {
            String name = filesRoot.getJSONObject(i).getString("name");
            String urlDownload = filesRoot.getJSONObject(i).getString("browser_download_url");
            long size = filesRoot.getJSONObject(i).getLong("size");

            UpdateFile updateFile = new UpdateFile(name, urlDownload, size);

            if (updateFiles.contains(updateFile))
                continue;

            updateFiles.add(updateFile);
        }

        return updateFiles;
    }

    public static @NotNull List<UpdateData> listUpdates() throws Exception {
        URL url = new URL("https://api.github.com/repos/BeChris100/aosp-ftp-client/releases");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder jsonBody = new StringBuilder();
        InputStream is = conn.getInputStream();
        int charData;

        while ((charData = is.read()) != -1)
            jsonBody.append((char) charData);

        List<UpdateData> updatesList = new ArrayList<>();

        JSONArray root = new JSONArray(jsonBody.toString());
        for (int i = 0; i < root.length(); i++) {
            List<UpdateFile> updateFiles = getFiles(root.getJSONObject(i).getJSONArray("assets"));

            updatesList.add(new UpdateData(root.getJSONObject(i).getString("tag_name"),
                    root.getJSONObject(i).getBoolean("prerelease"), updateFiles, root.getJSONObject(i).getString("body")));
        }

        return updatesList;
    }

}
