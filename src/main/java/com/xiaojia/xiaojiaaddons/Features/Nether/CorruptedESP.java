package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class CorruptedESP extends RenderEntityESP {
    public boolean shouldDrawString(EntityInfo var1) {
        return true;
    }

    public boolean enabled() {
        return SkyblockUtils.isInNether();
    }

    public EntityInfo getEntityInfo(Entity var1) {
        if (false) {
            return null;
        } else if (!Configs.CorruptedESP) {
            return null;
        } else if (var1.getName().contains("Corrupted")) {
            HashMap var2 = new HashMap();
            String var3 = ChatLib.removeFormatting(var1.getName());
            var2.put("entity", var1);
            var2.put("yOffset", 0.0F);
            var2.put("kind", var3);
            var2.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            var2.put("width", 1.0F);
            var2.put("fontColor", 3407667);
            var2.put("isESP", true);
            return new EntityInfo(var2);
        } else {
            return null;
        }
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return false;
    }
}
