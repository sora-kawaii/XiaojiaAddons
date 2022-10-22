package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JadeCrystalHelper {

    private final ArrayList result = new ArrayList();
    private final Vector3d lastPlayerPos = null;
    private final HashMap distanceMap = new HashMap();
    private long lastPositionTime = 0L;
    private Vector3d playerPos = null;

    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent var1) {
        if (true) {
            if (Configs.JadeCrystal) {
                if (SkyblockUtils.isInCrystalHollows()) {
                    if (var1.type == 2) {
                        if (this.distanceMap.size() != 3) {
                            String var2 = ChatLib.removeFormatting(var1.message.getUnformattedText().toUpperCase());
                            Pattern var3 = Pattern.compile("TREASURE: ([0-9.]*)M");
                            Matcher var4 = var3.matcher(var2);
                            if (var4.find()) {
                                double var5 = Double.parseDouble(var4.group(1));
                                if (TimeUtils.curTime() - this.lastPositionTime > (long) Configs.JadeCrystalCD && !this.distanceMap.containsKey(this.playerPos)) {
                                    this.distanceMap.put(this.playerPos, var5);
                                    ChatLib.chat(String.format("Finished (%d / 3) points", this.distanceMap.size()));
                                    if (this.distanceMap.size() == 3) {
                                        try {
                                            this.calculate();
                                            if (this.result.get(0).equals(new BlockPos(0, 0, 0))) {
                                                this.distanceMap.remove(this.playerPos);
                                                this.result.clear();
                                                ChatLib.chat("Invalid! Try another position.\n3 Points should not be in the same line.\nIt's better to get Y difference among them.");
                                                return;
                                            }

                                            ChatLib.chat("Chest Found.");
                                        } catch (Exception var8) {
                                            var8.printStackTrace();
                                            ChatLib.chat("error calculating");
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
    public void onTick(TickEndEvent event) {
        if (true) {
            if (Configs.JadeCrystal) {
                if (SkyblockUtils.isInCrystalHollows()) {
                    if (MinecraftUtils.getPlayer() != null && MinecraftUtils.getPlayer().getPosition() != null) {
                        Vector3d var2 = new Vector3d(MathUtils.getX(MinecraftUtils.getPlayer()), MathUtils.getY(MinecraftUtils.getPlayer()), MathUtils.getZ(MinecraftUtils.getPlayer()));
                        if (this.playerPos != null && !MathUtils.equal(var2, this.playerPos)) {
                            this.lastPositionTime = TimeUtils.curTime();
                        }

                        this.playerPos = var2;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        this.distanceMap.clear();
        this.lastPositionTime = 0L;
        this.playerPos = null;
        this.result.clear();
    }

    private void calculate() {
        ArrayList var1 = new ArrayList(this.distanceMap.keySet());
        Vector3d var2 = (Vector3d) var1.get(0);
        Vector3d var3 = (Vector3d) var1.get(1);
        Vector3d var4 = (Vector3d) var1.get(2);
        double var5 = var2.x;
        double var7 = var3.x;
        double var9 = var4.x;
        double var11 = var2.y;
        double var13 = var3.y;
        double var15 = var4.y;
        double var17 = var2.z;
        double var19 = var3.z;
        double var21 = var4.z;
        double var23 = (Double) this.distanceMap.get(var2);
        double var25 = (Double) this.distanceMap.get(var3);
        double var27 = (Double) this.distanceMap.get(var4);
        double var29 = var23 * var23 - var5 * var5 - var11 * var11 - var17 * var17;
        double var31 = var25 * var25 - var7 * var7 - var13 * var13 - var19 * var19;
        double var33 = var27 * var27 - var9 * var9 - var15 * var15 - var21 * var21;
        double var35 = -(var31 - var29) / 2.0;
        double var37 = -(var33 - var29) / 2.0;
        double var39 = var7 - var5;
        double var41 = var9 - var5;
        double var43 = var13 - var11;
        double var45 = var15 - var11;
        double var47 = var19 - var17;
        double var49 = var21 - var17;
        double var51 = var39 * var45 - var43 * var41;
        double var53 = (var35 * var45 - var37 * var43) / var51;
        double var55 = (var43 * var49 - var45 * var47) / var51;
        double var57 = (var37 * var39 - var35 * var41) / var51;
        double var59 = (var41 * var47 - var39 * var49) / var51;
        double var61 = var55 * var55 + var59 * var59 + 1.0;
        double var63 = var55 * (var53 - var5) + var59 * (var57 - var11) - var17;
        double var65 = (var53 - var5) * (var53 - var5) + (var57 - var11) * (var57 - var11) + var17 * var17 - var23 * var23;
        double var67 = Math.sqrt(var63 * var63 - var61 * var65);
        double var69 = (-var63 + var67) / var61;
        double var71 = (-var63 - var67) / var61;
        double var73 = var53 + var55 * var69;
        double var75 = var57 + var59 * var69;
        double var77 = var53 + var55 * var71;
        double var79 = var57 + var59 * var71;
        this.result.add(new BlockPos(var73, var75, var69));
        this.result.add(new BlockPos(var77, var79, var71));
    }

    @SubscribeEvent
    public void onRefresh(ClientChatReceivedEvent var1) {
        if (SkyblockUtils.isInCrystalHollows()) {
            String var2 = var1.message.getUnformattedText();
            Pattern var3 = Pattern.compile("You found .* with your Metal Detector!");
            Matcher var4 = var3.matcher(var2);
            if (var4.find()) {
                this.distanceMap.clear();
                this.lastPositionTime = TimeUtils.curTime();
                this.result.clear();
                ChatLib.chat("clearing points!");
            }

        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent var1) {
        if (true) {
            if (Configs.JadeCrystal) {
                if (SkyblockUtils.isInCrystalHollows()) {
                    if (!this.result.isEmpty()) {
                        Iterator var2 = this.result.iterator();

                        while (var2.hasNext()) {
                            BlockPos var3 = (BlockPos) var2.next();
                            GuiUtils.enableESP();
                            GuiUtils.drawBoxAtBlock(var3.getX(), var3.getY(), var3.getZ(), 65, 65, 185, 100, 1, 1, 0.0F);
                            GuiUtils.disableESP();
                        }
                    }

                }
            }
        }
    }
}
