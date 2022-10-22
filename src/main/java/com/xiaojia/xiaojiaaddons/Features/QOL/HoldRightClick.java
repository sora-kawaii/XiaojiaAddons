package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class HoldRightClick {

   private long systemTimeRogue;

   private long systemTimeTerm;

   private static final KeyBind useKeyBind;

   private double currentSpeed;

   private static final ArrayList terminatorNames = new ArrayList() {
      {
         this.add("Terminator");
      }
   };

   private boolean isRightClicking = false;

   private static final ArrayList rogueNames = new ArrayList() {
      {
         this.add("Rogue");
      }
   };

   public void executeRogue() {
      if (Checker.enabled) {
         if (Configs.RogueAutoRightClick) {
            if (useKeyBind.isKeyDown() && ControlUtils.checkHoldingItem((List)rogueNames)) {
               if (!this.isRightClicking) {
                  this.isRightClicking = true;
                  this.currentSpeed = (double)(MinecraftUtils.getPlayer().capabilities.getWalkSpeed() * 1000.0F);
               }

               if (this.currentSpeed < (double)Configs.MaxSpeed) {
                  ControlUtils.rightClick();
                  this.currentSpeed += SkyblockUtils.isInDungeon() ? 3.33 : 10.0;
               }
            } else {
               this.isRightClicking = false;
            }

         }
      }
   }

   @SubscribeEvent
   public void onRenderTick(TickEvent.RenderTickEvent var1) {
      while(this.systemTimeTerm < Minecraft.getSystemTime() + (long)(1000 / Configs.TerminatorCPS)) {
         this.systemTimeTerm += (long)(1000 / Configs.TerminatorCPS);
         this.executeTerminator();
      }

      while(this.systemTimeRogue < Minecraft.getSystemTime() + (long)(1000 / Configs.RogueCPS)) {
         this.systemTimeRogue += (long)(1000 / Configs.RogueCPS);
         this.executeRogue();
      }

   }

   public void executeTerminator() {
      if (Checker.enabled) {
         if (Configs.TerminatorAutoRightClick) {
            if (useKeyBind.isKeyDown() && ControlUtils.checkHoldingItem((List)terminatorNames)) {
               ControlUtils.rightClick();
            }

         }
      }
   }

   static {
      useKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindUseItem);
   }
}
