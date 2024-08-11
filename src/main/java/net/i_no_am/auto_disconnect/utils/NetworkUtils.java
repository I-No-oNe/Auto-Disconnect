package net.i_no_am.auto_disconnect.utils;

import io.github.itzispyder.improperui.util.ChatUtils;
import net.i_no_am.auto_disconnect.client.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class NetworkUtils implements Global {
    /*Meteor Client,thanks for helping me with this code*/
    public static void disconnectPlayer(String reason) {
        MutableText text = Text.literal("§3Auto-Disconnect was triggered§r");
        text = text.append(Text.literal("\n\n" + reason).withColor(Colors.RED));
       PlayerUtils.player().networkHandler.onDisconnect(new DisconnectS2CPacket(text));
        if (Config.autoDisable.getVal()){
        Config.autoDisable.setVal("enable",false);
        }
    }
    public static void sendUpdate(){
        ChatUtils.sendMessage(PREFIX + "Download the new version of Auto Disconnect from Modrinth!");
        Text literal = Text.literal("§b https://modrinth.com/mod/auto-disconnect");
        ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/auto-disconnect");
        MutableText text = literal.copy();
        PlayerUtils.sendText(text.fillStyle(text.getStyle().withClickEvent(event)));
    }
}
