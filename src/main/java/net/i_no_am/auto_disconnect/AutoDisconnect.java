package net.i_no_am.auto_disconnect;

import io.github.itzispyder.improperui.ImproperUIAPI;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.i_no_am.auto_disconnect.config.Config;
import net.i_no_am.auto_disconnect.impl.AutoDis;
import net.i_no_am.auto_disconnect.version.Version;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("unused")
public class AutoDisconnect implements ModInitializer, Global {

	public static boolean isOutdated = false;

	public static final KeyBinding BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"auto-disconnect.i_no_am.menu",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_K,
			"auto-disconnect.i_no_am.category"
	));

	@Override
	public void onInitialize() {
		Version.checkUpdates();
		ImproperUIAPI.init(modId, AutoDisconnect.class, screens);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			AutoDis AD = new AutoDis();
			Config.loadConfig();
			while (BIND.wasPressed()) {
				ImproperUIAPI.parseAndRunFile(modId, "screen.ui");
			}
		});
	}
}