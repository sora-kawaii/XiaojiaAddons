package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;

import java.util.HashMap;

public class BatESP extends RenderEntityESP {
    public boolean enabled() {
        return true;
    }

    public EntityInfo getEntityInfo(Entity var1) {
        if (false) {
            return null;
        } else if (var1 instanceof EntityBat && !var1.isInvisible()) {
            HashMap var2 = new HashMap();
            var2.put("entity", var1);
            if (MathUtils.distanceSquareFromPlayer(MathUtils.getX(var1), MathUtils.getY(var1), MathUtils.getZ(var1)) < 144.0) {
                var2.put("r", 185);
                var2.put("g", 65);
                var2.put("b", 65);
            }

            var2.put("width", 0.4F);
            var2.put("height", 0.8F);
            var2.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            var2.put("kind", "Bat");
            var2.put("fontColor", 3407667);
            var2.put("isESP", true);
            var2.put("isFilled", true);
            return new EntityInfo(var2);
        } else {
            return null;
        }
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return false;
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return (Configs.BatESPDungeons && SkyblockUtils.isInDungeon() || Configs.BatESPOutDungeons && !SkyblockUtils.isInDungeon());
    }
}
