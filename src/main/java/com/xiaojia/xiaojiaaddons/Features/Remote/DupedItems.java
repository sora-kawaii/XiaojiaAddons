package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DupedItems {

   private static final String url = "https://api.ner.gg/duped";

   private long lastFetch = 0L;

   private static final HashSet dupedUuids = new HashSet();

   private static final String dupedString = "§c§lDup";

   private static final double dupedScale = 0.7;

   private static void update(JsonObject var0) {
      JsonArray var1 = var0.getAsJsonArray("dupedUuids");
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         JsonElement var3 = (JsonElement)var2.next();
         String var4 = var3.getAsString();
         dupedUuids.add(var4);
      }

   }

   public static void load() {
      try {
         ResourceLocation var0 = new ResourceLocation("xiaojiaaddons:dupedUuids.json");
         InputStream var1 = XiaojiaAddons.mc.getResourceManager().getResource(var0).getInputStream();
         JsonParser var2 = new JsonParser();
         BufferedReader var3 = new BufferedReader(new InputStreamReader(var1));
         JsonObject var4 = var2.parse(var3).getAsJsonObject();
         update(var4);
      } catch (Exception var5) {
      }

   }

   @SubscribeEvent
   public void onRenderItem(ItemDrawnEvent var1) {
      if (Configs.ShowDupedItems) {
         ItemStack var2 = var1.itemStack;
         if (isItemDuped(var2)) {
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            GlStateManager.pushMatrix();
            GlStateManager.translate((double)var1.x, (double)(var1.y + 16 - MathUtils.ceil((double)RenderUtils.getStringHeight("§c§lDup") * 0.7)), 1.0);
            GlStateManager.scale(0.7, 0.7, 1.0);
            var1.renderer.drawString("§c§lDup", 1.0F, 1.0F, -1, true);
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }

      }
   }

   public static boolean isItemDuped(ItemStack var0) {
      return dupedUuids.contains(NBTUtils.getUUID(var0));
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Configs.ShowDupedItems) {
         if (TimeUtils.curTime() - this.lastFetch > 600000L) {
            this.lastFetch = TimeUtils.curTime();
            (new Thread(() -> {
               String var0 = RemoteUtils.get("https://api.ner.gg/duped", new ArrayList(), false);
               JsonParser var1 = new JsonParser();
               JsonObject var2 = var1.parse(var0).getAsJsonObject();
               update(var2);
            })).start();
         }

      }
   }
}
