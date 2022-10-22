package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook.EnumFlags;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;

import java.lang.reflect.Field;

public class PacketUtils {

    public static Field messageField = null;

    static {
        try {
            messageField = S45PacketTitle.class.getDeclaredField("field_179810_b");
        } catch (NoSuchFieldException var3) {
            try {
                messageField = S45PacketTitle.class.getDeclaredField("message");
            } catch (NoSuchFieldException var2) {
                var2.printStackTrace();
            }
        }

        messageField.setAccessible(true);
    }

    public static String getPosLookPacket(S08PacketPlayerPosLook var0) {
        StringBuilder var1 = new StringBuilder();
        var1.append(String.format("%.2f, %.2f; %.2f, %.2f, %.2f: ", var0.getYaw(), var0.getPitch(), var0.getX(), var0.getY(), var0.getZ()));
        if (var0.func_179834_f().contains(EnumFlags.X)) {
            var1.append("X");
        }

        if (var0.func_179834_f().contains(EnumFlags.Y)) {
            var1.append("Y");
        }

        if (var0.func_179834_f().contains(EnumFlags.Z)) {
            var1.append("Z");
        }

        if (var0.func_179834_f().contains(EnumFlags.X_ROT)) {
            var1.append(" XR");
        }

        if (var0.func_179834_f().contains(EnumFlags.Y_ROT)) {
            var1.append(" YR");
        }

        var1.append("\n");
        return var1.toString();
    }

    public static void setMessage(S45PacketTitle var0, IChatComponent var1) {
        try {
            messageField.set(var0, var1);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static IChatComponent getMessage(S45PacketTitle var0) {
        try {
            return (IChatComponent) messageField.get(var0);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
