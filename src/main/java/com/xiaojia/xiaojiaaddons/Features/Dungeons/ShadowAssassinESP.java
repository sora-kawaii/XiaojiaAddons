package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.HashMap;

public class ShadowAssassinESP extends RenderEntityESP {
    public EntityInfo getEntityInfo(Entity var1) {
        if (!(var1 instanceof EntityPlayer)) {
            return null;
        } else {
            String var2 = ChatLib.removeFormatting(var1.getCommandSenderEntity().getName());
            if (var2.contains("Shadow Assassin")) {
                HashMap var3 = new HashMap();
                Color var4 = ColorUtils.realColors[Configs.ShadowAssassinESPColor];
                var3.put("r", var4.getRed());
                var3.put("g", var4.getGreen());
                var3.put("b", var4.getBlue());
                var3.put("a", var4.getAlpha());
                var3.put("entity", var1);
                var3.put("kind", "SA");
                var3.put("fontColor", 0);
                var3.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
                var3.put("width", 0.45F);
                var3.put("height", 1.9F);
                var3.put("isESP", true);
                return new EntityInfo(var3);
            } else {
                return null;
            }
        }
    }

    public boolean enabled() {
        return true;
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return SkyblockUtils.isInDungeon() && Configs.ShadowAssassinESP;
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return false;
    }
}
