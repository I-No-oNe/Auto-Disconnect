package net.i_no_am.auto_disconnect.client;

import net.i_no_am.auto_disconnect.utils.NetworkUtils;
import net.i_no_am.auto_disconnect.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Version implements Global {

    public static boolean checked = false;
    private static final String REPO_URL = "https://api.github.com/repos/I-No-oNe/Auto-Disconnect/releases/latest";

    public static void updateChecker() {
        if (!checked) {
            try {
                String latestVersion = getGithubVersion();
                if (latestVersion != null && isUpdateAvailable(latestVersion)) {
                    NetworkUtils.sendUpdate();
                }
                checked = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getGithubVersion() throws Exception {
        URL url = new URL(REPO_URL);
        String jsonString = getString(url);
        String versionTag = "\"tag_name\":\"";
        int startIndex = jsonString.indexOf(versionTag);
        if (startIndex != -1) {
            int endIndex = jsonString.indexOf('"', startIndex + versionTag.length());
            if (endIndex != -1) {
                return jsonString.substring(startIndex + versionTag.length(), endIndex);
            }
        }
        return null;
    }

    private static @NotNull String getString(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();
        return sb.toString();
    }

    public static boolean isUpdateAvailable(String latestVersion) {
        return !Utils.getModVersion().equals(latestVersion);
    }
}