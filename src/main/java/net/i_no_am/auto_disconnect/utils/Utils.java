package net.i_no_am.auto_disconnect.utils;

import net.i_no_am.auto_disconnect.client.Global;
import net.minecraft.text.Text;

public class Utils implements Global {
    public static void sendText(Text text) {
        PlayerUtils.player().sendMessage(Text.of(text), false);
    }
/*Left Over code for a method that I thought that I could use for auto disable*/
 public static boolean timer(double limit) {
     double elapsedTime = 0;
        elapsedTime++;
        return elapsedTime >= limit;
    }
}
