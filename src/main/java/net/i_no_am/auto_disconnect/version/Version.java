package net.i_no_am.auto_disconnect.version;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.auto_disconnect.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;

import java.awt.Desktop;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Version implements Global {

    private static final Map<String, Double> versionCache = new HashMap<>();
    private final String api;
    private final String download;
    private static boolean bl = false;
    private final double version;

    /**
     @param api The link to the GITHUB repo api.
     @param download The link to the download page.
     ***/

    private Version(String api, String download) throws Exception {
        this.api = api;
        this.download = download;
        this.version = Config.shouldCheck ? getVApi() : 0.0;
    }

    public static Version create(String apiLink, String downloadLink) {
        try {
            return new Version(apiLink, downloadLink);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyUpdate(boolean printVersions) {
        if (!bl && mc.gui.screen() == null && mc.player != null && !isUpdated() && Config.shouldCheck) {
            if (printVersions) {
                log("Versions: \nCurrent Version: " + getSelf() + "\n" + "Online Version: " + getApi());
            }
            mc.gui.setScreen(new ConfirmScreen(confirmed -> {
                if (confirmed) {
                    try {
                        Desktop.getDesktop().browse(URI.create(download));
                    } catch (Exception e) {
                        System.out.println("[Auto-Disconnect] Failed to open download page: " + e.getMessage());
                    }
                }
                mc.gui.setScreen(null);
            }, Component.literal(ChatFormatting.RED + "You are using an outdated version of Auto Disconnect"), Component.literal("Please download the latest version from " + ChatFormatting.GREEN + "Modrinth"), Component.literal("Download"), Component.literal("Continue playing")));
            bl = true;
        }
    }

    private boolean isUpdated() {
        return getSelf() >= getApi();
    }

    private double getApi() {
        return version;
    }

    private static double getSelf() {
        String versionString = FabricLoader.getInstance().getModContainer(modId).orElseThrow(() -> new RuntimeException(modId + " Isn't loaded")).getMetadata().getVersion().getFriendlyString();
        return parseVersion(versionString);
    }

    private static double parseVersion(String version) {
        String[] parts = version.split("-");

        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+\\.\\d+")) {
                String[] versionNumbers = part.split("\\.");
                double parsedVersion = Double.parseDouble(versionNumbers[0] + "." + versionNumbers[1]);
                return parsedVersion * 10;
            } else if (part.matches("\\d+\\.\\d+")) return Double.parseDouble(part);
        }

        return 0.0;
    }

    private double getVApi() throws Exception {
        if (versionCache.containsKey(api)) {
            return versionCache.get(api);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(api)).timeout(Duration.ofSeconds(600)).header("Accept", "application/vnd.github.v3+json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch latest version: " + response.statusCode());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        String versionString = jsonResponse.get("tag_name").getAsString();

        double parsedVersion = parseVersion(versionString);
        versionCache.put(api, parsedVersion);
        return parsedVersion;
    }
}
