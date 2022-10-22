package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;

import java.util.HashMap;

public class GhastESP extends RenderEntityESP {
    public boolean enabled() {
        return true;
    }

    public EntityInfo getEntityInfo(Entity var1) {
        if (!Checker.enabled) {
            return null;
        } else if (!Configs.GhastESP) {
            return null;
        } else if (var1 instanceof EntityGhast) {
            HashMap var2 = new HashMap();
            var2.put("entity", var1);
            var2.put("yOffset", 0.0F);
            var2.put("kind", "Ghast");
            var2.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            var2.put("width", 2.0F);
            var2.put("height", 4.0F);
            var2.put("fontColor", 3407667);
            var2.put("isESP", true);
            return new EntityInfo(var2);
        } else {
            return null;
        }
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return true;
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return true;
    }
}
