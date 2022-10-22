package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSnowball {

   private Thread snowballThread = null;

   private final KeyBind keyBind = new KeyBind("Auto Snowball", 0);

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         Inventory var2 = ControlUtils.getOpenedInventory();
         if (var2 != null && var2.getSize() == 45) {
            if (this.keyBind.isKeyDown() && (this.snowballThread == null || !this.snowballThread.isAlive())) {
               this.snowballThread = new Thread(() -> {
                  int var1 = ControlUtils.getHeldItemIndex();
                  List var2x = var2.getItemStacks().subList(36, 45);

                  for(int var3 = 0; var3 < 9; ++var3) {
                     ItemStack var4 = (ItemStack)var2x.get(var3);
                     if (var4 != null) {
                        Item var5 = var4.getItem();
                        if (var5 instanceof ItemSnowball) {
                           ControlUtils.setHeldItemIndex(var3);

                           for(int var6 = 0; var6 < var4.stackSize * 2; ++var6) {
                              ControlUtils.rightClick();
                           }
                        }
                     }
                  }

                  ControlUtils.setHeldItemIndex(var1);
                  boolean var7 = false;
                  var2x = var2.getItemStacks().subList(36, 45);

                  for(int var8 = 0; var8 < 9; ++var8) {
                     if (var2x.get(var8) == null) {
                        var7 = true;
                        break;
                     }
                  }

                  if (var7) {
                     CommandsUtils.addCommand("/pickupstash", 3);
                  }

               });
               this.snowballThread.start();
            }

         }
      }
   }
}
