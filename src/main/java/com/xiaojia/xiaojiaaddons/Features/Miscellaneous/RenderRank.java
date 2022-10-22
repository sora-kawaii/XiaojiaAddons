package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;

public class RenderRank {
    private Method getRenderMethod(RenderPlayer var1) throws NoSuchMethodException {
        for (Class var2 = var1.getClass(); var2 != null; var2 = var2.getSuperclass()) {
            Method[] var3 = var2.getDeclaredMethods();
            Method[] var4 = var3;
            int var5 = var3.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Method var7 = var4[var6];
                if (var7.getName().equals("renderLivingLabel") || var7.getName().equals("func_147906_a")) {
                    return var7;
                }
            }
        }

        throw new NoSuchMethodException();
    }

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Specials.Pre var1) {
        if (Checker.enabled && Configs.RenderRank) {
            String var2 = ChatLib.removeFormatting(var1.entity.getName());
            if (ColorName.rankMap.containsKey(var2)) {
                if (var1.renderer instanceof RenderPlayer) {
                    double var3 = var1.x;
                    double var5 = var1.y;
                    double var7 = var1.z;
                    String var9 = ChatLib.addColor((String) ColorName.rankMap.get(var2));
                    var5 += (float) XiaojiaAddons.mc.fontRendererObj.FONT_HEIGHT * 1.15F * 0.02666667F * 2.0F;

                    try {
                        Method var10 = this.getRenderMethod((RenderPlayer) var1.renderer);
                        var10.setAccessible(true);
                        var10.invoke(var1.renderer, var1.entity, var9, var3, var5, var7, 64);
                    } catch (Exception var12) {
                    }

                }
            }
        }
    }
}
