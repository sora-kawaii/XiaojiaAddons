package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.RenderEntityModelEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

public class StarredMobESP extends RenderEntityESP {

    private static final HashMap highlightedEntities = new HashMap();
    public static HashSet fixEntities = new HashSet();
    private final HashSet checkedEntities = new HashSet();

    private static void highlightEntity(Entity var0, Color var1) {
        highlightedEntities.put(var0, var1);
    }

    public static void show() {
        Iterator var0 = fixEntities.iterator();

        while (var0.hasNext()) {
            Entity var1 = (Entity) var0.next();
            ChatLib.chat(var1.getCustomNameTag() + ": " + var1.isDead + ", " + MathUtils.getPosString(var1));
        }

    }

    public static int getSetSize() {
        return fixEntities.size();
    }

    public boolean enabled() {
        return SkyblockUtils.isInDungeon() && Dungeon.bossEntry < Dungeon.runStarted;
    }

    public List getEntities() {
        if (fixEntities.isEmpty()) {
            return new ArrayList();
        } else {
            List var1 = EntityUtils.getEntities();
            ArrayList var2 = new ArrayList();
            Iterator var3 = var1.iterator();

            while (var3.hasNext()) {
                Entity var4 = (Entity) var3.next();
                if (fixEntities.contains(var4)) {
                    var2.add(var4);
                }
            }

            return var2;
        }
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return false;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        this.checkedEntities.clear();
        highlightedEntities.clear();
        fixEntities.clear();
    }

    public boolean shouldRenderESP(EntityInfo var1) {
        return true;
    }

    @SubscribeEvent
    public void onRenderEntityModel(RenderEntityModelEvent var1) {
        if (Checker.enabled) {
            if (Configs.StarredMobESP == 1) {
                if (SkyblockUtils.isInDungeon()) {
                    EntityLivingBase var2 = var1.entity;
                    if (!this.checkedEntities.contains(var2)) {
                        if (var2 instanceof EntityArmorStand && var2.hasCustomName() && var2.getName().contains("✯")) {
                            this.checkedEntities.add(var2);
                            List var3 = MinecraftUtils.getWorld().getEntitiesInAABBexcluding(var2, var2.getEntityBoundingBox().expand(0.1, 3.0, 0.1), (var0) -> {
                                return !(var0 instanceof EntityArmorStand);
                            });
                            if (!var3.isEmpty()) {
                                Color var4 = ColorUtils.realColors[Configs.StarredMobESPColor];
                                highlightEntity((Entity) var3.get(0), var4);
                            } else if (Configs.ShowBoxWhenBoundingNotWork) {
                                fixEntities.add(var2);
                            }
                        }

                        if (highlightedEntities.containsKey(var2)) {
                            GuiUtils.enableESP();
                            OutlineUtils.outlineEntity(var1, (Color) highlightedEntities.get(var1.entity), Configs.StarredMobESPOutlineLength);
                            GuiUtils.disableESP();
                        }

                    }
                }
            }
        }
    }

    public EntityInfo getEntityInfo(Entity var1) {
        String var2 = ChatLib.removeFormatting(var1.getName());
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
