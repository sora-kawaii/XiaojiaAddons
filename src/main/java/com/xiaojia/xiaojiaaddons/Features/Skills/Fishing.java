package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Sounds.SoundHandler;
import com.xiaojia.xiaojiaaddons.Sounds.Sounds;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fishing {

    public static long startPushing = 0L;
    private static long startTime = 0L;
    private final KeyBind autoMoveKeyBind = new KeyBind("Auto Move", 0);
    private Thread pushingThread = null;
    private long lastMove = 0L;
    private long lastReeledIn = 0L;
    private boolean shouldMove = false;

    public static String timer() {
        if (!Configs.AutoMoveTimer) {
            return "";
        } else if (startTime == 0L) {
            return "";
        } else {
            int var0 = (int) (TimeUtils.curTime() - startTime);
            int var1 = var0 / 1000;
            int var2 = var1 / 60;
            int var3 = var1 % 60;
            return String.format("%02d:%02d", var2, var3);
        }
    }

    public static void warn(double var0) {
        (new Thread(() -> {
            double var2 = Math.random();
            int var4 = (int) (Math.random() * 1000.0);
            ChatLib.debug("Rolled d: " + var2);
            if (var2 < var0) {
                ChatLib.chat("&d&lCRAZY RARE SOUND! &6BENK's roar &b(+" + var4 + " ✯ Magic Find!)&r");
                SoundHandler.playSound(Sounds.bk());
            } else if (var2 < var0 * 2.0) {
                ChatLib.chat("&d&lCRAZY RARE SOUND! &6ICY FILL &b(+" + var4 + " ✯ Magic Find!)&r");
                SoundHandler.playSound(Sounds.icyFill());
            } else {
                SoundHandler.playSound(Sounds.destiny());
            }

        })).start();
    }

    private void reelIn() {
        try {
            ControlUtils.rightClick();
            if (Configs.FishingMode == 1) {
                Thread.sleep(Configs.PullCastCD);
                this.cast();
            } else {
                int var1 = HotbarUtils.getIndex("ICE_SPRAY_WAND");
                int var2 = HotbarUtils.getIndex("HYPERION");
                if (var2 == -1) {
                    var2 = HotbarUtils.getIndex("SCYLLA");
                }

                if (var2 == -1) {
                    var2 = HotbarUtils.getIndex("VALKYRIE");
                }

                if (var2 == -1) {
                    var2 = HotbarUtils.getIndex("ASTRAEA");
                }

                if (var1 == -1 || var2 == -1) {
                    ChatLib.chat("Requires ice spray wand and wither blade in hotbar.");
                    return;
                }

                ControlUtils.setHeldItemIndex(var1, false);
                Thread.sleep(Configs.PullCastCD);
                ControlUtils.rightClick();
                ControlUtils.setHeldItemIndex(var2);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    @SubscribeEvent
    public void onParticle(PacketReceivedEvent var1) {
        if (Checker.enabled) {
            if (Configs.AutoPullRod) {
                if (var1.packet instanceof S2APacketParticles) {
                    S2APacketParticles var2 = (S2APacketParticles) var1.packet;
                    if (var2.getParticleType() == EnumParticleTypes.WATER_BUBBLE || var2.getParticleType() == EnumParticleTypes.FLAME) {
                        if (MinecraftUtils.getPlayer() != null) {
                            if (MinecraftUtils.getPlayer().fishEntity != null) {
                                EntityFishHook var3 = MinecraftUtils.getPlayer().fishEntity;
                                if (MathUtils.equal(var2.getXOffset(), 0.25) && MathUtils.equal(var2.getYOffset(), 0.0) && MathUtils.equal(var2.getZOffset(), 0.25) && MathUtils.equal(var2.getParticleSpeed(), 0.2)) {
                                    if (var2.getParticleCount() == 6) {
                                        if (MathUtils.distanceSquaredFromPoints(var3.posX, var3.posY, var3.posZ, var2.getXCoordinate(), var3.posY, var2.getZCoordinate()) < 1.0 && Math.abs(var3.posY - var2.getYCoordinate()) < 2.0 && TimeUtils.curTime() - this.lastReeledIn > (long) Configs.ReelCD) {
                                            this.lastReeledIn = TimeUtils.curTime();
                                            (new Thread(this::reelIn)).start();
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

    private void cast() {
        this.lastReeledIn = TimeUtils.curTime();
        ControlUtils.rightClick();
    }

    @SubscribeEvent
    public void onTickMove(TickEndEvent var1) {
        if (Checker.enabled) {
            if (Configs.AutoMove) {
                if (this.autoMoveKeyBind.isPressed()) {
                    this.shouldMove = !this.shouldMove;
                    if (this.shouldMove) {
                        startTime = this.lastReeledIn = TimeUtils.curTime();
                        ChatLib.chat("Auto Move &aactivated");
                    } else {
                        this.stopMove();
                    }
                }

                if (this.shouldMove) {
                    EntityFishHook var2 = MinecraftUtils.getPlayer().fishEntity;
                    if (var2 != null && (var2.isInLava() || var2.isInWater()) && this.pushingThread != null) {
                        this.pushingThread.interrupt();
                    }

                    long var3 = TimeUtils.curTime();
                    if (Configs.AutoMoveRecast && var3 - this.lastReeledIn >= (long) (1000L * Configs.AutoMoveRecastTime)) {
                        this.lastReeledIn = TimeUtils.curTime();
                        (new Thread(this::reelIn)).start();
                    }

                    if (var3 - this.lastMove > 2000L) {
                        this.lastMove = var3;
                        if (Configs.MainLobbyAutoMove) {
                            (new Thread(() -> {
                                try {
                                    ControlUtils.sneak();
                                    ControlUtils.holdJump();
                                    if (!this.shouldMove) {
                                        ControlUtils.unSneak();
                                        ControlUtils.releaseJump();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            })).start();
                        } else {
                            (new Thread(() -> {
                                try {
                                    int var1_ = MathUtils.floor(Math.random() * 4.0);
                                    if (Configs.AutoMoveSneak) {
                                        ControlUtils.sneak();
                                    }

                                    int var2_ = Configs.AutoMoveTime;
                                    int var3_ = var2_ + MathUtils.floor(Math.random() * (double) var2_);
                                    switch (var1_) {
                                        case 0:
                                            ControlUtils.moveLeft(var3_);
                                            Thread.sleep(100L);
                                            if (!this.shouldMove) {
                                                return;
                                            }

                                            ControlUtils.moveRight(var3_);
                                            break;
                                        case 1:
                                            ControlUtils.moveRight(var3_);
                                            Thread.sleep(100L);
                                            if (!this.shouldMove) {
                                                return;
                                            }

                                            ControlUtils.moveLeft(var3_);
                                            break;
                                        case 2:
                                            ControlUtils.moveForward(var3_);
                                            Thread.sleep(100L);
                                            if (!this.shouldMove) {
                                                return;
                                            }

                                            ControlUtils.moveBackward(var3_);
                                            break;
                                        case 3:
                                            ControlUtils.moveBackward(var3_);
                                            Thread.sleep(100L);
                                            if (!this.shouldMove) {
                                                return;
                                            }

                                            ControlUtils.moveForward(var3_);
                                    }

                                    Thread.sleep(100L);
                                    float var4 = 0.0F;
                                    float var5 = 0.1F;
                                    float var6 = 0.2F;
                                    float var7 = MathUtils.getYaw();
                                    float var8 = MathUtils.getPitch();

                                    while ((double) var4 < 6.283185307179586 && this.shouldMove) {
                                        float var9 = MathUtils.getYaw();
                                        float var10 = MathUtils.getPitch();
                                        var9 = (float) ((double) var9 + Math.sin(var4) * (double) var5 + (Math.random() * (double) var5 * 2.0 - (double) var5) / 4.0);
                                        var10 = (float) ((double) var10 + Math.cos(var4) * (double) var6 + (Math.random() * (double) var6 * 2.0 - (double) var6) / 4.0);
                                        var4 = (float) ((double) var4 + Math.PI / (8.0 + Math.random() * 25.0));
                                        if (var10 > 90.0) {
                                            var10 = 180.0F - var10;
                                        }

                                        if ((double) var10 < -90.0) {
                                            var10 = -180.0F - var10;
                                        }

                                        if (var9 >= 180.0) {
                                            var9 -= 360.0F;
                                        }

                                        if (var9 <= -180.0F) {
                                            var9 += 360.0F;
                                        }

                                        ControlUtils.changeDirection(var9, var10);
                                        Thread.sleep(20L);
                                        ControlUtils.checkDirection(var9, var10, true);
                                    }

                                    ControlUtils.changeDirection(var7 + var5 - 2.0F * var5 * (float) Math.random(), var8 + var6 - 2.0F * var6 * (float) Math.random());
                                } catch (Exception var11) {
                                    var11.printStackTrace();
                                }

                            })).start();
                        }
                    }

                    if (var3 - startTime >= 280000L && Configs.AutoMoveTimer) {
                        MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load var1) {
        if (Checker.enabled) {
            if (Configs.AutoMove) {
                if (Configs.SafeAutoMove) {
                    if (this.shouldMove) {
                        this.stopMove();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent var1) {
        if (Checker.enabled) {
            if (Configs.AutoMove) {
                String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText());
                if (var2.matches(" ☠ [a-zA-Z0-9_]+ was killed by Lord Jawbus.") && Configs.JawbusWarn) {
                    warn(0.001);
                }

                if (this.shouldMove) {
                    if (var2.equals("The Golden Fish escapes your hook but looks weakened.")) {
                        this.lastReeledIn = TimeUtils.curTime();
                        (new Thread(this::cast)).start();
                    }

                    if (var2.equals("The Golden Fish is weak!")) {
                        this.lastReeledIn = TimeUtils.curTime();
                        (new Thread(this::reelIn)).start();
                    }

                    if (var2.equals("NEW DISCOVERY") && Configs.StopWhenNewDiscovery) {
                        this.stopMove();
                    }

                }
            }
        }
    }

    private void stopMove() {
        this.shouldMove = false;
        startTime = 0L;
        ChatLib.chat("Auto Move &cdeactivated");
        ControlUtils.stopMoving();
    }

    @SubscribeEvent
    public void onTickPushingThread(TickEndEvent var1) {
        if (Checker.enabled) {
            if (!this.shouldMove) {
                if (this.pushingThread != null) {
                    this.pushingThread.interrupt();
                }

            } else {
                if (this.pushingThread == null || !this.pushingThread.isAlive()) {
                    this.pushingThread = new Thread(() -> {
                        startPushing = TimeUtils.curTime();

                        try {
                            Thread.sleep(Configs.FishCheckCD * 1000L);
                            this.cast();
                            Thread.sleep(Configs.FishCheckCD * 1000L);
                            this.cast();
                            Thread.sleep(Configs.FishCheckCD * 1000L);
                            ChatLib.chat("Too long since last catch!");
                            MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
                            this.stopMove();
                        } catch (InterruptedException var2) {
                        }

                    });
                    this.pushingThread.start();
                }

            }
        }
    }
}
