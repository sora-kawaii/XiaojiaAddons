package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OneTick {

    private final KeyBind useKeyBind = new KeyBind("One Tick", 0);
    private final KeyBind keyBind = new KeyBind("Left Click One Tick", 0);
    private boolean should = false;

    private void oneTick() {
        int var1 = ControlUtils.getHeldItemIndex();
        int var2 = HotbarUtils.soulwhipSlot;
        int var3 = HotbarUtils.aotsSlot;
        int var4 = HotbarUtils.terminatorSlot;
        if (Configs.SoulWhipWithAnything && var2 != -1) {
            ControlUtils.setHeldItemIndex(var2);
            ControlUtils.rightClick();
            ControlUtils.setHeldItemIndex(var1);
        }

        if (Configs.AotsWithAnything && var3 != -1) {
            ControlUtils.setHeldItemIndex(var3);
            ControlUtils.rightClick();
            ControlUtils.setHeldItemIndex(var1);
        }

        if (Configs.TerminatorWithAnything && var4 != -1) {
            ControlUtils.setHeldItemIndex(var4);
            ControlUtils.rightClick();
            ControlUtils.setHeldItemIndex(var1);
        }

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent var1) {
        if (true) {
            if (this.keyBind.isPressed()) {
                this.should = !this.should;
                ChatLib.chat(this.should ? "Left Click One Tick &aactivated" : "Left Click One Tick &cdeactivated");
            }

            if (this.useKeyBind.isKeyDown()) {
                this.oneTick();
            }

        }
    }

    @SubscribeEvent
    public void onLeftClick(LeftClickEvent var1) {
        if (true) {
            if (this.should) {
                if (!ControlUtils.checkHoldingItem("Gloomlock Grimoire")) {
                    if (!ControlUtils.checkHoldingItem("Gyrokinetic Wand")) {
                        if (!ControlUtils.checkHoldingItem("Terminator")) {
                            this.oneTick();
                        }
                    }
                }
            }
        }
    }
}
