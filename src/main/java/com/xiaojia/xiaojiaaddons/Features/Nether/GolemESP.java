package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;

import java.util.HashMap;

public class GolemESP extends RenderEntityESP {
    public EntityInfo getEntityInfo(Entity var1) {
        if (false) {
            return null;
        } else if (!Configs.GolemESP) {
            return null;
        } else if (var1 instanceof EntityGolem) {
            HashMap var2 = new HashMap();
            var2.put("entity", var1);
            var2.put("yOffset", 0.0F);
            var2.put("kind", "Golem");
            var2.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            var2.put("width", 1.5F);
            var2.put("height", 3.0F);
            var2.put("fontColor", 3407667);
            var2.put("isESP", true);
            return new EntityInfo(var2);
        } else {
            return null;
        }
    }

    public boolean enabled() {
        return true;
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return true;
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return true;
    }
}
