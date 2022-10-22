package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;

public class DevWater {

    public static int process = 0;
    private static long lastKey = 0L;
    private static EnumState[][] board = new EnumState[24][21];
    private static BufferedImage map = null;
    private final KeyBind devKeyBind = new KeyBind("Dev Water", 0);

    public static void setBoard(int var0, int var1) {
        Patterns.Pattern var2 = (Patterns.Pattern) Patterns.patterns.get(var0);
        board = new EnumState[24][21];

        for (int var3 = 0; var3 < 24; ++var3) {
            System.arraycopy(var2.board[var3], 0, board[var3], 0, 21);
        }

        WaterUtils.operations = var2.operations[var1].operations;
        WaterUtils.getBoardString(board);
        System.err.println(WaterUtils.boardString);
        ChatLib.chat("flag: " + var1);
        Iterator var5 = WaterUtils.operations.entrySet().iterator();

        while (var5.hasNext()) {
            Map.Entry var6 = (Map.Entry) var5.next();
            if (!var6.getValue().equals(EnumOperation.empty)) {
                ChatLib.chat("  " + (double) (Integer) var6.getKey() * 0.25 + "s: " + WaterSolver.getMessageFromOperation((EnumOperation) var6.getValue()));
            }
        }

        WaterUtils.processBoard(board);
        process = -1;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (SessionUtils.isDev()) {
                if (this.devKeyBind.isKeyDown()) {
                    if (TimeUtils.curTime() - lastKey < 250L) {
                        return;
                    }

                    lastKey = TimeUtils.curTime();
                    if (WaterUtils.operations.containsKey(process)) {
                        ChatLib.chat("processing " + WaterUtils.operations.get(process));
                        board = WaterUtils.getStatesFromOperation(board, (EnumOperation) WaterUtils.operations.get(process));
                    }

                    board = (EnumState[][]) WaterUtils.simulate(board).getKey();
                    ++process;
                    BufferedImage var2 = new BufferedImage(21, 24, 6);

                    for (int var3 = 0; var3 < 24; ++var3) {
                        for (int var4 = 0; var4 < 21; ++var4) {
                            Color var5 = null;
                            if (WaterUtils.isWater(board[var3][var4])) {
                                var5 = new Color(65, 65, 255);
                            } else if (board[var3][var4] == EnumState.cc) {
                                var5 = new Color(0, 0, 0);
                            } else if (board[var3][var4] == EnumState.ccl) {
                                var5 = new Color(180, 65, 65);
                            } else if (board[var3][var4] == EnumState.cd) {
                                var5 = new Color(90, 240, 240);
                            } else if (board[var3][var4] == EnumState.cg) {
                                var5 = new Color(255, 180, 0);
                            } else if (board[var3][var4] == EnumState.ce) {
                                var5 = new Color(0, 180, 0);
                            } else if (board[var3][var4] == EnumState.cq) {
                                var5 = new Color(255, 255, 255);
                            } else {
                                if (!WaterUtils.isBlock(board[var3][var4])) {
                                    continue;
                                }

                                var5 = new Color(120, 120, 120);
                            }

                            var2.setRGB(var4, 24 - var3 - 1, var5.getRGB());
                        }
                    }

                    map = var2;
                }

            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre var1) {
        if (Checker.enabled) {
            if (SessionUtils.isDev()) {
                if (Configs.DevWater) {
                    if (var1.type == ElementType.TEXT) {
                        if (map != null) {
                            RenderUtils.start();
                            RenderUtils.drawImage(new Image(map), Configs.MapX, Configs.MapY, 25 * Configs.MapScale, 25 * Configs.MapScale);
                            RenderUtils.retainTransforms(true);
                            RenderUtils.translate((float) Configs.MapX, (float) Configs.MapY, 0.0F);
                            RenderUtils.scale(0.1F * (float) Configs.MapScale, 0.1F * (float) Configs.MapScale);

                            for (int var2 = 0; var2 < 24; ++var2) {
                                for (int var3 = 0; var3 < 21; ++var3) {
                                    int var4 = WaterUtils.getWaterValueOf(board[var2][var3]);
                                    if (var4 != 0) {
                                        if (board[var2][var3] == EnumState.w) {
                                            var4 = 9;
                                        }

                                        RenderUtils.drawStringWithShadow(var4 + "", (float) var3 * 12.0F, (float) (23 - var2) * 10.5F);
                                    }
                                }
                            }

                            RenderUtils.retainTransforms(false);
                            RenderUtils.end();
                        }
                    }
                }
            }
        }
    }
}
