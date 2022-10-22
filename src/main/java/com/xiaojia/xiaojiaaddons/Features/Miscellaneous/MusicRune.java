package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MusicRune {
    public static void play() {
        (new Thread(() -> {
            int var0 = (int) (Math.random() * 10.0);

            try {
                MinecraftUtils.getPlayer().playSound("note.harp", 300.0F, (float) var0 / 12.0F);
                Thread.sleep(80L);
                MinecraftUtils.getPlayer().playSound("note.harp", 300.0F, (float) (var0 + 3) / 12.0F);
                Thread.sleep(80L);
                MinecraftUtils.getPlayer().playSound("note.harp", 300.0F, (float) (var0 + 6) / 12.0F);
                Thread.sleep(80L);
                MinecraftUtils.getPlayer().playSound("note.harp", 300.0F, (float) (var0 + 9) / 12.0F);
            } catch (Exception var2) {
                var2.printStackTrace();
            }

        })).start();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
        }
    }
}
