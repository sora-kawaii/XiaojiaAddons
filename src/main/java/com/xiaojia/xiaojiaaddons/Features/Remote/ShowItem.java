package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ShowItem {
    public static void show() {
        ItemStack var0 = ControlUtils.getHeldItemStack();
        if (var0 == null) {
            ChatLib.chat("You're not holding any item!");
        } else {
            NBTTagCompound var1 = new NBTTagCompound();
            var0.writeToNBT(var1);
            String var2 = var1.toString();
            XiaojiaChat.chat(var2, var0.getDisplayName(), 3);
        }
    }
}
