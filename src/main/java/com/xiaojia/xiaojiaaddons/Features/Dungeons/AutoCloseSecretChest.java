package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoCloseSecretChest {
    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent var1) {
        if (true) {
            if (Configs.AutoCloseSecretChest && SkyblockUtils.isInDungeon()) {
                Inventory var2 = ControlUtils.getOpenedInventory();
                if (var2 != null && var2.getName().equals("Chest")) {
                    MinecraftUtils.getPlayer().closeScreen();
                }
            }
        }
    }
}
