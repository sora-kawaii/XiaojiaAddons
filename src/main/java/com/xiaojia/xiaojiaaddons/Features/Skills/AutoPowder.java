package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashSet;

public class AutoPowder {

    private static final KeyBind keyBind = new KeyBind("Auto Powder", 0);
    private final HashSet solved = new HashSet();
    private boolean isMiningThreadRunning = false;
    private BlockPos closestChest = null;
    private boolean isOpeningChest = false;
    private BlockPos closestStone = null;
    private boolean enabled = false;

    private Vector3f particalPos = null;

    private boolean isOpeningChestThreadRunning = false;

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent var1) {
        if (Configs.AutoPowder && this.enabled) {
            if (SkyblockUtils.isInCrystalHollows()) {
                if (var1.type == 0) {
                    if (ChatLib.removeFormatting(var1.message.getUnformattedText()).startsWith("You received")) {
                        ControlUtils.releaseLeftClick();
                        if (this.closestChest != null) {
                            this.solved.add(this.closestChest);
                        }

                        this.isOpeningChest = false;
                        System.err.println("Closing chest, stop opening chest");
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            if (Configs.AutoPowder) {
                if (SkyblockUtils.isInCrystalHollows()) {
                    if (keyBind.isPressed()) {
                        this.enabled = !this.enabled;
                        ChatLib.chat(this.enabled ? "Auto Powder &aactivated" : "Auto Powder &cdeactivated");
                    }

                    if (this.enabled) {
                        if (!this.isMiningThreadRunning && !this.isOpeningChestThreadRunning) {
                            if (this.isOpeningChest && (this.closestChest == null || MinecraftUtils.getWorld().getBlockState(this.closestChest).getBlock() != Blocks.chest)) {
                                this.isOpeningChest = false;
                            }

                            this.closestStone = this.getClosest(-4, 4, 0, 2, -4, 4, Blocks.stone, new HashSet(), true);
                            this.closestChest = this.getClosest(-4, 4, -1, 3, -4, 4, Blocks.chest, this.solved, false);
                            if (this.particalPos != null) {
                                this.isOpeningChest = true;
                                this.isOpeningChestThreadRunning = true;
                                ControlUtils.releaseLeftClick();
                                System.err.println("Force Release");
                                (new Thread(() -> {
                                    Vector3f var1 = (Vector3f) this.particalPos.clone();

                                    try {
                                        ControlUtils.faceSlowly(this.particalPos.x, this.particalPos.y, this.particalPos.z);
                                    } catch (Exception var6) {
                                        var6.printStackTrace();
                                    } finally {
                                        this.isOpeningChestThreadRunning = false;
                                        if (this.particalPos.equals(var1)) {
                                            this.particalPos = null;
                                        }

                                    }

                                })).start();
                            } else if (!this.isOpeningChest) {
                                if (Math.random() < 0.05) {
                                    (new Thread(() -> {
                                        try {
                                            ControlUtils.moveRandomly(400L);
                                        } catch (Exception var1) {
                                            var1.printStackTrace();
                                        }

                                    })).start();
                                } else if (this.closestStone != null) {
                                    if (this.isMiningThreadRunning) {
                                        return;
                                    }

                                    this.isMiningThreadRunning = true;
                                    (new Thread(() -> {
                                        try {
                                            System.err.println("Start thread! " + this.closestStone);
                                            ControlUtils.faceSlowly((float) this.closestStone.getX() + 0.5F, (float) this.closestStone.getY() + 0.5F, (float) this.closestStone.getZ() + 0.5F);
                                            MovingObjectPosition var1 = XiaojiaAddons.mc.objectMouseOver;
                                            if (var1 != null && var1.typeOfHit.toString().equals("BLOCK")) {
                                                BlockPos var2 = var1.getBlockPos();
                                                if (var2.getY() >= MathUtils.getBlockY(MinecraftUtils.getPlayer()) && MathUtils.distanceSquareFromPlayer((double) var2.getX() + 0.5, (double) var2.getY() + 0.5, (double) var2.getZ() + 0.5) <= Math.pow(XiaojiaAddons.mc.playerController.getBlockReachDistance(), 2.0)) {
                                                    ControlUtils.holdLeftClick();
                                                    long var3 = TimeUtils.curTime();

                                                    while (true) {
                                                        if (MinecraftUtils.getWorld().getBlockState(var2).getBlock() == Blocks.air || TimeUtils.curTime() - var3 >= 200L) {
                                                            ControlUtils.releaseLeftClick();
                                                            Thread.sleep(20L);
                                                            break;
                                                        }

                                                        Thread.sleep(50L);
                                                    }
                                                }
                                            }

                                            ControlUtils.releaseLeftClick();
                                        } catch (Exception var8) {
                                            this.stop();
                                            var8.printStackTrace();
                                        } finally {
                                            this.isMiningThreadRunning = false;
                                            System.err.println("End thread! " + this.closestStone);
                                        }

                                    })).start();
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent var1) {
        if (Configs.AutoPowder && this.enabled) {
            if (SkyblockUtils.isInCrystalHollows()) {
                if (this.closestStone != null) {
                    GuiUtils.enableESP();
                    GuiUtils.drawBoxAtBlock(this.closestStone.getX(), this.closestStone.getY(), this.closestStone.getZ(), 65, 185, 65, 180, 1, 1, 0.01F);
                    GuiUtils.disableESP();
                }

                if (this.closestChest != null) {
                    GuiUtils.enableESP();
                    GuiUtils.drawBoxAtBlock(this.closestChest.getX(), this.closestChest.getY(), this.closestChest.getZ(), 185, 65, 65, 180, 1, 1, 0.01F);
                    GuiUtils.disableESP();
                }

            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        this.solved.clear();
    }

    @SubscribeEvent
    public void receivePacket(PacketReceivedEvent var1) {
        if (Configs.AutoPowder && this.enabled) {
            if (SkyblockUtils.isInCrystalHollows()) {
                if (var1.packet instanceof S2APacketParticles) {
                    S2APacketParticles var2 = (S2APacketParticles) var1.packet;
                    if (var2.getParticleType() == EnumParticleTypes.CRIT) {
                        Vector3f var3 = new Vector3f((float) var2.getXCoordinate(), (float) var2.getYCoordinate() - 0.1F, (float) var2.getZCoordinate());
                        if (this.closestChest != null && MathUtils.distanceSquaredFromPoints(var3.x, var3.y, var3.z, (double) this.closestChest.getX() + 0.5, (double) this.closestChest.getY() + 0.5, (double) this.closestChest.getZ() + 0.5) < 1.0) {
                            this.particalPos = var3;
                        }
                    }
                }

            }
        }
    }

    private void stop() {
        if (this.enabled) {
            this.enabled = false;
            this.isMiningThreadRunning = false;
            this.isOpeningChest = this.isOpeningChestThreadRunning = false;
            MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
            ControlUtils.releaseLeftClick();
            ChatLib.chat("Auto Powder &cdeactivated");
        }

    }

    private BlockPos getClosest(int var1, int var2, int var3, int var4, int var5, int var6, Block var7, HashSet var8, boolean var9) {
        BlockPos var10 = MinecraftUtils.getPlayer().getPosition();
        ArrayList var11 = new ArrayList();

        int var12;
        for (var12 = var10.getX() + var1; var12 <= var10.getX() + var2; ++var12) {
            for (int var13 = var10.getY() + var3; var13 <= var10.getY() + var4; ++var13) {
                for (int var14 = var10.getZ() + var5; var14 <= var10.getZ() + var6; ++var14) {
                    BlockPos var15 = new BlockPos(var12, var13, var14);
                    if (MinecraftUtils.getWorld().getBlockState(var15).getBlock() == var7 && !var8.contains(var15) && MathUtils.distanceSquareFromPlayer((float) var15.getX() + 0.5F, (float) var15.getY() + 0.5F, (float) var15.getZ() + 0.5F) <= Math.pow(XiaojiaAddons.mc.playerController.getBlockReachDistance(), 2.0) && (!var9 || MathUtils.checkBlocksBetween(var12, var13, var14))) {
                        var11.add(var15);
                    }
                }
            }
        }

        var11.sort((var0_, var1x_) -> {
            BlockPos var0 = (BlockPos) var0_;
            BlockPos var1x = (BlockPos) var1x_;
            return MathUtils.yawPitchSquareFromPlayer((float) var0.getX() + 0.5F, (float) var0.getY() + 0.5F, (float) var0.getZ() + 0.5F) > MathUtils.yawPitchSquareFromPlayer((float) var1x.getX() + 0.5F, (float) var1x.getY() + 0.5F, (float) var1x.getZ() + 0.5F) ? 1 : -1;
        });
        if (var11.isEmpty()) {
            return null;
        } else {
            var12 = var11.size();
            double var16 = Math.random() * (double) var12;
            int var17 = Math.min((int) (var16 * var16 * var16) + 1, var12);
            return (BlockPos) var11.get(var12 / var17 - 1);
        }
    }
}
