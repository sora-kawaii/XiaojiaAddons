package com.xiaojia.xiaojiaaddons.Features.Remote.API;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HypixelPlayerData {

   private final JsonObject playerData;

   private final String uuid;

   public JsonObject getPlayerData() {
      return this.playerData;
   }

   public HypixelPlayerData(String var1, String var2) throws ApiException {
      HttpClient var3 = new HttpClient("https://api.hypixel.net/player?key=" + var2 + "&uuid=" + var1);
      String var4 = var3.getrawresponse();
      if (var4 == null) {
         throw new ApiException("No response from Hypixel's Api");
      } else {
         JsonParser var5 = new JsonParser();
         JsonObject var6 = var5.parse(var4).getAsJsonObject();
         if (var6 == null) {
            throw new ApiException("Cannot parse response from Hypixel's Api");
         } else {
            JsonElement var7;
            if (!var6.get("success").getAsBoolean()) {
               var7 = var6.get("cause");
               String var9 = var7.getAsString();
               if (var9 == null) {
                  throw new ApiException("Failed to retreive data from Hypixel's Api for this player");
               } else {
                  throw new ApiException(var9);
               }
            } else {
               var7 = var6.get("player");
               if (var7 != null && var7.isJsonObject()) {
                  JsonObject var8 = var7.getAsJsonObject();
                  if (var8 == null) {
                     throw new ApiException("An error occured while parsing data for this player on Hypixel's Api");
                  } else {
                     this.playerData = var8;
                     this.uuid = var1;
                  }
               } else {
                  throw new ApiException("This player never joined Hypixel, it might be a nick.");
               }
            }
         }
      }
   }

   public String getUuid() {
      return this.uuid;
   }
}
