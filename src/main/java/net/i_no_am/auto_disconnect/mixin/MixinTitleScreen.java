package net.i_no_am.auto_disconnect.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.auto_disconnect.AutoDisconnect;
import net.i_no_am.auto_disconnect.Global;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;

@Mixin(TitleScreen.class)
public class MixinTitleScreen implements Global {

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if (AutoDisconnect.isOutdated && mc.currentScreen instanceof TitleScreen && !FabricLoader.getInstance().isDevelopmentEnvironment()) {
            mc.setScreen(new ConfirmScreen(
                    confirm -> {
                        if (confirm)
                            Util.getOperatingSystem().open(URI.create("https://modrinth.com/mod/auto-disconnect/versions"));
                        else mc.stop();
                    },
                    Text.of(Formatting.RED + "You are using an outdated version of " + starter), Text.of("Please download the latest version from " + Formatting.GREEN + "modrinth"), Text.of("Download"), Text.of("Quit Game")));
        }
    }
}