package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class CloseButton extends GuiButton {

    ConfigGuiNew gui;

    public CloseButton(ConfigGuiNew var1, int var2, int var3, int var4, int var5, int var6, String var7) {
        super(var2, var3, var4, var5, var6, var7);
        this.gui = var1;
    }

    public void drawButton(Minecraft var1, int var2, int var3) {
    }

    public boolean mousePressed(Minecraft var1, int var2, int var3) {
        if (var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height) {
            MinecraftUtils.getPlayer().closeScreen();
            return true;
        } else {
            return false;
        }
    }
}
