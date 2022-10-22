package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;

public class Inventory {

   public final Container container;

   public List getSlots() {
      return this.container.inventorySlots;
   }

   public final ItemStack getItemInSlot(int var1) {
      if (this.getSize() <= var1) {
         return null;
      } else {
         try {
            return this.container.getSlot(var1).getStack();
         } catch (Exception var3) {
            return null;
         }
      }
   }

   public Inventory(Container var1) {
      this.container = var1;
   }

   public void click(int var1, boolean var2, String var3) {
      this.click(var1, var2, var3, 0);
   }

   public final String getName() {
      if (this.container instanceof ContainerChest) {
         return ((ContainerChest)this.container).getLowerChestInventory().getName();
      } else {
         return this.container instanceof ContainerMerchant ? ((ContainerMerchant)this.container).getMerchantInventory().getName() : "container";
      }
   }

   public int getWindowId() {
      return this.container.windowId;
   }

   public final ArrayList getItemStacks() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.getSize(); ++var2) {
         var1.add(this.getItemInSlot(var2));
      }

      return var1;
   }

   public void click(int var1, boolean var2, String var3, int var4) {
      int var5 = this.getWindowId() + var4;
      byte var7 = 0;
      int var6;
      switch (var3) {
         case "MIDDLE":
            var6 = 2;
            if (var2) {
               var7 = 1;
            } else {
               var7 = 3;
            }
            break;
         case "RIGHT":
            var6 = 1;
            var7 = 3;
            break;
         case "SWAP":
            var6 = ControlUtils.getHeldItemIndex();
            var7 = 2;
            break;
         default:
            var6 = 0;
            if (var2) {
               var7 = 1;
            }
      }

      XiaojiaAddons.mc.playerController.windowClick(var5, var1, var6, var7, MinecraftUtils.getPlayer());
      ChatLib.debug(String.format("%d %d %d %d", var5, var1, var6, Integer.valueOf(var7)));
   }

   public int getSize() {
      return this.container.inventoryItemStacks.size();
   }

   public void click(int var1) {
      this.click(var1, false, "LEFT");
   }
}
