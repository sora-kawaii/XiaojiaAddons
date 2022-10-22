package com.xiaojia.xiaojiaaddons.Features.QOL;


import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.Objects.StepEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AutoFuse extends StepEvent {

    private static boolean isFusingBooks = false;

    public AutoFuse() {
        super(4L);
    }

    private static boolean fusingBooksNotFull() {
        return isAir(29) || isAir(33);
    }

    private static boolean isAir(int var0) {
        ItemStack var1 = ControlUtils.getItemStackInSlot(var0, false);
        return var1 == null || !var1.hasDisplayName() || var1.getDisplayName().toLowerCase().contains("air");
    }

    private static boolean haveItemsInAnvil() {
        return !isAir(29) || !isAir(33);
    }

    private static boolean fusingBooksFull() {
        return !fusingBooksNotFull();
    }

    public static List getItemLore(ItemStack var0) {
        return NBTUtils.getLore(var0);
    }

    public boolean enabled() {
        return true;
    }

    public abstract boolean canFuse();

    public abstract void clear();

    public void execute() {
        if (true) {
            if (this.enabled()) {
                if (ControlUtils.getInventoryName().equals(this.inventoryName())) {
                    Inventory var1 = ControlUtils.getOpenedInventory();
                    if (!isFusingBooks && var1 != null) {
                        isFusingBooks = true;

                        try {
                            if (haveItemsInAnvil()) {
                                isFusingBooks = false;
                                return;
                            }

                            this.clear();

                            for (int var2 = 54; var2 < 90; ++var2) {
                                ItemStack var3 = var1.getItemInSlot(var2);
                                this.add(var3, var2);
                            }

                            (new Thread(() -> {
                                try {
                                    ArrayList list = new ArrayList();

                                    for (int var2 = 0; var2 < 90; ++var2) {
                                        list.add(false);
                                    }

                                    while (true) {
                                        Pair var15 = this.getNext(list);
                                        if (var15 == null || (Integer) var15.getKey() == -1) {
                                            return;
                                        }

                                        int var3 = (Integer) var15.getKey();
                                        int var4 = (Integer) var15.getValue();

                                        while (fusingBooksNotFull()) {
                                            if (ControlUtils.getOpenedInventory().getSize() != 90) {
                                                ChatLib.chat("You quit anvil!");
                                                return;
                                            }

                                            while (fusingBooksNotFull()) {
                                                try {
                                                    Thread.sleep(60L);
                                                    ControlUtils.getOpenedInventory().click(var3, true, "LEFT");
                                                } catch (Exception var12) {
                                                    var12.printStackTrace();
                                                }

                                                try {
                                                    Thread.sleep(60L);
                                                    ControlUtils.getOpenedInventory().click(var4, true, "LEFT");
                                                } catch (Exception var11) {
                                                    var11.printStackTrace();
                                                }
                                            }

                                            Thread.sleep(60L);
                                            int var5 = 0;

                                            while (!this.canFuse()) {
                                                Thread.sleep(5L);
                                                ++var5;
                                                if (var5 > 100) {
                                                    break;
                                                }
                                            }
                                        }

                                        if (fusingBooksFull() && this.checkFuse()) {
                                            ControlUtils.getOpenedInventory().click(22);

                                            for (ItemStack var16 = ControlUtils.getOpenedInventory().getItemInSlot(13); var16 == null || !var16.hasDisplayName() || !var16.getDisplayName().contains(this.inventoryName()); var16 = ControlUtils.getOpenedInventory().getItemInSlot(13)) {
                                                Thread.sleep(150L);
                                                if (ControlUtils.getOpenedInventory().getSize() != 90) {
                                                    ChatLib.chat("You quit anvil!");
                                                    return;
                                                }

                                                ControlUtils.getOpenedInventory().click(22);
                                            }
                                        } else {
                                            ChatLib.chat("WTF? Something wrong happened");
                                        }

                                        list.set(var3, true);
                                        list.set(var4, true);
                                    }
                                } catch (Exception var13) {
                                    var13.printStackTrace();
                                } finally {
                                    isFusingBooks = false;
                                }
                            })).start();
                        } catch (Exception var4) {
                            var4.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    public abstract Pair getNext(ArrayList var1);

    public abstract String inventoryName();

    public abstract void add(ItemStack var1, int var2);

    public abstract boolean checkFuse();
}
