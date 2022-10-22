package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MerchantUtils;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRongYao {

   private static final ArrayList recipes = new ArrayList() {
      {
         this.add(new DailyQuestRecipe("PlaceHolder", 0));
         this.add(new DailyQuestRecipe("〖白羽鸡肉〗", 64));
         this.add(new DailyQuestRecipe("〖哥布林的手骨〗", 48));
         this.add(new DailyQuestRecipe("〖钥匙〗", 48));
         this.add(new DailyQuestRecipe("〖蜘蛛卵〗", 64));
         this.add(new DailyQuestRecipe("西瓜片", 64));
         this.add(new DailyQuestRecipe(">> 鸡神证明 <<", 4));
         this.add(new DailyQuestRecipe("金蛋", 1));
         this.add(new DailyQuestRecipe("✪ 金", 1));
      }
   };

   private Thread sellThread = null;

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (this.should() && (this.sellThread == null || !this.sellThread.isAlive())) {
         this.sellThread = new Thread(() -> {
            try {
               Inventory var0 = ControlUtils.getOpenedInventory();
               int[] var1 = new int[9];
               int[][] var2 = new int[9][2];

               for(int var3 = 3; var3 < 39; ++var3) {
                  ItemStack var4 = var0.getItemInSlot(var3);
                  if (var4 != null) {
                     String var5 = ChatLib.removeFormatting(var4.getDisplayName());
                     int var6 = -1;
                     if (var5.contains("[每日任务")) {
                        if (var5.contains("#")) {
                           var6 = Integer.parseInt(var5.substring(6, 7));
                        } else {
                           var6 = 8;
                        }

                        var2[var6][0] = var3;
                        var1[var6] |= 2;
                     } else {
                        for(int var7 = 1; var7 <= 8; ++var7) {
                           if (var5.contains(((DailyQuestRecipe)recipes.get(var7)).name) && var4.stackSize >= ((DailyQuestRecipe)recipes.get(var7)).count) {
                              var2[var7][1] = var3;
                              var1[var7] |= 1;
                              var6 = var7;
                              break;
                           }
                        }
                     }

                     if (var6 > 0 && var1[var6] == 3) {
                        var0.click(var2[var6][0], false, "LEFT", 0);
                        var0.click(0, false, "LEFT", 0);
                        var0.click(var2[var6][1], false, "LEFT", 0);
                        var0.click(1, false, "LEFT", 0);
                        var0.click(2, true, "LEFT", 0);
                        var0.click(0, true, "LEFT", 0);
                        var0.click(1, true, "LEFT", 0);
                        Thread.sleep(100L);
                        return;
                     }
                  }
               }
            } catch (Exception var8) {
               var8.printStackTrace();
            }

         });
         this.sellThread.start();
      }

   }

   private boolean should() {
      if (Checker.enabled && Configs.AutoRongYao) {
         Inventory var1 = ControlUtils.getOpenedInventory();
         if (var1 == null) {
            return false;
         } else if (var1.getItemInSlot(0) != null) {
            return false;
         } else if (var1.getItemInSlot(1) != null) {
            return false;
         } else if (var1.getItemInSlot(2) != null) {
            return false;
         } else {
            String var2 = MerchantUtils.getCurrentMerchant();
            return var2 != null && var2.contains("荣耀使者");
         }
      } else {
         return false;
      }
   }
}
