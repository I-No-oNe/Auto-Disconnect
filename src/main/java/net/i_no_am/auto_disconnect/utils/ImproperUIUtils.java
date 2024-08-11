package net.i_no_am.auto_disconnect.utils;

import net.i_no_am.auto_disconnect.client.Global;

public class ImproperUIUtils implements Global {
    public static String[] scriptNames(String... script_names) {
        String[] paths = new String[script_names.length];
        for (int i = 0; i < script_names.length; i++) {
            paths[i] = "assets/" + modId + "/improperui/" + script_names[i] + ".ui";
        }
        return paths;
    }
}
