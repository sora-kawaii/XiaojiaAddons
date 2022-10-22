package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Iterator;

public class ChestFiller {

    private static boolean done = false;

    private static String name = null;

    private static boolean enabled = false;

    private static int lastId = -1;
    private static boolean six = false;
    private Thread pushingThread = null;

    public static void disable() {
        if (enabled) {
            ChatLib.chat("Chest Filler &cdisabled");
        }

        lastId = -1;
        enabled = false;
        done = false;
        name = null;
        six = false;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void enable(String var0, boolean var1) {
        name = var0;
        six = var1;
        ChatLib.chat("Chest Filler &aenabled&r (&b" + name + "&r)");
        enabled = true;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        disable();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        Inventory var2 = ControlUtils.getOpenedInventory();
        if (var2 != null) {
            if (lastId == -1) {
                lastId = var2.getWindowId();
            } else if (lastId != var2.getWindowId()) {
                lastId = var2.getWindowId();
                done = false;
            }

            if (enabled && !done && var2.getName().equals("Large Chest")) {
                done = true;
                if (this.pushingThread == null || !this.pushingThread.isAlive()) {
                    this.pushingThread = new Thread(() -> {
                        try {
                            boolean var3 = true;
                            Inventory var4 = ControlUtils.getOpenedInventory();
                            HashSet var5 = new HashSet();

                            int var1;
                            for (var1 = 0; var1 < 54; ++var1) {
                                if ((!six || var1 != 5) && var4.getItemInSlot(var1) == null) {
                                    var5.add(var4.getSlots().get(var1));
                                }
                            }

                            for (var1 = 54; var1 < 90; ++var1) {
                                ItemStack var6 = var4.getItemInSlot(var1);
                                if (var6 != null) {
                                    int var2_ = var6.stackSize;
                                    if (StringUtils.stripControlCodes(var6.getDisplayName()).toLowerCase().contains(name.toLowerCase()) && var2_ >= var5.size()) {
                                        var3 = false;
                                        break;
                                    }
                                }
                            }

                            if (var5.isEmpty() && var4.getItemInSlot(5) != null) {
                                return;
                            }

                            if (!var3) {
                                this.click(var4, var1, 0, 0, 0);
                                this.click(var4, -999, 5, 4, 0);
                                Iterator var9 = var5.iterator();

                                while (var9.hasNext()) {
                                    Slot var7 = (Slot) var9.next();
                                    this.click(var4, var7.slotNumber, 5, 5, 0);
                                }

                                this.click(var4, -999, 5, 6, 0);
                                Thread.sleep(50L);
                                if (var4.getItemInSlot(5) == null) {
                                    this.click(var4, 5, 0, 1, 0);
                                }

                                MinecraftUtils.getPlayer().closeScreen();
                            } else {
                                ChatLib.chat("Not enough items.");
                                disable();
                            }
                        } catch (Exception var8) {
                            var8.printStackTrace();
                        }

                    });
                    this.pushingThread.start();
                }
            }

        }
    }

    public void click(Inventory var1, int var2, int var3, int var4, int var5) {
        int var6 = var1.getWindowId() + var5;
        XiaojiaAddons.mc.playerController.windowClick(var6, var2, var4, var3, MinecraftUtils.getPlayer());
    }
}
