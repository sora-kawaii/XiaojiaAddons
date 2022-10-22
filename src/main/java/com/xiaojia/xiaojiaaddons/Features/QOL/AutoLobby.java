package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoLobby {

    private static final double[] yCoordList = new double[]{-1.0, -1.0, -1.0, -1.0};
    private static final long[] timeList = new long[]{-1L, -1L, -1L, -1L};
    private static double accelerate = 0.0;
    private static double currentY = 0.0;
    private static double velocity = 0.0;
    private long lastTime = 0L;
    private boolean isSending = false;

    public static double getCurrentY() {
        return currentY;
    }

    public static double getVelocity() {
        return velocity;
    }

    public static double getAccelerate() {
        return accelerate;
    }

    public static boolean isFalling() {
        return velocity < -8.0 && velocity + accelerate < -30.0;
    }

    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent var1) {
        if (ChatLib.removeFormatting(var1.message.getUnformattedText()).startsWith("Sending to server ")) {
            this.isSending = true;

            for (int var2 = 0; var2 < yCoordList.length; ++var2) {
                yCoordList[var2] = (double) (timeList[var2] = -1L);
            }
        }

    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (!this.isSending) {
                if (MinecraftUtils.getPlayer() != null) {
                    double var2 = MathUtils.getY(MinecraftUtils.getPlayer());
                    long var4 = TimeUtils.curTime();
                    if (timeList[timeList.length - 1] != var4) {
                        int var6;
                        for (var6 = 1; var6 < yCoordList.length; ++var6) {
                            yCoordList[var6 - 1] = yCoordList[var6];
                            timeList[var6 - 1] = timeList[var6];
                        }

                        yCoordList[yCoordList.length - 1] = var2;
                        timeList[timeList.length - 1] = TimeUtils.curTime();
                        var6 = yCoordList.length / 2;
                        double var7 = yCoordList[var6] - yCoordList[0];
                        double var9 = yCoordList[yCoordList.length - 1] - yCoordList[var6];
                        double var11 = (float) (timeList[var6] - timeList[0]) / 1000.0F;
                        double var13 = (float) (timeList[yCoordList.length - 1] - timeList[var6]) / 1000.0F;
                        double var15 = var7 / var11;
                        double var17 = var9 / var13;
                        accelerate = (var17 - var15) / ((var11 + var13) / 2.0);
                        velocity = var17;
                        double var19 = MinecraftUtils.getPlayer().motionY;
                        if (!(Math.abs(var19) < 1.0E-5) && velocity / var19 <= 30.0) {
                            if (timeList[0] != -1L) {
                                if (Configs.AutoLobby) {
                                    if (SkyblockUtils.isInSkyblock()) {
                                        if (!SkyblockUtils.isInDungeon()) {
                                            currentY = var2 + velocity * (double) SkyblockUtils.getPing() / 1000.0;
                                            if (isFalling() && currentY < 15.0 && TimeUtils.curTime() - this.lastTime > 2000L) {
                                                for (int var21 = MathUtils.floor(var2); var21 >= 0; --var21) {
                                                    if (!BlockUtils.isBlockAir(MathUtils.getX(MinecraftUtils.getPlayer()), (float) var21, MathUtils.getZ(MinecraftUtils.getPlayer()))) {
                                                        return;
                                                    }
                                                }

                                                StringBuilder var27 = new StringBuilder();
                                                long[] var22 = timeList;
                                                int var23 = var22.length;

                                                int var24;
                                                for (var24 = 0; var24 < var23; ++var24) {
                                                    long var25 = var22[var24];
                                                    var27.append(var25).append(" ");
                                                }

                                                var27.append("\n");
                                                double[] var28 = yCoordList;
                                                var23 = var28.length;

                                                for (var24 = 0; var24 < var23; ++var24) {
                                                    double var29 = var28[var24];
                                                    var27.append(String.format("%.2f ", var29));
                                                }

                                                System.out.println(var27);
                                                ChatLib.chat("Detected falling to the void, auto /l");
                                                CommandsUtils.addCommand("/l");
                                                this.lastTime = TimeUtils.curTime();
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        this.isSending = false;
    }
}
