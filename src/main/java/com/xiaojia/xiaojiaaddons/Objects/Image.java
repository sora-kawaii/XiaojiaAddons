package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Image {

   private int textureWidth;

   private DynamicTexture texture;

   private BufferedImage image;

   private int textureHeight;

   @SubscribeEvent
   public void onRender(RenderGameOverlayEvent.Pre var1) {
      if (this.image != null) {
         this.texture = new DynamicTexture(this.image);
         this.image = null;
         MinecraftForge.EVENT_BUS.unregister(this);
      }

   }

   public DynamicTexture getTexture() {
      if (this.texture == null) {
         try {
            this.texture = new DynamicTexture(this.image);
            this.image = null;
            MinecraftForge.EVENT_BUS.unregister(this);
         } catch (Exception var2) {
            System.out.println("Trying to bake texture in a non-rendering context.");
            throw var2;
         }
      }

      return this.texture;
   }

   public Image(BufferedImage var1) {
      this.image = var1;
      this.textureWidth = var1.getWidth();
      this.textureHeight = var1.getHeight();
      MinecraftForge.EVENT_BUS.register(this);
   }

   public Image(String var1) {
      ResourceLocation var2 = new ResourceLocation("xiaojiaaddons:" + var1);

      try {
         InputStream var3 = XiaojiaAddons.mc.getResourceManager().getResource(var2).getInputStream();
         this.image = ImageIO.read(var3);
         this.textureWidth = this.image.getWidth();
         this.textureHeight = this.image.getHeight();
         MinecraftForge.EVENT_BUS.register(this);
      } catch (Exception var4) {
         var4.printStackTrace();
         System.err.println("Failed to load image.");
      }

   }
}
