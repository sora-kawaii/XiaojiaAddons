package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class AutoTerminalNew {

    private ArrayList lastInventory = new ArrayList();

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent var1) {
        this.lastInventory.clear();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled && Configs.AutoTerminal) {
            Inventory var2 = ControlUtils.getOpenedInventory();
            if (var2 != null && var2.getName().startsWith("Click the button on time!")) {
                ArrayList var3 = new ArrayList();
                Iterator var4 = var2.getSlots().iterator();

                while (var4.hasNext()) {
                    Slot var5 = (Slot) var4.next();
                    if (var5.getStack() != null && var5.getSlotIndex() < 45) {
                        var3.add(var5.getStack().getItem());
                    }
                }

                if (!this.lastInventory.toString().equals(var3.toString())) {
                    int var10 = 0;

                    int var7;
                    int var11;
                    for (var11 = 1; var11 <= 5; ++var11) {
                        ItemStack var6 = var2.getItemInSlot(var11);
                        if (var6 != null) {
                            var7 = var6.getItemDamage();
                            if (var7 == 2 || var7 == 10) {
                                var10 = var11;
                            }
                        }
                    }

                    var11 = 0;

                    for (int var12 = 1; var12 <= 4; ++var12) {
                        var7 = var12 * 9 + var10;
                        ItemStack var8 = var2.getItemInSlot(var7);
                        if (var8 != null) {
                            int var9 = var8.getItemDamage();
                            if (var9 == 5) {
                                var11 = var12;
                            }
                        }
                    }

                    if (var11 != 0) {
                        var2.click(var11 * 9 + 7, false, "MIDDLE");
                    }

                    this.lastInventory = var3;
                }

            }
        }
    }
}
