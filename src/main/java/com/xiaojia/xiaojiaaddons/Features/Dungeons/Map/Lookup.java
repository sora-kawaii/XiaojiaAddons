package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import java.util.ArrayList;
import java.util.Iterator;

public class Lookup {
   public static Room getRoomFromCoords(Vector2i var0) {
      Vector2i var1 = getRoomCenterCoords(var0);
      if (var1 == null) {
         return null;
      } else {
         Room var2 = new Room(var1.x, var1.y, new Data("Unknown", "normal", 0, new ArrayList()));
         Room var3 = getRoomDataFromHash(var2.core);
         if (var3 == null) {
            return null;
         } else {
            var2.name = var3.name;
            var2.type = var3.type;
            var2.secrets = var3.secrets;
            return var2;
         }
      }
   }

   public static Vector2i getRoomCenterCoords(Vector2i var0) {
      for(int var1 = Dungeon.startX; var1 <= Dungeon.startX + (Dungeon.roomSize + 1) * 5; var1 += 32) {
         for(int var2 = Dungeon.startZ; var2 <= Dungeon.startZ + (Dungeon.roomSize + 1) * 5; var2 += 32) {
            if (MathUtils.isBetween(var0.x, var1 + 16, var1 - 16) && MathUtils.isBetween(var0.y, var2 + 16, var2 - 16)) {
               return new Vector2i(var1, var2);
            }
         }
      }

      return null;
   }

   public static Room getRoomDataFromHash(int var0) {
      ArrayList var1 = RoomLoader.maps;
      Iterator var2 = var1.iterator();

      Data var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Data)var2.next();
      } while(!MapUtils.includes(var3.cores, var0));

      return new Room(-1, -1, new Data(var3.name, var3.type, var3.secrets, var3.cores));
   }
}
