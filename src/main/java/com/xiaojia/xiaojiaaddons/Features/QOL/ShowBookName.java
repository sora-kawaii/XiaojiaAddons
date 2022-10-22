package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowBookName {

    private static final float nameScale = 0.8F;

    private static final float levelScale = 0.7F;

    private static final HashMap cachedStrings = new HashMap();

    public static int getCacheSize() {
        return cachedStrings.size();
    }

    @SubscribeEvent
    public void onItemDrawn(ItemDrawnEvent var1) {
        Inventory var2 = ControlUtils.getOpenedInventory();
        if (Checker.enabled && var2 != null && Configs.ShowBookName) {
            if (!var2.getName().contains("Superpairs (")) {
                ItemStack var3 = var1.itemStack;
                if (var3 != null && var3.hasDisplayName()) {
                    String var4 = ChatLib.removeFormatting(var3.getDisplayName());
                    if (var3.getUnlocalizedName().contains("enchantedBook")) {
                        boolean var5 = var2.getName().contains("Auction") || var2.getName().contains("Bids");
                        if (var4.toLowerCase().startsWith("enchanted book") || var5) {
                            String var6 = "";
                            String var7 = "";
                            if (cachedStrings.containsKey(var3)) {
                                String var8 = (String) cachedStrings.get(var3);
                                var6 = var8.split(" ")[0];
                                var7 = var8.split(" ")[1];
                            } else {
                                try {
                                    ArrayList var14 = NBTUtils.getBookNameAndLevel(var3);
                                    if (var14.size() != 2) {
                                        return;
                                    }

                                    boolean var9 = NBTUtils.isBookUltimate(var3);
                                    String var10 = var9 ? "§d§l" : "";
                                    String var11 = (String) var14.get(0);
                                    String var12 = var11.substring(0, Math.min(var11.length(), 3));
                                    if (var9) {
                                        var12 = var11.replaceAll("[a-z ]", "");
                                    }

                                    var6 = var10 + var12;
                                    var7 = (String) var14.get(1);
                                    cachedStrings.put(var3, var6 + " " + var7);
                                    if (cachedStrings.size() > 1000) {
                                        cachedStrings.clear();
                                    }
                                } catch (Exception var13) {
                                    var13.printStackTrace();
                                    return;
                                }
                            }

                            GuiUtils.drawNameAndLevel(var1.renderer, var6, var7, var1.x, var1.y, 0.800000011920929, 0.699999988079071);
                        }
                    }
                }
            }
        }
    }
}
