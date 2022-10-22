package com.xiaojia.xiaojiaaddons.utils;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {
   public static JsonObject getJsonObject(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsJsonObject() : null;
   }

   public static long getLong(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsLong() : 0L;
   }

   public static String getString(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsString() : null;
   }

   public static List getList(JsonObject var0, String var1) {
      ArrayList var2 = Lists.newArrayList();
      JsonElement var3 = var0.get(var1);
      if (var3 != null && !var3.isJsonNull()) {
         Iterator var4 = var3.getAsJsonArray().iterator();

         while(var4.hasNext()) {
            JsonElement var5 = (JsonElement)var4.next();
            var2.add(var5.getAsString());
         }

         return var2;
      } else {
         return var2;
      }
   }

   public static int getInt(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsInt() : 0;
   }

   public static double getDouble(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && !var2.isJsonNull() ? var2.getAsDouble() : 0.0;
   }
}
