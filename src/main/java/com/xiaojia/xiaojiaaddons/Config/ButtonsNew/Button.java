package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public abstract class Button extends GuiButton {

    public ConfigGuiNew gui;

    public Setting setting;

    public Button(ConfigGuiNew var1, Setting var2, int var3, int var4) {
        super(0, var3, var4, "");
        this.setting = var2;
        this.gui = var1;
    }

    public static Button buttonFromSetting(ConfigGuiNew var0, Setting var1, int var2, int var3) {
        switch (var1.annotation.type()) {
            case BOOLEAN:
                return new SwitchInput(var0, (BooleanSetting) var1, var2, var3);
            case FOLDER:
                return new FolderInput(var0, (FolderSetting) var1, var2, var3);
            case NUMBER:
                return new NumberInput(var0, (NumberSetting) var1, var2, var3);
            case SELECT:
                return new SelectInput(var0, (SelectSetting) var1, var2, var3);
            default:
                return null;
        }
    }

    public void drawButton(Minecraft var1, int var2, int var3) {
    }

    public abstract void draw(Minecraft var1, int var2, int var3);
}
