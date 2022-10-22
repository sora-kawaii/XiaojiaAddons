package com.xiaojia.xiaojiaaddons.utils;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MathUtils {

   public static float partialTicks = 0.0F;

   public static boolean equal(Vector3d var0, Vector3d var1) {
      double var2 = distanceSquaredFromPoints(var0.x, var0.y, var0.z, var1.x, var1.y, var1.z);
      return var2 < 1.0E-5;
   }

   public static int floor(double var0) {
      return (int)Math.floor(var0);
   }

   public static double distanceSquaredFromPoints(Vector3d var0, Vector3d var1) {
      return distanceSquaredFromPoints(var0.x, var0.y, var0.z, var1.x, var1.y, var1.z);
   }

   public static float getZ(Entity var0) {
      return var0 == null ? -10000.0F : (float)(var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * (double)partialTicks);
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onTick(RenderWorldLastEvent var1) {
      partialTicks = var1.partialTicks;
   }

   public static double distanceSquaredFromPoints(double var0, double var2, double var4, double var6, double var8, double var10) {
      return (var6 - var0) * (var6 - var0) + (var8 - var2) * (var8 - var2) + (var10 - var4) * (var10 - var4);
   }

   private static int llIIIIIllll(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   public static int floor(float var0) {
      return (int)Math.floor((double)var0);
   }

   private static int IIIllllIlll(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   public static int getBlockX(Entity var0) {
      return floor(getX(var0));
   }

   public static float getEyeHeight(Entity var0) {
      return var0 == null ? 0.0F : var0.getEyeHeight();
   }

   public static Vector3d diff(Vector3d var0, Vector3d var1) {
      return new Vector3d(var1.x - var0.x, var1.y - var0.y, var1.z - var0.z);
   }

   public static double validYaw(double var0) {
      if (lIIIIIIllll(var0, 180.0) >= 0) {
         var0 -= 360.0;
      }

      if (var0 <= -180.0) {
         var0 += 360.0;
      }

      return var0;
   }

   private static boolean isBlockMinableOrAir(float var0, float var1, float var2) {
      Block var3 = MinecraftUtils.getWorld().getBlockState(new BlockPos((double)var0, (double)var1, (double)var2)).getBlock();
      boolean var4 = var3 == Blocks.air || var3 == Blocks.stone || var3 == Blocks.coal_ore || var3 == Blocks.diamond_ore || var3 == Blocks.emerald_ore || var3 == Blocks.gold_ore || var3 == Blocks.iron_ore || var3 == Blocks.lapis_ore || var3 == Blocks.redstone_ore;
      return var4;
   }

   public static String getPosString(Entity var0) {
      return String.format("(%.2f %.2f %.2f)", getX(var0), getY(var0), getZ(var0));
   }

   public static Vector3d mul(double var0, Vector3d var2) {
      return new Vector3d(var2.x * var0, var2.y * var0, var2.z * var0);
   }

   private static int lIIlIIIllll(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   public static boolean equal(double var0, double var2) {
      return Math.abs(var0 - var2) < 1.0E-5;
   }

   private static double cross(Vector2d var0, Vector2d var1) {
      return var0.x * var1.y - var0.y * var1.x;
   }

   public static boolean sameYaw(double var0, double var2) {
      var0 = validYaw(var0);
      var2 = validYaw(var2);
      return Math.abs(var0 - var2) < 1.0E-4 || IllIIIIllll(Math.abs(var0 - var2), 359.9999) > 0;
   }

   public static boolean samePitch(double var0, double var2) {
      var0 = validPitch(var0);
      var2 = validPitch(var2);
      return Math.abs(var0 - var2) < 1.0E-4;
   }

   private static int lIlllllIlll(float var0, float var1) {
      float var2;
      return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
   }

   public static double validPitch(double var0) {
      if (llIIIIIllll(var0, 90.0) > 0) {
         var0 = 180.0 - var0;
      }

      if (var0 < -90.0) {
         var0 = -180.0 - var0;
      }

      return var0;
   }

   public static String getPosString(Vec3 var0) {
      return String.format("(%.2f %.2f %.2f)", var0.xCoord, var0.yCoord, var0.zCoord);
   }

   public static Vec3 getCurrentLook(float var0, float var1) {
      float var2 = MathHelper.cos(-var1 * 0.017453292F - 3.1415927F);
      float var3 = MathHelper.sin(-var1 * 0.017453292F - 3.1415927F);
      float var4 = -MathHelper.cos(-var0 * 0.017453292F);
      float var5 = MathHelper.sin(-var0 * 0.017453292F);
      return new Vec3((double)(var3 * var4), (double)var5, (double)(var2 * var4));
   }

   public static float getYaw() {
      if (MinecraftUtils.getPlayer() == null) {
         return 0.0F;
      } else {
         float var0;
         for(var0 = MinecraftUtils.getPlayer().rotationYaw; lIlllllIlll(var0, -180.0F) < 0; var0 += 360.0F) {
         }

         while(var0 >= 180.0F) {
            var0 -= 360.0F;
         }

         return var0;
      }
   }

   private static int lIIIIIIllll(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   public static double distanceSquareFromPlayer(BlockPos var0) {
      double var1 = (double)var0.getX();
      double var3 = (double)var0.getY();
      double var5 = (double)var0.getZ();
      return distanceSquareFromPlayer(var1, var3, var5);
   }

   public static int ceil(float var0) {
      return (int)Math.ceil((double)var0);
   }

   public static int getBlockZ(Entity var0) {
      return floor(getZ(var0));
   }

   public static int ceil(double var0) {
      return (int)Math.ceil(var0);
   }

   public static double distanceSquareFromPlayer(double var0, double var2, double var4) {
      double var6 = (double)getX(MinecraftUtils.getPlayer());
      double var8 = (double)(getY(MinecraftUtils.getPlayer()) + getEyeHeight(MinecraftUtils.getPlayer()));
      double var10 = (double)getZ(MinecraftUtils.getPlayer());
      return distanceSquaredFromPoints(var0, var2, var4, var6, var8, var10);
   }

   public static float getPitch() {
      return MinecraftUtils.getPlayer() == null ? 0.0F : MinecraftUtils.getPlayer().rotationPitch;
   }

   public static float getX(Entity var0) {
      return var0 == null ? -10000.0F : (float)(var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * (double)partialTicks);
   }

   public static double distanceSquareFromPlayer(Vector3d var0) {
      return distanceSquareFromPlayer(var0.x, var0.y, var0.z);
   }

   public static double distanceSquareFromPlayer(Entity var0) {
      double var1 = (double)getX(var0);
      double var3 = (double)getY(var0);
      double var5 = (double)getZ(var0);
      return distanceSquareFromPlayer(var1, var3, var5);
   }

   private static int IllIIIIllll(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   public static double distanceToLine(double var0, double var2, double var4) {
      if (lIIlIIIllll(var4, 0.0) == 0) {
         return Math.abs(var0);
      } else {
         Vector2d var6 = new Vector2d(1.0 / Math.tan(var4), 1.0);
         Vector2d var7 = new Vector2d(var0, var2);
         return cross(var6, var7) / var6.length();
      }
   }

   public static double yawPitchSquareFromPlayer(float var0, float var1, float var2) {
      float var3 = getYaw();
      float var4 = getPitch();
      Tuple var5 = ControlUtils.getFaceYawAndPitch(var0, var1, var2);
      float var6 = (Float)var5.getFirst();
      float var7 = (Float)var5.getSecond();
      float var8 = Math.abs(var3 - var6);
      float var9 = Math.abs(var4 - var7);
      var8 = Math.min(var8, 360.0F - var8);
      return (double)(var8 * var8 + var9 * var9);
   }

   public static boolean differentPosition(float var0, float var1, float var2) {
      return IIIllllIlll((double)Math.abs(getX(MinecraftUtils.getPlayer()) - var0), 0.01) > 0 || IIIllllIlll((double)Math.abs(getY(MinecraftUtils.getPlayer()) - var1), 0.01) > 0 || IIIllllIlll((double)Math.abs(getZ(MinecraftUtils.getPlayer()) - var2), 0.01) > 0;
   }

   public static boolean checkBlocksBetween(int var0, int var1, int var2) {
      float var3 = getX(MinecraftUtils.getPlayer());
      float var4 = getY(MinecraftUtils.getPlayer()) + 1.5F;
      float var5 = getZ(MinecraftUtils.getPlayer());
      float var6 = (float)var0 + 0.5F;
      float var7 = (float)var1 + 0.5F;
      float var8 = (float)var2 + 0.5F;
      float var9 = var6 - var3;
      float var10 = var7 - var4;
      float var11 = var8 - var5;
      byte var12 = 20;

      for(int var13 = 0; var13 < var12; ++var13) {
         float var14 = var3 + var9 * (float)var13 / (float)var12;
         float var15 = var4 + var10 * (float)var13 / (float)var12;
         float var16 = var5 + var11 * (float)var13 / (float)var12;
         if (!isBlockMinableOrAir(var14, var15, var16)) {
            return false;
         }
      }

      return true;
   }

   public static BlockPos getBlockPos() {
      if (MinecraftUtils.getPlayer() == null) {
         return null;
      } else {
         EntityPlayerSP var0 = MinecraftUtils.getPlayer();
         return new BlockPos(var0.posX, var0.posY, var0.posZ);
      }
   }

   public static boolean isBetween(double var0, double var2, double var4) {
      return (var0 - var2) * (var0 - var4) <= 0.0;
   }

   public static boolean isBetween(int var0, int var1, int var2) {
      return (var0 - var1) * (var0 - var2) <= 0;
   }

   public static Vector3d add(Vector3d var0, Vector3d var1) {
      return new Vector3d(var0.x + var1.x, var0.y + var1.y, var0.z + var1.z);
   }

   public static int getBlockY(Entity var0) {
      return floor(getY(var0));
   }

   public static float getY(Entity var0) {
      return var0 == null ? -10000.0F : (float)(var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * (double)partialTicks);
   }
}
