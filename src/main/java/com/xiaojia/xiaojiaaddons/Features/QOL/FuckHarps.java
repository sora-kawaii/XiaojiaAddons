package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FuckHarps {

    public HashMap speedOf = new HashMap() {
        {
            this.put("Hymn to the Joy", 350);
            this.put("Through the Campfire", 250);
        }
    };
    public HashMap notesOf = new HashMap() {
        {
            this.put("Hymn to the Joy", FuckHarps.this.getHymn());
            this.put("Through the Campfire", FuckHarps.this.getCamp());
        }
    };
    private String currentSong = "";
    private boolean isThreadRunning = false;
    private boolean stop = false;

    private ArrayList getHymn() {
        ArrayList var1 = new ArrayList();
        var1.add(new Tuple(0, 2));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(2, 3));
        var1.add(new Tuple(2, 4));
        var1.add(new Tuple(2, 4));
        var1.add(new Tuple(2, 3));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(3, 1));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(3, 2));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(2, 3));
        var1.add(new Tuple(2, 4));
        var1.add(new Tuple(2, 4));
        var1.add(new Tuple(2, 3));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(3, 0));
        var1.add(new Tuple(1, 0));
        return var1;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            Inventory var2 = ControlUtils.getOpenedInventory();
            if (var2 != null) {
                if (!this.isThreadRunning) {
                    if (var2.getName().startsWith("Harp - ")) {
                        String var3 = var2.getName().substring(7);
                        ArrayList var4 = (ArrayList) this.notesOf.get(var3);
                        if (var4 == null) {
                            return;
                        }

                        this.isThreadRunning = true;
                        this.currentSong = var3;
                        ChatLib.chat("Starting " + var3);
                        long var5 = TimeUtils.curTime();
                        (new Thread(() -> {
                            try {
                                int var5x = (Integer) this.speedOf.get(this.currentSong);
                                Thread.sleep(3000L);
                                ChatLib.chat("Starting notes! " + (TimeUtils.curTime() - var5));
                                Iterator var6 = var4.iterator();

                                while (var6.hasNext()) {
                                    Tuple var7 = (Tuple) var6.next();
                                    int var8 = (Integer) var7.getFirst();
                                    int var9 = (Integer) var7.getSecond() + 37;
                                    Thread.sleep((long) var5x * var8);
                                    if (this.stop) {
                                        return;
                                    }

                                    ChatLib.chat("clicking! " + (TimeUtils.curTime() - var5));
                                    var2.click(var9, false, "MIDDLE");
                                }

                                Thread.sleep(100L);
                            } catch (Exception var13) {
                                var13.printStackTrace();
                            } finally {
                                MinecraftUtils.getPlayer().closeScreen();
                                this.isThreadRunning = false;
                                this.stop = false;
                            }

                        })).start();
                    }

                }
            }
        }
    }

    private ArrayList getCamp() {
        ArrayList var1 = new ArrayList();
        var1.add(new Tuple(0, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 6));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(2, 0));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 6));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(2, 2));
        var1.add(new Tuple(1, 5));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 4));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(1, 3));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 2));
        var1.add(new Tuple(1, 1));
        var1.add(new Tuple(1, 0));
        var1.add(new Tuple(2, 1));
        var1.add(new Tuple(1, 6));
        return var1;
    }

    @SubscribeEvent
    public void onTickCheckStop(TickEndEvent var1) {
        if (this.isThreadRunning) {
            Inventory var2 = ControlUtils.getOpenedInventory();
            if (var2 == null || !var2.getName().contains(this.currentSong)) {
                this.stop = true;
            }

        }
    }
}
