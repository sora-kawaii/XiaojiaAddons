package com.xiaojia.xiaojiaaddons.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Features.Remote.RemoteUtils;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MinecraftUtils {

   private static final HashMap cachedUuids = new HashMap();

   public static World getWorld() {
      return XiaojiaAddons.mc.theWorld;
   }

   public static NetworkPlayerInfo getPlayerInfo(String var0) {
      EntityPlayer var1 = getWorld().getPlayerEntityByName(var0);
      return var1 == null ? null : XiaojiaAddons.mc.getNetHandler().getPlayerInfo(var0);
   }

   public static String getUUIDFromName(String var0) {
      if (cachedUuids.containsKey(var0)) {
         return (String)cachedUuids.get(var0);
      } else {
         String var1 = "https://api.mojang.com/users/profiles/minecraft/" + var0;
         String var2 = RemoteUtils.get(var1, new ArrayList(), false);
         JsonObject var3 = (JsonObject)(new Gson()).fromJson(var2, JsonObject.class);
         String var4 = var3.get("id").getAsString();
         if (var4 != null && var4.length() == 32) {
            cachedUuids.put(var0, var4);
         }

         System.err.println("Got uuid for name " + var0 + ": " + var4);
         return var4;
      }
   }

   public static BufferedImage getHeadFromMC(String var0) {
      try {
         NetworkPlayerInfo var1 = getPlayerInfo(var0);
         ITextureObject var2 = XiaojiaAddons.mc.getTextureManager().getTexture(var1.getLocationSkin());
         Field var3 = ThreadDownloadImageData.class.getDeclaredField("bufferedImage");
         var3.setAccessible(true);
         BufferedImage var4 = (BufferedImage)var3.get(var2);
         BufferedImage var5 = new BufferedImage(8, 8, 2);
         BufferedImage var6 = var4.getSubimage(8, 8, 8, 8);
         BufferedImage var7 = var4.getSubimage(40, 8, 8, 8);
         Graphics var8 = var5.getGraphics();
         var8.drawImage(var6, 0, 0, (ImageObserver)null);
         var8.drawImage(var7, 0, 0, (ImageObserver)null);
         return var5;
      } catch (Exception var9) {
         return null;
      }
   }

   public static EntityPlayerSP getPlayer() {
      return XiaojiaAddons.mc.thePlayer;
   }

   public static BufferedImage getHead(String var0) throws IOException {
      BufferedImage var1 = getHeadFromMC(var0);
      if (var1 == null) {
         Image var2 = ImageIO.read(new URL("https://crafatar.com/avatars/" + getUUIDFromName(var0))).getScaledInstance(8, 8, 4);
         var1 = new BufferedImage(8, 8, 6);
         Graphics var3 = var1.getGraphics();
         var3.drawImage(var2, 0, 0, (ImageObserver)null);
      }

      return var1;
   }
}
