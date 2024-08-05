package net.i_no_am.auto_disconnect.mixin;

import io.github.itzispyder.improperui.util.ChatUtils;
import net.i_no_am.auto_disconnect.client.Global;
import net.i_no_am.auto_disconnect.client.Version;
import net.i_no_am.auto_disconnect.utils.ConfigUtils;
import net.i_no_am.auto_disconnect.utils.PlayerUtils;
import net.i_no_am.auto_disconnect.utils.Utils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler implements Global {
    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void OnGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        ConfigUtils.setDisconnect(false);
        Version.compareVersions();
        if (!Version.isRightVersion) {
            if (PlayerUtils.valid()) {
                ChatUtils.sendMessage(PREFIX + "Download the new version of Auto Disconnect from Modrinth!");
                Text literal = Text.literal("§a https://modrinth.com/mod/auto-disconnect");
                ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/auto-disconnect");
                MutableText text = literal.copy();
                Utils.sendText(text.fillStyle(text.getStyle().withClickEvent(event)));
                Version.isVersionChecked = false;
            }
        }
    }
}