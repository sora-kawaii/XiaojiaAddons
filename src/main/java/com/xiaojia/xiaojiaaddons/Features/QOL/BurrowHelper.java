package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BurrowHelper {

   private static long lastUpdate = 0L;

   private int particleCount = 0;

   private Vec3 pos2 = null;

   private Vec3 vec1 = null;

   private Vec3 pos1 = null;

   private boolean awaiting = false;

   private BlockPos solution;

   private long lastItem = -1L;

   private Vec3 vec2 = null;

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent var1) {
      if (Checker.enabled) {
         if (this.solution != null) {
            GuiUtils.enableESP();
            GuiUtils.renderBeaconBeam(this.solution, 16516076, 0.5F);
            GuiUtils.drawBoxAtBlock(this.solution.down(), new Color(-2130967572, true), 1, 1, 0.002F);
            GuiUtils.disableESP();
         }
      }
   }

   @SubscribeEvent
   public void onTickUpdate(TickEndEvent var1) {
      if (Checker.enabled) {
         if (Configs.BurrowHelper) {
            long var2 = TimeUtils.curTime();
            if (this.solution != null && this.solution.getY() < 5 && var2 - lastUpdate > 1000L) {
               lastUpdate = var2;
               int var4 = this.solution.getX();
               int var5 = this.solution.getZ();
               this.solution = new BlockPos((double)var4, calcHighestGrass((double)var4, (double)var5), (double)var5);
            }

         }
      }
   }

   private void calculateIntercept() {
      double var1 = this.pos1.xCoord;
      double var3 = this.pos1.zCoord;
      double var5 = this.vec1.xCoord;
      double var7 = this.vec1.zCoord;
      double var9 = this.pos2.xCoord;
      double var11 = this.pos2.zCoord;
      double var13 = this.vec2.xCoord;
      double var15 = this.vec2.zCoord;
      double var17 = var7 / var5 * var1 - var3;
      double var19 = var15 / var13 * var9 - var11;
      double var21 = (var17 - var19) / (var7 / var5 - var15 / var13);
      double var23 = var7 / var5 * var21 - var17;
      this.solution = new BlockPos(var21, calcHighestGrass(var21, var23), var23);
      ChatLib.chat("Solution: (" + this.solution.getX() + ", " + this.solution.getZ() + ")");
      this.pos1 = this.pos2 = this.vec1 = this.vec2 = null;
   }

   private static int IIllllIIIl(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   @SubscribeEvent
   public void onChatMessage(ClientChatReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.BurrowHelper) {
            if (var1.type == 0) {
               String var2 = EnumChatFormatting.getTextWithoutFormattingCodes(var1.message.getUnformattedText());
               if (var2.equals("You finished the Griffin burrow chain! (4/4)")) {
                  this.clear();
               } else if (var2.startsWith("You dug out a Griffin Burrow!")) {
                  this.clear();
               }

            }
         }
      }
   }

   @SubscribeEvent
   public void onPacketReceived(PacketReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.BurrowHelper) {
            if (var1.packet instanceof S2APacketParticles) {
               S2APacketParticles var2 = (S2APacketParticles)var1.packet;
               if (var2.getParticleType() == EnumParticleTypes.FIREWORKS_SPARK && MathUtils.equal((double)var2.getXOffset(), 0.0) && MathUtils.equal((double)var2.getYOffset(), 0.0) && MathUtils.equal((double)var2.getZOffset(), 0.0)) {
                  ++this.particleCount;
                  if (this.awaiting) {
                     if (this.particleCount == 10 && this.pos1 == null) {
                        this.pos1 = new Vec3(var2.getXCoordinate(), var2.getYCoordinate(), var2.getZCoordinate());
                        this.awaiting = false;
                     } else if (this.particleCount == 10 && this.pos2 == null) {
                        this.pos2 = new Vec3(var2.getXCoordinate(), var2.getYCoordinate(), var2.getZCoordinate());
                        this.awaiting = false;
                     }
                  } else if (this.vec1 == null && this.pos1 != null) {
                     this.vec1 = (new Vec3(var2.getXCoordinate() - this.pos1.xCoord, var2.getYCoordinate() - this.pos1.yCoord, var2.getZCoordinate() - this.pos1.zCoord)).normalize();
                  } else if (this.vec2 == null && this.pos2 != null) {
                     this.vec2 = (new Vec3(var2.getXCoordinate() - this.pos2.xCoord, var2.getYCoordinate() - this.pos2.yCoord, var2.getZCoordinate() - this.pos2.zCoord)).normalize();
                     this.calculateIntercept();
                  }
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void onRightClick(PlayerInteractEvent var1) {
      if (Checker.enabled) {
         if (Configs.BurrowHelper) {
            if (var1.action == Action.RIGHT_CLICK_AIR || var1.action == Action.RIGHT_CLICK_BLOCK) {
               ItemStack var2 = Minecraft.getMinecraft().thePlayer.getHeldItem();
               if (var2 == null) {
                  return;
               }

               if (StringUtils.stripControlCodes(var2.getDisplayName()).contains("Ancestral Spade")) {
                  if (Configs.BlockAncestralSpade && System.currentTimeMillis() - this.lastItem < 4000L) {
                     ChatLib.chat("Last trail hasn't disappeared yet, chill.");
                     var1.setCanceled(true);
                     return;
                  }

                  double var3 = MathUtils.validPitch((double)MathUtils.getPitch());
                  if (Configs.BlockInvalidClicks && !MathUtils.equal(var3, 90.0) && !MathUtils.equal(var3, -90.0)) {
                     ChatLib.chat("Look straight up or down!");
                     var1.setCanceled(true);
                     return;
                  }

                  this.awaiting = true;
                  this.particleCount = 0;
                  this.lastItem = System.currentTimeMillis();
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      this.clear();
   }

   public void clear() {
      this.pos1 = null;
      this.pos2 = null;
      this.vec1 = null;
      this.vec2 = null;
      this.awaiting = false;
      this.solution = null;
      this.lastItem = -1L;
      this.particleCount = 0;
   }

   private static double calcHighestGrass(double var0, double var2) {
      double var4;
      for(var4 = 255.0; IIllllIIIl(var4, 0.0) > 0; --var4) {
         BlockPos var6 = new BlockPos(var0, var4, var2);
         if (BlockUtils.getBlockAt(var6) == Blocks.grass || BlockUtils.getBlockAt(var6) == Blocks.dirt) {
            break;
         }
      }

      return var4 + 1.0;
   }
}
