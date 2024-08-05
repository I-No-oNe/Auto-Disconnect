package net.i_no_am.auto_disconnect.config;

import io.github.itzispyder.improperui.ImproperUIAPI;
import io.github.itzispyder.improperui.config.ConfigReader;
import net.i_no_am.auto_disconnect.client.Global;
import net.i_no_am.auto_disconnect.config.settings.ConfigSettings;

public class Config implements Global {

    public static ConfigSettings<Boolean> enable;
    public static ConfigSettings<Integer> range;
    public static ConfigSettings<Boolean> checkPlayerHealth;
    public static ConfigSettings<Integer> selfPlayerHealth;
    public static ConfigSettings<Boolean> checkAnchors;
    public static ConfigSettings<Boolean> checkCrystals;
//    public static ConfigSettings<Boolean> autoDisable;
    public static ConfigSettings<Boolean> checkNewPlayers;
//    public static ConfigSettings<Double> restart;

    public static void loadConfig() {
        ConfigReader ADconfig = ImproperUIAPI.getConfigReader(modId, "config.properties");
        enable = new ConfigSettings<>(Boolean.class, ADconfig.readBool("enable", true));
        range = new ConfigSettings<>(Integer.class, ADconfig.readInt("range", 5));
        checkPlayerHealth = new ConfigSettings<>(Boolean.class, ADconfig.readBool("checkPlayerHealth", true));
        selfPlayerHealth = new ConfigSettings<>(Integer.class, ADconfig.readInt("selfPlayerHealth", 2));
        checkAnchors = new ConfigSettings<>(Boolean.class, ADconfig.readBool("checkAnchors", true));
        checkCrystals = new ConfigSettings<>(Boolean.class, ADconfig.readBool("checkCrystals", true));
//        autoDisable = new ConfigSettings<>(Boolean.class, ADconfig.readBool("autoDisable", true));
//        restart = new ConfigSettings<>(Double.class, ADconfig.readDouble("restart",20));
        checkNewPlayers = new ConfigSettings<>(Boolean.class, ADconfig.readBool("checkNewPlayers", false));
    }
}
