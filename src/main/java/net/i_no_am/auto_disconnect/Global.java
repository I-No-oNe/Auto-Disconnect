package net.i_no_am.auto_disconnect;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public interface Global {
    boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
    Minecraft mc = Minecraft.getInstance();
    String PREFIX = "Auto Disconnect";
    String modId = "auto-disconnect";

    default void log(String message, Object... args) {
        if (isDev) System.out.printf((PREFIX + ": " + message + "%n"), args);
    }
}
