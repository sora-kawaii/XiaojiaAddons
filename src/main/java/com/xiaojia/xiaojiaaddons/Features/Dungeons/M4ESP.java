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

public class M4ESP extends RenderEntityESP {
    public boolean enabled() {
        return SkyblockUtils.isInDungeon() && Dungeon.bossEntry > Dungeon.runStarted && Dungeon.floorInt == 4;
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return false;
    }

    public EntityInfo getEntityInfo(Entity var1) {
        String var2 = ChatLib.removeFormatting(var1.getName());
        if (var1 instanceof EntityArmorStand && (var2.contains("Spirit Bear") && Configs.SpiritBearESPType != 0 || var2.contains("Spirit Bow") && Configs.SpiritBowESPType != 0)) {
            HashMap var3 = new HashMap();
            Color var4;
            float var5;
            float var6;
            boolean var7;
            if (var2.contains("Spirit Bow")) {
                var4 = ColorUtils.realColors[Configs.SpiritBowColor];
                var5 = 0.8F;
                var6 = -1.5F;
                var7 = Configs.SpiritBowESPType == 2;
            } else {
                var4 = ColorUtils.realColors[Configs.SpiritBearColor];
                var5 = 2.0F;
                var6 = 1.0F;
                var7 = Configs.SpiritBearESPType == 2;
            }

            var3.put("r", var4.getRed());
            var3.put("g", var4.getGreen());
            var3.put("b", var4.getBlue());
            var3.put("a", var4.getAlpha());
            var3.put("entity", var1);
            var3.put("yOffset", var6);
            var3.put("kind", var2);
            var3.put("fontColor", 0);
            var3.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
            var3.put("width", 0.4F);
            var3.put("height", var5);
            var3.put("isESP", true);
            var3.put("isFilled", var7);
            return new EntityInfo(var3);
        } else {
            return null;
        }
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return SkyblockUtils.isInDungeon();
    }
}
