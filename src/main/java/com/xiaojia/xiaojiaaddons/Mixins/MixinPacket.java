package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkManager.class})
public class MixinPacket {
    @Inject(
            method = {"channelRead0"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onReceivePacket(ChannelHandlerContext var1, Packet var2, CallbackInfo var3) {
        if (MinecraftForge.EVENT_BUS.post(new PacketReceivedEvent(var2))) {
            var3.cancel();
        }

    }
}
