package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.TextSetting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class TextInput {

    public static int width = 130;
    public ConfigGuiNew gui;
    public int y;
    public GuiTextField textField;
    public TextSetting setting;

    public int height;

    public GuiButton button;

    public int x;

    public TextInput(ConfigGuiNew var1, TextSetting var2, int var3, int var4) {
        this.setting = var2;
        this.height = 10;
        this.x = var3 + var2.width - 5 - width;
        this.y = var4 + 5;
        this.gui = var1;
        this.textField = new GuiTextField(0, XiaojiaAddons.mc.fontRendererObj, this.x, this.y, width, this.height);
        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setMaxStringLength(255);
        this.textField.setFocused(false);
        this.textField.setText((String) var2.get(String.class));
    }

    public void clearText() {
        this.textField.setText("");
    }

    public void keyTyped(char var1, int var2) {
        this.textField.textboxKeyTyped(var1, var2);
        this.setting.set(this.textField.getText());
    }

    public boolean isFocused() {
        return this.textField.isFocused();
    }

    public void setFocused(boolean var1) {
        this.textField.setFocused(var1);
    }

    public void mouseClicked(int var1, int var2, int var3) {
        boolean var4 = this.textField.isFocused();
        this.textField.mouseClicked(var1, var2, var3);
        if (var4 && !this.textField.isFocused()) {
            this.gui.update(this.setting, true);
        }

    }

    public void draw() {
        this.textField.drawTextBox();
        Gui.drawRect(this.x, this.y + 9, this.x + width, this.y + 10, -1);
    }
}
