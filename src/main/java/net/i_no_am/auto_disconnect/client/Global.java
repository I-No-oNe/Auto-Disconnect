package net.i_no_am.auto_disconnect.client;

import net.i_no_am.auto_disconnect.utils.Utils;
import net.minecraft.client.MinecraftClient;

public interface Global {
    MinecraftClient mc = MinecraftClient.getInstance();
    String PREFIX = "§bAuto-Disconnect§r ";
    String modId = "auto-disconnect";
    String[] screens = {
            Utils.scriptName("screen")
    };
}
