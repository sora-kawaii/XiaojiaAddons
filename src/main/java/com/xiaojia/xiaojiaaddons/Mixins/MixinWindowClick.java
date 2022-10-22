package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.WindowClickEvent;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerControllerMP.class})
public abstract class MixinWindowClick {
    @Inject(
            method = {"windowClick"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void windowClick(int var1, int var2, int var3, int var4, EntityPlayer var5, CallbackInfoReturnable var6) {
        if (XiaojiaAddons.isDebug()) {
            ChatLib.chat(String.format("windowClick %d %d %d %d", var1, var2, var3, var4));
        }

        if (MinecraftForge.EVENT_BUS.post(new WindowClickEvent(var1, var2, var3, var4))) {
            var6.setReturnValue(null);
        }

    }
}
