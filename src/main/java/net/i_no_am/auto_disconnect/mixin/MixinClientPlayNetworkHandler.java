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

    /*Ported from https://github.com/I-No-oNe/View-Model/blob/main/src/main/java/net/i_no_am/viewmodel/mixin/MixinClientPlayNetworkHandler.java*/

    @Inject(method = "getConnection", at = @At("RETURN"))
    private void onWorldLoadMixin(CallbackInfoReturnable<ClientConnection> cir) {
        Version.updateChecker();
        Version.checked = true;
    }
}
