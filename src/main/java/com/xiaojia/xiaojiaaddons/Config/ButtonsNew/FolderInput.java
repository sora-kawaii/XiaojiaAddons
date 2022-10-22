package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import net.minecraft.client.Minecraft;

public class FolderInput extends Button {

   public FolderSetting setting;

   public void draw(Minecraft var1, int var2, int var3) {
   }

   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      if (var2 >= this.xPosition && var3 >= this.yPosition && var2 <= this.xPosition + this.width && var3 <= this.yPosition + this.height) {
         boolean var4 = !(Boolean)this.setting.get(Boolean.class);
         this.setting.setRecursively(var4);
         this.gui.update(this.setting, var4);
         return true;
      } else {
         return false;
      }
   }

   public FolderInput(ConfigGuiNew var1, FolderSetting var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.setting = var2;
      this.width = var2.width;
      this.height = var2.height;
   }
}
