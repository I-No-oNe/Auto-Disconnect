package net.i_no_am.auto_disconnect;

import net.minecraft.client.MinecraftClient;

public interface Global {
    MinecraftClient mc = MinecraftClient.getInstance();
    String starter = "Auto Disconnect";
    String modId = "auto-disconnect";
    String[] screens = {
            "assets/auto-disconnect/improperui/screen.ui"
    };
}
