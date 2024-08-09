package net.i_no_am.auto_disconnect.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.auto_disconnect.client.Global;
import net.minecraft.text.Text;

public class Utils implements Global {

    public static void sendText(Text text) {
        PlayerUtils.player().sendMessage(Text.of(text), false);
    }

    public static String scriptName(String script_name){
       return  "assets/"+ modId +"/improperui/" + script_name + ".ui";
    }

    public static String getModVersion() {
        String fullVersionString = FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getVersion().getFriendlyString();
        String[] parts = fullVersionString.split("-");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+")) {
                return part;
            }
        }
        return "Unknown";
    }
}
