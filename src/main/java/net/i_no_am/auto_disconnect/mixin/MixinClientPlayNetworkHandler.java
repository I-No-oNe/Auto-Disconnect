package net.i_no_am.auto_disconnect.mixin;

import net.i_no_am.auto_disconnect.client.Version;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Inject(method = "getConnection", at = @At("RETURN"))
    private void onWorldLoad(CallbackInfoReturnable<ClientConnection> cir) {
        Version.updateChecker();
    }
}
