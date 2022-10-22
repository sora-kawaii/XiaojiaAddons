package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.awt.*;
import java.util.HashMap;

public class StarredMobESPBox extends RenderEntityESP {
    public boolean enabled() {
        return SkyblockUtils.isInDungeon() && Dungeon.bossEntry < Dungeon.runStarted;
    }

    public EntityInfo getEntityInfo(Entity var1) {
        String var2 = ChatLib.removeFormatting(var1.getName());
        if (!(var1 instanceof EntityArmorStand) || !var2.contains("✯") && !var2.contains("Shadow Assassin")) {
            return null;
        } else {
            float var3 = 0.0F;
            if (!var2.contains("Fel") && !var2.contains("Withermancer")) {
                var3 = 1.9F;
            } else {
                var3 = 2.8F;
            }

            HashMap var4 = new HashMap();
            Color var5 = ColorUtils.realColors[Configs.StarredMobESPColor];
            var4.put("r", var5.getRed());
            var4.put("g", var5.getGreen());
            var4.put("b", var5.getBlue());
            var4.put("a", var5.getAlpha());
            var4.put("entity", var1);
            var4.put("yOffset", 1.0F);
            var4.put("kind", "Starred");
            var4.put("fontColor", 0);
            var4.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
            var4.put("width", 0.45F);
            var4.put("height", var3);
            var4.put("isESP", true);
            return new EntityInfo(var4);
        }
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return SkyblockUtils.isInDungeon() && Configs.StarredMobESP == 2;
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return false;
    }
}
