package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class NumberInput extends Button {

   private final int maxLen;

   private final int superMinusWidth;

   private final int minusWidth;

   private final int superPlusWidth;

   private final int gap;

   private final int plusWidth;

   public NumberSetting setting;

   private boolean minusHovered(int var1, int var2) {
      int var10001 = this.xPosition - this.width + this.superMinusWidth;
      this.getClass();
      boolean var10000;
      if (var1 >= var10001 + 3 && var2 >= this.yPosition) {
         var10001 = this.xPosition - this.width + this.superMinusWidth;
         this.getClass();
         var10001 = var10001 + 3 + this.minusWidth;
         this.getClass();
         if (var1 < var10001 + 3 && var2 < this.yPosition + this.height) {
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   private boolean superMinusHovered(int var1, int var2) {
      boolean var10000;
      if (var1 >= this.xPosition - this.width && var2 >= this.yPosition) {
         int var10001 = this.xPosition - this.width + this.superMinusWidth;
         this.getClass();
         if (var1 < var10001 + 3 && var2 < this.yPosition + this.height) {
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   public void updateText() {
      this.displayString = (this.setting.prefix == null ? "" : this.setting.prefix) + this.setting.get(Integer.class) + (this.setting.suffix == null ? "" : this.setting.suffix);
   }

   private boolean plusHovered(int var1, int var2) {
      int var10001 = this.xPosition - this.superPlusWidth;
      this.getClass();
      var10001 = var10001 - 3 - this.plusWidth;
      this.getClass();
      boolean var10000;
      if (var1 >= var10001 - 3 && var2 >= this.yPosition) {
         var10001 = this.xPosition - this.superPlusWidth;
         this.getClass();
         if (var1 < var10001 - 3 && var2 < this.yPosition + this.height) {
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   public NumberInput(ConfigGuiNew var1, NumberSetting var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.minusWidth = XiaojiaAddons.mc.fontRendererObj.getStringWidth("-");
      this.plusWidth = XiaojiaAddons.mc.fontRendererObj.getStringWidth("+");
      this.superMinusWidth = XiaojiaAddons.mc.fontRendererObj.getStringWidth("--");
      this.superPlusWidth = XiaojiaAddons.mc.fontRendererObj.getStringWidth("++");
      this.gap = 3;
      this.setting = var2;
      this.height = 10;
      this.width = XiaojiaAddons.mc.fontRendererObj.getStringWidth(var2.max + "");
      if (var2.prefix != null) {
         this.width += XiaojiaAddons.mc.fontRendererObj.getStringWidth(var2.prefix);
      }

      if (var2.suffix != null) {
         this.width += XiaojiaAddons.mc.fontRendererObj.getStringWidth(var2.suffix);
      }

      this.maxLen = this.width;
      this.width += this.plusWidth + this.minusWidth + 6;
      this.width += this.superPlusWidth + this.superMinusWidth + 6;
      this.xPosition += var2.width - 5;
      this.yPosition += 5;
      this.updateText();
   }

   public void draw(Minecraft var1, int var2, int var3) {
      if ((Integer)this.setting.get(Integer.class) > this.setting.min) {
         var1.fontRendererObj.drawString((this.superMinusHovered(var2, var3) ? "§c" : "§7") + "--", this.xPosition - this.width, this.yPosition, -1);
      }

      FontRenderer var10000 = var1.fontRendererObj;
      String var10001 = (this.minusHovered(var2, var3) ? "§c" : "§7") + "-";
      int var10002 = this.xPosition - this.width;
      this.getClass();
      var10000.drawString(var10001, var10002 + 3 + this.superMinusWidth, this.yPosition, -1);
      int var6 = this.xPosition - this.width + this.minusWidth;
      this.getClass();
      var6 = var6 + 3 + this.superPlusWidth;
      this.getClass();
      int var4 = var6 + 3;
      int var5 = var1.fontRendererObj.getStringWidth(this.displayString);
      var1.fontRendererObj.drawString(this.displayString, var4 + (this.maxLen - var5) / 2, this.yPosition, -1);
      var10000 = var1.fontRendererObj;
      var10001 = (this.plusHovered(var2, var3) ? "§a" : "§7") + "+";
      var10002 = this.xPosition - this.plusWidth;
      this.getClass();
      var10000.drawString(var10001, var10002 - 3 - this.superPlusWidth, this.yPosition, -1);
      if ((Integer)this.setting.get(Integer.class) < this.setting.max) {
         var1.fontRendererObj.drawString((this.superPlusHovered(var2, var3) ? "§a" : "§7") + "++", this.xPosition - this.superPlusWidth, this.yPosition, -1);
      }

   }

   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      int var4 = this.setting.min;
      int var5 = this.setting.max;
      int var6 = this.setting.step;
      int var7 = (Integer)this.setting.get(Integer.class);
      int var8;
      if (this.plusHovered(var2, var3)) {
         var8 = var7 + var6;
      } else if (this.minusHovered(var2, var3)) {
         var8 = var7 - var6;
      } else if (this.superPlusHovered(var2, var3)) {
         var8 = var7 + var6 * 10;
      } else {
         if (!this.superMinusHovered(var2, var3)) {
            return false;
         }

         var8 = var7 - var6 * 10;
      }

      if (var8 <= var4) {
         var8 = var4;
      }

      if (var8 >= var5) {
         var8 = var5;
      }

      this.setting.set(var8);
      this.updateText();
      this.gui.update(this.setting, true);
      return true;
   }

   private boolean superPlusHovered(int var1, int var2) {
      int var10001 = this.xPosition - this.superPlusWidth;
      this.getClass();
      return var1 >= var10001 - 3 && var2 >= this.yPosition && var1 < this.xPosition && var2 < this.yPosition + this.height;
   }
}
