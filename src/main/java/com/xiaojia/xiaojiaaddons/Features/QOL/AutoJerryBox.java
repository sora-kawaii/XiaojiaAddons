package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoJerryBox {

    private final KeyBind keyBind = new KeyBind("Auto Jerry Box", 0);
    private boolean enabled = false;
    private Thread openThread = null;
    private boolean guiOpened = false;

    private boolean received = false;

    @SubscribeEvent
    public void onOpen(GuiOpenEvent var1) {
        try {
            if (var1.gui instanceof GuiChest && this.openThread != null && this.openThread.isAlive() && ((ContainerChest) ((GuiChest) var1.gui).inventorySlots).getLowerChestInventory().getName().equals("Open a Jerry Box")) {
                this.guiOpened = true;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private boolean isHoldingJerryBox() {
        ItemStack var1 = ControlUtils.getHeldItemStack();
        return this.isJerryBox(var1);
    }

    @SubscribeEvent
    public void onReceiveOpenMessage(ClientChatReceivedEvent var1) {
        if (var1.type == 0 && ChatLib.removeFormatting(var1.message.getUnformattedText()).matches(" ☺ ")) {
            this.received = true;
        }

    }

    private boolean isJerryBox(ItemStack var1) {
        return var1 != null && DisplayUtils.getDisplayString(var1).endsWith(" Jerry Box");
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            if (this.keyBind.isPressed()) {
                if (!this.isHoldingJerryBox()) {
                    ChatLib.chat("Hold Jerry Box to continue!");
                    return;
                }

                if (this.openThread == null || !this.openThread.isAlive()) {
                    this.enabled = true;
                    ChatLib.chat("Auto Jerry Box &aStarted");
                }
            }

            if (this.enabled) {
                this.enabled = false;
                this.openThread = new Thread(() -> {
                    try {
                        this.received = this.guiOpened = false;
                        ControlUtils.rightClick();

                        while (true) {
                            ControlUtils.stopMoving();

                            int var1;
                            for (var1 = 0; !this.guiOpened && var1 < 10; ++var1) {
                                Thread.sleep(100L);
                            }

                            if (var1 >= 10) {
                                return;
                            }

                            this.guiOpened = false;
                            Thread.sleep(100L);
                            ControlUtils.getOpenedInventory().click(22);
                            MinecraftUtils.getPlayer().closeScreen();

                            for (var1 = 0; !this.received && var1 < 10; ++var1) {
                                Thread.sleep(100L);
                            }

                            if (var1 >= 10) {
                                return;
                            }

                            this.received = false;
                            if (!this.isHoldingJerryBox()) {
                                boolean var2 = false;
                                Inventory var3 = ControlUtils.getOpenedInventory();

                                for (int var4 = 0; var4 < 45; ++var4) {
                                    if (this.isJerryBox(var3.getItemInSlot(var4))) {
                                        var2 = true;
                                        var3.click(var4, false, "SWAP");
                                        break;
                                    }
                                }

                                if (!var2) {
                                    ChatLib.chat("Cannot find jerry box!");
                                    return;
                                }
                            }

                            Thread.sleep(100L);
                            ControlUtils.rightClick();
                        }
                    } catch (Exception var8) {
                        var8.printStackTrace();
                    } finally {
                        this.disable();
                    }
                });
                this.openThread.start();
            }
        }
    }

    private void disable() {
        ChatLib.chat("Auto Jerry Box &cStopped.");
        this.guiOpened = false;
    }
}
