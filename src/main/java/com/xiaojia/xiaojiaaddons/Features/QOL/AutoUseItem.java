package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayDeque;

public class AutoUseItem {

    private static final KeyBind keyBind = new KeyBind("Auto Use Item", 0);
    private final ArrayDeque queue = new ArrayDeque();
    private long lastGyro = 0L;
    private long lastPlasma = 0L;
    private boolean enabled = false;
    private long lastUse = 0L;
    private long lastHealing = 0L;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (keyBind.isPressed()) {
                this.enabled = !this.enabled;
                if (this.enabled) {
                    ChatLib.chat("Auto Item &aactivated");
                    if (Configs.PlasmaFluxCD != 0) {
                        ChatLib.chat("&bPlasma: &6" + Configs.PlasmaFluxCD);
                    }

                    if (Configs.HealingWandCD != 0) {
                        ChatLib.chat("&bHealing Wand: &6" + Configs.HealingWandCD);
                    }

                    if (Configs.GyroCD != 0) {
                        ChatLib.chat("&bGyrokinetic Wand: &6" + Configs.GyroCD);
                    }
                } else {
                    ChatLib.chat("Auto Item &cdeactivated");
                }
            }

            if (this.enabled) {
                long var2 = TimeUtils.curTime();

                try {
                    if (Configs.PlasmaFluxCD != 0 && var2 - this.lastPlasma >= (long) (1000L * Configs.PlasmaFluxCD)) {
                        this.lastPlasma = var2;
                        this.queue.add("Plasma");
                        this.queue.add(ControlUtils.getHeldItemIndex() + "");
                    }

                    if (Configs.HealingWandCD != 0 && var2 - this.lastHealing >= (long) (1000L * Configs.HealingWandCD)) {
                        this.lastHealing = var2;
                        this.queue.add("Healing");
                        this.queue.add(ControlUtils.getHeldItemIndex() + "");
                    }

                    if (Configs.GyroCD != 0 && var2 - this.lastGyro >= (long) (1000L * Configs.GyroCD)) {
                        this.lastGyro = var2;
                        this.queue.add("Gyro");
                        this.queue.add(ControlUtils.getHeldItemIndex() + "");
                    }

                    if ((var2 - this.lastUse > 70L || !Configs.LegitAutoItem) && !this.queue.isEmpty()) {
                        boolean var4 = true;
                        String var5 = (String) this.queue.pollFirst();
                        boolean var7 = true;
                        int var6;
                        switch (var5) {
                            case "Plasma":
                                var6 = HotbarUtils.plasmaSlot;
                                var4 = ControlUtils.checkHotbarItem(var6, "Plasmaflux");
                                break;
                            case "Healing":
                                var6 = HotbarUtils.healingwandSlot;
                                var4 = ControlUtils.checkHotbarItem(var6, "Wand of");
                                break;
                            case "Gyro":
                                var6 = HotbarUtils.gyroSlot;
                                var4 = ControlUtils.checkHotbarItem(var6, "Gyro");
                                break;
                            default:
                                var6 = Integer.parseInt(var5);
                                var7 = false;
                        }

                        if (!var4) {
                            return;
                        }

                        this.lastUse = var2;
                        ControlUtils.setHeldItemIndex(var6);
                        if (var7) {
                            ControlUtils.rightClick();
                        }
                    }
                } catch (Exception var10) {
                    var10.printStackTrace();
                }

            }
        }
    }
}
