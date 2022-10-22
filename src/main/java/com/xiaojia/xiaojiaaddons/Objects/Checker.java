package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Checker {

   public static long connect = 0L;

   public static boolean enabled = true;

   public static long auth = 0L;

   public static void onConnect() {
      connect = TimeUtils.curTime();
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
   }

   public static void onAuth() {
      auth = TimeUtils.curTime();
   }
}
