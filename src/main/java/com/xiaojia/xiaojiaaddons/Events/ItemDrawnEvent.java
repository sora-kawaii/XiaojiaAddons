package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemDrawnEvent extends Event {

    public FontRenderer renderer;

    public ItemStack itemStack;

    public int x;

    public int y;

    public String text;

    public ItemDrawnEvent(FontRenderer var1, ItemStack var2, int var3, int var4, String var5) {
        this.renderer = var1;
        this.itemStack = var2;
        this.x = var3;
        this.y = var4;
        this.text = var5;
    }
}
