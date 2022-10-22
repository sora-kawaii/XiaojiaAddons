package com.xiaojia.xiaojiaaddons.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings.GameType;

import java.util.*;

public class TabUtils {

    private static final Ordering playerComparator = Ordering.from(new PlayerComparator());

    public static ArrayList getNames() {
        GuiPlayerTabOverlay var0 = getTabGui();
        ArrayList var1 = new ArrayList();
        if (MinecraftUtils.getPlayer() != null && var0 != null) {
            Collection var2 = MinecraftUtils.getPlayer().sendQueue.getPlayerInfoMap();
            List var3 = playerComparator.sortedCopy(var2);
            Iterator var4 = var3.iterator();

            while (var4.hasNext()) {
                NetworkPlayerInfo var5 = (NetworkPlayerInfo) var4.next();
                var1.add(ChatLib.removeFormatting(var0.getPlayerName(var5)));
            }

            return var1;
        } else {
            return var1;
        }
    }

    public static GuiPlayerTabOverlay getTabGui() {
        GuiIngame var0 = XiaojiaAddons.mc.ingameGUI;
        return var0 != null ? var0.getTabList() : null;
    }

    public static void printTab() {
        System.err.println("Tab:");
        ArrayList var0 = getNames();

        for (int var1 = 0; var1 < var0.size(); ++var1) {
            System.err.println(var1 + ": " + var0.get(var1));
        }

        System.err.println();
    }

    public static final class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        @Override
        public int compare(NetworkPlayerInfo var1, NetworkPlayerInfo var2) {
            ScorePlayerTeam var3 = var1.getPlayerTeam();
            ScorePlayerTeam var4 = var2.getPlayerTeam();
            String var5 = var3 == null ? null : var3.getRegisteredName();
            String var6 = var4 == null ? null : var4.getRegisteredName();
            return ComparisonChain.start().compareTrueFirst(var1.getGameType() != GameType.SPECTATOR, var2.getGameType() != GameType.SPECTATOR).compare(var5 == null ? "" : var5, var6 == null ? "" : var6).compare(var1.getGameProfile().getName(), var2.getGameProfile().getName()).result();
        }
    }
}
