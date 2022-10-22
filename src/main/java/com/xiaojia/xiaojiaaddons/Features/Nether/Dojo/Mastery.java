package com.xiaojia.xiaojiaaddons.Features.Nether.Dojo;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.awt.Color;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Mastery {

   private static long lastRelease = 0L;

   private static final ConcurrentHashMap officialCountDown = new ConcurrentHashMap();

   private static final ConcurrentHashMap countDown = new ConcurrentHashMap();

   private static final ConcurrentHashMap link = new ConcurrentHashMap();

   private static Color getColorFromDelta(int var0) {
      Color var1 = new Color(0, 0, 255);
      if (var0 >= 3500 && var0 <= 7000) {
         var1 = new Color((7000 - var0) * 255 / 3500, 255, 0);
      } else if (var0 >= 500 && var0 < 3500) {
         var1 = new Color(255, 255 - (3500 - var0) * 255 / 3000, 0);
      } else if (var0 < 500 && var0 > 0) {
         var1 = new Color(255, 0, 0);
      } else {
         ChatLib.debug(var0 + "");
      }

      return var1;
   }

   @SubscribeEvent
   public void onBlockChange(BlockChangeEvent var1) {
      if (Checker.enabled) {
         if (DojoUtils.getTask() == EnumDojoTask.MASTERY) {
            if (IIllIlll((float)var1.position.getX(), MathUtils.getX(MinecraftUtils.getPlayer()) - 20.0F) >= 0 && !((float)var1.position.getX() > MathUtils.getX(MinecraftUtils.getPlayer()) + 20.0F)) {
               if (IIllIlll((float)var1.position.getZ(), MathUtils.getZ(MinecraftUtils.getPlayer()) - 20.0F) >= 0 && !((float)var1.position.getZ() > MathUtils.getZ(MinecraftUtils.getPlayer()) + 20.0F)) {
                  if (IIllIlll((float)var1.position.getY(), MathUtils.getY(MinecraftUtils.getPlayer()) - 4.0F) >= 0 && !((float)var1.position.getY() > MathUtils.getY(MinecraftUtils.getPlayer()) + 10.0F)) {
                     if (var1.oldBlock.getBlock() != Blocks.wool && var1.newBlock.getBlock() == Blocks.wool) {
                        countDown.put(var1.position, TimeUtils.curTime());
                     } else if (var1.oldBlock.getBlock() == Blocks.wool && var1.newBlock.getBlock() != Blocks.wool) {
                        countDown.remove(var1.position);
                        officialCountDown.remove(var1.position);
                     }

                  }
               }
            }
         }
      }
   }

   private static int IIllIlll(float var0, float var1) {
      float var2;
      return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (Configs.MasteryAutoTurn) {
            BlockPos var2 = null;
            long var3 = 0L;
            long var5 = TimeUtils.curTime();
            Stream var10000;
            ConcurrentHashMap var10001;
            if (Configs.MasteryMode == 1) {
               var10000 = officialCountDown.keySet().stream();
               var10001 = officialCountDown;
               var10001.getClass();
               ConcurrentHashMap finalVar1000 = var10001;
               var2 = (BlockPos)var10000.min(Comparator.comparing((a) -> { return (int) finalVar1000.get(a); })).orElse((Object)null);
               if (var2 != null) {
                  var3 = (long)(Integer)officialCountDown.get(var2);
               }
            }

            if (var2 == null) {
               var10000 = countDown.keySet().stream();
               var10001 = countDown;
               var10001.getClass();
               ConcurrentHashMap finalVar10001 = var10001;
               var2 = (BlockPos)var10000.min(Comparator.comparing((a) -> { return (int) finalVar10001.get(a); })).orElse((Object)null);
               if (var2 != null) {
                  var3 = 7000L - (var5 - (Long)countDown.get(var2));
               }
            }

            if (var2 != null) {
               ControlUtils.face((double)var2.getX() + 0.5, (double)var2.getY() + 1.4, (double)var2.getZ() + 0.5);
               if (Configs.MasteryAutoRelease) {
                  if (var3 < (long)Configs.MasteryAutoReleaseCD && var5 - lastRelease > 900L) {
                     lastRelease = var5;
                     (new Thread(() -> {
                        try {
                           ControlUtils.releaseRightClick();
                           Thread.sleep(40L);
                           ControlUtils.holdRightClick();
                        } catch (Exception ignored) {
                        }

                     })).start();
                  }

               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent var1) {
      if (Checker.enabled) {
         if (Configs.MasteryHelper) {
            if (DojoUtils.getTask() == EnumDojoTask.MASTERY) {
               long var2 = TimeUtils.curTime();
               Iterator var4 = countDown.keySet().iterator();

               int var6;
               while(var4.hasNext()) {
                  BlockPos var5 = (BlockPos)var4.next();
                  var6 = (int)(7000L - (var2 - (Long)countDown.get(var5)));
                  Color var7 = getColorFromDelta(var6);
                  GuiUtils.drawString(String.format("%.2f s", (float)var6 / 1000.0F), (float)var5.getX() + 0.5F, (float)var5.getY() + 0.7F, (float)var5.getZ() + 0.5F, var7.getRGB(), 1.5F, true);
                  var6 = (Integer)officialCountDown.getOrDefault(var5, -1);
                  if (var6 != -1) {
                     var7 = getColorFromDelta(var6);
                     GuiUtils.drawString(String.format("%.2f s", (float)var6 / 1000.0F), (float)var5.getX() + 0.5F, (float)var5.getY() + 1.7F, (float)var5.getZ() + 0.5F, var7.getRGB(), 1.5F, true);
                  }
               }

               GuiUtils.enableESP();
               var4 = EntityUtils.getEntities().iterator();

               while(var4.hasNext()) {
                  Entity var11 = (Entity)var4.next();
                  var6 = var11.getEntityId();
                  if (link.containsKey(var6)) {
                     BlockPos var12 = (BlockPos)link.get(var6);
                     float var8 = (float)Math.sqrt(MathUtils.distanceSquareFromPlayer(var11));
                     float var9 = (float)((var11.posX - (double)MathUtils.getX(MinecraftUtils.getPlayer())) / (double)var8);
                     float var10 = (float)((var11.posZ - (double)MathUtils.getZ(MinecraftUtils.getPlayer())) / (double)var8);
                     GuiUtils.drawLine((float)var11.posX + var9, (float)var11.posY + 3.0F, (float)var11.posZ + var10, (float)var12.getX() + 0.5F, (float)var12.getY() + 0.5F, (float)var12.getZ() + 0.5F, new Color(255, 0, 0), 2);
                  }
               }

               GuiUtils.disableESP();
            }
         }
      }
   }

   public static void clear() {
      officialCountDown.clear();
      countDown.clear();
      link.clear();
   }

   public static void printLog() {
      System.err.println("Player at: " + MathUtils.getPosString((Entity)MinecraftUtils.getPlayer()));
      System.err.println("Mastery count down set:");
      Iterator var0 = countDown.keySet().iterator();

      while(var0.hasNext()) {
         BlockPos var1 = (BlockPos)var0.next();
         System.err.println(var1);
      }

      System.err.println("Mastery entities:");
      var0 = EntityUtils.getEntities().iterator();

      while(var0.hasNext()) {
         Entity var8 = (Entity)var0.next();
         String var2 = var8.getName();
         Pattern var3 = Pattern.compile("^§(\\w)§l([\\d:]+)$");
         Matcher var4 = var3.matcher(var2);
         if (var4.find()) {
            String var5 = var4.group(1);
            double var6 = Double.parseDouble(var4.group(2).replaceAll(":", "."));
            System.err.println("Entity " + var5 + " " + var6 + " at " + MathUtils.getPosString(var8));
         }
      }

   }

   @SubscribeEvent
   public void onTickCheck(TickEndEvent var1) {
      if (Checker.enabled) {
         if (Configs.MasteryHelper) {
            if (DojoUtils.getTask() == EnumDojoTask.MASTERY) {
               List var2 = EntityUtils.getEntities();
               HashMap var3 = new HashMap();
               Iterator var4 = var2.iterator();

               while(var4.hasNext()) {
                  Entity var5 = (Entity)var4.next();
                  String var6 = var5.getName();
                  Pattern var7 = Pattern.compile("^§(\\w)§l([\\d:]+)$");
                  Matcher var8 = var7.matcher(var6);
                  if (var8.find()) {
                     String var9 = var8.group(1);
                     double var10 = Double.parseDouble(var8.group(2).replaceAll(":", "."));
                     switch (var9) {
                        case "c":
                           break;
                        case "e":
                           var10 += 0.5;
                           break;
                        case "a":
                           var10 += 3.5;
                           break;
                        default:
                           ChatLib.debug(var9 + "");
                     }

                     int var12 = (int)(var10 * 1000.0);
                     BlockPos var14 = (BlockPos)countDown.keySet().stream().min(Comparator.comparing((var1x) -> {
                        return getDis(var5, (BlockPos) var1x);
                     })).orElse((Object)null);
                     if (var14 != null) {
                        officialCountDown.put(var14, var12);
                        var3.put(var5.getEntityId(), var14);
                     }
                  }
               }

               link.clear();
               link.putAll(var3);
            }
         }
      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      clear();
   }

   public static double getDis(Entity var0, BlockPos var1) {
      double var2 = Math.sqrt(MathUtils.distanceSquareFromPlayer(var0));
      double var4 = (var0.posX - (double)MathUtils.getX(MinecraftUtils.getPlayer())) / var2;
      double var6 = (var0.posZ - (double)MathUtils.getZ(MinecraftUtils.getPlayer())) / var2;
      return var0.getDistanceSq((double)((float)var1.getX() + 0.5F) - var4, (double)(var1.getY() + 3), (double)((float)var1.getZ() + 0.5F) - var6);
   }

   public static void onEnter() {
      if (Checker.enabled) {
         if (Configs.MasteryHelper) {
            ControlUtils.setHeldItemIndex(0);
            (new Thread(() -> {
               try {
                  Thread.sleep(4000L);
                  ControlUtils.holdRightClick();
               } catch (Exception var1) {
                  var1.printStackTrace();
               }

            })).start();
         }
      }
   }
}
