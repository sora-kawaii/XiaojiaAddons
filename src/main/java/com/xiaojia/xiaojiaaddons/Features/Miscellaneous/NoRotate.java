package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook.EnumFlags;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRotate {

    private boolean doneLoadingTerrain;

    @SubscribeEvent
    public void onRespawnPacket(PacketReceivedEvent var1) {
        if (var1.packet instanceof S07PacketRespawn) {
            this.doneLoadingTerrain = false;
        }

    }

    @SubscribeEvent(
            priority = EventPriority.LOW
    )
    public void onPosLookPacket(PacketReceivedEvent var1) {
        if (Checker.enabled) {
            if (var1.packet instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook var2 = (S08PacketPlayerPosLook) var1.packet;
                EntityPlayerSP var3 = MinecraftUtils.getPlayer();
                this.doneLoadingTerrain = true;
                if ((double) var2.getPitch() != 0.0) {
                    if (Configs.NoRotate && var3 != null) {
                        if (!Configs.NoRotateDisableHoldingLeaps || !NBTUtils.getSkyBlockID(ControlUtils.getHeldItemStack()).equals("SPIRIT_LEAP")) {
                            if (!Configs.NoRotateDisableInMaze || !Dungeon.currentRoom.equals("Teleport Maze")) {
                                var1.setCanceled(true);
                                double var4 = var2.getX();
                                double var6 = var2.getY();
                                double var8 = var2.getZ();
                                float var10 = var2.getYaw();
                                float var11 = var2.getPitch();
                                if (var2.func_179834_f().contains(EnumFlags.X)) {
                                    var4 += var3.posX;
                                } else {
                                    var3.motionX = 0.0;
                                }

                                if (var2.func_179834_f().contains(EnumFlags.Y)) {
                                    var6 += var3.posY;
                                } else {
                                    var3.motionY = 0.0;
                                }

                                if (var2.func_179834_f().contains(EnumFlags.Z)) {
                                    var8 += var3.posZ;
                                } else {
                                    var3.motionZ = 0.0;
                                }

                                if (var2.func_179834_f().contains(EnumFlags.X_ROT)) {
                                    var11 += var3.rotationPitch;
                                }

                                if (var2.func_179834_f().contains(EnumFlags.Y_ROT)) {
                                    var10 += var3.rotationYaw;
                                }

                                var3.setPositionAndRotation(var4, var6, var8, var3.rotationYaw, var3.rotationPitch);
                                C03PacketPlayer.C06PacketPlayerPosLook var12 = new C03PacketPlayer.C06PacketPlayerPosLook(var3.posX, var3.getEntityBoundingBox().minY, var3.posZ, var10, var11, false);
                                XiaojiaAddons.mc.getNetHandler().getNetworkManager().sendPacket(var12);
                                if (!this.doneLoadingTerrain) {
                                    var3.prevPosX = var3.posX;
                                    var3.prevPosY = var3.posY;
                                    var3.prevPosZ = var3.posZ;
                                    var3.closeScreen();
                                }

                                float var13 = var3.rotationYaw;
                                float var14 = var3.rotationPitch;
                                if (Configs.NoRotateOptimize) {
                                    (new Thread(() -> {
                                        float var2_ = var13;
                                        float var3_ = var14;

                                        try {
                                            Thread.sleep(40L);
                                            float yaw = MinecraftUtils.getPlayer().rotationYaw;
                                            float var5 = MinecraftUtils.getPlayer().rotationPitch;
                                            if (yaw == var2_ && var5 == var3_) {
                                                ChatLib.chat("Changing direction");
                                                ControlUtils.randomChangeDirection(0.001);
                                            }
                                        } catch (Exception e) {
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
}
