package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

   private static final Tessellator tessellator = Tessellator.getInstance();

   private static Integer drawMode;

   private static final WorldRenderer worldRenderer;

   private static boolean retainTransforms;

   private static Long colorized;

   public static void retainTransforms(boolean var0) {
      retainTransforms = var0;
      finishDraw();
   }

   public static int getScreenHeight() {
      return (new ScaledResolution(XiaojiaAddons.mc)).getScaledHeight();
   }

   public static void drawStringWithShadow(String var0, float var1, float var2) {
      XiaojiaAddons.mc.fontRendererObj.drawString(ChatLib.addColor(var0), var1, var2, colorized == null ? -1 : colorized.intValue(), true);
      finishDraw();
   }

   public static void translate(float var0, float var1, float var2) {
      GlStateManager.translate(var0, var1, var2);
   }

   public static void drawString(String var0, float var1, int var2, int var3, Color var4, boolean var5) {
      GlStateManager.enableBlend();
      GlStateManager.scale(var1, var1, var1);
      GuiUtils.drawString(var0, MathUtils.ceil((float)var2 / var1), MathUtils.ceil((float)var3 / var1), var5, var4);
      GlStateManager.disableBlend();
      finishDraw();
   }

   public static void drawRect(int var0, int var1, int var2, int var3, int var4) {
      Gui.drawRect(var1, var2, var1 + var3, var2 + var4, var0);
      finishDraw();
   }

   public static void translate(double var0, double var2) {
      translate((float)var0, (float)var2);
   }

   public static int getStringWidth(String var0) {
      return XiaojiaAddons.mc.fontRendererObj.getStringWidth(var0);
   }

   static {
      worldRenderer = tessellator.getWorldRenderer();
      colorized = null;
      drawMode = null;
      retainTransforms = false;
   }

   public static void drawImage(Image var0, double var1, double var3, double var5, double var7) {
      if (colorized == null) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      }

      GlStateManager.enableBlend();
      GlStateManager.scale(1.0F, 1.0F, 50.0F);
      GlStateManager.bindTexture(var0.getTexture().getGlTextureId());
      GlStateManager.enableTexture2D();
      worldRenderer.begin(drawMode == null ? 7 : drawMode, DefaultVertexFormats.POSITION_TEX);
      worldRenderer.pos(var1, var3 + var7, 0.0).tex(0.0, 1.0).endVertex();
      worldRenderer.pos(var1 + var5, var3 + var7, 0.0).tex(1.0, 1.0).endVertex();
      worldRenderer.pos(var1 + var5, var3, 0.0).tex(1.0, 0.0).endVertex();
      worldRenderer.pos(var1, var3, 0.0).tex(0.0, 0.0).endVertex();
      tessellator.draw();
      finishDraw();
   }

   public static int getStringHeight(String var0) {
      return XiaojiaAddons.mc.fontRendererObj.FONT_HEIGHT;
   }

   public static void start() {
      GlStateManager.pushMatrix();
   }

   public static void translate(float var0, float var1) {
      GlStateManager.translate(var0, var1, 0.0F);
   }

   private static void doColor(long var0) {
      int var2 = (int)var0;
      if (colorized == null) {
         float var3 = (float)(var2 >> 24 & 255) / 255.0F;
         float var4 = (float)(var2 >> 16 & 255) / 255.0F;
         float var5 = (float)(var2 >> 8 & 255) / 255.0F;
         float var6 = (float)(var2 & 255) / 255.0F;
         GlStateManager.color(var4, var5, var6, var3);
      }

   }

   public static void rotate(float var0) {
      GL11.glRotatef(var0, 0.0F, 0.0F, 1.0F);
   }

   public static void scale(float var0, float var1) {
      GlStateManager.scale(var0, var1, 1.0F);
   }

   public static void end() {
      GlStateManager.popMatrix();
   }

   public static int getScreenWidth() {
      return (new ScaledResolution(XiaojiaAddons.mc)).getScaledWidth();
   }

   public static void finishDraw() {
      if (!retainTransforms) {
         colorized = null;
         drawMode = null;
         GL11.glPopMatrix();
         GL11.glPushMatrix();
      }

   }
}
