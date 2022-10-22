package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import java.util.Deque;
import java.util.LinkedList;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandsUtils {

   private static long lastSent = 0L;

   private static final Deque commandsQueue = new LinkedList();

   private static final Deque sentQueue = new LinkedList();

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         while(!sentQueue.isEmpty() && TimeUtils.curTime() - (Long)sentQueue.getFirst() > 3000L) {
            sentQueue.pollFirst();
         }

         if (sentQueue.size() <= 3) {
            if (TimeUtils.curTime() - lastSent > 200L && commandsQueue.size() > 0) {
               String var2 = (String)commandsQueue.pollFirst();
               sentQueue.addLast(TimeUtils.curTime());
               if (!var2.startsWith("/") || ClientCommandHandler.instance.executeCommand(XiaojiaAddons.mc.thePlayer, var2) == 0) {
                  XiaojiaAddons.mc.thePlayer.sendChatMessage(var2);
               }

               lastSent = TimeUtils.curTime();
            }

         }
      }
   }

   public static void addCommand(String var0, int var1) {
      if (commandsQueue.size() <= var1) {
         addCommand(var0);
      }
   }

   public static void addCommand(String var0) {
      commandsQueue.offerLast(var0);
   }
}
