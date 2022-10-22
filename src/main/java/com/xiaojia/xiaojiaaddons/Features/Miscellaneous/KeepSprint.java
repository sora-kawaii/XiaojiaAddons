package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KeepSprint {
    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            if (Configs.KeepSprint) {
                ControlUtils.holdSprint();
            }
        }
    }
}
