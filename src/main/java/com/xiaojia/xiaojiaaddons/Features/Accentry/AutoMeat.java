package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MerchantUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoMeat {

    private Thread sellThread = null;

    private EnumNPC should() {
        if (Configs.AutoMeat) {
            Inventory var1 = ControlUtils.getOpenedInventory();
            if (var1 == null) {
                return EnumNPC.NONE;
            } else if (var1.getItemInSlot(2) != null) {
                return EnumNPC.NONE;
            } else {
                String var2 = MerchantUtils.getCurrentMerchant();
                if (var2 == null) {
                    return EnumNPC.NONE;
                } else {
                    return !var2.contains("精盐贤者") && !var2.contains("Villager") ? (var2.contains("小男孩") ? EnumNPC.XNH : (var2.contains("蔡徐坤") ? EnumNPC.CXK : EnumNPC.NONE)) : EnumNPC.JY;
                }
            }
        } else {
            return EnumNPC.NONE;
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (this.should() != EnumNPC.NONE && (this.sellThread == null || !this.sellThread.isAlive())) {
            this.sellThread = new Thread(() -> {
                try {
                    int var1 = 0;

                    for (int var2 = 3; var2 < 39; ++var2) {
                        EnumNPC var3 = this.should();
                        if (var3 == EnumNPC.NONE) {
                            return;
                        }

                        Inventory var4 = ControlUtils.getOpenedInventory();
                        if (var4 == null) {
                            return;
                        }

                        ItemStack var5 = var4.getItemInSlot(var2);
                        if (var5 == null || var3 == EnumNPC.JY && !var5.getDisplayName().contains("精盐肉块") && !var5.getDisplayName().contains("寒冰碎片") || var3 == EnumNPC.XNH && !var5.getDisplayName().contains("臭肉") || var3 == EnumNPC.CXK && !var5.getDisplayName().contains("白羽鸡肉") || var5.stackSize != 64) {
                            if (var5 == null || var3 != EnumNPC.JY || !var5.getDisplayName().contains("浓缩精华")) {
                                continue;
                            }

                            var4.click(0, true, "LEFT", 0);
                        }

                        var4.click(var2, false, "LEFT", 0);
                        var4.click(var1, false, "LEFT", 0);
                        if (var3 == EnumNPC.CXK) {
                            var1 = 1 - var1;
                        }

                        Thread.sleep(100L);
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                }

            });
            this.sellThread.start();
        }

    }
}
