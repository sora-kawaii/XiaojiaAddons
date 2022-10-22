package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoCloseCrystalHollowsChest {
    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent var1) {
        if (Checker.enabled) {
            if (Configs.AutoCloseCrystalHollowsChest) {
                Inventory var2 = ControlUtils.getOpenedInventory();
                if (var2 != null && var2.getName().contains("Loot Chest")) {
                    MinecraftUtils.getPlayer().closeScreen();
                }
            }
        }
    }
}
