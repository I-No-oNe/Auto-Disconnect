package net.i_no_am.auto_disconnect.client;

import net.minecraft.client.MinecraftClient;

public interface Global {
    MinecraftClient mc = MinecraftClient.getInstance();
    String PREFIX = "§bAuto-Disconnect§r ";
    String modId = "auto-disconnect";
    String[] screens = {
            "assets/"+ modId +"/improperui/screen.ui"
    };
}
