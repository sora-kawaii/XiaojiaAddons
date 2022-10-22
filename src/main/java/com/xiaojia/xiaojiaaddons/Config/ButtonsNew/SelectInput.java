package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.SelectSetting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.Minecraft;

public class SelectInput extends Button {

    private final int leftWidth;
    private final int rightWidth;
    private final int maxLen;
    private final int gap;
    public SelectSetting setting;

    public SelectInput(ConfigGuiNew var1, SelectSetting var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        this.leftWidth = XiaojiaAddons.mc.fontRendererObj.getStringWidth("<");
        this.rightWidth = XiaojiaAddons.mc.fontRendererObj.getStringWidth(">");
        this.gap = 3;
        this.setting = var2;
        int var5 = 0;
        String[] var6 = var2.options;
        int var7 = var6.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            var5 = Math.max(var5, XiaojiaAddons.mc.fontRendererObj.getStringWidth(var9));
        }

        this.maxLen = var5;
        int var10001 = var5 + this.rightWidth + this.leftWidth;
        this.getClass();
        this.width = var10001 + 3 * 2;
        this.height = 10;
        this.updateText();
        this.xPosition += var2.width - 5;
        this.yPosition += 5;
    }

    private boolean leftHovered(int var1, int var2) {
        boolean var10000;
        if (var1 >= this.xPosition - this.width && var2 >= this.yPosition) {
            int var10001 = this.xPosition - this.width + this.leftWidth;
            this.getClass();
            if (var1 < var10001 + 3 && var2 < this.yPosition + this.height) {
                var10000 = true;
                return var10000;
            }
        }

        var10000 = false;
        return var10000;
    }

    public boolean mousePressed(Minecraft var1, int var2, int var3) {
        if (!this.rightHovered(var2, var3) && !this.leftHovered(var2, var3)) {
            return false;
        } else {
            if (this.rightHovered(var2, var3)) {
                this.setting.set((Integer) this.setting.get(Integer.class) + 1);
            }

            if (this.leftHovered(var2, var3)) {
                this.setting.set((Integer) this.setting.get(Integer.class) - 1);
            }

            this.updateText();
            this.gui.update(this.setting, true);
            return true;
        }
    }

    private boolean rightHovered(int var1, int var2) {
        int var10001 = this.xPosition - this.rightWidth;
        this.getClass();
        return var1 >= var10001 - 3 && var2 >= this.yPosition && var1 < this.xPosition && var2 < this.yPosition + this.height;
    }

    public void draw(Minecraft var1, int var2, int var3) {
        var1.fontRendererObj.drawString((this.leftHovered(var2, var3) ? "§a" : "§7") + "<", this.xPosition - this.width, this.yPosition, -1);
        int var10000 = this.xPosition - this.width + this.leftWidth;
        this.getClass();
        int var4 = var10000 + 3;
        int var5 = var1.fontRendererObj.getStringWidth(this.displayString);
        var1.fontRendererObj.drawString(this.displayString, var4 + (this.maxLen - var5) / 2, this.yPosition, -1);
        var1.fontRendererObj.drawString((this.rightHovered(var2, var3) ? "§a" : "§7") + ">", this.xPosition - this.rightWidth, this.yPosition, -1);
    }

    public void updateText() {
        this.displayString = this.setting.options[(Integer) this.setting.get(Integer.class)];
    }
}
