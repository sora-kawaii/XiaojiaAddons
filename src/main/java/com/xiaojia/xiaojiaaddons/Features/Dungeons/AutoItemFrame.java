package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class AutoItemFrame {

    private static final HashMap neededRotation = new HashMap();
    private static final HashSet grid = new HashSet();
    private static BlockPos bottomRight = new BlockPos(-2, 121, 75);
    private static Iterable iterableBox;
    private static BlockPos topLeft = new BlockPos(-2, 125, 79);
    private static StringBuilder log;
    private static ArrayList box;

    static {
        iterableBox = BlockPos.getAllInBox(topLeft, bottomRight);
        box = new ArrayList() {
            {
                AutoItemFrame.iterableBox.forEach(this::add);
                this.sort((var0_, var1_) -> {
                    BlockPos var0 = (BlockPos) var0_, var1 = (BlockPos) var1_;
                    return var0.getY() == var1.getY() ? var1.getZ() - var0.getZ() : var1.getY() - var0.getY();
                });
            }
        };
        log = new StringBuilder();
    }

    private Thread thread = null;

    public static void setPosition(int var0, int var1, int var2) {
        topLeft = new BlockPos(var0, var1, var2);
        bottomRight = new BlockPos(var0, var1 - 4, var2 - 4);
        iterableBox = BlockPos.getAllInBox(topLeft, bottomRight);
        box = new ArrayList() {
            {
                AutoItemFrame.iterableBox.forEach(this::add);
                this.sort((var0_, var1_) -> {
                    BlockPos var0 = (BlockPos) var0_, var1 = (BlockPos) var1_;
                    return var0.getY() == var1.getY() ? var1.getZ() - var0.getZ() : var1.getY() - var0.getY();
                });
            }
        };
        grid.clear();
        neededRotation.clear();
    }

    public static void printLog() {
        System.err.println("AutoItemFrame Log:");
        System.err.println(log);
        System.err.println();
    }

    private static HashMap getDis(MazeGrid var0, MazeGrid var1) {
        int[] var2 = new int[]{0, 0, -1, 1};
        int[] var3 = new int[]{-1, 1, 0, 0};
        int[] var4 = new int[]{5, 1, 7, 3};
        MazeGrid[][] var5 = new MazeGrid[5][5];
        int[][] var6 = new int[5][5];
        int[][] var7 = new int[5][5];

        for (int var8 = 0; var8 < 5; ++var8) {
            for (int var9 = 0; var9 < 5; ++var9) {
                var6[var8][var9] = 1000;
            }
        }

        MazeGrid var21;
        for (Iterator var19 = grid.iterator(); var19.hasNext(); var5[var21.gridPos.x][var21.gridPos.y] = var21) {
            var21 = (MazeGrid) var19.next();
        }

        var6[var0.gridPos.x][var0.gridPos.y] = 0;
        HashMap var20 = new HashMap();
        var20.put(var0, 0);
        var20.getClass();
        PriorityQueue var22 = new PriorityQueue(Comparator.comparingInt((a) -> (Integer) var20.get(a)));
        var22.add(var0);

        while (!var22.isEmpty()) {
            MazeGrid var10 = (MazeGrid) var22.poll();
            int var11 = var10.gridPos.x;
            int var12 = var10.gridPos.y;

            for (int var13 = 0; var13 < 4; ++var13) {
                int var14 = var11 + var2[var13];
                int var15 = var12 + var3[var13];
                if (var14 >= 0 && var14 < 5 && var15 >= 0 && var15 < 5 && var5[var14][var15].type != Type.EMPTY && var6[var14][var15] > var6[var11][var12] + 1) {
                    var6[var14][var15] = var6[var11][var12] + 1;
                    var7[var14][var15] = var13;
                    if (var14 == var1.gridPos.x && var15 == var1.gridPos.y) {
                        HashMap var16 = new HashMap();
                        new Vector2i(var14, var15);

                        while (var14 != var0.gridPos.x || var15 != var0.gridPos.y) {
                            int var18 = var7[var14][var15];
                            var14 -= var2[var18];
                            var15 -= var3[var18];
                            var16.put(new Vector2i(var14, var15), var4[var18]);
                        }

                        return var16;
                    }

                    var20.put(var5[var14][var15], var6[var14][var15]);
                    var22.add(var5[var14][var15]);
                }
            }
        }

        return new HashMap();
    }

    @SubscribeEvent
    public void onTickCheck(TickEndEvent var1) {
        if (true) {
            if (Configs.AutoItemFrameArrows) {
                if (SkyblockUtils.isInDungeon()) {
                    if (MathUtils.distanceSquareFromPlayer(topLeft) <= 625.0) {
                        if (this.thread == null || !this.thread.isAlive()) {
                            this.thread = new Thread(() -> {
                                try {
                                    int var2;
                                    ArrayList var14;
                                    MazeGrid var25;
                                    if (grid.size() < 25) {
                                        var14 = new ArrayList();
                                        List var16 = EntityUtils.getEntities();
                                        Iterator var18 = var16.iterator();

                                        label201:
                                        while (true) {
                                            Entity var20;
                                            Item var23;
                                            do {
                                                do {
                                                    do {
                                                        if (!var18.hasNext()) {
                                                            for (var2 = 0; var2 < 25; ++var2) {
                                                                Vector2i var22 = new Vector2i(var2 / 5, var2 % 5);
                                                                BlockPos var24 = (BlockPos) box.get(var2);
                                                                Type var26 = Type.EMPTY;
                                                                Iterator var27 = var14.iterator();

                                                                while (var27.hasNext()) {
                                                                    EntityItemFrame var28 = (EntityItemFrame) var27.next();
                                                                    if (var28.getPosition().equals(var24)) {
                                                                        ItemStack var30 = var28.getDisplayedItem();
                                                                        if (var30.getItem() == Items.arrow) {
                                                                            var26 = Type.PATH;
                                                                        } else if (var30.getItem() == Item.getItemFromBlock(Blocks.wool)) {
                                                                            if (var30.getItemDamage() == 5) {
                                                                                var26 = Type.START;
                                                                            } else if (var30.getItemDamage() == 14) {
                                                                                var26 = Type.END;
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                var25 = new MazeGrid(var24, var26, var22);
                                                                grid.add(var25);
                                                                log.append(var25).append("\n");
                                                            }
                                                            break label201;
                                                        }

                                                        var20 = (Entity) var18.next();
                                                    } while (!(var20 instanceof EntityItemFrame));
                                                } while (!box.contains(var20.getPosition()));

                                                var23 = ((EntityItemFrame) var20).getDisplayedItem().getItem();
                                            } while (!var23.equals(Items.arrow) && !var23.equals(Item.getItemFromBlock(Blocks.wool)));

                                            var14.add(var20);
                                        }
                                    } else {
                                        int var3;
                                        int var5;
                                        if (neededRotation.isEmpty()) {
                                            var14 = new ArrayList();
                                            ArrayList var15 = new ArrayList();
                                            ArrayList var17 = new ArrayList();
                                            grid.stream().filter((var0x) -> {
                                                return ((MazeGrid) var0x).type == Type.START;
                                            }).forEach(var14::add);
                                            grid.stream().filter((var0x) -> {
                                                return ((MazeGrid) var0x).type == Type.END;
                                            }).forEach((var2x) -> {
                                                var15.add(var2x);
                                                var17.add(false);
                                            });
                                            log.append("grid:\n");
                                            grid.forEach((var0x) -> {
                                                log.append(var0x.toString()).append("\n");
                                            });
                                            log.append("start:").append("\n");
                                            var14.forEach((var0x) -> {
                                                log.append(var0x.toString()).append("\n");
                                            });
                                            log.append("end:").append("\n");
                                            var15.forEach((var0x) -> {
                                                log.append(var0x.toString()).append("\n");
                                            });
                                            Iterator var19 = var14.iterator();

                                            HashMap var7;
                                            HashMap var10001;
                                            MazeGrid var21;
                                            while (var19.hasNext()) {
                                                var21 = (MazeGrid) var19.next();
                                                var5 = 1000;
                                                var25 = new MazeGrid(null, Type.EMPTY, new Vector2i(-1, -1));
                                                var7 = new HashMap();
                                                int var8 = -1;

                                                for (int var9 = 0; var9 < var15.size(); ++var9) {
                                                    MazeGrid var10 = (MazeGrid) var15.get(var9);
                                                    HashMap var11 = getDis(var21, var10);
                                                    int var12 = var11.size();
                                                    if (var12 != 0 && (var12 < var5 || var12 == var5 && var25.compareTo(var10) < 0)) {
                                                        log.append("end: " + var10.gridPos + ", corr: " + var25.gridPos).append("\n");
                                                        log.append("dis: " + var12 + ", minDis: " + var5).append("\n");
                                                        var5 = var12;
                                                        var25 = var10;
                                                        var8 = var9;
                                                        var7 = var11;
                                                    }
                                                }

                                                if (!neededRotation.isEmpty()) {
                                                    var17.set(var8, true);
                                                }

                                                var10001 = neededRotation;
                                                var7.forEach(var10001::put);
                                            }

                                            label150:
                                            for (var3 = 0; var3 < var15.size(); ++var3) {
                                                var21 = (MazeGrid) var15.get(var3);
                                                if (!(Boolean) var17.get(var3)) {
                                                    var5 = 1000;
                                                    var25 = new MazeGrid(null, Type.EMPTY, new Vector2i(-1, -1));
                                                    var7 = new HashMap();
                                                    Iterator var29 = var14.iterator();

                                                    while (true) {
                                                        HashMap var32;
                                                        int var33;
                                                        do {
                                                            do {
                                                                if (!var29.hasNext()) {
                                                                    var10001 = neededRotation;
                                                                    var7.forEach(var10001::put);
                                                                    continue label150;
                                                                }

                                                                MazeGrid var31 = (MazeGrid) var29.next();
                                                                var32 = getDis(var31, var21);
                                                                var33 = var32.size();
                                                            } while (var33 == 0);
                                                        } while (var33 >= var5 && (var33 != var5 || var25.compareTo(var21) >= 0));

                                                        log.append("start: " + var21.gridPos + ", corr: " + var25.gridPos).append("\n");
                                                        log.append("dis: " + var33 + ", minDis: " + var5).append("\n");
                                                        var5 = var33;
                                                        var25 = var21;
                                                        var7 = var32;
                                                    }
                                                }
                                            }
                                        } else if (XiaojiaAddons.mc.objectMouseOver != null && XiaojiaAddons.mc.objectMouseOver.entityHit != null) {
                                            Entity var0 = XiaojiaAddons.mc.objectMouseOver.entityHit;
                                            if (!(var0 instanceof EntityItemFrame)) {
                                                return;
                                            }

                                            ItemStack var1_ = ((EntityItemFrame) var0).getDisplayedItem();
                                            if (var1_ == null || !var1_.getUnlocalizedName().contains("arrow")) {
                                                return;
                                            }

                                            var2 = ((EntityItemFrame) var0).getRotation();
                                            var3 = box.indexOf(var0.getPosition());
                                            Vector2i var4 = new Vector2i(var3 / 5, var3 % 5);
                                            var5 = (Integer) neededRotation.get(var4) - var2;
                                            if (var5 < 0) {
                                                var5 += 8;
                                            }

                                            for (int var6 = 0; var6 < var5; ++var6) {
                                                if (XiaojiaAddons.mc.objectMouseOver == null || XiaojiaAddons.mc.objectMouseOver.entityHit != var0) {
                                                    return;
                                                }

                                                if ((Integer) neededRotation.get(var4) == ((EntityItemFrame) XiaojiaAddons.mc.objectMouseOver.entityHit).getRotation()) {
                                                    return;
                                                }

                                                ControlUtils.rightClick();
                                                Thread.sleep(Configs.ArrowCD);
                                            }

                                            Thread.sleep(Configs.ArrowCDBetween);
                                        }
                                    }
                                } catch (Exception var13) {
                                    var13.printStackTrace();
                                }

                            });
                            this.thread.start();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        grid.clear();
        neededRotation.clear();
        log = new StringBuilder();
    }
}
