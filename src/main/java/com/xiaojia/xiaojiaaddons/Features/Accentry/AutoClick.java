package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoClick {

    private final KeyBind keyBind = new KeyBind("Auto Left Click", 0);

    private long lastClicked = 0L;

    private boolean should = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent var1) {
        if (Configs.AutoLeftClick) {
            if (this.keyBind.isPressed()) {
                this.should = !this.should;
                ChatLib.chat(this.should ? "Auto Left Click &aactivated" : "Auto Left Click &cdeactivated");
            }

            if (this.should) {
                long var2 = TimeUtils.curTime();
                if (var2 - this.lastClicked > (long) (1000 / Configs.AutoClickCPS) && PacketRelated.getReceivedQueueLength() != 0) {
                    this.lastClicked = var2;
                    ControlUtils.leftClick();
                }

            }
        }
    }
}
