package net.i_no_am.auto_disconnect;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.i_no_am.auto_disconnect.config.Config;
import net.i_no_am.auto_disconnect.version.Version;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class AutoDisconnect implements ModInitializer, Global {

    public static final KeyBinding BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("auto-disconnect.i_no_am.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, KeyBinding.Category.create(Identifier.of("auto-disconnect.i_no_am.category"))));
    public static final String API = "https://api.github.com/repos/I-No-oNe/Auto-Disconnect/releases/latest";
    public static final String DOWNLOAD = "https://modrinth.com/mod/Auto-Disconnect/versions";

    @Override
    public void onInitialize() {

        Config.init(modId, Config.class);

        WorldRenderEvents.END_MAIN.register((context) -> Version.create(API, DOWNLOAD).notifyUpdate(isDev));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (BIND.wasPressed() && mc.player != null) {
                var screenConfig = Config.getScreen(mc.currentScreen, modId);
                mc.setScreen(screenConfig);
                if (screenConfig.shouldCloseOnEsc()) MidnightConfig.write(modId);
            }
        });
    }
}