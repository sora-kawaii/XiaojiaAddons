package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class ShowLowestBin {
    private String getStringValueFromDouble(double var1) {
        int var3 = MathUtils.floor(var1);
        DecimalFormat var4 = new DecimalFormat(",###");
        return var4.format(var3);
    }

    @SubscribeEvent
    public void onItemToolTip(ItemTooltipEvent var1) {
        if (Checker.enabled) {
            if (Configs.DisplayLowestBin) {
                if (SkyblockUtils.isInSkyblock()) {
                    ItemStack var2 = var1.itemStack;

                    try {
                        double var3 = LowestBin.getItemValue(var2);
                        double var5 = var3 * (double) var2.stackSize;
                        var1.toolTip.add("§6LB Price: §b" + this.getStringValueFromDouble(var5));
                    } catch (Exception var7) {
                    }

                }
            }
        }
    }
}
