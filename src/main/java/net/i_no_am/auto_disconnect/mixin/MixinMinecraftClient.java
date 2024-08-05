package net.i_no_am.auto_disconnect.mixin;

import net.i_no_am.auto_disconnect.utils.ConfigUtils;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "disconnect()V", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
        ConfigUtils.setDisconnect(true);
    }
}