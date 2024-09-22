package net.i_no_am.auto_disconnect.utils;

import net.i_no_am.auto_disconnect.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class NetworkUtils implements Global {
    // Credits -> Meteor Client 🔥
    public static void disconnectPlayer(String reason) {
        MutableText text = Text.literal("§3Auto-Disconnect was triggered§r");
        text = text.append(Text.literal(Colors.RED + "\n\n" + reason));
        PlayerUtils.player().networkHandler.onDisconnect(new DisconnectS2CPacket(text));
        if (Config.autoDisable.getVal()) {
            Config.autoDisable.setVal("enable", false);
        }
    }
}
