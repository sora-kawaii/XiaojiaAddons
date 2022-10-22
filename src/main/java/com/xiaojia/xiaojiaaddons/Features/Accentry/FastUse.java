package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.UpdateEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastUse {
    @SubscribeEvent
    public void onRightClick(UpdateEvent var1) {
        if (Checker.enabled) {
            if (Configs.FastUse) {
                EntityPlayerSP var2 = MinecraftUtils.getPlayer();
                if (var2 != null) {
                    ItemStack var3 = var2.getItemInUse();
                    if (var3 != null && var3.getItem() instanceof ItemFood) {
                        for (int var4 = 0; var4 < 35; ++var4) {
                            XiaojiaAddons.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(MinecraftUtils.getPlayer().onGround));
                        }

                        var2.stopUsingItem();
                        ChatLib.debug("Fast Use!");
                    }

                }
            }
        }
    }
}
