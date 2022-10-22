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

public class KeyESP extends RenderEntityESP {
    public EntityInfo getEntityInfo(Entity var1) {
        String var2 = ChatLib.removeFormatting(var1.getName());
        if (!(var1 instanceof EntityArmorStand) || !var2.contains("Wither Key") && !var2.contains("Blood Key")) {
            return null;
        } else {
            HashMap var3 = new HashMap();
            Color var4 = ColorUtils.realColors[Configs.WitherKeyColor];
            if (var2.contains("Blood Key")) {
                var4 = ColorUtils.realColors[Configs.BloodKeyColor];
            }

            var3.put("r", var4.getRed());
            var3.put("g", var4.getGreen());
            var3.put("b", var4.getBlue());
            var3.put("a", var4.getAlpha());
            var3.put("entity", var1);
            var3.put("yOffset", -1.5F);
            var3.put("kind", "Key");
            var3.put("fontColor", 0);
            var3.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
            var3.put("width", 0.4F);
            var3.put("height", 0.8F);
            var3.put("isESP", true);
            var3.put("isFilled", Configs.KeyESPType == 2);
            return new EntityInfo(var3);
        }
    }

    public boolean enabled() {
        return SkyblockUtils.isInDungeon() && Dungeon.bloodOpen <= Dungeon.runStarted;
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return false;
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return SkyblockUtils.isInDungeon() && Configs.KeyESPType != 0;
    }
}
