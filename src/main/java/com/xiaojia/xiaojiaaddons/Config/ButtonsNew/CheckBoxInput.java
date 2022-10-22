package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import net.minecraft.client.Minecraft;

public class CheckBoxInput extends Button {

   public BooleanSetting setting;

   public CheckBoxInput(ConfigGuiNew var1, BooleanSetting var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.setting = var2;
      this.width = this.height = 9;
      this.xPosition -= this.width;
   }

   public void draw(Minecraft var1, int var2, int var3) {
   }

   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      if (var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height) {
         boolean var4 = !(Boolean)this.setting.get(Boolean.class);
         this.setting.set(var4);
         this.gui.update(this.setting, var4);
         return true;
      } else {
         return false;
      }
   }
}
