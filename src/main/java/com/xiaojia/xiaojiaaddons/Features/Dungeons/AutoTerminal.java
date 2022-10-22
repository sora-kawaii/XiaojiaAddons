package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoTerminal {

    private final Deque clickQueue = new ArrayDeque();
    private String color;
    private char startChar;

    private int windowClicks;

    private EnumTerminal enumTerminal;

    private boolean recalculate;

    private int windowID;

    private long lastClickTime;

    public AutoTerminal() {
        this.enumTerminal = EnumTerminal.NONE;
        this.recalculate = false;
        this.startChar = ' ';
        this.color = "";
        this.lastClickTime = 0L;
        this.windowID = 0;
        this.windowClicks = 0;
    }

    private boolean calculate(ArrayList var1) {
        this.clickQueue.clear();
        int var11;
        int var14;
        int var21;
        int var22;
        String var24;
        switch (this.enumTerminal) {
            case MAZE:
                if (var1.size() < 54) {
                    return true;
                } else {
                    int[] var2 = new int[]{0, 0, -1, 1};
                    int[] var3 = new int[]{1, -1, 0, 0};
                    Tuple var4 = new Tuple(-1, -1);
                    ArrayList var5 = new ArrayList();

                    for (int var6 = 0; var6 < 6; ++var6) {
                        for (var21 = 0; var21 < 9; ++var21) {
                            var5.add(false);
                            var22 = var6 * 9 + var21;
                            if (var1.get(var22) != null && ((ItemStack) var1.get(var22)).getItemDamage() == 5) {
                                var4 = new Tuple(var6, var21);
                            }
                        }
                    }

                    if ((Integer) var4.getFirst() == -1) {
                        return true;
                    } else {
                        ArrayDeque var20 = new ArrayDeque();
                        var20.addLast(var4);

                        while (!var20.isEmpty()) {
                            Tuple var23 = (Tuple) var20.pollFirst();
                            var22 = (Integer) var23.getFirst();
                            int var25 = (Integer) var23.getSecond();
                            if ((Boolean) var5.get(var22 * 9 + var25) || var22 < 0 || var25 < 0 || var22 >= 6 || var25 >= 9) {
                                return true;
                            }

                            var5.set(var22 * 9 + var25, true);

                            for (int var26 = 0; var26 < 4; ++var26) {
                                var11 = var22 + var2[var26];
                                int var27 = var25 + var3[var26];
                                if (var11 >= 0 && var11 < 6 && var27 >= 0 && var27 < 9) {
                                    int var28 = var11 * 9 + var27;
                                    if (!(Boolean) var5.get(var28) && var1.get(var28) != null) {
                                        var14 = ((ItemStack) var1.get(var28)).getItemDamage();
                                        if (var14 == 0 || var14 == 5) {
                                            if (var14 == 0) {
                                                this.clickQueue.add(var28);
                                            }

                                            var20.addLast(new Tuple(var11, var27));
                                        }

                                        if (var14 == 14) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }

                        return true;
                    }
                }
            case CORRECT:
                if (var1.size() < 45) {
                    return true;
                }

                for (var21 = 0; var21 < 45; ++var21) {
                    if (var1.get(var21) != null && ((ItemStack) var1.get(var21)).getItemDamage() == 14) {
                        this.clickQueue.add(var21);
                    }
                }

                return false;
            case ORDER:
                if (var1.size() < 36) {
                    return true;
                } else {
                    int[] var7 = new int[14];

                    for (var22 = 0; var22 < 14; ++var22) {
                        var7[var22] = -1;
                    }

                    for (var22 = 10; var22 <= 25; ++var22) {
                        if (var22 != 17 && var22 != 18 && var1.get(var22) != null && ((ItemStack) var1.get(var22)).getItemDamage() == 14) {
                            if (((ItemStack) var1.get(var22)).stackSize < 1 || ((ItemStack) var1.get(var22)).stackSize > 14) {
                                return true;
                            }

                            var7[((ItemStack) var1.get(var22)).stackSize - 1] = var22;
                        }
                    }

                    for (var22 = 0; var22 < 14; ++var22) {
                        if (var7[var22] != -1) {
                            this.clickQueue.add(var7[var22]);
                        }
                    }

                    return false;
                }
            case START:
                if (var1.size() < 54) {
                    return true;
                }

                for (var22 = 0; var22 < 54; ++var22) {
                    if (var1.get(var22) != null) {
                        var24 = ChatLib.removeFormatting(((ItemStack) var1.get(var22)).getDisplayName());
                        if (var24 != null && var24.length() >= 1 && var24.charAt(0) == this.startChar && !((ItemStack) var1.get(var22)).isItemEnchanted()) {
                            this.clickQueue.add(var22);
                        }
                    }
                }

                return false;
            case COLOR:
                if (var1.size() < 54) {
                    return true;
                }

                for (var22 = 0; var22 < 54; ++var22) {
                    if (var1.get(var22) != null) {
                        var24 = ChatLib.removeFormatting(((ItemStack) var1.get(var22)).getDisplayName()).toUpperCase();
                        if (var24.contains(this.color) || this.color.equals("SILVER") && var24.contains("LIGHT GRAY") || this.color.equals("WHITE") && var24.equals("WOOL") || this.color.equals("WHITE") && var24.contains("BONE") || this.color.equals("BLACK") && var24.contains("INK") || this.color.equals("BROWN") && var24.contains("COCOA") || this.color.equals("BLUE") && var24.contains("LAPIS")) {
                            this.clickQueue.add(var22);
                        }
                    }
                }

                return false;
            case COLORPAD:
                if (var1.size() < 54) {
                    return true;
                }

                int[] var8 = new int[]{12, 13, 14, 21, 22, 23, 30, 31, 32};
                HashMap var9 = new HashMap();
                ArrayList var10 = new ArrayList();

                for (var11 = 0; var11 < 5; ++var11) {
                    var9.put(4, (4 + var11) % 5);
                    var9.put(13, (3 + var11) % 5);
                    var9.put(11, (2 + var11) % 5);
                    var9.put(14, (1 + var11) % 5);
                    var9.put(1, (var11) % 5);
                    ArrayList var12 = new ArrayList();
                    int[] var13 = var8;
                    var14 = var8.length;

                    for (int var15 = 0; var15 < var14; ++var15) {
                        int var16 = var13[var15];
                        if (var1.get(var16) != null) {
                            int var17 = ((ItemStack) var1.get(var16)).getItemDamage();
                            int var18 = (Integer) var9.getOrDefault(var17, 0);

                            for (int var19 = 0; var19 < var18; ++var19) {
                                var12.add(var16);
                            }
                        }
                    }

                    if (var10.size() == 0 || var10.size() > var12.size()) {
                        var10 = var12;
                    }
                }

                this.clickQueue.addAll(var10);
                return false;
            default:
                return true;
        }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent var1) {
        if (true) {
            if (SkyblockUtils.isInDungeon()) {
                Inventory var2 = ControlUtils.getOpenedInventory();
                if (var2 == null) {
                    this.clickQueue.clear();
                } else {
                    String var3 = var2.getName();
                    if (var3.startsWith("Click in order!")) {
                        this.enumTerminal = EnumTerminal.ORDER;
                    } else if (var3.startsWith("Navigate the maze!")) {
                        this.enumTerminal = EnumTerminal.MAZE;
                    } else if (var3.startsWith("Correct all the panes!")) {
                        this.enumTerminal = EnumTerminal.CORRECT;
                    } else if (var3.startsWith("What starts with: '")) {
                        this.enumTerminal = EnumTerminal.START;
                        this.startChar = var3.charAt(var3.indexOf("'") + 1);
                    } else if (var3.startsWith("Select all the ")) {
                        this.enumTerminal = EnumTerminal.COLOR;
                        Pattern var4 = Pattern.compile("Select all the (.*) items!");
                        Matcher var5 = var4.matcher(var3);
                        if (var5.find()) {
                            this.color = var5.group(1).toUpperCase();
                        }
                    } else if (var3.startsWith("Change all to same color!")) {
                        this.enumTerminal = EnumTerminal.COLORPAD;
                    }

                    if (this.enumTerminal == EnumTerminal.NONE) {
                        this.clickQueue.clear();
                    } else if (Configs.AutoTerminal) {
                        try {
                            if (this.clickQueue.size() == 0 || this.recalculate) {
                                this.recalculate = this.calculate(var2.getItemStacks());
                            }

                            if (this.clickQueue.size() > 20 && Configs.QuitWhenLongMaze) {
                                MinecraftUtils.getPlayer().closeScreen();
                                this.clickQueue.clear();
                                return;
                            }

                            if (!this.clickQueue.isEmpty() && TimeUtils.curTime() - this.lastClickTime > (long) Configs.AutoTerminalCD) {
                                this.lastClickTime = TimeUtils.curTime();
                                int var7 = (Integer) this.clickQueue.getFirst();
                                if (this.windowClicks == 0 || this.windowID + this.windowClicks < var2.getWindowId()) {
                                    this.windowID = var2.getWindowId();
                                    this.windowClicks = 0;
                                }

                                if (this.windowID + this.windowClicks > var2.getWindowId() + Configs.TerminalClicksInAdvance) {
                                    return;
                                }

                                XiaojiaAddons.mc.playerController.windowClick(this.windowID + this.windowClicks, var7, 2, 3, MinecraftUtils.getPlayer());
                                if (Configs.ZeroPingTerminal) {
                                    ++this.windowClicks;
                                    this.clickQueue.pollFirst();
                                }
                            }
                        } catch (Exception var6) {
                            var6.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTickCheck(TickEndEvent var1) {
        Inventory var2 = ControlUtils.getOpenedInventory();
        if (var2 != null && var2.getName().equals("container")) {
            this.clickQueue.clear();
            this.enumTerminal = EnumTerminal.NONE;
            this.recalculate = false;
            this.startChar = ' ';
            this.color = "";
            this.lastClickTime = this.windowClicks = this.windowID = 0;
        }

    }
}
