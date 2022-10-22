package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Map {

   public static Vector2i startCorner = new Vector2i(5, 5);

   public static boolean calibrated = false;

   private static StringBuilder log = new StringBuilder();

   public static int roomSize = 16;

   public static void calibrate() {
      if (Dungeon.floorInt != -1) {
         log = new StringBuilder();
         if (Dungeon.totalRooms > 30) {
            startCorner = new Vector2i(5, 5);
         } else if (Dungeon.totalRooms == 30) {
            startCorner = new Vector2i(16, 5);
         } else if (Dungeon.totalRooms == 25) {
            startCorner = new Vector2i(11, 11);
         }

         if (Dungeon.floorInt > 3 && Dungeon.totalRooms != 25) {
            roomSize = 16;
         } else {
            roomSize = 18;
         }

         if (Dungeon.floorInt == 1) {
            startCorner = new Vector2i(22, 11);
         } else if (Dungeon.floorInt != 2 && Dungeon.floorInt != 3) {
            if (Dungeon.floorInt == 4 && Dungeon.totalRooms > 25) {
               startCorner = new Vector2i(5, 16);
            }
         } else {
            startCorner = new Vector2i(11, 11);
         }

         log.append("start corner: " + startCorner.x + ", " + startCorner.y).append("\n");
         log.append("roomsize: " + roomSize).append("\n");
         log.append("dungeon floor int: " + Dungeon.floorInt).append("\n");
         calibrated = true;
      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      reset();
   }

   public static MapData getMapData() {
      ItemStack var0 = getMap();
      return var0 != null && var0.getItem() instanceof ItemMap ? ((ItemMap)var0.getItem()).getMapData(var0, MinecraftUtils.getWorld()) : null;
   }

   public static ItemStack getMap() {
      ItemStack var0 = ControlUtils.getItemStackInSlot(44, true);
      return var0 != null && Item.getIdFromItem(var0.getItem()) == 358 && var0.getDisplayName().contains("Magical Map") ? var0 : null;
   }

   public static java.util.Map getMapDecorators() {
      MapData var0 = getMapData();
      return var0 == null ? null : var0.mapDecorations;
   }

   public static void reset() {
      calibrated = false;
      startCorner = new Vector2i(5, 5);
      roomSize = 16;
   }

   public static void printLog() {
      System.err.println("Map Log:");
      System.err.println(log);
      System.err.println();
   }

   public static byte[] getMapColors() {
      MapData var0 = getMapData();
      return var0 == null ? null : var0.colors;
   }
}
