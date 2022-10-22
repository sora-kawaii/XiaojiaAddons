package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity {

    private static long lastShot = 0L;

    private static boolean enabled = true;

    private final KeyBind keyBind = new KeyBind("Velocity", 0);

    public static boolean canDisableKnockBack() {
        return TimeUtils.curTime() - lastShot > (long) Configs.VelocityCD && (!MinecraftUtils.getPlayer().isInLava() || !SkyblockUtils.isInDungeon()) && enabled && !wearingSpringBoots();
    }

    public static void onPlayerNearby(Entity var0) {
        if (Configs.VelocityStop) {
            int var1 = Configs.VelocityStopRadius;
            double var2 = Math.sqrt(MathUtils.distanceSquareFromPlayer(var0));
            if (var2 <= (double) var1) {
                if (enabled) {
                    enabled = false;
                    MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
                    ChatLib.chat(String.format("Found player nearby, stopped. Player: %s, distance: %.2f", var0.getName(), var2));
                    ChatLib.chat("Velocity &cdeactivated");
                }

            }
        }
    }

    private static boolean wearingSpringBoots() {
        ItemStack var0 = ControlUtils.getBoots();
        String var1 = NBTUtils.getSkyBlockID(var0);
        return var1.equals("SPRING_BOOTS") || var1.equals("TARANTULA_BOOTS") || var1.equals("THORNS_BOOTS") || var1.equals("SPIDER_BOOTS");
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (this.keyBind.isPressed()) {
                enabled = !enabled;
                ChatLib.chat(enabled ? "Velocity &aactivated" : "Velocity &cdeactivated");
            }

        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent var1) {
        if (var1.action == Action.RIGHT_CLICK_AIR || var1.action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack var2 = ControlUtils.getHeldItemStack();
            String var3 = NBTUtils.getSkyBlockID(var2);
            if (var3.equals("BONZO_STAFF") || var3.equals("JERRY_STAFF") || var3.equals("STARRED_BONZO_STAFF") || var3.equals("GRAPPLING_HOOK")) {
                lastShot = TimeUtils.curTime();
            }
        }

    }
}
