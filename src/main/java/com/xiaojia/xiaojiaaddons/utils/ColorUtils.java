package com.xiaojia.xiaojiaaddons.utils;

import java.awt.Color;

public class ColorUtils {

   public static final String[] colors = new String[]{"&a", "&b", "&c", "&d", "&e", "&f", "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9"};

   public static final Color[] realColors = new Color[]{new Color(5635925), new Color(5636095), new Color(16733525), new Color(16733695), new Color(16777045), new Color(16777215), new Color(0), new Color(170), new Color(43520), new Color(43690), new Color(11141120), new Color(11141290), new Color(16755200), new Color(11184810), new Color(5592405), new Color(5592575)};

   public static Color getColorFromCode(String var0) {
      for(int var1 = 0; var1 < colors.length; ++var1) {
         if (colors[var1].equals(var0)) {
            return realColors[var1];
         }
      }

      return null;
   }

   public static Color getColorFromLong(long var0) {
      return new Color((float)((int)(var0 >> 16 & 255L)) / 255.0F, (float)((int)(var0 >> 8 & 255L)) / 255.0F, (float)((int)(var0 & 255L)) / 255.0F, (float)((int)(var0 >> 24 & 255L)) / 255.0F);
   }

   public static Color getColorFromString(String var0, Color var1) {
      try {
         return getColorFromLong(Long.parseLong(var0, 16));
      } catch (Exception var3) {
         System.out.println("/" + var0 + "/");
         return var1;
      }
   }
}
