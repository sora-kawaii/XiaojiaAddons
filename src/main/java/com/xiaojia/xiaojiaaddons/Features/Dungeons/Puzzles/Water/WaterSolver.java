package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoPuzzle;
import com.xiaojia.xiaojiaaddons.Features.Remote.ClientSocket;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.vecmath.Vector3d;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WaterSolver {

   private static EnumState[][] board = new EnumState[24][21];

   private static EnumFacing facing = null;

   private static boolean tpPacketReceived = false;

   private final KeyBind keyBind;

   private static boolean should = false;

   public static Room room = null;

   private static Thread solveThread = null;

   private static EnumState[][] originBoard = new EnumState[24][21];

   private static int lastFlag;

   private static void upload(String var0) {
      (new Thread(() -> {
         String var1 = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"water\": \"%s\", \"type\": \"%d\"}", SessionUtils.getUUID(), SessionUtils.getName(), var0, 11);
         ClientSocket.chat(var1);
      })).start();
   }

   public static void printLog() {
      System.err.println("WaterSolver Log:");
      System.err.println(WaterUtils.boardString);
      System.err.println();
   }

   @SubscribeEvent
   public void onPacketReceived(PacketReceivedEvent var1) {
      if (var1.packet instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook var2 = (S08PacketPlayerPosLook)var1.packet;
         tpPacketReceived = true;
      }

   }

   public static void calc(int var0, Patterns.Operation var1) {
      ChatLib.chat("Calculating possible solutions...");
      long var2 = TimeUtils.curTime();
      if (var1 != null && var1.time < 150) {
         WaterUtils.operations = var1.operations;
         ChatLib.chat(String.format("Estimate best solution: %.2fs (From Cache)", (double)var1.time * 0.25));
      } else {
         if (lastFlag != var0) {
            if (WaterUtils.raw) {
               ChatLib.chat("This is a new pattern! Sent to server for calculation.");
               upload(WaterUtils.boardString);
            } else {
               ChatLib.chat("Levers are flipped, so calculating without cache.");
            }

            WaterUtils.operations = new TreeMap();
            WaterUtils.bestTime = 120;
            WaterUtils.dfs(board, -15, new HashMap(), var0, false);
            if (WaterUtils.bestTime == 120) {
               WaterUtils.dfs(board, -15, new HashMap(), var0, true);
            }

            if (WaterUtils.bestTime != 120) {
               System.err.println(Patterns.getPatternString(originBoard, var0, WaterUtils.bestTime, WaterUtils.operations));
            }

            lastFlag = var0;
         }

         ChatLib.chat(String.format("Estimate best solution: %.2fs (%.2fs to calc)", (double)WaterUtils.bestTime * 0.25, (float)(TimeUtils.curTime() - var2) / 1000.0F));
      }

      Iterator var4 = WaterUtils.operations.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         if (!((EnumOperation)var5.getValue()).equals(EnumOperation.empty) && !((EnumOperation)var5.getValue()).equals(EnumOperation.trig)) {
            ChatLib.chat("  " + (double)(Integer)var5.getKey() * 0.25 + "s: " + getMessageFromOperation((EnumOperation)var5.getValue()));
         }
      }

   }

   public static String getMessageFromOperation(EnumOperation var0) {
      if (var0 == EnumOperation.c) {
         return "&0Coal";
      } else if (var0 == EnumOperation.cl) {
         return "&4Clay";
      } else if (var0 == EnumOperation.e) {
         return "&aEmerald";
      } else if (var0 == EnumOperation.d) {
         return "&bDiamond";
      } else if (var0 == EnumOperation.q) {
         return "&fQuartz";
      } else if (var0 == EnumOperation.g) {
         return "&6Gold";
      } else {
         return var0 == EnumOperation.trig ? "Trig" : "";
      }
   }

   public static void setRoom(Room var0) {
      if (Checker.enabled) {
         if (Configs.WaterSolver) {
            room = var0;
            if (room != null) {
               ChatLib.chat("set room: " + room.x + ", " + room.z);
               solveThread = new Thread(() -> {
                  try {
                     solve();
                  } catch (Exception var1) {
                  }

               });
               solveThread.start();
            }

         }
      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      setRoom((Room)null);
      facing = null;
      board = new EnumState[24][21];
      lastFlag = -1;
   }

   public WaterSolver() {
      this.keyBind = AutoPuzzle.keyBind;
   }

   @SubscribeEvent
   public void onTickAuto(TickEndEvent var1) {
      if (Checker.enabled && Configs.WaterSolver && Dungeon.currentRoom.equals("Water Board")) {
         if (this.keyBind.isPressed()) {
            should = !should;
            if (should) {
               ChatLib.chat("Auto Water &aactivated");
            } else {
               ChatLib.chat("Auto Water &cdeactivated");
            }
         }

         if (should && Dungeon.isFullyScanned && room != null) {
            if (solveThread == null || !solveThread.isAlive()) {
               solveThread = new Thread(() -> {
                  try {
                     int aotvSlot = HotbarUtils.aotvSlot;
                     if (aotvSlot == -1) {
                        ChatLib.chat("Requires aotv in hotbar.");
                        throw new Exception();
                     }

                     ControlUtils.setHeldItemIndex(aotvSlot);
                     long var2 = TimeUtils.curTime();
                     int var4 = 0;
                     Vector3d var5 = null;
                     Iterator var6 = WaterUtils.operations.entrySet().iterator();

                     while(var6.hasNext()) {
                        Map.Entry var7 = (Map.Entry)var6.next();
                        if (!((EnumOperation)var7.getValue()).equals(EnumOperation.empty)) {
                           int var8 = ((Integer)var7.getKey() - var4) * 250;
                           var4 = (Integer)var7.getKey();
                           EnumOperation var9 = (EnumOperation)var7.getValue();
                           Vector3d var10 = WaterUtils.getEtherwarpPointFor(var9);
                           if (!var10.equals(var5)) {
                              etherWarpTo(var10);
                              var5 = var10;
                           }

                           ControlUtils.faceSlowly(WaterUtils.getPosFor(var9));
                           long var11 = TimeUtils.curTime() - var2;
                           if (var11 < (long)var8) {
                              Thread.sleep((long)var8 - var11);
                           }

                           if (BlockUtils.getBlockAt(BlockUtils.getLookingAtPos(5)) != Blocks.lever) {
                              throw new Exception("Not looking at levers");
                           }

                           ControlUtils.rightClick();
                           Thread.sleep(200L);
                           var2 = TimeUtils.curTime();
                        }
                     }

                     this.deactivate();
                  } catch (Exception var13) {
                     this.deactivate();
                     var13.printStackTrace();
                  }

               });
               solveThread.start();
            }

         } else {
            this.deactivate();
         }
      } else {
         this.deactivate();
      }
   }

   public static void reset() {
      board = WaterUtils.getBoard(room, facing);
      WaterUtils.processBoard(board);
   }

   public static void solve() throws InterruptedException {
      facing = WaterUtils.getFacing(room);
      if (facing != null) {
         System.err.println("facing: " + facing + ", x " + room.x + ", z " + room.z);
         board = WaterUtils.getBoard(room, facing);
         originBoard = new EnumState[24][21];

         int var0;
         for(var0 = 0; var0 < 24; ++var0) {
            for(int var1 = 0; var1 < 21; ++var1) {
               originBoard[var0][var1] = board[var0][var1];
            }
         }

         WaterUtils.calculateVectors(room, facing);
         Thread.sleep(200L);

         for(var0 = WaterUtils.getFlag(room, facing); var0 == 0 && Dungeon.currentRoom.equals("Water Board"); var0 = WaterUtils.getFlag(room, facing)) {
            Thread.sleep(100L);
         }

         if (var0 != 0) {
            System.err.println("flag: " + var0);
            Patterns.Operation var2 = Patterns.getOperation(board, var0);
            WaterUtils.processBoard(board);
            WaterUtils.print(board);
            calc(var0, var2);
         }
      }
   }

   private static void etherWarpTo(Vector3d var0) throws Exception {
      tpPacketReceived = false;
      ControlUtils.etherWarp(var0);
      int var1 = 0;

      while(!tpPacketReceived && should) {
         Thread.sleep(20L);
         ++var1;
         if (var1 >= 50) {
            ChatLib.chat("Too long no packet, please try again.");
            throw new Exception();
         }
      }

      Thread.sleep((long)Configs.EtherWarpDelayAfter);
   }

   private void deactivate() {
      if (should) {
         should = false;
         ChatLib.chat("Auto Water &cdeactivated");
      }

   }
}
