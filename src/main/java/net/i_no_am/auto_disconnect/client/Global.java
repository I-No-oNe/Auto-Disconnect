package net.i_no_am.auto_disconnect.client;

import net.i_no_am.auto_disconnect.utils.ImproperUIUtils;
import net.minecraft.client.MinecraftClient;

public interface Global {
    MinecraftClient mc = MinecraftClient.getInstance();
    String PREFIX = "§b[Auto-Disconnect]:§r ";
    String modId = "auto-disconnect";
    String[] screens = ImproperUIUtils.scriptNames("screen");
}
