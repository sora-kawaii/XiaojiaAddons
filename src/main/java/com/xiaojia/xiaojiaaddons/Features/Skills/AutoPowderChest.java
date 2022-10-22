package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashSet;

public class AutoPowderChest {

    private static final KeyBind keyBind = new KeyBind("Auto Powder Chest", 0);
    private static boolean enabled = true;
    private final HashSet solved = new HashSet();
    private Thread thread = null;
    private long lastWarnTime = 0L;
    private Vector3f particalPos = null;
    private BlockPos closestChest = null;

    private static boolean isChestsParticle(Vector3f var0, BlockPos var1) {
        if (var0.x < (float) var1.getX()) {
            return (int) (var0.x * 10.0F) + 1 == var1.getX() * 10 && var0.y >= (float) var1.getY() && var0.y <= (float) (var1.getY() + 1) && var0.z >= (float) var1.getZ() && var0.z <= (float) (var1.getZ() + 1);
        } else if (var0.x > (float) (var1.getX() + 1)) {
            return (int) (var0.x * 10.0F) - 1 == var1.getX() * 10 + 10 && var0.y >= (float) var1.getY() && var0.y <= (float) (var1.getY() + 1) && var0.z >= (float) var1.getZ() && var0.z <= (float) (var1.getZ() + 1);
        } else if (var0.z < (float) var1.getZ()) {
            return (int) (var0.z * 10.0F) + 1 == var1.getZ() * 10 && var0.y >= (float) var1.getY() && var0.y <= (float) (var1.getY() + 1) && var0.x >= (float) var1.getX() && var0.x <= (float) (var1.getX() + 1);
        } else if (!(var0.z > (float) (var1.getZ() + 1))) {
            return false;
        } else {
            return (int) (var0.z * 10.0F) - 1 == var1.getZ() * 10 + 10 && var0.y >= (float) var1.getY() && var0.y <= (float) (var1.getY() + 1) && var0.x >= (float) var1.getX() && var0.x <= (float) (var1.getX() + 1);
        }
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent var1) {
        if (Checker.enabled) {
            if (Configs.AutoPowderChest && enabled) {
                if (SkyblockUtils.isInCrystalHollows()) {
                    if (!Configs.AutoPowder) {
                        if (var1.type == 0) {
                            if (ChatLib.removeFormatting(var1.message.getUnformattedText()).startsWith("You received") && this.closestChest != null) {
                                this.solved.add(this.closestChest);
                            }

                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void receivePacket(PacketReceivedEvent var1) {
        if (Checker.enabled) {
            if (Configs.AutoPowderChest && enabled) {
                if (SkyblockUtils.isInCrystalHollows()) {
                    if (!Configs.AutoPowder) {
                        if (var1.packet instanceof S2APacketParticles) {
                            S2APacketParticles var2 = (S2APacketParticles) var1.packet;
                            if (var2.getParticleType() == EnumParticleTypes.CRIT) {
                                Vector3f var3 = new Vector3f((float) var2.getXCoordinate(), (float) var2.getYCoordinate() - 0.1F, (float) var2.getZCoordinate());
                                if (this.closestChest != null && isChestsParticle(var3, this.closestChest)) {
                                    this.particalPos = var3;
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private BlockPos getClosestChest() {
        BlockPos var1 = MinecraftUtils.getPlayer().getPosition();
        ArrayList var2 = new ArrayList();

        for (int var3 = var1.getX() - 4; var3 <= var1.getX() + 4; ++var3) {
            for (int var4 = var1.getY() - 4; var4 <= var1.getY() + 4; ++var4) {
                for (int var5 = var1.getZ() - 4; var5 <= var1.getZ() + 4; ++var5) {
                    BlockPos var6 = new BlockPos(var3, var4, var5);
                    if (MinecraftUtils.getWorld().getBlockState(var6).getBlock() == Blocks.chest && !this.solved.contains(var6)) {
                        var2.add(var6);
                    }
                }
            }
        }

        var2.sort((var0, var1x) -> {
            return MathUtils.distanceSquareFromPlayer((BlockPos) var0) > MathUtils.distanceSquareFromPlayer((BlockPos) var1x) ? 1 : -1;
        });
        if (var2.isEmpty()) {
            return null;
        } else {
            return (BlockPos) var2.get(0);
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (Configs.AutoPowderChest) {
                if (SkyblockUtils.isInCrystalHollows()) {
                    if (Configs.AutoPowder) {
                        if (TimeUtils.curTime() - this.lastWarnTime > 10000L) {
                            ChatLib.chat("Don't turn on Auto Powder / Auto Crystal Hollows Chest on at the same time!");
                            ChatLib.chat("Disabled Auto Crystal Hollows Chest.");
                            this.lastWarnTime = TimeUtils.curTime();
                        }

                    } else {
                        if (keyBind.isPressed()) {
                            enabled = !enabled;
                            ChatLib.chat(enabled ? "Auto Crystal Hollows Chest &aactivated" : "Auto Crystal Hollows Chest &cdeactivated");
                        }

                        if (enabled) {
                            this.closestChest = this.getClosestChest();
                            if (this.particalPos != null && (this.thread == null || !this.thread.isAlive())) {
                                this.thread = new Thread(() -> {
                                    Vector3f var1 = (Vector3f) this.particalPos.clone();

                                    try {
                                        ControlUtils.faceSlowly(var1.x, var1.y, var1.z, false);
                                    } catch (Exception var6) {
                                        var6.printStackTrace();
                                    } finally {
                                        if (this.particalPos.equals(var1)) {
                                            this.particalPos = null;
                                        }

                                    }

                                });
                                this.thread.start();
                            }

                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent var1) {
        if (Checker.enabled) {
            if (Configs.AutoPowderChest) {
                if (SkyblockUtils.isInCrystalHollows()) {
                    if (!Configs.AutoPowder) {
                        if (this.closestChest != null) {
                            GuiUtils.enableESP();
                            GuiUtils.drawBoxAtBlock(this.closestChest.getX(), this.closestChest.getY(), this.closestChest.getZ(), 65, 185, 65, 100, 1, 1, 0.01F);
                            GuiUtils.disableESP();
                        }

                    }
                }
            }
        }
    }
}
