package com.xiaojia.xiaojiaaddons.Events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class WindowClickEvent extends Event {

    public int mode;

    public int slotId;

    public int mouseButtonClicked;

    public int windowId;

    public WindowClickEvent(int var1, int var2, int var3, int var4) {
        this.windowId = var1;
        this.slotId = var2;
        this.mouseButtonClicked = var3;
        this.mode = var4;
    }
}
