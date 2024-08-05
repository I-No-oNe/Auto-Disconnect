package net.i_no_am.auto_disconnect.utils;

import net.i_no_am.auto_disconnect.client.Global;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.Objects;

public class NetworkUtils implements Global {
    public static void disconnectPlayer(String reason) {
        MutableText text = Text.literal("§3Auto-Disconnect was triggered§r");
        text = text.append(Text.literal("\n\n" + reason).withColor(Colors.RED));
        Objects.requireNonNull(mc.player).networkHandler.onDisconnect(new DisconnectS2CPacket(text));
        ConfigUtils.setDisconnect(true);
    }
}
