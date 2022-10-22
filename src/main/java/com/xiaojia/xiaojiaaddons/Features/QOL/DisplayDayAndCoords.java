package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.DevMode;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Features.Skills.Fishing;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.List;

public class DisplayDayAndCoords {

    private final Display display = new Display();

    public DisplayDayAndCoords() {
        this.display.setShouldRender(true);
        this.display.setBackground("full");
        this.display.setBackgroundColor(0);
        this.display.setAlign("left");
    }

    private String getDistanceString(Entity var1) {
        String var2 = "a";
        double var3 = Math.sqrt(MathUtils.distanceSquareFromPlayer(var1));
        if (var3 < 10.0) {
            var2 = "c";
        } else if (var3 < 50.0) {
            var2 = "6";
        }

        return String.format(": &%s&l%.2f", var2, var3);
    }

    @SubscribeEvent
    public void renderString(RenderGameOverlayEvent.Pre var1) {
        if (var1.type == ElementType.TEXT) {
            if (true) {
                EntityPlayerSP var2 = MinecraftUtils.getPlayer();
                BlockPos var3 = MathUtils.getBlockPos();
                if (var2 != null && var3 != null) {
                    int var4 = MathUtils.floor((double) var2.worldObj.getWorldTime() / 24000.0);
                    this.display.clearLines();
                    this.display.setRenderLoc(Configs.DisplayDayX, Configs.DisplayDayY);
                    if (Configs.DisplayCoords) {
                        this.display.addLine((new DisplayLine("Coords: " + var3.getX() + " " + var3.getY() + " " + var3.getZ())).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    if (Configs.DisplayDay) {
                        this.display.addLine((new DisplayLine("Day " + var4)).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    if (Configs.DisplayPing) {
                        this.display.addLine((new DisplayLine("Ping: " + SkyblockUtils.getPing())).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    if (Configs.FindFairyGrottoMap && SkyblockUtils.isInCrystalHollows()) {
                        this.display.addLine((new DisplayLine("Jasper: " + FindFairy.getBlock())).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    if (Configs.DisplayPacketReceived) {
                        this.display.addLine((new DisplayLine("Packet Received: " + PacketRelated.getReceivedQueueLength())).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    if (Configs.DisplayPacketSent) {
                        this.display.addLine((new DisplayLine("Packet Sent: " + PacketRelated.getSentQueueLength())).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    int var5;
                    Inventory var6;
                    Iterator var7;
                    ItemStack var8;
                    if (Configs.DisplayBricks) {
                        var5 = 0;
                        var6 = ControlUtils.getOpenedInventory();
                        if (var6 != null) {
                            var7 = var6.getItemStacks().iterator();

                            while (var7.hasNext()) {
                                var8 = (ItemStack) var7.next();
                                if (var8 != null && var8.getItem() == Items.brick) {
                                    var5 += var8.stackSize;
                                }
                            }
                        }

                        this.display.addLine((new DisplayLine("Bricks: &c&l" + var5)).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    if (Configs.DisplayIronIngots) {
                        var5 = 0;
                        var6 = ControlUtils.getOpenedInventory();
                        if (var6 != null) {
                            var7 = var6.getItemStacks().iterator();

                            while (var7.hasNext()) {
                                var8 = (ItemStack) var7.next();
                                if (var8 != null && var8.getItem() == Items.iron_ingot) {
                                    var5 += var8.stackSize;
                                }
                            }
                        }

                        this.display.addLine((new DisplayLine("Iron Ingots: &0&l" + var5)).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    String var12 = Fishing.timer();
                    if (!var12.equals("")) {
                        this.display.addLine((new DisplayLine("&cFishing Timer: " + var12)).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    if (SessionUtils.isDev()) {
                        this.display.addLine((new DisplayLine("yaw: " + MathUtils.getYaw())).setScale((float) Configs.DisplayScale / 20.0F));
                        this.display.addLine((new DisplayLine("pitch: " + MathUtils.getPitch())).setScale((float) Configs.DisplayScale / 20.0F));
                        synchronized (DevMode.lines) {
                            var7 = DevMode.lines.iterator();

                            while (var7.hasNext()) {
                                DisplayLine var14 = (DisplayLine) var7.next();
                                this.display.addLine(var14);
                            }
                        }

                        this.display.addLine((new DisplayLine(String.format("Hidden entity: %d", EntityQOL.getHiddenEntityCount()))).setScale((float) Configs.DisplayScale / 20.0F));
                    }

                    if (Configs.DisplayPlayers) {
                        try {
                            List var13 = EntityUtils.getEntities();
                            var13.removeIf((var0) -> {
                                return !EntityUtils.isPlayer((EntityOtherPlayerMP) var0);
                            });
                            var13.sort((var0, var1x) -> {
                                return (int) (MathUtils.distanceSquareFromPlayer((BlockPos) var0) - MathUtils.distanceSquareFromPlayer((BlockPos) var1x));
                            });
                            this.display.addLine((new DisplayLine(String.format("&f&lNearby Players: (%d)", var13.size()))).setScale((float) Configs.DisplayScale / 20.0F));
                            var13.forEach((var1x) -> {
                                this.display.addLine((new DisplayLine(" " + ((Entity) var1x).getName() + this.getDistanceString((Entity) var1x))).setScale((float) Configs.DisplayScale / 20.0F));
                            });
                        } catch (Exception var10) {
                        }
                    }

                }
            }
        }
    }
}
