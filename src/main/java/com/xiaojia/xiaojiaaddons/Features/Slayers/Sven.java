package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Sven extends RenderEntityESP {

    private static final HashMap kindColorMap = new HashMap() {
        {
            this.put("Pack Enforcer", 8087790);
            this.put("Sven Follower", 16711935);
            this.put("Sven Alpha", 14423100);
            this.put("Sven Packmaster", 14423100);
        }
    };

    public boolean enabled() {
        return true;
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return true;
    }

    public EntityInfo getEntityInfo(Entity var1) {
        if (Checker.enabled && (SkyblockUtils.isInHowlingCave() || SkyblockUtils.isInForest() || SkyblockUtils.isInMountain() || SkyblockUtils.isInRuin())) {
            if (!Configs.WolfMiniESP) {
                return null;
            } else if (var1 instanceof EntityArmorStand && var1.getName() != null) {
                Iterator var2 = kindColorMap.entrySet().iterator();

                String var4;
                int var5;
                do {
                    if (!var2.hasNext()) {
                        return null;
                    }

                    Map.Entry var3 = (Map.Entry) var2.next();
                    var4 = (String) var3.getKey();
                    var5 = (Integer) var3.getValue();
                } while (!var1.getName().contains(var4));

                HashMap var6 = new HashMap();
                var6.put("entity", var1);
                var6.put("height", 0.7F);
                var6.put("yOffset", 1.0F);
                var6.put("drawString", EntityInfo.EnumDraw.DRAW_ARMORSTAND_HP);
                var6.put("scale", 2.0F);
                var6.put("kind", var4);
                var6.put("isESP", true);
                var6.put("fontColor", var5);
                return new EntityInfo(var6);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return Configs.ShowWolfMiniHP;
    }
}
