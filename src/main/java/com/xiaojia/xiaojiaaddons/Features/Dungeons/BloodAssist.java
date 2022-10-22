package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BloodAssist {

   private static final HashSet skulls = new HashSet();

   private static final ArrayDeque newArmorStands = new ArrayDeque();

   private static final HashMap infos = new HashMap();

   private static Vector2i bloodTrunk = new Vector2i(-100000, -100000);

   public static StringBuilder log = new StringBuilder();

   private int spawnId = 0;

   @SubscribeEvent
   public void onEntityJoin(EntityJoinWorldEvent var1) {
      if (Checker.enabled) {
         if (Configs.BloodAssist) {
            if (var1.entity instanceof EntityArmorStand) {
               newArmorStands.offerLast((EntityArmorStand)var1.entity);
            }

         }
      }
   }

   public void addSkull(EntityArmorStand var1) {
      ItemStack var2 = var1.getEquipmentInSlot(4);
      if (var2 != null) {
         String var3 = ChatLib.removeFormatting(var2.getDisplayName()).trim();
         log.append("detected ").append(var3).append("\n");
         if (!bloodTrunk.equals(new Vector2i(-100000, -100000))) {
            if ((new Vector2i(((int)var1.posX + 8) / 16 / 2, ((int)var1.posZ + 8) / 16 / 2)).equals(bloodTrunk)) {
               skulls.add(var1);
               log.append("added ").append(var3).append(", ").append(var1.getUniqueID().toString()).append("\n");
            }
         } else if (var3.endsWith(MinecraftUtils.getPlayer().getName() + "'s Head") || var3.endsWith(MinecraftUtils.getPlayer().getName() + "' Head")) {
            bloodTrunk = new Vector2i(((int)var1.posX + 8) / 16 / 2, ((int)var1.posZ + 8) / 16 / 2);
            log.append("Head: ").append(var3).append(", trunk: ").append(bloodTrunk).append("\n");
            Iterator var4 = EntityUtils.getEntities().iterator();

            while(var4.hasNext()) {
               Entity var5 = (Entity)var4.next();
               if (var5 instanceof EntityArmorStand) {
                  var2 = ((EntityArmorStand)var5).getEquipmentInSlot(4);
                  if (var2 != null) {
                     var3 = ChatLib.removeFormatting(var2.getDisplayName()).trim();
                     if (var3.endsWith("Head")) {
                        this.addSkull((EntityArmorStand)var5);
                     }
                  }
               }
            }
         }

      }
   }

   public static void showBloodMobs() {
      System.err.println("-------------------------------------------------------");
      Iterator var0 = skulls.iterator();

      while(var0.hasNext()) {
         EntityArmorStand var1 = (EntityArmorStand)var0.next();
         System.err.println("skulls: " + var1.getUniqueID().toString());
         if (var1.getEquipmentInSlot(4) != null) {
            System.err.println(var1.getEquipmentInSlot(4).getDisplayName());
         }
      }

      var0 = EntityUtils.getEntities().iterator();

      while(var0.hasNext()) {
         Entity var2 = (Entity)var0.next();
         if (var2 instanceof EntityArmorStand) {
            System.err.println("entity: " + var2.getUniqueID().toString());
            if (((EntityArmorStand)var2).getEquipmentInSlot(4) != null) {
               System.err.println(((EntityArmorStand)var2).getEquipmentInSlot(4).getDisplayName());
            }

            System.err.println(String.format("pos: (%.2f %.2f %.2f)", var2.posX, var2.posY, var2.posZ));
         }
      }

      System.err.println("-------------------------------------------------------");
   }

   private static int IIllIlllIIl(float var0, float var1) {
      float var2;
      return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
   }

   public static void printLog() {
      System.err.println("BloodAssist Log:");
      System.err.println(log);
      showBloodMobs();
      System.err.println();
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (Configs.BloodAssist) {
            if (SkyblockUtils.isInDungeon()) {
               ArrayList var2 = new ArrayList();
               Iterator var3 = skulls.iterator();

               while(true) {
                  while(var3.hasNext()) {
                     EntityArmorStand var4 = (EntityArmorStand)var3.next();
                     float var5 = (float)(var4.posX - var4.lastTickPosX);
                     float var6 = (float)(var4.posY - var4.lastTickPosY);
                     float var7 = (float)(var4.posZ - var4.lastTickPosZ);
                     String var8 = var4.getUniqueID().toString();
                     BloodMobInfo var9 = (BloodMobInfo)infos.get(var8);
                     if (var9 != null && var9.startTime < TimeUtils.curTime() - 5000L) {
                        this.setDead(var8, var9, 1);
                        var2.add(var8);
                     } else if (var5 != 0.0F || var6 != 0.0F || var7 != 0.0F) {
                        log.append(String.format("%s speed: (%.2f %.2f %.2f)", var8, var5, var6, var7)).append("\n");
                        if (var9 == null) {
                           var9 = new BloodMobInfo(TimeUtils.curTime(), var4.posX, var4.posY, var4.posZ);
                           infos.put(var8, var9);
                           log.append("new info: " + var8 + ", " + var9).append("\n");
                        }

                        if (lllIllllIIl(var9.lastX, var4.posX) == 0 && lllIllllIIl(var9.lastY, var4.posY) == 0 && lllIllllIIl(var9.lastZ, var4.posZ) == 0) {
                           ItemStack var18 = var4.getEquipmentInSlot(4);
                           if (!var9.isDead && (var4.isDead || var18 == null || !var18.getDisplayName().endsWith("Head"))) {
                              this.setDead(var8, var9, 2);
                              var2.add(var8);
                           }
                        } else {
                           long var10 = TimeUtils.curTime() - var9.startTime;
                           var5 = (float)((var4.posX - var9.startX) / (double)var10);
                           var6 = (float)((var4.posY - var9.startY) / (double)var10);
                           var7 = (float)((var4.posZ - var9.startZ) / (double)var10);
                           long var12 = (long)(this.spawnId >= 4 ? 2950 : 4875) - var10;
                           int var14 = SkyblockUtils.getPing();
                           if (var12 <= (long)var14) {
                              var9.shouldAttack = true;
                           }

                           var9.endX = (float)var4.posX + (float)var12 * var5;
                           var9.endY = (float)(var4.posY + (double)((float)var12 * var6));
                           var9.endZ = (float)(var4.posZ + (double)((float)var12 * var7));
                        }

                        var9.lastX = var4.posX;
                        var9.lastY = var4.posY;
                        var9.lastZ = var4.posZ;
                     }
                  }

                  skulls.removeIf((var0_) -> {
                     EntityArmorStand var0 = (EntityArmorStand) var0_;
                     return var0.isDead || infos.get(var0.getUniqueID().toString()) != null && ((BloodMobInfo)infos.get(var0.getUniqueID().toString())).isDead;
                  });
                  HashMap var10001 = infos;
                  var2.forEach(var10001::remove);

                  while(!newArmorStands.isEmpty()) {
                     EntityArmorStand var15 = (EntityArmorStand)newArmorStands.pollFirst();
                     ItemStack var16 = var15.getEquipmentInSlot(4);
                     if (var16 != null) {
                        String var17 = ChatLib.removeFormatting(var16.getDisplayName()).trim();
                        if (var17.endsWith("Head")) {
                           this.addSkull(var15);
                        }
                     }
                  }

                  return;
               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onRenderWorld(RenderWorldLastEvent var1) {
      if (Checker.enabled) {
         if (Configs.BloodAssist) {
            if (SkyblockUtils.isInDungeon()) {
               if (Dungeon.bossEntry <= Dungeon.runStarted) {
                  Iterator var2 = skulls.iterator();

                  while(var2.hasNext()) {
                     EntityArmorStand var3 = (EntityArmorStand)var2.next();
                     String var4 = var3.getUniqueID().toString();
                     BloodMobInfo var5 = (BloodMobInfo)infos.get(var4);
                     if (var5 != null && IIllIlllIIl(var5.endX, -10000.0F) >= 0) {
                        Color var6 = new Color(0, 255, 0);
                        if (var5.shouldAttack) {
                           var6 = new Color(0, 0, 255);
                        }

                        GuiUtils.enableESP();
                        GuiUtils.drawBoundingBoxAtPos(var5.endX, var5.endY + 1.0F, var5.endZ, var6, 0.5F, 1.0F);
                        GuiUtils.disableESP();
                        GuiUtils.enableESP();
                        GuiUtils.drawLine((float)var3.posX, (float)var3.posY + 2.0F, (float)var3.posZ, var5.endX, var5.endY + 2.0F, var5.endZ, new Color(255, 0, 0), Configs.BoxLineThickness);
                        GuiUtils.disableESP();
                     }
                  }

               }
            }
         }
      }
   }

   private static int lllIllllIIl(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   private void setDead(String var1, BloodMobInfo var2, int var3) {
      var2.isDead = true;
      ++this.spawnId;
      log.append("set dead: ").append(var1).append(", ").append(var2).append(", from: ").append(var3).append("\n");
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      log = new StringBuilder();
      newArmorStands.clear();
      bloodTrunk = new Vector2i(-100000, -100000);
      if (MinecraftUtils.getWorld() != null) {
         this.spawnId = 0;
         infos.clear();
         skulls.clear();

         try {
            Iterator var2 = EntityUtils.getEntities().iterator();

            while(var2.hasNext()) {
               Entity var3 = (Entity)var2.next();
               if (var3 instanceof EntityArmorStand) {
                  newArmorStands.offerLast((EntityArmorStand)var3);
                  log.append("worldload, ").append(var3.getDisplayName()).append("\n");
               }
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }

      }
   }

   public static class BloodMobInfo {
   
      public double lastY = -100000.0;
   
      public double startY = -100000.0;
   
      public long startTime = -100000L;
   
      public boolean shouldAttack = false;
   
      public boolean isDead = false;
   
      public double lastX = -100000.0;
   
      public float endY = -100000.0F;
   
      public double startZ = -100000.0;
   
      public float endZ = -100000.0F;
   
      public float endX = -100000.0F;
   
      public double startX = -100000.0;
   
      public double lastZ = -100000.0;

      public BloodMobInfo(long var1, double var3, double var5, double var7) {
         this.startTime = var1;
         this.startX = var3;
         this.startY = var5;
         this.startZ = var7;
         this.isDead = false;
      }

      public String toString() {
         return String.format("%d (%.2f %.2f %.2f) (%.2f %.2f %.2f) ", this.startTime, this.startX, this.startY, this.startZ, this.lastX, this.lastY, this.lastZ) + this.isDead;
      }
   }
}
