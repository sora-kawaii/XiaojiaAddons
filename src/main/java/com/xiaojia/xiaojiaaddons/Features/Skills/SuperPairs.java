package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

public class SuperPairs {

    static ItemStack[] experimentTableSlots = new ItemStack[54];

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent var1) {
        experimentTableSlots = new ItemStack[54];
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent var1) {
        Inventory var2 = ControlUtils.getOpenedInventory();
        if (Checker.enabled && var2 != null && Configs.SuperpairsSolver) {
            String var3 = var2.getName();
            List var4 = var2.getSlots();
            if (var3.contains("Superpairs (")) {
                HashMap var5 = new HashMap();

                for (int var6 = 0; var6 < 53; ++var6) {
                    ItemStack var7 = experimentTableSlots[var6];
                    if (var7 != null) {
                        String var8 = var7.getDisplayName();
                        String var9 = var8 + var7.getUnlocalizedName();
                        var5.computeIfAbsent(var9, (var0) -> {
                            return new HashSet();
                        });
                        ((HashSet) var5.get(var9)).add(var6);
                    }
                }

                Color[] var10 = new Color[]{new Color(255, 0, 0, 100), new Color(0, 0, 255, 100), new Color(100, 179, 113, 100), new Color(255, 114, 255, 100), new Color(255, 199, 87, 100), new Color(119, 105, 198, 100), new Color(135, 199, 112, 100), new Color(240, 37, 240, 100), new Color(178, 132, 190, 100), new Color(63, 135, 163, 100), new Color(146, 74, 10, 100), new Color(255, 255, 255, 100), new Color(217, 252, 140, 100), new Color(255, 82, 82, 100)};
                Iterator var11 = Arrays.stream(var10).iterator();
                var5.forEach((var2x, var3x_) -> {
                    List var3x = (List) var3x_;
                    if (var3x.size() >= 2) {
                        ArrayList var4x = new ArrayList();
                        var3x.forEach((var2_) -> {
                            var4x.add(var4.get((Integer) var2_));
                        });
                        Color col = (Color) var11.next();
                        var4x.forEach((stack) -> {
                            GuiUtils.drawOnSlot(var4.size(), ((Slot) stack).xDisplayPosition, ((Slot) stack).yDisplayPosition, col.getRGB());
                        });
                    }
                });
            }

        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            Inventory var2 = ControlUtils.getOpenedInventory();
            if (var2 != null && Configs.SuperpairsSolver && XiaojiaAddons.mc.currentScreen instanceof GuiChest) {
                String var3 = var2.getName();
                List var4 = ((GuiChest) XiaojiaAddons.mc.currentScreen).inventorySlots.inventorySlots;
                if (var3.startsWith("Superpairs (")) {
                    for (int var5 = 0; var5 < 53; ++var5) {
                        ItemStack var6 = ((Slot) var4.get(var5)).getStack();
                        if (var6 != null) {
                            String var7 = var6.getDisplayName();
                            if (Item.getIdFromItem(var6.getItem()) != 95 && Item.getIdFromItem(var6.getItem()) != 160 && !var7.contains("Instant Find") && !var7.contains("Gained +")) {
                                if (var7.contains("Enchanted Book")) {
                                    var7 = var6.getTooltip(MinecraftUtils.getPlayer(), false).get(3);
                                }

                                if (var6.stackSize > 1) {
                                    var7 = var6.stackSize + " " + var7;
                                }

                                if (experimentTableSlots[var5] == null) {
                                    experimentTableSlots[var5] = var6.copy().setStackDisplayName(var7);
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent var1) {
        if (Checker.enabled) {
            if (var1.toolTip != null) {
                ItemStack var2 = var1.itemStack;
                Inventory var3 = ControlUtils.getOpenedInventory();
                if (var3 != null) {
                    String var4 = var3.getName();
                    if (Configs.SuperpairsSolver && var4.contains("Superpairs (")) {
                        if (Item.getIdFromItem(var2.getItem()) != 95) {
                            return;
                        }

                        if (var2.getDisplayName().contains("Click any button") || var2.getDisplayName().contains("Click a second button") || var2.getDisplayName().contains("Next button is instantly rewarded") || var2.getDisplayName().contains("?")) {
                            Slot var5 = ((GuiChest) XiaojiaAddons.mc.currentScreen).getSlotUnderMouse();
                            ItemStack var6 = experimentTableSlots[var5.getSlotIndex()];
                            if (var6 == null) {
                                return;
                            }

                            String var7 = var6.getDisplayName();
                            if (var1.toolTip.stream().anyMatch((var1x) -> {
                                return StringUtils.stripControlCodes(var1x).equals(StringUtils.stripControlCodes(var7));
                            })) {
                                return;
                            }

                            var1.toolTip.removeIf((var0) -> {
                                var0 = StringUtils.stripControlCodes(var0);
                                return var0.equals("minecraft:stained_glass") || var0.startsWith("NBT: ");
                            });
                            var1.toolTip.add(var7);
                            var1.toolTip.add(var6.getItem().getRegistryName());
                        }
                    }

                }
            }
        }
    }
}
