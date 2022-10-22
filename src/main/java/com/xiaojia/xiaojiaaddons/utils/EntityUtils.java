package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityUtils {

   private static final ArrayList summonItemIDs = new ArrayList(Arrays.asList("HEAVY_HELMET", "ZOMBIE_KNIGHT_HELMET", "SKELETOR_HELMET", "SUPER_HEAVY_HELMET"));

   public static boolean isPlayer(EntityOtherPlayerMP var0) {
      UUID var1 = var0.getUniqueID();
      return (var1.version() == 3 || var1.version() == 4) && var0.getName().matches("[a-zA-Z0-9_]+");
   }

   public static String getHeadOwner(Entity var0) {
      if (!(var0 instanceof EntityArmorStand)) {
         return null;
      } else {
         ItemStack var1 = ((EntityArmorStand)var0).getEquipmentInSlot(4);
         if (var1 == null) {
            return null;
         } else {
            String var2 = "";

            try {
               var2 = var1.getTagCompound().getCompoundTag("SkullOwner").getString("Id");
            } catch (Exception var4) {
            }

            return var2;
         }
      }
   }

   public static List getEntities() {
      World var0 = MinecraftUtils.getWorld();
      if (var0 == null) {
         return new ArrayList();
      } else {
         ArrayList var1 = new ArrayList(var0.loadedEntityList);
         if (Configs.UnloadUnusedNPCEntity && SkyblockUtils.isInNether()) {
            var1.removeIf((var0x) -> {
               return MathUtils.equal((double)MathUtils.getX((Entity) var0x), 0.0) && MathUtils.equal((double)MathUtils.getY((Entity) var0x), 0.0) && MathUtils.equal((double)MathUtils.getZ((Entity) var0x), 0.0) && ((Entity)var0x).getName().matches("§(d|5)[A-Z][a-z]+ ");
            });
         }

         return var1;
      }
   }

   public static void tryRightClickEntity(Entity var0, double var1) {
      ControlUtils.face(MathUtils.getX(var0), MathUtils.getY(var0) + var0.height / 2.0F, MathUtils.getZ(var0));
      Vec3 var3 = MinecraftUtils.getPlayer().getPositionEyes(1.0F);
      double var4 = (double)XiaojiaAddons.mc.playerController.getBlockReachDistance();
      if (lIllIIIIlIl(var1, var4) > 0) {
         var4 = var1;
      }

      Vec3 var6 = MinecraftUtils.getPlayer().getLook(1.0F);
      Vec3 var7 = var3.addVector(var6.xCoord * var4, var6.yCoord * var4, var6.zCoord * var4);
      float var8 = var0.getCollisionBorderSize();
      AxisAlignedBB var9 = var0.getEntityBoundingBox().expand((double)var8, (double)var8, (double)var8);
      MovingObjectPosition var10 = var9.calculateIntercept(var3, var7);
      if (var10 == null) {
         ChatLib.chat("Failed to click!");
      } else {
         Vec3 var11 = new Vec3(var10.hitVec.xCoord - var0.posX, var10.hitVec.yCoord - var0.posY, var10.hitVec.zCoord - var0.posZ);
         XiaojiaAddons.mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(var0, var11));
      }
   }

   public static String getHeadTexture(Entity var0) {
      if (!(var0 instanceof EntityArmorStand)) {
         return null;
      } else {
         ItemStack var1 = ((EntityArmorStand)var0).getEquipmentInSlot(4);
         if (var1 == null) {
            return null;
         } else {
            try {
               NBTTagCompound var2 = var1.getTagCompound();
               if (var2 == null) {
                  return null;
               } else {
                  NBTTagCompound var3 = var2.getCompoundTag("SkullOwner");
                  if (var3 == null) {
                     return null;
                  } else {
                     NBTTagCompound var4 = var3.getCompoundTag("Properties");
                     if (var4 == null) {
                        return null;
                     } else {
                        NBTTagList var5 = var4.getTagList("textures", 10);
                        if (var5 != null && var5.tagCount() >= 1) {
                           NBTTagCompound var6 = (NBTTagCompound)var5.get(0);
                           return var6.getString("Value");
                        } else {
                           return null;
                        }
                     }
                  }
               }
            } catch (Exception var7) {
               return null;
            }
         }
      }
   }

   public static boolean isSummon(Entity var0) {
      if (var0 instanceof EntityPlayer) {
         return var0.getName().equals("Lost Adventurer");
      } else {
         if (var0 instanceof EntityZombie || var0 instanceof EntitySkeleton) {
            for(int var1 = 0; var1 < 5; ++var1) {
               ItemStack var2 = ((EntityMob)var0).getEquipmentInSlot(var1);
               if (summonItemIDs.contains(NBTUtils.getSkyBlockID(var2))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean isGoldenFish(Entity var0) {
      String var1 = getHeadTexture(var0);
      return var1 != null && var1.equals("ewogICJ0aW1lc3RhbXAiIDogMTY0MzgzMTA2MDE5OCwKICAicHJvZmlsZUlkIiA6ICJiN2ZkYmU2N2NkMDA0NjgzYjlmYTllM2UxNzczODI1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzEyMGNmM2MwYTQwZmM2N2UwZTVmZTBjNDZiMGFlNDA5YWM3MTAzMGE3NjU2ZGExN2IxMWVkMDAxNjQ1ODg4ZmUiCiAgICB9CiAgfQp9==");
   }

   public static boolean isEmptyArmorStand(Entity var0) {
      if (var0 instanceof EntityArmorStand) {
         EntityArmorStand var1 = (EntityArmorStand)var0;
         if (!var1.isInvisible()) {
            return false;
         } else if (var1.hasCustomName()) {
            return false;
         } else {
            for(int var2 = 0; var2 < 5; ++var2) {
               if (var1.getEquipmentInSlot(var2) != null) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private static int lIllIIIIlIl(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   public static boolean isPlayer(Entity var0) {
      return var0 instanceof EntityOtherPlayerMP ? isPlayer((EntityOtherPlayerMP)var0) : false;
   }
}
