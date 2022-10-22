package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class HoldRightClick {

    private static final KeyBind useKeyBind;
    private static final ArrayList terminatorNames = new ArrayList() {
        {
            this.add("Terminator");
        }
    };
    private static final ArrayList rogueNames = new ArrayList() {
        {
            this.add("Rogue");
        }
    };

    static {
        useKeyBind = new KeyBind(XiaojiaAddons.mc.gameSettings.keyBindUseItem);
    }

    private long systemTimeRogue;
    private long systemTimeTerm;
    private double currentSpeed;
    private boolean isRightClicking = false;

    public void executeRogue() {
        if (true) {
            if (Configs.RogueAutoRightClick) {
                if (useKeyBind.isKeyDown() && ControlUtils.checkHoldingItem(rogueNames)) {
                    if (!this.isRightClicking) {
                        this.isRightClicking = true;
                        this.currentSpeed = MinecraftUtils.getPlayer().capabilities.getWalkSpeed() * 1000.0F;
                    }

                    if (this.currentSpeed < (double) Configs.MaxSpeed) {
                        ControlUtils.rightClick();
                        this.currentSpeed += SkyblockUtils.isInDungeon() ? 3.33 : 10.0;
                    }
                } else {
                    this.isRightClicking = false;
                }

            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent var1) {
        while (this.systemTimeTerm < Minecraft.getSystemTime() + (long) (1000 / Configs.TerminatorCPS)) {
            this.systemTimeTerm += 1000 / Configs.TerminatorCPS;
            this.executeTerminator();
        }

        while (this.systemTimeRogue < Minecraft.getSystemTime() + (long) (1000 / Configs.RogueCPS)) {
            this.systemTimeRogue += 1000 / Configs.RogueCPS;
            this.executeRogue();
        }

    }

    public void executeTerminator() {
        if (true) {
            if (Configs.TerminatorAutoRightClick) {
                if (useKeyBind.isKeyDown() && ControlUtils.checkHoldingItem(terminatorNames)) {
                    ControlUtils.rightClick();
                }

            }
        }
    }
}
