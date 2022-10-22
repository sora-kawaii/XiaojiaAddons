package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DevMode {

   public static final ArrayList lines = new ArrayList();

   private static String getLog(Entity var0) {
      String var1 = getClassLog(var0) + " ";
      var1 = var1 + var0.hasCustomName() + " ";
      if (var0.hasCustomName()) {
         var1 = var1 + var0.getName() + " ";
      }

      var1 = var1 + MathUtils.getPosString(var0);
      return var1;
   }

   @SubscribeEvent
   public void onReceive(PacketReceivedEvent var1) {
      if (Checker.enabled) {
         if (SessionUtils.isDev()) {
            if (Configs.ParticleSpawnMessage) {
               if (var1.packet instanceof S2APacketParticles) {
                  ChatLib.debug(getLog((S2APacketParticles)var1.packet));
               }

            }
         }
      }
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (SessionUtils.isDev()) {
            synchronized(lines) {
               lines.clear();
               MovingObjectPosition var3 = XiaojiaAddons.mc.objectMouseOver;
               if (var3 != null) {
                  Entity var4 = var3.entityHit;
                  BlockPos var5 = var3.getBlockPos();
                  DisplayLine var6;
                  if (var4 != null) {
                     var6 = new DisplayLine("Entity: " + getClassLog(var4));
                     var6.setScale((float)Configs.DisplayScale / 20.0F);
                     lines.add(var6);
                  }

                  if (var5 != null) {
                     var6 = new DisplayLine("Block: " + BlockUtils.getBlockInfo(var5));
                     var6.setScale((float)Configs.DisplayScale / 20.0F);
                     lines.add(var6);
                  }

               }
            }
         }
      }
   }

   private static String getLog(S2APacketParticles var0) {
      StringBuilder var1 = new StringBuilder();
      int[] var2 = var0.getParticleArgs();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         var1.append(" ").append(var5);
      }

      return var0.getParticleType() + String.format(" at %.2f %.2f %.2f, %.2f %.2f %.2f, %.2f, %d %b", var0.getXCoordinate(), var0.getYCoordinate(), var0.getZCoordinate(), var0.getXOffset(), var0.getYOffset(), var0.getZOffset(), var0.getParticleSpeed(), var0.getParticleCount(), var0.isLongDistance()) + var1;
   }

   @SubscribeEvent
   public void onEntitySpawn(EntityJoinWorldEvent var1) {
      if (Checker.enabled) {
         if (SessionUtils.isDev()) {
            if (Configs.EntityJoinEvent) {
               ChatLib.debug(getLog(var1.entity));
            }
         }
      }
   }

   @SubscribeEvent
   public void onPacketSent(PacketSendEvent var1) {
      if (Checker.enabled) {
         if (SessionUtils.isDev()) {
            if (Configs.PacketSent) {
               ChatLib.debug(var1.packet.getClass().toString());
            }
         }
      }
   }

   private static String getClassLog(Entity var0) {
      String var1 = "";
      if (var0 instanceof EntityArmorStand) {
         String var2 = var0.getDisplayName().getFormattedText();
         ItemStack var3 = ((EntityArmorStand)var0).getEquipmentInSlot(4);
         if (var3 != null) {
            var1 = String.format("AS(%s, %s)", var2, var3.getDisplayName());
         } else {
            var1 = String.format("AS(%s)", var2);
         }
      } else {
         ItemStack var4;
         if (var0 instanceof EntityItem) {
            var4 = ((EntityItem)var0).getEntityItem();
            var1 = String.format("EI(%s)", var4.getDisplayName());
         } else if (var0 instanceof EntityItemFrame) {
            var4 = ((EntityItemFrame)var0).getDisplayedItem();
            if (var4 != null) {
               var1 = String.format("EIF(%s)", var4.getDisplayName());
            }
         } else {
            var1 = var0.getClass().getSimpleName() + "(" + var0.getName() + ")";
         }
      }

      return var1;
   }
}
