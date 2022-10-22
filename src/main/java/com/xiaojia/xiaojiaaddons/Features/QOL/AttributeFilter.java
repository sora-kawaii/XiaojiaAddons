package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AttributeFilter {

   private static final float nameScale = 0.8F;

   private static final float levelScale = 0.7F;

   @SubscribeEvent
   public void onItemDrawn(ItemDrawnEvent var1) {
      Inventory var2 = ControlUtils.getOpenedInventory();
      if (Checker.enabled && var2 != null && Configs.ItemAttributeFilter) {
         ItemStack var3 = var1.itemStack;
         NBTTagCompound var4 = NBTUtils.getCompoundFromExtraAttributes(var3, "attributes");
         if (var4 != null) {
            String var5 = Configs.Attribute1.toLowerCase().replaceAll(" ", "_");
            String var6 = Configs.Attribute2.toLowerCase().replaceAll(" ", "_");
            if (var4.hasKey(var5) || var4.hasKey(var6)) {
               int var7 = 0;
               int var8 = 0;
               if (var4.hasKey(var5)) {
                  var7 = var4.getInteger(var5);
               }

               if (var4.hasKey(var6)) {
                  var8 = var4.getInteger(var6);
               }

               int var9 = (var7 == 0 ? 0 : 1) + (var8 == 0 ? 0 : 2);
               String var10 = var9 == 1 ? "6" : (var9 == 2 ? "b" : "c");
               String var11 = "§" + var10 + "§l" + var9;
               String var12 = (var7 == 0 ? " " : var7) + " " + (var8 == 0 ? " " : var8);
               GuiUtils.drawNameAndLevel(var1.renderer, var11, var12, var1.x, var1.y, 0.800000011920929, 0.699999988079071);
            }
         }
      }
   }
}
