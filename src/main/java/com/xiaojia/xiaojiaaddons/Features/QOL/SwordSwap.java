package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.StepEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SwordSwap extends StepEvent {

    private static final KeyBind keyBind = new KeyBind("Ghost SwordSwap", 0);

    private static boolean should = false;

    public SwordSwap() {
        super(2L);
    }

    public void execute() {
        if (true) {
            if (Configs.GhostSwordSwap) {
                if (should) {
                    if (HotbarUtils.checkSoulwhip() && HotbarUtils.checkEmeraldBlade()) {
                        (new Thread(() -> {
                            try {
                                ControlUtils.setHeldItemIndex(HotbarUtils.emeraldBladeSlot);
                                ControlUtils.setHeldItemIndex(HotbarUtils.soulwhipSlot);
                                ControlUtils.rightClick();
                                ControlUtils.setHeldItemIndex(HotbarUtils.emeraldBladeSlot);
                            } catch (Exception var1) {
                                var1.printStackTrace();
                            }

                        })).start();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent var1) {
        String var2 = var1.message.getUnformattedText();
        if (var2.equals("You died!") && should) {
            should = false;
            ChatLib.chat("Ghost SwordSwap &cdeactivated");
        }

    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            if (keyBind.isPressed()) {
                should = !should;
                ChatLib.chat(should ? "Ghost SwordSwap &aactivated" : "Ghost SwordSwap &cdeactivated");
            }

        }
    }
}
