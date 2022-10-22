package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.item.ItemStack;

public class DisplayUtils {
    public static String hpToString(double var0) {
        return hpToString(var0, false);
    }

    public static String getDisplayString(ItemStack var0) {
        String var1 = " x" + var0.stackSize;
        String var2 = ChatLib.removeFormatting(var0.getDisplayName());
        if (var2.endsWith(var1)) {
            var2 = var2.substring(0, var2.length() - var1.length());
        }

        return var2;
    }

    public static String getHPDisplayFromArmorStandName(String var0, String var1) {
        int var2 = var0.indexOf(var1) + var1.length() + 3;
        int var3 = var0.indexOf("❤") - 2;
        return var0.substring(var2, var3);
    }

    public static String hpToString(double var0, boolean var2) {
        String var3 = "";
        double var4;
        if (var0 >= 1000000.0) {
            var4 = var0 / 1000000.0;
            if (var2) {
                var3 = String.format("%.0f", var4) + "M";
            } else {
                var3 = String.format("%.2f", var4) + "M";
            }
        } else if (var0 >= 1000.0) {
            var4 = var0 / 1000.0;
            if (var2) {
                var3 = String.format("%.0f", var4) + "K";
            } else {
                var3 = String.format("%.2f", var4) + "K";
            }
        } else if (var2) {
            var3 = String.format("%.0f", var0);
        } else {
            var3 = String.format("%.2f", var0);
        }

        return var3;
    }
}
