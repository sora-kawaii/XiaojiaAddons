package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class AutoHarp {

    private ArrayList lastInventory = new ArrayList();

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled && Configs.AutoHarp) {
            Inventory var2 = ControlUtils.getOpenedInventory();
            if (var2 != null && var2.getName().startsWith("Harp -")) {
                ArrayList var3 = new ArrayList();
                Iterator var4 = var2.getSlots().iterator();

                Slot var5;
                while (var4.hasNext()) {
                    var5 = (Slot) var4.next();
                    if (var5.getStack() != null && var5.getSlotIndex() < 45) {
                        var3.add(var5.getStack().getItem());
                    }
                }

                if (!this.lastInventory.toString().equals(var3.toString())) {
                    var4 = var2.getSlots().iterator();

                    while (var4.hasNext()) {
                        var5 = (Slot) var4.next();
                        if (var5.getStack() != null && var5.getStack().getItem() instanceof ItemBlock && ((ItemBlock) var5.getStack().getItem()).getBlock() == Blocks.quartz_block) {
                            var2.click(var5.slotNumber, false, "MIDDLE");
                            break;
                        }
                    }
                }

                this.lastInventory = var3;
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent var1) {
        this.lastInventory.clear();
    }
}
