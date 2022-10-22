package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.awt.*;
import java.io.IOException;

public class BugReport extends GuiScreen {

    private static final int guiWidth = 200;

    private static final int guiHeight = 12;

    private static GuiTextField reportBar = null;

    protected void keyTyped(char var1, int var2) throws IOException {
        super.keyTyped(var1, var2);
        reportBar.textboxKeyTyped(var1, var2);
    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        super.drawScreen(var1, var2, var3);
        int var4 = this.getStartX();
        int var5 = this.getStartY();
        this.drawGradientRect(var4, var5 - 5, var4 + 200, var5 + 12, (new Color(25, 25, 25, 65)).getRGB(), (new Color(25, 25, 25, 100)).getRGB());
        this.drawCenteredString(this.fontRendererObj, "Briefly introduce the bug:", this.width / 2, this.getStartY() - 25, -1);
        reportBar.drawTextBox();
    }

    private int getStartY() {
        return (this.height - 12) / 2;
    }

    protected void mouseClicked(int var1, int var2, int var3) throws IOException {
        super.mouseClicked(var1, var2, var3);
        reportBar.mouseClicked(var1, var2, var3);
    }

    public void updateScreen() {
        super.updateScreen();
        reportBar.updateCursorCounter();
    }

    public void initGui() {
        reportBar = new GuiTextField(1, this.fontRendererObj, this.getStartX(), this.getStartY(), 200, 12);
        reportBar.setMaxStringLength(100);
        reportBar.setEnableBackgroundDrawing(false);
        reportBar.setText("");
        reportBar.setFocused(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.getStartY() + 20, "Submit Report") {
            public boolean mousePressed(Minecraft var1, int var2, int var3) {
                if (var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height) {
                    XiaojiaChat.bugReport(BugReport.reportBar.getText());
                    MinecraftUtils.getPlayer().closeScreen();
                    ChatLib.chat("Reporting bug...");
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private int getStartX() {
        return (this.width - 200) / 2;
    }
}
