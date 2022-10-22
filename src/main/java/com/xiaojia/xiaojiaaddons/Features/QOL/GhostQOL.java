package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.OutlineUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GhostQOL extends RenderEntityESP {

    public static final String GHOST_STRING = "Ghost";
    private static final HashMap runicGhostUUIDs = new HashMap();
    public static int FILLED_OUTLINE_BOX = 2;
    public static int VANILLA_CREEPER = 3;
    public static int OUTLINE_BOX = 1;

    public boolean shouldRenderESP(EntityInfo var1) {
        return true;
    }

    public boolean enabled() {
        return SkyblockUtils.isInMist();
    }

    public boolean shouldDrawString(EntityInfo var1) {
        return true;
    }

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre var1) {
        if (Checker.enabled) {
            if (SkyblockUtils.isInMist()) {
                EntityLivingBase var2 = var1.entity;
                if (var2 instanceof EntityCreeper) {
                    if (Configs.VisibleGhost == OUTLINE_BOX) {
                        this.render(var2, var1.renderer, var1.x, var1.y, var1.z);
                    } else if (Configs.VisibleGhost == VANILLA_CREEPER) {
                        var2.setInvisible(false);
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        runicGhostUUIDs.clear();
    }

    public EntityInfo getEntityInfo(Entity var1) {
        if (var1 instanceof EntityCreeper && runicGhostUUIDs.containsKey(var1.getUniqueID().toString())) {
            HashMap var2 = new HashMap();
            var2.put("entity", var1);
            var2.put("drawString", EntityInfo.EnumDraw.DRAW_ENTITY_HP);
            var2.put("width", 0.35F);
            var2.put("height", 1.8F);
            var2.put("fontColor", 3407667);
            var2.put("isFilled", true);
            var2.put("kind", "Ghost");
            return new EntityInfo(var2);
        } else {
            return null;
        }
    }

    protected void rotateCorpse(EntityLivingBase var1, float var2, float var3, float var4) {
        GlStateManager.rotate(180.0F - var3, 0.0F, 1.0F, 0.0F);
        if (var1.deathTime > 0) {
            float var5 = ((float) var1.deathTime + var4 - 1.0F) / 20.0F * 1.6F;
            var5 = MathHelper.sqrt_float(var5);
            if (var5 > 1.0F) {
                var5 = 1.0F;
            }

            GlStateManager.rotate(var5 * 90.0F, 0.0F, 0.0F, 1.0F);
        }

    }

    @SubscribeEvent
    public void onTickRunicGhost(TickEndEvent var1) {
        if (Checker.enabled) {
            if (SkyblockUtils.isInMist() && Configs.ShowRunicGhost) {
                List var2 = EntityUtils.getEntities();
                Iterator var3 = var2.iterator();

                while (var3.hasNext()) {
                    Entity var4 = (Entity) var3.next();
                    if (var4 instanceof EntityCreeper) {
                        String var5 = var4.getUniqueID().toString();
                        if ((double) ((EntityCreeper) var4).getHealth() > 1000000.1 && !runicGhostUUIDs.containsKey(var5)) {
                            runicGhostUUIDs.put(var5, var4);
                        }
                    }
                }

            }
        }
    }

    protected float interpolateRotation(float var1, float var2, float var3) {
        float var4;
        for (var4 = var2 - var1; var4 < -180.0F; var4 += 360.0F) {
        }

        while (var4 >= 180.0F) {
            var4 -= 360.0F;
        }

        return var1 + var3 * var4;
    }

    private void render(EntityLivingBase var1, RendererLivingEntity var2, double var3, double var5, double var7) {
        float var9 = 1.0F - MathUtils.partialTicks;
        ModelBase var10 = var2.getMainModel();
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        var10.swingProgress = var1.getSwingProgress(var9);

        try {
            float var11 = this.interpolateRotation(var1.prevRenderYawOffset, var1.renderYawOffset, var9);
            float var12 = this.interpolateRotation(var1.prevRotationYawHead, var1.rotationYawHead, var9);
            float var13 = var12 - var11;
            float var14 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;
            GlStateManager.translate((float) var3, (float) var5, (float) var7);
            float var15 = (float) var1.ticksExisted + var9;
            this.rotateCorpse(var1, var15, var11, var9);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
            float var16 = var1.prevLimbSwingAmount + (var1.limbSwingAmount - var1.prevLimbSwingAmount) * var9;
            float var17 = var1.limbSwing - var1.limbSwingAmount * (1.0F - var9);
            if (var1.isChild()) {
                var17 *= 3.0F;
            }

            if (var16 > 1.0F) {
                var16 = 1.0F;
            }

            GlStateManager.enableAlpha();
            var10.setLivingAnimations(var1, var17, var16, var9);
            var10.setRotationAngles(var17, var16, var15, var13, var14, 0.0625F, var1);
            OutlineUtils.outlineEntity(var10, var1, var17, var16, var15, var13, var14, 0.0625F, new Color(85, 255, 85, 150), 7);
            GlStateManager.disableRescaleNormal();
        } catch (Exception var18) {
            var18.printStackTrace();
        }

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
