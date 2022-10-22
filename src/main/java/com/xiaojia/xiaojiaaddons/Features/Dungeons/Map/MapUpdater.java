package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MapUpdater {

   private long lastMakeMap = 0L;

   private long lastUpdate = 0L;

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (TimeUtils.curTime() - this.lastMakeMap >= 500L && Configs.MapEnabled && Dungeon.isInDungeon && Dungeon.isFullyScanned) {
         this.lastMakeMap = TimeUtils.curTime();
         (new Thread(Dungeon::makeMap)).start();
      }

      if (TimeUtils.curTime() - this.lastUpdate >= 200L && Configs.MapEnabled && Dungeon.isInDungeon) {
         this.lastUpdate = TimeUtils.curTime();
         (new Thread(() -> {
            try {
               Dungeon.updatePlayers();
            } catch (Exception var3) {
               var3.printStackTrace();
               ChatLib.chat("error updatePlayers");
            }

            try {
               Dungeon.updateRooms();
            } catch (Exception var2) {
               var2.printStackTrace();
               ChatLib.chat("error updateRooms");
            }

            try {
               Dungeon.updateDoors();
            } catch (Exception var1) {
               var1.printStackTrace();
               ChatLib.chat("error updateDoors");
            }

         })).start();
      }

   }
}
