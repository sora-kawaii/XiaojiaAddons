package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity {

   private static long lastShot = 0L;

   private static boolean enabled = true;

   private final KeyBind keyBind = new KeyBind("Velocity", 0);

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (this.keyBind.isPressed()) {
            enabled = !enabled;
            ChatLib.chat(enabled ? "Velocity &aactivated" : "Velocity &cdeactivated");
         }

      }
   }

   public static boolean canDisableKnockBack() {
      return TimeUtils.curTime() - lastShot > (long)Configs.VelocityCD && (!MinecraftUtils.getPlayer().isInLava() || !SkyblockUtils.isInDungeon()) && enabled && !wearingSpringBoots();
   }

   @SubscribeEvent
   public void onRightClick(PlayerInteractEvent var1) {
      if (var1.action == Action.RIGHT_CLICK_AIR || var1.action == Action.RIGHT_CLICK_BLOCK) {
         ItemStack var2 = ControlUtils.getHeldItemStack();
         String var3 = NBTUtils.getSkyBlockID(var2);
         if (var3.equals("BONZO_STAFF") || var3.equals("JERRY_STAFF") || var3.equals("STARRED_BONZO_STAFF") || var3.equals("GRAPPLING_HOOK")) {
            lastShot = TimeUtils.curTime();
         }
      }

   }

   public static void onPlayerNearby(Entity var0) {
      if (Configs.VelocityStop) {
         int var1 = Configs.VelocityStopRadius;
         double var2 = Math.sqrt(MathUtils.distanceSquareFromPlayer(var0));
         if (IllIll(var2, (double)var1) <= 0) {
            if (enabled) {
               enabled = false;
               MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
               ChatLib.chat(String.format("Found player nearby, stopped. Player: %s, distance: %.2f", var0.getName(), var2));
               ChatLib.chat("Velocity &cdeactivated");
            }

         }
      }
   }

   private static int IllIll(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   private static boolean wearingSpringBoots() {
      ItemStack var0 = ControlUtils.getBoots();
      String var1 = NBTUtils.getSkyBlockID(var0);
      return var1.equals("SPRING_BOOTS") || var1.equals("TARANTULA_BOOTS") || var1.equals("THORNS_BOOTS") || var1.equals("SPIDER_BOOTS");
   }
}
