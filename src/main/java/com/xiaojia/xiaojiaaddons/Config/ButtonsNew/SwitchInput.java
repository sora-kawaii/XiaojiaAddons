package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.Colors;
import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import net.minecraft.client.Minecraft;

public class SwitchInput extends Button {

   public BooleanSetting setting;

   public SwitchInput(ConfigGuiNew var1, BooleanSetting var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.setting = var2;
      this.width = var2.width;
      this.height = var2.height;
   }

   public void draw(Minecraft var1, int var2, int var3) {
      int var4 = this.xPosition + this.width - 30;
      int var5 = this.yPosition + 5;
      drawRect(var4, var5 + 3, var4 + 25, var5 + 6, Colors.WHITE.getRGB());
      if ((Boolean)this.setting.get(Boolean.class)) {
         drawRect(var4 + 25 - 9, var5, var4 + 25, var5 + 9, Colors.GREEN.getRGB());
      } else {
         drawRect(var4, var5, var4 + 9, var5 + 9, Colors.RED.getRGB());
      }

   }

   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      if (var2 >= this.xPosition && var3 >= this.yPosition && var2 <= this.xPosition + this.width && var3 <= this.yPosition + this.height) {
         boolean var4 = !(Boolean)this.setting.get(Boolean.class);
         this.setting.set(var4);
         this.gui.update(this.setting, var4);
         return true;
      } else {
         return false;
      }
   }
}
