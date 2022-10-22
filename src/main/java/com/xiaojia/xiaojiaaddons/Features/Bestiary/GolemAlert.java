package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GolemAlert {

    private static long golemSpawnTime = 0L;
    private final Display display = new Display();

    public GolemAlert() {
        this.display.setShouldRender(false);
        this.display.setBackground("full");
        this.display.setBackgroundColor(0);
        this.display.setAlign("center");
    }

    public static void golemWarn() {
        golemSpawnTime = TimeUtils.curTime() + 20000L;

        try {
            MinecraftUtils.getPlayer().playSound("mob.irongolem.hit", 1000.0F, 1.0F);
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }

    private String makeTimer(long var1) {
        long var3 = var1 / 1000L;
        long var5 = var1 % 1000L;
        return String.format("%02d:%03d", var3, var5);
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent var1) {
        if (var1.message.getUnformattedText().equals("The ground begins to shake as an Endstone Protector rises from below!")) {
            golemWarn();
        }

    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (Configs.GolemAlert) {
                this.display.clearLines();
                this.display.setShouldRender(false);
                if (golemSpawnTime >= TimeUtils.curTime()) {
                    long var2 = TimeUtils.curTime();
                    long var4 = golemSpawnTime - var2;
                    this.display.setRenderLoc(RenderUtils.getScreenWidth() / 2, RenderUtils.getScreenHeight() / 2 - 40);
                    this.display.setShouldRender(true);
                    DisplayLine var6 = new DisplayLine("&fGolem Alert!");
                    var6.setShadow(true);
                    var6.setScale(1.51F);
                    this.display.setLine(0, var6);
                    DisplayLine var7 = new DisplayLine("&c" + this.makeTimer(var4));
                    var7.setShadow(true);
                    var7.setScale(1.5F);
                    this.display.setLine(1, var7);
                }
            }
        }
    }
}
