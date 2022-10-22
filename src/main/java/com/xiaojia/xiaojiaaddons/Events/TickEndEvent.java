package com.xiaojia.xiaojiaaddons.Events;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import java.lang.reflect.Field;

public class TickEndEvent extends Event {

    public static int[] timeConsumed = new int[1000];
    private static boolean owo = false;

    public static void owo() {
        owo = !owo;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent var1) {
        if (var1.phase == Phase.END) {
            try {
                TickEndEvent var2 = new TickEndEvent();
                Field var3 = EventBus.class.getDeclaredField("busID");
                var3.setAccessible(true);
                int var4 = var3.getInt(MinecraftForge.EVENT_BUS);
                IEventListener[] var5 = var2.getListenerList().getListeners(var4);
                int var6 = 0;

                int var9;
                for (long var7 = TimeUtils.curTime(); var6 < var5.length; ++var6) {
                    var9 = 1;
                    if (owo && !var5[var6].toString().matches(".*(HIGHEST|HIGH|NORMAL|LOW|LOWEST).*")) {
                        var9 = 100;
                    }

                    for (int var10 = 0; var10 < var9; ++var10) {
                        var5[var6].invoke(var2);
                    }

                    long var13 = TimeUtils.curTime();
                    timeConsumed[var6] = (int) (var13 - var7);
                    var7 = var13;
                }

                if (owo) {
                    for (var9 = 0; var9 < var5.length; ++var9) {
                        ChatLib.chat(var5[var9].toString() + " " + timeConsumed[var9]);
                    }

                    owo = false;
                }
            } catch (Exception var12) {
                var12.printStackTrace();
                owo = false;
            }
        }

    }
}
