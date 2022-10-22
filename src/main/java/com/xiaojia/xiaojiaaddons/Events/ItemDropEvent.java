package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ItemDropEvent extends Event {

    public ItemStack itemStack;

    public boolean dropAll;

    public ItemDropEvent(boolean var1, ItemStack var2) {
        this.dropAll = var1;
        this.itemStack = var2;
    }
}
