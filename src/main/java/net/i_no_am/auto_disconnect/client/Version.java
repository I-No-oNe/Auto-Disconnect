package net.i_no_am.auto_disconnect.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Version implements Global {

    public static boolean isRightVersion = true;
    public static boolean isVersionChecked = false;

    public static String getLatestVersion() throws Exception {
        URL url = new URL("https://api.github.com/repos/I-No-oNe/Auto-Disconnect/releases/latest");
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();

        connect.setRequestMethod("GET");

        BufferedReader r = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = r.readLine()) != null) {
            response.append(line);
        }

        r.close();

        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        return jsonResponse.get("tag_name").getAsString();
    }

    public static String getModVersion() {
        ModContainer ad = FabricLoader.getInstance().getModContainer("auto_disconnect")
                .orElseThrow(() -> new IllegalArgumentException("Auto-Disconnect has not been loaded"));
        String ver = ad.getMetadata().getVersion().getFriendlyString();
        String[] parts = ver.split("-");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+")) {
                return part;
            }
        }
        return "Unknown";
    }

    public static boolean compareVersions() {
        if (!isVersionChecked) {
            try {
                String latestVersion = getLatestVersion();
                String modVersion = getModVersion();
                isRightVersion = latestVersion.equals(modVersion);
            } catch (Exception e) {
                e.printStackTrace();
                isRightVersion = false;
            }
            isVersionChecked = true;
        }
        return isRightVersion;
    }
}
