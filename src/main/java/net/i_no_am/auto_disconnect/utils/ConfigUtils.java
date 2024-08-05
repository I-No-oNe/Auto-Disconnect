package net.i_no_am.auto_disconnect.utils;

import net.i_no_am.auto_disconnect.config.Config;

public class ConfigUtils {

    public static boolean disconnect = false;

    public static void setDisconnect(boolean val) {
        disconnect = val;
    }

    public static boolean isOn() {
        return Config.enable.getVal();
    }
}
