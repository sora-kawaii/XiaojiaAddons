package com.xiaojia.xiaojiaaddons.Features.Nether.Dojo;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class DojoUtils {

    private static EnumDojoTask currentTask;

    private static EnumDojoTask getCurrentTask() {
        if (!SkyblockUtils.isInDojo()) {
            return null;
        } else {
            ArrayList var0 = ScoreBoard.getLines();
            Iterator var1 = var0.iterator();

            String var2;
            do {
                if (!var1.hasNext()) {
                    return null;
                }

                var2 = (String) var1.next();
                StringBuilder var3 = new StringBuilder();
                var2 = ChatLib.removeFormatting(var2);

                for (int var4 = 0; var4 < var2.length(); ++var4) {
                    char var5 = var2.charAt(var4);
                    if (var5 >= 'A' && var5 <= 'Z' || var5 >= 'a' && var5 <= 'z' || var5 == ':' || var5 == ' ') {
                        var3.append(var5);
                    }
                }

                var2 = var3.toString();
            } while (!var2.startsWith("Challenge: "));

            return EnumDojoTask.valueOf(var2.substring(11).toUpperCase());
        }
    }

    public static EnumDojoTask getTask() {
        return currentTask;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        EnumDojoTask var2 = currentTask;
        currentTask = getCurrentTask();
        if (currentTask != EnumDojoTask.MASTERY) {
            Mastery.clear();
        }

        if (currentTask != var2 && currentTask == EnumDojoTask.MASTERY) {
            Mastery.onEnter();
        }

    }
}
