package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.Iterator;

public class ChestProfit {

    private static final String[] chestNames = new String[]{"Wood Chest", "Gold Chest", "Emerald Chest", "Diamond Chest", "Obsidian Chest", "Bedrock Chest"};

    private static String getStringProfit(double var0) {
        String var2 = "+";
        if (var0 < 0.0) {
            var2 = "-";
            var0 = -var0;
        }

        if (var0 >= 1000000.0) {
            return var2 + String.format("%.2fM", var0 / 1000000.0);
        } else {
            return var0 >= 1000.0 ? var2 + String.format("%.2fK", var0 / 1000.0) : var2 + String.format("%.0f", var0);
        }
    }

    @SubscribeEvent
    public void onTick(GuiScreenEvent.DrawScreenEvent var1) {
        if (Checker.enabled && SkyblockUtils.isInDungeon()) {
            if (Configs.ShowChestProfit) {
                String var2 = ControlUtils.getOpenedInventoryName();
                if (!Arrays.stream(chestNames).noneMatch((var1x) -> {
                    return var1x.equals(var2);
                })) {
                    try {
                        Inventory var3 = ControlUtils.getOpenedInventory();
                        ItemStack var4 = var3.getItemInSlot(31);
                        double var5 = 0.0;
                        Iterator var7 = NBTUtils.getLore(var4).iterator();

                        String var8;
                        while (var7.hasNext()) {
                            var8 = (String) var7.next();
                            String var9 = ChatLib.removeFormatting(var8);
                            if (var9.endsWith(" Coins")) {
                                var5 = -Integer.parseInt(var9.substring(0, var9.length() - 6).replaceAll(",", ""));
                            }
                        }

                        for (int var12 = 9; var12 < 18; ++var12) {
                            try {
                                var5 += LowestBin.getItemValue(var3.getItemInSlot(var12));
                            } catch (Exception var10) {
                            }
                        }

                        String var13 = "§c";
                        if (var5 > 700000.0) {
                            var13 = "§a";
                        } else if (var5 > 0.0) {
                            var13 = "§2";
                        }

                        var8 = var13 + getStringProfit(var5);
                        GuiUtils.drawStringAtRightUpOfDoubleChest(var8);
                    } catch (Exception var11) {
                    }

                }
            }
        }
    }
}
