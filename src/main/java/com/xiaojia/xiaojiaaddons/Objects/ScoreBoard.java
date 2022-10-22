package com.xiaojia.xiaojiaaddons.Objects;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class ScoreBoard {

    public static boolean update = true;

    public static String title = "";

    private static ArrayList lines = new ArrayList();

    private static void updateLines() {
        ArrayList var0 = new ArrayList();

        try {
            Scoreboard var1 = MinecraftUtils.getPlayer().getWorldScoreboard();
            title = var1.getObjectiveInDisplaySlot(1).getDisplayName();
            ArrayList var2 = new ArrayList(var1.getSortedScores(var1.getObjectiveInDisplaySlot(1)));
            Iterator var3 = var2.iterator();

            while (var3.hasNext()) {
                Score var4 = (Score) var3.next();
                String var5 = "";
                ScorePlayerTeam var6 = var1.getPlayersTeam(var4.getPlayerName());

                try {
                    var5 = var5 + var6.getColorPrefix();
                } catch (Exception var9) {
                }

                var5 = var5 + var4.getPlayerName();

                try {
                    var5 = var5 + var6.getColorSuffix();
                } catch (Exception var8) {
                }

                var5 = ChatFormatting.stripFormatting(var5);
                var0.add(var5);
            }

            lines = var0;
        } catch (Exception var10) {
        }

    }

    public static ArrayList getLines() {
        if (update) {
            updateLines();
            update = false;
        }

        return lines;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        update = true;
    }
}
