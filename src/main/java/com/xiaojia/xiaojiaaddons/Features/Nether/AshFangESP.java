package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.awt.*;
import java.util.HashMap;

public class AshFangESP extends RenderEntityESP {
    public void dealWithEntityInfo(EntityInfo var1) {
        if (var1.getKind().equals("")) {
            Entity var2 = var1.getEntity();
            float var3 = MathUtils.getX(var2);
            float var4 = MathUtils.getY(var2);
            float var5 = MathUtils.getZ(var2);
            GuiUtils.drawLine(var3, var4, var5, -484.5F, 137.5F, -1015.5F, new Color(255, 0, 0), 2);
        }
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return !var1.getKind().equals("");
    }

    public boolean enabled() {
        return SkyblockUtils.isInAshFang();
    }

    public EntityInfo getEntityInfo(Entity var1) {
        String var2 = ChatLib.removeFormatting(var1.getDisplayName().getFormattedText());
        if (!(var1 instanceof EntityArmorStand) || (!var2.contains("Ashfang Follower") || !Configs.AshfangFollowerESP) && (!var2.contains("Ashfang Acolyte") || !Configs.AshfangAcolyteESP) && (!var2.contains("Ashfang Underling") || !Configs.AshfangUnderlingESP) && (!var2.contains("Blazing Soul") || !Configs.BlazingSoulESP)) {
            return null;
        } else {
            HashMap var3 = new HashMap();
            Color var4 = null;
            String var5 = "";
            if (var2.contains("Ashfang Follower")) {
                var4 = ColorUtils.getColorFromCode("&8");
                var5 = "Ashfang Follower";
            } else if (var2.contains("Ashfang Acolyte")) {
                var4 = ColorUtils.getColorFromCode("&9");
                var5 = "Ashfang Acolyte";
            } else if (var2.contains("Ashfang Underling")) {
                var4 = ColorUtils.getColorFromCode("&c");
                var5 = "Ashfang Underling";
            } else {
                var4 = new Color(255, 0, 0);
            }

            if (var4 == null) {
                return null;
            } else {
                var3.put("r", var4.getRed());
                var3.put("g", var4.getGreen());
                var3.put("b", var4.getBlue());
                var3.put("a", 120);
                var3.put("entity", var1);
                var3.put("yOffset", 1.0F);
                var3.put("kind", var5);
                var3.put("fontColor", var4.getRGB());
                var3.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
                var3.put("width", 0.4F);
                var3.put("height", 2.0F);
                var3.put("isESP", true);
                var3.put("isFilled", true);
                return new EntityInfo(var3);
            }
        }
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return !var1.getKind().equals("");
    }
}
