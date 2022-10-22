package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class GuiUtils {

    private static final Tessellator tessellator = Tessellator.getInstance();

    private static final ResourceLocation beaconBeam;

    private static final WorldRenderer worldRenderer;

    static {
        worldRenderer = tessellator.getWorldRenderer();
        beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");
    }

    public static void drawTexture(ResourceLocation var0, int var1, int var2, int var3, int var4) {
        drawTexture(var0, var1, var2, var3, var4, var3, var4, 0, 0);
    }

    public static void showTitle(String var0, String var1, int var2, int var3, int var4) {
        GuiIngame var5 = Minecraft.getMinecraft().ingameGUI;
        var5.displayTitle(ChatLib.addColor(var0), null, var2, var3, var4);
        var5.displayTitle(null, ChatLib.addColor(var1), var2, var3, var4);
        var5.displayTitle(null, null, var2, var3, var4);
    }

    public static void drawRotatedTexture(ResourceLocation var0, int var1, int var2, int var3, int var4, int var5) {
        drawRotatedTexture(var0, var1, var2, var3, var4, var3, var4, 0, 0, var5);
    }

    public static void drawStringOnSlot(String var0, int var1, int var2, int var3, float var4, int var5, int var6) {
        Vector2i var7 = getXYForSlot(var1, var2, var3);
        int var8 = var7.x + var5;
        int var9 = var7.y + var6;
        GlStateManager.translate(0.0F, 0.0F, 1.0F);
        GlStateManager.pushMatrix();
        drawScaledString(var0, var4, var8, var9, false);
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0F, 0.0F, -1.0F);
    }

    public static void drawRotatedTexture(ResourceLocation var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) var1 + (float) var3 / 2.0F, (float) var2 + (float) var4 / 2.0F, 0.0F);
        GlStateManager.rotate((float) var9, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate((float) (-var1) - (float) var3 / 2.0F, (float) (-var2) - (float) var4 / 2.0F, 0.0F);
        drawTexture(var0, var1, var2, var3, var4, var5, var6, var7, var8);
        GlStateManager.popMatrix();
    }

    public static void drawStringOnSlot(String var0, int var1, int var2, int var3, Color var4) {
        Vector2i var5 = getXYForSlot(var1, var2, var3);
        int var6 = var5.x + 8 - RenderUtils.getStringWidth(var0) / 2;
        int var7 = var5.y + 4;
        GL11.glTranslated(0.0, 0.0, 1.0);
        drawString(var0, var6, var7, false, var4);
        GL11.glTranslated(0.0, 0.0, -1.0);
    }

    private static void drawFilledBoundingBoxAbsolute(float var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7, int var8, int var9) {
        EntityPlayerSP var10 = MinecraftUtils.getPlayer();
        float var11 = MathUtils.getX(var10);
        float var12 = MathUtils.getY(var10);
        float var13 = MathUtils.getZ(var10);
        var0 -= var11;
        var1 -= var12;
        var2 -= var13;
        var3 -= var11;
        var4 -= var12;
        var5 -= var13;
        drawFilledBoundingBoxRelative(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
    }

    public static void drawString(String var0, int var1, int var2, boolean var3) {
        drawString(var0, var1, var2, var3, Color.WHITE);
    }

    public static void drawBoxAtEntity(Entity var0, int var1, int var2, int var3, int var4, float var5, float var6, float var7) {
        float var8 = MathUtils.getX(var0);
        float var9 = MathUtils.getY(var0) - var7 * var6;
        float var10 = MathUtils.getZ(var0);
        drawBoxAt(var8, var9, var10, var1, var2, var3, var4, var5, var6);
    }

    public static void drawString(String var0, float var1, float var2, float var3, int var4, float var5, boolean var6) {
        float var7 = var5;
        RenderManager var8 = XiaojiaAddons.mc.getRenderManager();
        FontRenderer var9 = XiaojiaAddons.mc.fontRendererObj;
        Vector3f var10 = getRenderPos(var1, var2, var3);
        if (var6) {
            float var11 = var10.x * var10.x + var10.y * var10.y + var10.z * var10.z;
            float var12 = (float) Math.sqrt(var11);
            float var13 = var12 / 120.0F;
            var7 = var5 * 0.45F * var13;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GL11.glPushMatrix();
        GL11.glTranslatef(var10.x, var10.y, var10.z);
        GL11.glRotatef(-var8.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(var8.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-var7, -var7, var7);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        int var14 = var9.getStringWidth(var0);
        var9.drawString(var0, -var14 / 2, 0, var4);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }

    public static void disableESP() {
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private static void renderBeaconBeam(double var0, double var2, double var4, int var6, float var7) {
        short var8 = 300;
        byte var9 = 0;
        int var10 = var9 + var8;
        Tessellator var11 = Tessellator.getInstance();
        WorldRenderer var12 = var11.getWorldRenderer();
        Minecraft.getMinecraft().getTextureManager().bindTexture(beaconBeam);
        GL11.glTexParameterf(3553, 10242, 10497.0F);
        GL11.glTexParameterf(3553, 10243, 10497.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double var13 = (float) Minecraft.getMinecraft().theWorld.getTotalWorldTime() + MathUtils.partialTicks;
        double var15 = MathHelper.func_181162_h(-var13 * 0.2 - (double) MathHelper.floor_double(-var13 * 0.1));
        float var17 = (float) (var6 >> 16 & 255) / 255.0F;
        float var18 = (float) (var6 >> 8 & 255) / 255.0F;
        float var19 = (float) (var6 & 255) / 255.0F;
        double var20 = var13 * 0.025 * -1.5;
        double var22 = 0.5 + Math.cos(var20 + 2.356194490192345) * 0.2;
        double var24 = 0.5 + Math.sin(var20 + 2.356194490192345) * 0.2;
        double var26 = 0.5 + Math.cos(var20 + 0.7853981633974483) * 0.2;
        double var28 = 0.5 + Math.sin(var20 + 0.7853981633974483) * 0.2;
        double var30 = 0.5 + Math.cos(var20 + 3.9269908169872414) * 0.2;
        double var32 = 0.5 + Math.sin(var20 + 3.9269908169872414) * 0.2;
        double var34 = 0.5 + Math.cos(var20 + 5.497787143782138) * 0.2;
        double var36 = 0.5 + Math.sin(var20 + 5.497787143782138) * 0.2;
        double var38 = -1.0 + var15;
        double var40 = (double) var8 * 2.5 + var38;
        var12.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        var12.pos(var0 + var22, var2 + (double) var10, var4 + var24).tex(1.0, var40).color(var17, var18, var19, var7).endVertex();
        var12.pos(var0 + var22, var2 + (double) var9, var4 + var24).tex(1.0, var38).color(var17, var18, var19, 1.0F).endVertex();
        var12.pos(var0 + var26, var2 + (double) var9, var4 + var28).tex(0.0, var38).color(var17, var18, var19, 1.0F).endVertex();
        var12.pos(var0 + var26, var2 + (double) var10, var4 + var28).tex(0.0, var40).color(var17, var18, var19, var7).endVertex();
        var12.pos(var0 + var34, var2 + (double) var10, var4 + var36).tex(1.0, var40).color(var17, var18, var19, var7).endVertex();
        var12.pos(var0 + var34, var2 + (double) var9, var4 + var36).tex(1.0, var38).color(var17, var18, var19, 1.0F).endVertex();
        var12.pos(var0 + var30, var2 + (double) var9, var4 + var32).tex(0.0, var38).color(var17, var18, var19, 1.0F).endVertex();
        var12.pos(var0 + var30, var2 + (double) var10, var4 + var32).tex(0.0, var40).color(var17, var18, var19, var7).endVertex();
        var12.pos(var0 + var26, var2 + (double) var10, var4 + var28).tex(1.0, var40).color(var17, var18, var19, var7).endVertex();
        var12.pos(var0 + var26, var2 + (double) var9, var4 + var28).tex(1.0, var38).color(var17, var18, var19, 1.0F).endVertex();
        var12.pos(var0 + var34, var2 + (double) var9, var4 + var36).tex(0.0, var38).color(var17, var18, var19, 1.0F).endVertex();
        var12.pos(var0 + var34, var2 + (double) var10, var4 + var36).tex(0.0, var40).color(var17, var18, var19, var7).endVertex();
        var12.pos(var0 + var30, var2 + (double) var10, var4 + var32).tex(1.0, var40).color(var17, var18, var19, var7).endVertex();
        var12.pos(var0 + var30, var2 + (double) var9, var4 + var32).tex(1.0, var38).color(var17, var18, var19, 1.0F).endVertex();
        var12.pos(var0 + var22, var2 + (double) var9, var4 + var24).tex(0.0, var38).color(var17, var18, var19, 1.0F).endVertex();
        var12.pos(var0 + var22, var2 + (double) var10, var4 + var24).tex(0.0, var40).color(var17, var18, var19, var7).endVertex();
        var11.draw();
        GlStateManager.disableCull();
        double var42 = -1.0 + var15;
        double var44 = (double) var8 + var42;
        var12.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        var12.pos(var0 + 0.2, var2 + (double) var10, var4 + 0.2).tex(1.0, var44).color(var17, var18, var19, 0.25F * var7).endVertex();
        var12.pos(var0 + 0.2, var2 + (double) var9, var4 + 0.2).tex(1.0, var42).color(var17, var18, var19, 0.25F).endVertex();
        var12.pos(var0 + 0.8, var2 + (double) var9, var4 + 0.2).tex(0.0, var42).color(var17, var18, var19, 0.25F).endVertex();
        var12.pos(var0 + 0.8, var2 + (double) var10, var4 + 0.2).tex(0.0, var44).color(var17, var18, var19, 0.25F * var7).endVertex();
        var12.pos(var0 + 0.8, var2 + (double) var10, var4 + 0.8).tex(1.0, var44).color(var17, var18, var19, 0.25F * var7).endVertex();
        var12.pos(var0 + 0.8, var2 + (double) var9, var4 + 0.8).tex(1.0, var42).color(var17, var18, var19, 0.25F).endVertex();
        var12.pos(var0 + 0.2, var2 + (double) var9, var4 + 0.8).tex(0.0, var42).color(var17, var18, var19, 0.25F).endVertex();
        var12.pos(var0 + 0.2, var2 + (double) var10, var4 + 0.8).tex(0.0, var44).color(var17, var18, var19, 0.25F * var7).endVertex();
        var12.pos(var0 + 0.8, var2 + (double) var10, var4 + 0.2).tex(1.0, var44).color(var17, var18, var19, 0.25F * var7).endVertex();
        var12.pos(var0 + 0.8, var2 + (double) var9, var4 + 0.2).tex(1.0, var42).color(var17, var18, var19, 0.25F).endVertex();
        var12.pos(var0 + 0.8, var2 + (double) var9, var4 + 0.8).tex(0.0, var42).color(var17, var18, var19, 0.25F).endVertex();
        var12.pos(var0 + 0.8, var2 + (double) var10, var4 + 0.8).tex(0.0, var44).color(var17, var18, var19, 0.25F * var7).endVertex();
        var12.pos(var0 + 0.2, var2 + (double) var10, var4 + 0.8).tex(1.0, var44).color(var17, var18, var19, 0.25F * var7).endVertex();
        var12.pos(var0 + 0.2, var2 + (double) var9, var4 + 0.8).tex(1.0, var42).color(var17, var18, var19, 0.25F).endVertex();
        var12.pos(var0 + 0.2, var2 + (double) var9, var4 + 0.2).tex(0.0, var42).color(var17, var18, var19, 0.25F).endVertex();
        var12.pos(var0 + 0.2, var2 + (double) var10, var4 + 0.2).tex(0.0, var44).color(var17, var18, var19, 0.25F * var7).endVertex();
        var11.draw();
        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
    }

    public static void drawScaledString(String var0, float var1, int var2, int var3, boolean var4) {
        GlStateManager.scale(var1, var1, var1);
        drawString(var0, (int) ((float) var2 / var1), (int) ((float) var3 / var1), var4);
    }

    public static void drawBoundingBoxAtPos(float var0, float var1, float var2, Color var3, float var4, float var5) {
        drawBoxAt(var0, var1, var2, var3.getRed(), var3.getGreen(), var3.getBlue(), var3.getAlpha(), var4, var5);
    }

    public static void drawSelectionBoundingBoxAtBlock(BlockPos var0, Color var1) {
        AxisAlignedBB var2 = BlockUtils.getAABBOfBlock(var0);
        if (var2 != null) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth((float) Configs.EtherwarpPointBoundingThickness);
            GlStateManager.color((float) var1.getRed() / 255.0F, (float) var1.getGreen() / 255.0F, (float) var1.getBlue() / 255.0F, (float) var1.getAlpha() / 255.0F);
            RenderGlobal.drawSelectionBoundingBox(var2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glLineWidth((float) Configs.BoxLineThickness);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawStringAtRightUpOfDoubleChest(String var0) {
        ScaledResolution var1 = new ScaledResolution(Minecraft.getMinecraft());
        int var2 = (var1.getScaledWidth() + 176) / 2;
        int var3 = (var1.getScaledHeight() - 222) / 2;
        int var4 = var2 - 8 - RenderUtils.getStringWidth(var0);
        int var5 = var3 + 6;
        GL11.glTranslated(0.0, 0.0, 1.0);
        XiaojiaAddons.mc.fontRendererObj.drawString(var0, (float) var4, (float) var5, -1, false);
        GL11.glTranslated(0.0, 0.0, -1.0);
    }

    public static void renderBeaconBeam(BlockPos var0, int var1, float var2) {
        double var3 = (float) var0.getX() - MathUtils.getX(MinecraftUtils.getPlayer());
        double var5 = (float) var0.getY() - MathUtils.getY(MinecraftUtils.getPlayer());
        double var7 = (float) var0.getZ() - MathUtils.getZ(MinecraftUtils.getPlayer());
        renderBeaconBeam(var3, var5, var7, var1, var2);
    }

    public static void drawBoxAtBlock(BlockPos var0, Color var1, int var2, int var3, float var4) {
        drawBoxAtBlock(var0.getX(), var0.getY(), var0.getZ(), var1.getRed(), var1.getGreen(), var1.getBlue(), var1.getAlpha(), var2, var3, var4);
    }

    public static void drawLine(Vec3 var0, Vec3 var1, Color var2, int var3) {
        drawLine((float) var0.xCoord, (float) var0.yCoord, (float) var0.zCoord, (float) var1.xCoord, (float) var1.yCoord, (float) var1.zCoord, var2, var3);
    }

    private static void drawLineWithDepthRelative(float var0, float var1, float var2, float var3, float var4, float var5, Color var6, int var7) {
        GlStateManager.pushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth((float) var7);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
        GlStateManager.color((float) var6.getRed(), (float) var6.getGreen(), (float) var6.getBlue());
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(var0, var1, var2).endVertex();
        worldRenderer.pos(var3, var4, var5).endVertex();
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GlStateManager.popMatrix();
    }

    public static void drawSelectionFilledBoxAtBlock(BlockPos var0, Color var1) {
        AxisAlignedBB var2 = BlockUtils.getAABBOfBlock(var0);
        if (var2 != null) {
            drawFilledBoundingBoxRelative((float) var2.minX, (float) var2.minY, (float) var2.minZ, (float) var2.maxX, (float) var2.maxY, (float) var2.maxZ, var1.getRed(), var1.getGreen(), var1.getBlue(), var1.getAlpha());
        }
    }

    private static void drawFilledBoundingBoxRelative(float var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7, int var8, int var9) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color((float) var6 / 255.0F, (float) var7 / 255.0F, (float) var8 / 255.0F, (float) var9 / 255.0F);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(var0, var1, var2).endVertex();
        worldRenderer.pos(var3, var1, var2).endVertex();
        worldRenderer.pos(var3, var1, var5).endVertex();
        worldRenderer.pos(var0, var1, var5).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(var0, var4, var5).endVertex();
        worldRenderer.pos(var3, var4, var5).endVertex();
        worldRenderer.pos(var3, var4, var2).endVertex();
        worldRenderer.pos(var0, var4, var2).endVertex();
        tessellator.draw();
        GlStateManager.color((float) var6 / 255.0F * 0.8F, (float) var7 / 255.0F * 0.8F, (float) var8 / 255.0F * 0.8F, (float) var9 / 255.0F);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(var0, var1, var5).endVertex();
        worldRenderer.pos(var0, var4, var5).endVertex();
        worldRenderer.pos(var0, var4, var2).endVertex();
        worldRenderer.pos(var0, var1, var2).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(var3, var1, var2).endVertex();
        worldRenderer.pos(var3, var4, var2).endVertex();
        worldRenderer.pos(var3, var4, var5).endVertex();
        worldRenderer.pos(var3, var1, var5).endVertex();
        tessellator.draw();
        GlStateManager.color((float) var6 / 255.0F * 0.9F, (float) var7 / 255.0F * 0.9F, (float) var8 / 255.0F * 0.9F, (float) var9 / 255.0F);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(var0, var4, var2).endVertex();
        worldRenderer.pos(var3, var4, var2).endVertex();
        worldRenderer.pos(var3, var1, var2).endVertex();
        worldRenderer.pos(var0, var1, var2).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(var0, var1, var5).endVertex();
        worldRenderer.pos(var3, var1, var5).endVertex();
        worldRenderer.pos(var3, var4, var5).endVertex();
        worldRenderer.pos(var0, var4, var5).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawRect(int var0, int var1, int var2, int var3, int var4) {
        Gui.drawRect(var1, var2, var1 + var3, var2 + var4, var0);
    }

    public static void drawBoxAtPos(float var0, float var1, float var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9) {
        drawFilledBoundingBoxAbsolute(var0 - var9, var1 - var9, var2 - var9, var0 + var7 + var9, var1 + var8 + var9, var2 + var7 + var9, var3, var4, var5, var6);
    }

    public static void drawString(String var0, int var1, int var2, boolean var3, Color var4) {
        String[] var5 = var0.split("\n");
        String[] var6 = var5;
        int var7 = var5.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            XiaojiaAddons.mc.fontRendererObj.drawString(var9, (float) var1, (float) var2, var4.getRGB(), var3);
            var2 += XiaojiaAddons.mc.fontRendererObj.FONT_HEIGHT + 1;
        }

    }

    public static Vector3f getRenderPos(float var0, float var1, float var2) {
        return new Vector3f(var0 - MathUtils.getX(XiaojiaAddons.mc.thePlayer), var1 - MathUtils.getY(XiaojiaAddons.mc.thePlayer), var2 - MathUtils.getZ(XiaojiaAddons.mc.thePlayer));
    }

    public static void drawOnSlot(int var0, int var1, int var2, int var3) {
        Vector2i var4 = getXYForSlot(var0, var1, var2);
        int var5 = var4.x;
        int var6 = var4.y;
        GL11.glTranslated(0.0, 0.0, 1.0);
        Gui.drawRect(var5, var6, var5 + 16, var6 + 16, var3);
        GL11.glTranslated(0.0, 0.0, -1.0);
    }

    private static Vector2i getXYForSlot(int var0, int var1, int var2) {
        ScaledResolution var3 = new ScaledResolution(Minecraft.getMinecraft());
        int var4 = (var3.getScaledWidth() - 176) / 2;
        int var5 = (var3.getScaledHeight() - 222) / 2;
        int var6 = var4 + var1;
        int var7 = var5 + var2 + (6 - (var0 - 36) / 9) * 9;
        return new Vector2i(var6, var7);
    }

    private static void drawBoxAt(float var0, float var1, float var2, int var3, int var4, int var5, int var6, float var7, float var8) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glLineWidth((float) Configs.BoxLineThickness);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderManager var9 = XiaojiaAddons.mc.getRenderManager();
        GlStateManager.translate(-var9.viewerPosX, -var9.viewerPosY, -var9.viewerPosZ);
        GlStateManager.color((float) var3 / 255.0F, (float) var4 / 255.0F, (float) var5 / 255.0F, (float) var6 / 255.0F);
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(var0 + var7, var1 + var8, var2 + var7).endVertex();
        worldRenderer.pos(var0 + var7, var1 + var8, var2 - var7).endVertex();
        worldRenderer.pos(var0 - var7, var1 + var8, var2 - var7).endVertex();
        worldRenderer.pos(var0 - var7, var1 + var8, var2 + var7).endVertex();
        worldRenderer.pos(var0 + var7, var1 + var8, var2 + var7).endVertex();
        worldRenderer.pos(var0 + var7, var1, var2 + var7).endVertex();
        worldRenderer.pos(var0 + var7, var1, var2 - var7).endVertex();
        worldRenderer.pos(var0 - var7, var1, var2 - var7).endVertex();
        worldRenderer.pos(var0 - var7, var1, var2 + var7).endVertex();
        worldRenderer.pos(var0 - var7, var1, var2 - var7).endVertex();
        worldRenderer.pos(var0 - var7, var1 + var8, var2 - var7).endVertex();
        worldRenderer.pos(var0 - var7, var1, var2 - var7).endVertex();
        worldRenderer.pos(var0 + var7, var1, var2 - var7).endVertex();
        worldRenderer.pos(var0 + var7, var1 + var8, var2 - var7).endVertex();
        worldRenderer.pos(var0 + var7, var1, var2 - var7).endVertex();
        worldRenderer.pos(var0 + var7, var1, var2 + var7).endVertex();
        worldRenderer.pos(var0 - var7, var1, var2 + var7).endVertex();
        worldRenderer.pos(var0 - var7, var1 + var8, var2 + var7).endVertex();
        worldRenderer.pos(var0 + var7, var1 + var8, var2 + var7).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glPopMatrix();
    }

    public static void drawNameAndLevel(FontRenderer var0, String var1, String var2, int var3, int var4, double var5, double var7) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) var3, (float) var4, 1.0F);
        GlStateManager.scale(var5, var5, 1.0);
        var0.drawString(var1, 1.0F, 1.0F, -1, true);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (var3 + 16 - MathUtils.ceil((double) RenderUtils.getStringWidth(var2) * var7)), (float) (var4 + 16 - MathUtils.ceil((double) RenderUtils.getStringHeight(var2) * var7)), 1.0F);
        GlStateManager.scale(var7, var7, 1.0);
        var0.drawString(var2, 0.0F, 0.0F, -1, true);
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    public static void enableESP() {
        GlStateManager.disableCull();
        GlStateManager.disableDepth();
    }

    public static void drawBoundingBox(AxisAlignedBB var0, int var1, Color var2) {
        EntityPlayerSP var3 = MinecraftUtils.getPlayer();
        float var4 = MathUtils.getX(var3);
        float var5 = MathUtils.getY(var3);
        float var6 = MathUtils.getZ(var3);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-var4, -var5, -var6);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth((float) var1);
        RenderGlobal.drawOutlinedBoundingBox(var0, var2.getRed(), var2.getGreen(), var2.getBlue(), var2.getAlpha());
        GlStateManager.translate(var4, var5, var6);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private static void drawLineWithDepthAbsolute(float var0, float var1, float var2, float var3, float var4, float var5, Color var6, int var7) {
        EntityPlayerSP var8 = MinecraftUtils.getPlayer();
        float var9 = MathUtils.getX(var8);
        float var10 = MathUtils.getY(var8);
        float var11 = MathUtils.getZ(var8);
        var0 -= var9;
        var1 -= var10;
        var2 -= var11;
        var3 -= var9;
        var4 -= var10;
        var5 -= var11;
        drawLineWithDepthRelative(var0, var1, var2, var3, var4, var5, var6, var7);
    }

    public static void drawFilledFace(BlockUtils.Face var0, Color var1) {
        drawFilledBoundingBoxAbsolute((float) var0.sx, (float) var0.sy, (float) var0.sz, (float) var0.tx, (float) var0.ty, (float) var0.tz, var1.getRed(), var1.getGreen(), var1.getBlue(), var1.getAlpha());
    }

    public static void drawBoundingBoxAtBlock(BlockPos var0, Color var1) {
        drawBoxAt((float) var0.getX() + 0.5F, (float) var0.getY() + 0.5F, (float) var0.getZ() + 0.5F, var1.getRed(), var1.getGreen(), var1.getBlue(), var1.getAlpha(), 0.5F, 0.5F);
    }

    public static void drawTexture(ResourceLocation var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        XiaojiaAddons.mc.getTextureManager().bindTexture(var0);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        Gui.drawModalRectWithCustomSizedTexture(var1, var2, (float) var7, (float) var8, var3, var4, (float) var5, (float) var6);
    }

    public static void drawLine(float var0, float var1, float var2, float var3, float var4, float var5, Color var6, int var7) {
        drawLineWithDepthAbsolute(var0, var1, var2, var3, var4, var5, var6, var7);
    }

    public static void drawBoxAtBlock(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, float var9) {
        drawFilledBoundingBoxAbsolute((float) var0 - var9, (float) var1 - var9, (float) var2 - var9, (float) (var0 + var7) + var9, (float) (var1 + var8) + var9, (float) (var2 + var7) + var9, var3, var4, var5, var6);
    }

    public static void drawFilledBoxAtEntity(Entity var0, int var1, int var2, int var3, int var4, float var5, float var6, float var7) {
        float var8 = MathUtils.getX(var0);
        float var9 = MathUtils.getY(var0) - var7 * var6;
        float var10 = MathUtils.getZ(var0);
        drawFilledBoundingBoxAbsolute(var8 - var5, var9, var10 - var5, var8 + var5, var9 + var6, var10 + var5, var1, var2, var3, var4);
    }
}
