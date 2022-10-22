package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.network.Packet;

public class NetUtils {
    public static final void sendPacket(Packet var0) {
        XiaojiaAddons.mc.getNetHandler().getNetworkManager().sendPacket(var0);
    }
}
