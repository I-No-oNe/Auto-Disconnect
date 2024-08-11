package net.i_no_am.auto_disconnect.client;

import net.i_no_am.auto_disconnect.utils.NetworkUtils;
import net.i_no_am.auto_disconnect.utils.PlayerUtils;
import net.i_no_am.auto_disconnect.utils.VersionUtils;

public class Version {

    static boolean isChecked = false;

    public static void updateChecker() {
        try {
            if (!isChecked && VersionUtils.isUpdateAvailable() && PlayerUtils.valid()) {
                NetworkUtils.sendUpdate();
                isChecked = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
