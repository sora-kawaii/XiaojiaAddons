package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;

public class RoomLoader {

   public static ArrayList maps = new ArrayList();

   public static void load() {
      try {
         ResourceLocation var0 = new ResourceLocation("xiaojiaaddons:rooms.json");
         InputStream var1 = XiaojiaAddons.mc.getResourceManager().getResource(var0).getInputStream();
         JsonParser var2 = new JsonParser();
         BufferedReader var3 = new BufferedReader(new InputStreamReader(var1));
         JsonObject var4 = var2.parse(var3).getAsJsonObject();
         JsonArray var5 = var4.getAsJsonArray("rooms");
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            JsonElement var7 = (JsonElement)var6.next();
            Data var8 = new Data();
            JsonObject var9 = var7.getAsJsonObject();
            var8.name = var9.get("name").getAsString();
            var8.type = var9.get("type").getAsString();
            var8.secrets = var9.get("secrets").getAsInt();
            var8.cores = new ArrayList();
            JsonArray var10 = var9.getAsJsonArray("cores");
            Iterator var11 = var10.iterator();

            while(var11.hasNext()) {
               JsonElement var12 = (JsonElement)var11.next();
               var8.cores.add(var12.getAsInt());
            }

            maps.add(var8);
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }
}
