package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShowAttribute {

   private static final float nameScale = 0.8F;

   private static final HashMap cachedStrings = new HashMap();

   private static final float levelScale = 0.7F;

   public static int getCacheSize() {
      return cachedStrings.size();
   }

   @SubscribeEvent
   public void onItemDrawn(ItemDrawnEvent var1) {
      Inventory var2 = ControlUtils.getOpenedInventory();
      if (Checker.enabled && var2 != null && Configs.ShowAttribute) {
         ItemStack var3 = var1.itemStack;
         if (var3 != null && var3.hasDisplayName()) {
            String var4 = ChatLib.removeFormatting(var3.getDisplayName()).toLowerCase();
            if (var4.startsWith("attribute shard")) {
               String var5 = "";
               String var6 = "";
               if (cachedStrings.containsKey(var3)) {
                  String var7 = (String)cachedStrings.get(var3);
                  var5 = var7.split(":")[0];
                  var6 = var7.split(":")[1];
               } else {
                  try {
                     ArrayList var10 = NBTUtils.getBookNameAndLevel(var3);
                     if (var10.size() != 2) {
                        return;
                     }

                     var5 = (String)var10.get(0);
                     var6 = (String)var10.get(1);
                     cachedStrings.put(var3, var5 + ":" + var6);
                     if (cachedStrings.size() > 1000) {
                        cachedStrings.clear();
                     }
                  } catch (Exception var9) {
                     var9.printStackTrace();
                     return;
                  }
               }

               String[] var11 = Configs.ShowAttributeName.toLowerCase().split(",");
               String finalVar = var5;
               if (!Arrays.stream(var11).noneMatch((var1x) -> {
                  return finalVar.toLowerCase().contains(var1x);
               })) {
                  GuiUtils.drawNameAndLevel(var1.renderer, var5.substring(0, Math.min(var5.length(), 3)), var6, var1.x, var1.y, 0.800000011920929, 0.699999988079071);
               }
            }
         }
      }
   }
}
