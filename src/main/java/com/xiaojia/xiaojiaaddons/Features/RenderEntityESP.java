package com.xiaojia.xiaojiaaddons.Features;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class RenderEntityESP {

    private List renderEntities = new ArrayList();

    public void dealWithEntityInfo(EntityInfo var1) {
    }

    public List getEntities() {
        return EntityUtils.getEntities();
    }

    @SubscribeEvent
    public final void onTick(TickEndEvent event) {
        if (Configs.GeneralESP && MinecraftUtils.getWorld() != null && this.enabled()) {
            try {
                ArrayList var2 = new ArrayList();
                Iterator var3 = this.getEntities().iterator();

                while (var3.hasNext()) {
                    Entity var4 = (Entity) var3.next();
                    EntityInfo var5 = this.getEntityInfo(var4);
                    if (var5 != null) {
                        var2.add(var5);
                    }
                }

                this.renderEntities = var2;
            } catch (Exception var6) {
                var6.printStackTrace();
            }

        } else {
            this.renderEntities = new ArrayList();
        }
    }

    public abstract EntityInfo getEntityInfo(Entity var1);

    public abstract boolean shouldRenderESP(EntityInfo var1);

    public abstract boolean enabled();

    @SubscribeEvent
    public final void onRenderWorld(RenderWorldLastEvent var1) {
        if (true) {
            if (Configs.GeneralESP) {
                EntityInfo var3;
                try {
                    for (Iterator var2 = this.renderEntities.iterator(); var2.hasNext(); this.dealWithEntityInfo(var3)) {
                        var3 = (EntityInfo) var2.next();
                        EntityInfo.EnumDraw var4 = var3.getDrawString();
                        Entity var5 = var3.getEntity();
                        String var6 = var3.getKind();
                        String var7 = var5.getName();
                        String var8 = "";
                        boolean var9 = var3.isFilled();
                        if (var4 == EntityInfo.EnumDraw.DRAW_KIND) {
                            var8 = var3.getKind();
                        } else if (var4 == EntityInfo.EnumDraw.DRAW_ARMORSTAND_HP) {
                            var8 = DisplayUtils.getHPDisplayFromArmorStandName(var7, var6);
                        } else if (var4 == EntityInfo.EnumDraw.DRAW_ENTITY_HP) {
                            var8 = DisplayUtils.hpToString(((EntityLivingBase) var5).getHealth());
                        }

                        if (this.shouldDrawString(var3)) {
                            GuiUtils.drawString(var8, MathUtils.getX(var5), MathUtils.getY(var5), MathUtils.getZ(var5), var3.getFontColor(), var3.getScale(), true);
                        }

                        int var10 = var3.getR();
                        int var11 = var3.getG();
                        int var12 = var3.getB();
                        float var13 = var3.getWidth();
                        float var14 = var3.getHeight();
                        float var15 = var3.getyOffset();
                        if (this.shouldRenderESP(var3)) {
                            boolean var16 = var3.isESP();
                            if (var16) {
                                GuiUtils.enableESP();
                            }

                            if (!var9) {
                                GuiUtils.drawBoxAtEntity(var5, var10, var11, var12, 100, var13, var14, var15);
                            } else {
                                GuiUtils.drawFilledBoxAtEntity(var5, var10, var11, var12, 100, var13, var14, var15);
                            }

                            if (var16) {
                                GuiUtils.disableESP();
                            }
                        }
                    }
                } catch (Exception var17) {
                    var17.printStackTrace();
                }

            }
        }
    }

    public abstract boolean shouldDrawString(EntityInfo var1);
}
