package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Experimentation {

   private long lastExperimentationTime = 0L;

   private boolean ultrasequenceLastRender = false;

   private static final ArrayList chronomatronPatterns = new ArrayList();

   private static final ArrayList ultrasequenceList = new ArrayList() {
      {
         for(int var1 = 0; var1 < 45; ++var1) {
            this.add(0);
         }

      }
   };

   private int chronomatronLastRound = 0;

   private int ultrasequenceTot = 0;

   private int chronomatronCnt = 0;

   private int ultrasequenceCnt = 0;

   @SubscribeEvent
   public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent var1) {
      if (Checker.enabled) {
         if (Configs.AutoChronomatron || Configs.AutoUltrasequencer) {
            Inventory var2 = ControlUtils.getOpenedInventory();
            if (var2 != null) {
               try {
                  String var3 = var2.getName();
                  ItemStack var4;
                  if (var3.startsWith("Chronomatron")) {
                     if (!Configs.AutoChronomatron) {
                        return;
                     }

                     var4 = ControlUtils.getItemStackInSlot(4, false);
                     ItemStack var10 = ControlUtils.getItemStackInSlot(49, false);
                     if (var4 == null || var10 == null) {
                        return;
                     }

                     int var6 = var4.stackSize;
                     int var7 = var10.stackSize;
                     String var8 = var10.getDisplayName();
                     if (var8.contains("Timer:")) {
                        if (var6 != this.chronomatronLastRound && var7 == this.chronomatronLastRound + 3) {
                           this.chronomatronLastRound = var6;
                           this.addChronomatron();
                        }

                        if (this.chronomatronCnt < chronomatronPatterns.size() && TimeUtils.curTime() - this.lastExperimentationTime > (long)Configs.ExperimentClickCoolDown) {
                           this.lastExperimentationTime = TimeUtils.curTime();
                           this.clickChronomatron();
                        }
                     } else if (var8.contains("Remember the pattern")) {
                        this.chronomatronCnt = 0;
                     }
                  } else if (var3.startsWith("Ultrasequencer")) {
                     if (!Configs.AutoUltrasequencer) {
                        return;
                     }

                     var4 = ControlUtils.getItemStackInSlot(49, false);
                     if (var4 == null) {
                        return;
                     }

                     String var5 = var4.getDisplayName();
                     if (var5.contains("Remember the pattern!")) {
                        this.getUltraSequence();
                        this.ultrasequenceCnt = 0;
                        this.ultrasequenceLastRender = true;
                     } else if (var5.contains("Timer:")) {
                        if (this.ultrasequenceLastRender) {
                           ++this.ultrasequenceTot;
                        }

                        this.ultrasequenceLastRender = false;
                        if (this.chronomatronCnt < this.ultrasequenceTot && TimeUtils.curTime() - this.lastExperimentationTime > (long)Configs.ExperimentClickCoolDown) {
                           this.lastExperimentationTime = TimeUtils.curTime();
                           ++this.ultrasequenceCnt;
                           ControlUtils.getOpenedInventory().click((Integer)ultrasequenceList.get(this.ultrasequenceCnt));
                        }
                     }
                  }
               } catch (Exception var9) {
                  var9.printStackTrace();
               }

            }
         }
      }
   }

   private void clickChronomatron() {
      ((Inventory)Objects.requireNonNull(ControlUtils.getOpenedInventory())).click((Integer)chronomatronPatterns.get(this.chronomatronCnt), false, "MIDDLE");
      ++this.chronomatronCnt;
   }

   @SubscribeEvent
   public void onTickCheck(TickEndEvent var1) {
      Inventory var2 = ControlUtils.getOpenedInventory();
      if (var2 != null) {
         if (!var2.getName().startsWith("Chronomatron")) {
            if (!var2.getName().startsWith("Ultrasequencer")) {
               chronomatronPatterns.clear();
               this.chronomatronLastRound = this.chronomatronCnt = 0;
               this.lastExperimentationTime = 0L;
               this.ultrasequenceCnt = this.ultrasequenceTot = 0;
               this.ultrasequenceLastRender = false;
            }
         }
      }
   }

   private void getUltraSequence() {
      Inventory var1 = ControlUtils.getOpenedInventory();
      if (var1 == null) {
         ChatLib.chat("Something wrong happened! Please Contact Author.");
      } else {
         for(int var2 = 9; var2 < 45; ++var2) {
            ItemStack var3 = var1.getItemInSlot(var2);
            if (var3 != null && !var3.getItem().getRegistryName().contains("glass_pane")) {
               ultrasequenceList.set(var3.stackSize, var2);
            }
         }

      }
   }

   private void addChronomatron() {
      Inventory var1 = ControlUtils.getOpenedInventory();
      if (var1 == null) {
         ChatLib.chat("Something wrong happened. Please Contact Author.");
      } else {
         ArrayList var2 = var1.getItemStacks();

         for(int var3 = 10; var3 < 44; ++var3) {
            if (var2.get(var3) != null && ((ItemStack)var2.get(var3)).getItem() == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
               chronomatronPatterns.add(var3);
               return;
            }
         }

         ChatLib.chat("Xiaojia laji! Cannot find chronomatron.");
      }
   }
}
