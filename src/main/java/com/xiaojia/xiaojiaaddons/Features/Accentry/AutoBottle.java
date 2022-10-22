package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoBottle {

   private final KeyBind keyBind = new KeyBind("Auto Bottle", 0);

   private static boolean should = false;

   public static void stop(String var0) {
      ChatLib.chat(var0);
      ChatLib.chat("Auto Bottle &cdeactivated");
      should = false;
   }

   @SubscribeEvent
   public void onTick(TickEvent.ClientTickEvent var1) {
      if (Checker.enabled) {
         if (Configs.AutoBottle) {
            if (this.keyBind.isPressed()) {
               should = !should;
               ChatLib.chat(should ? "Auto Bottle &aactivated" : "Auto Bottle &cdeactivated");
            }

            if (should) {
               if (PacketRelated.getReceivedQueueLength() != 0) {
                  Inventory var2 = ControlUtils.getOpenedInventory();
                  if (var2 != null) {
                     ArrayList var3 = new ArrayList();
                     ArrayList var4 = new ArrayList();

                     int var5;
                     ItemStack var6;
                     for(var5 = 0; var5 < var2.getSize() - 9; ++var5) {
                        var6 = var2.getItemInSlot(var5);
                        if (var6 != null && var6.getItem() == Items.experience_bottle) {
                           var3.add(var5);
                        }
                     }

                     for(var5 = 0; var5 < 9; ++var5) {
                        var6 = var2.getItemInSlot(var5 + var2.getSize() - 9);
                        if (var6 != null && var6.getItem() == Items.experience_bottle) {
                           var4.add(new Pair(var5, var6.stackSize));
                        }
                     }

                     Iterator var8;
                     if (var4.size() == 0) {
                        if (var3.size() == 0) {
                           stop("No enough bottles.");
                        } else {
                           var8 = var3.iterator();

                           while(var8.hasNext()) {
                              int var9 = (Integer)var8.next();
                              var2.click(var9, true, "LEFT");
                           }
                        }
                     } else {
                        var8 = var4.iterator();

                        while(var8.hasNext()) {
                           Pair var10 = (Pair)var8.next();
                           ControlUtils.setHeldItemIndex((Integer)var10.getKey());

                           for(int var7 = 0; var7 < (Integer)var10.getValue(); ++var7) {
                              if (PacketRelated.getSentQueueLength() > Configs.PacketThreshold) {
                                 return;
                              }

                              ControlUtils.rightClick();
                           }
                        }
                     }

                  }
               }
            }
         }
      }
   }
}
