package net.i_no_am.auto_disconnect;

import com.mojang.blaze3d.platform.InputConstants;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.i_no_am.auto_disconnect.config.Config;
import net.i_no_am.auto_disconnect.version.Version;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

public class AutoDisconnect implements ModInitializer, Global {

    public static final KeyMapping BIND = KeyMappingHelper.registerKeyMapping(new KeyMapping("auto-disconnect.i_no_am.menu", InputConstants.KEY_K, KeyMapping.Category.register(Identifier.fromNamespaceAndPath("auto-disconnect", "i_no_am.category"))));
    public static final String API = "https://api.github.com/repos/I-No-oNe/Auto-Disconnect/releases/latest";
    public static final String DOWNLOAD = "https://modrinth.com/mod/Auto-Disconnect/versions";

    @Override
    public void onInitialize() {

        Config.init(modId, Config.class);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (BIND.consumeClick() && mc.player != null) {
                Screen screenConfig = Config.getScreen(mc.screen, modId);
                mc.setScreen(screenConfig);
                if (screenConfig.shouldCloseOnEsc()) MidnightConfig.write(modId);
            }
            Version.create(API, DOWNLOAD).notifyUpdate(isDev);
        });
    }
}
