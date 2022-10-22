package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Events.RenderEntityModelEvent;
import java.awt.Color;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class OutlineUtils {
   private static void setupFBO(Framebuffer var0) {
      EXTFramebufferObject.glDeleteRenderbuffersEXT(var0.depthBuffer);
      int var1 = EXTFramebufferObject.glGenRenderbuffersEXT();
      EXTFramebufferObject.glBindRenderbufferEXT(36161, var1);
      EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, XiaojiaAddons.mc.displayWidth, XiaojiaAddons.mc.displayHeight);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, var1);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, var1);
   }

   private static void renderFour(Color var0) {
      setColor(var0);
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(10754);
      GL11.glPolygonOffset(1.0F, -2000000.0F);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
   }

   private static void setColor(Color var0) {
      GL11.glColor4d((double)((float)var0.getRed() / 255.0F), (double)((float)var0.getGreen() / 255.0F), (double)((float)var0.getBlue() / 255.0F), (double)((float)var0.getAlpha() / 255.0F));
   }

   private static void renderThree() {
      GL11.glStencilFunc(514, 1, 15);
      GL11.glStencilOp(7680, 7680, 7680);
      GL11.glPolygonMode(1032, 6913);
   }

   private static void renderFive() {
      GL11.glPolygonOffset(1.0F, 2000000.0F);
      GL11.glDisable(10754);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(2960);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glEnable(3042);
      GL11.glEnable(2896);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      GL11.glPopAttrib();
   }

   private static void checkSetupFBO() {
      Framebuffer var0 = XiaojiaAddons.mc.getFramebuffer();
      if (var0 != null && var0.depthBuffer > -1) {
         setupFBO(var0);
         var0.depthBuffer = -1;
      }

   }

   public static void outlineEntity(RenderEntityModelEvent var0, Color var1, int var2) {
      outlineEntity(var0.model, var0.entity, var0.limbSwing, var0.limbSwingAmount, var0.ageInTicks, var0.headYaw, var0.headPitch, var0.scaleFactor, var1, var2);
   }

   private static void renderOne(float var0) {
      checkSetupFBO();
      GL11.glPushAttrib(1048575);
      GL11.glDisable(3008);
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(var0);
      GL11.glEnable(2848);
      GL11.glEnable(2960);
      GL11.glClear(1024);
      GL11.glClearStencil(15);
      GL11.glStencilFunc(512, 1, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6913);
   }

   private static void renderTwo() {
      GL11.glStencilFunc(512, 0, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1032, 6914);
   }

   public static void outlineEntity(ModelBase var0, Entity var1, float var2, float var3, float var4, float var5, float var6, float var7, Color var8, int var9) {
      boolean var10 = XiaojiaAddons.mc.gameSettings.fancyGraphics;
      float var11 = XiaojiaAddons.mc.gameSettings.gammaSetting;
      XiaojiaAddons.mc.gameSettings.fancyGraphics = false;
      XiaojiaAddons.mc.gameSettings.gammaSetting = Float.MAX_VALUE;
      GlStateManager.resetColor();
      setColor(var8);
      renderOne((float)var9);
      var0.render(var1, var2, var3, var4, var5, var6, var7);
      setColor(var8);
      renderTwo();
      var0.render(var1, var2, var3, var4, var5, var6, var7);
      setColor(var8);
      renderThree();
      var0.render(var1, var2, var3, var4, var5, var6, var7);
      setColor(var8);
      renderFour(var8);
      var0.render(var1, var2, var3, var4, var5, var6, var7);
      setColor(var8);
      renderFive();
      setColor(Color.WHITE);
      XiaojiaAddons.mc.gameSettings.fancyGraphics = var10;
      XiaojiaAddons.mc.gameSettings.gammaSetting = var11;
   }
}
