package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Colors;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;

public class FontUtils {
    public static void drawString(String var0, int var1, int var2, boolean var3) {
        String[] var4 = var0.split("\n");
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            XiaojiaAddons.mc.fontRendererObj.drawString(var7, (float) var1, (float) var2, Colors.WHITE.getRGB(), var3);
            var2 += XiaojiaAddons.mc.fontRendererObj.FONT_HEIGHT + 1;
        }

    }
}
