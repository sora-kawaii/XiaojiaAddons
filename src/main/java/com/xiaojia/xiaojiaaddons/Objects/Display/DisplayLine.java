package com.xiaojia.xiaojiaaddons.Objects.Display;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;

import java.awt.*;

public class DisplayLine {

    private boolean shadow = false;

    private float scale = 1.0F;

    private String text;

    public DisplayLine(String var1) {
        this.text = ChatLib.addColor(var1);
    }

    public void setText(String var1) {
        this.text = var1;
    }

    public int getHeight() {
        return MathUtils.ceil(this.scale * 9.0F);
    }

    public DisplayLine setScale(float var1) {
        this.scale = var1;
        return this;
    }

    public int getTextWidth() {
        return MathUtils.ceil(this.scale * (float) RenderUtils.getStringWidth(this.text));
    }

    public void draw(int var1, int var2, int var3, Background var4, int var5, int var6, Align var7) {
        int var8 = 0;
        int var9 = 0;
        if (var7 == Align.CENTER) {
            var8 = var1 - var3 / 2;
            var9 = var8 + (var3 - this.getTextWidth()) / 2;
        } else if (var7 == Align.LEFT) {
            var8 = var1;
            var9 = var1;
        } else if (var7 == Align.RIGHT) {
            var8 = var1 - var3;
            var9 = var8 + (var3 - this.getTextWidth());
        }

        RenderUtils.start();
        if (var4 == Background.FULL) {
            RenderUtils.drawRect(var5, var8 - 1, var2 - 1, var3 + 1, this.getHeight());
        } else if (var4 == Background.PER_LINE && !this.text.equals("")) {
            RenderUtils.drawRect(var5, var9 - 1, var2 - 1, this.getTextWidth() + 1, this.getHeight());
        }

        RenderUtils.drawString(this.text, this.scale, var9, var2, new Color(var6), this.shadow);
        RenderUtils.end();
    }

    public void setShadow(boolean var1) {
        this.shadow = var1;
    }
}
