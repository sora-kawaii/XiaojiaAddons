package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Accentry.AutoQuestion;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.GolemAlert;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoItemFrame;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.BloodAssist;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Data;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoBlaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.DevWater;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.Patterns;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.WaterSolver;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.*;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.DojoUtils;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.Mastery;
import com.xiaojia.xiaojiaaddons.Features.QOL.BatchCommands;
import com.xiaojia.xiaojiaaddons.Features.QOL.InCombatQOL;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmVertical;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Blaze;
import com.xiaojia.xiaojiaaddons.Tests.CopyWorldInfo;
import com.xiaojia.xiaojiaaddons.Tests.TestM7;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Command extends CommandBase {
    public String getCommandName() {
        return "xiaojia";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getCommandUsage(ICommandSender var1) {
        return this.getUsage();
    }

    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
        if (var2 != null) {
            if (var2.length == 0) {
                ChatLib.chat(this.getUsage());
            } else {
                String var6 = var2[0];
                String var7 = var2.length == 1 ? "" : String.join(" ", Arrays.copyOfRange(var2, 1, var2.length));

                int var3;
                int var4;
                int var5;
                switch (var6) {
                    case "curmap":
                        ChatLib.chat(SkyblockUtils.getCurrentMap());
                        break;
                    case "s":
                        XiaojiaAddons.guiToOpen = new ConfigGuiNew(XiaojiaAddons.mc.gameSettings.guiScale);
                        XiaojiaAddons.mc.gameSettings.guiScale = 3;
                        break;
                    case "show":
                        String var10 = var2[1];
                        String[] var11 = null;
                        if (var10.equals("dungarmor")) {
                            var11 = InCombatQOL.dungArmor;
                        }

                        if (var10.equals("dungtrash")) {
                            var11 = InCombatQOL.dungTrash;
                        }

                        if (var10.equals("runes")) {
                            var11 = InCombatQOL.runes;
                        }

                        if (var11 != null) {
                            ChatLib.chat(Arrays.toString(var11));
                            break;
                        }
                    case "report":
                        XiaojiaAddons.autoSneakyCreeper.printLog();
                        TabUtils.printTab();
                        Dungeon.showDungeonInfo();
                        AutoItemFrame.printLog();
                        BloodAssist.printLog();
                        WaterSolver.printLog();
                        System.out.println(AutoBlaze.log.toString());
                        break;
                    case "commands":
                        BatchCommands.execute();
                        break;
                    case "copy":
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ChatLib.removeColor(var7)), null);
                        break;
                    case "rename":
                        ItemRename.process(var2);
                        break;
                    case "showitem":
                        ShowItem.show();
                        break;
                    case "tick":
                        TickEndEvent.owo();
                        break;
                    case "patterns":
                        Patterns.printPatterns();
                        break;
                    case "dev":
                        DevWater.setBoard(Integer.parseInt(var2[1]), Integer.parseInt(var2[2]));
                        break;
                    case "step":
                        AutoBuildFarmVertical.setStep(Integer.parseInt(var2[1]));
                        break;
                    case "check":
                        var3 = Integer.parseInt(var2[1]);
                        var4 = Integer.parseInt(var2[2]);
                        var5 = Integer.parseInt(var2[3]);
                        int var12 = Integer.parseInt(var2[4]);
                        int var13 = Integer.parseInt(var2[5]);
                        int var14 = Integer.parseInt(var2[6]);
                        (new Thread(() -> {
                            AutoBuildFarmVertical.check(var3, var4, var5, var3 + var12, var4 + var13, var5 + var14);
                        })).start();
                        break;
                    case "mastery":
                        Mastery.printLog();
                        break;
                    case "musicrune":
                        MusicRune.play();
                        break;
                    case "fill":
                        if (ChestFiller.isEnabled()) {
                            ChestFiller.disable();
                        } else {
                            switch (var2.length) {
                                case 2:
                                    ChestFiller.enable(var2[1], false);
                                    return;
                                case 3:
                                    ChestFiller.enable(var2[1], var2[2].equals("-6"));
                            }
                        }
                        break;
                    case "dojo":
                        ChatLib.chat(DojoUtils.getTask() + "!");
                        break;
                    case "swap":
                        (new Thread(() -> {
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException var2x) {
                                var2x.printStackTrace();
                            }

                            Blaze.doSwap(var2[1]);
                        })).start();
                        break;
                    case "cache":
                        ColorName.showCache();
                        break;
                    case "core":
                        var3 = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
                        var5 = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                        ChatLib.chat(String.format("core for current (%d %d) is: %d", var3, var5, (new Room(var3, var5, new Data("unknown", "", 0, null))).core));
                        break;
                    case "debug":
                        XiaojiaAddons.setDebug();
                        break;
                    case "keybind":
                        switch (var2.length) {
                            case 1:
                            case 2:
                                ChatLib.chat(CommandKeybind.getUsage());
                                return;
                            default:
                                String var23 = String.join(" ", Arrays.copyOfRange(var2, 2, var2.length));
                                if (var2[1].equalsIgnoreCase("add")) {
                                    CommandKeybind.add(var23);
                                    return;
                                } else if (var2[1].equalsIgnoreCase("remove")) {
                                    CommandKeybind.remove(var23);
                                    return;
                                } else {
                                    if (var2[1].equalsIgnoreCase("removeWithKey")) {
                                        var23 = String.join(" ", Arrays.copyOfRange(var2, 3, var2.length));
                                        CommandKeybind.remove(var23, var2[2]);
                                    } else {
                                        ChatLib.chat(CommandKeybind.getUsage());
                                    }

                                    return;
                                }
                        }
                    case "keybinds":
                        CommandKeybind.list();
                        break;
                    case "room":
                        ChatLib.chat("Current Room: " + Dungeon.currentRoom);
                        break;
                    case "p3":
                        var3 = Integer.parseInt(var2[1]);
                        var4 = Integer.parseInt(var2[2]);
                        var5 = Integer.parseInt(var2[3]);
                        AutoItemFrame.setPosition(var3, var4, var5);
                        break;
                    case "golem":
                        GolemAlert.golemWarn();
                        break;
                    case "cpblock":
                        var3 = Integer.parseInt(var2[1]);
                        var4 = Integer.parseInt(var2[2]);
                        var5 = Integer.parseInt(var2[3]);
                        int var15 = Integer.parseInt(var2[4]);
                        int var16 = Integer.parseInt(var2[5]);
                        int var17 = Integer.parseInt(var2[6]);
                        XiaojiaAddons.mc.theWorld.setBlockState(new BlockPos(var3, var4, var5), BlockUtils.getBlockStateAt(new BlockPos(var15, var16, var17)));
                        break;
                    case "block":
                        if (var2.length == 1) {
                            var3 = MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer()));
                            var4 = MathUtils.floor(MathUtils.getY(MinecraftUtils.getPlayer()));
                            var5 = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                        } else {
                            var3 = Integer.parseInt(var2[1]);
                            var4 = Integer.parseInt(var2[2]);
                            var5 = Integer.parseInt(var2[3]);
                        }

                        IBlockState var18 = BlockUtils.getBlockStateAt(new BlockPos(var3, var4, var5));
                        if (var18 == null) {
                            return;
                        }

                        int var19 = var18.getBlock().getMetaFromState(var18);
                        ChatLib.chat(var18.getBlock() + ", meta: " + var19);
                        break;
                    case "entities":
                        List var20 = MinecraftUtils.getWorld().loadedEntityList;
                        Iterator var21 = var20.iterator();

                        while (var21.hasNext()) {
                            Entity var22 = (Entity) var21.next();
                            ChatLib.chat(var22.hasCustomName() + ", " + var22.getName() + ", " + MathUtils.getPosString(var22) + (var22 instanceof EntityLivingBase ? ((EntityLivingBase) var22).getHealth() : "" + (var2.length == 1 ? "" : EntityUtils.getHeadTexture(var22))));
                        }

                        return;
                    case "m7":
                        TestM7.m7();
                        break;
                    case "armorstand":
                        TestM7.show();
                        break;
                    case "sound":
                        XiaojiaAddons.mc.thePlayer.playSound(var2[1], Float.parseFloat(var2[2]), Float.parseFloat(var2[3]));
                        break;
                    case "setDungeon":
                        SkyblockUtils.setDungeon(var2[1]);
                        break;
                    case "setwater":
                        Dungeon.currentRoom = "Water Board";
                        WaterSolver.setRoom(new Room(MathUtils.floor(MathUtils.getX(MinecraftUtils.getPlayer())), MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer())), Data.blankRoom));
                        break;
                    case "flag":
                        (new Thread(() -> {
                            WaterSolver.reset();
                            WaterSolver.calc(Integer.parseInt(var2[1]), null);
                        })).start();
                        break;
                    case "sp":
                        XiaojiaAddons.mc.getNetHandler().handleParticles(new S2APacketParticles(EnumParticleTypes.SMOKE_LARGE, false, MathUtils.getX(MinecraftUtils.getPlayer()), MathUtils.getY(MinecraftUtils.getPlayer()), MathUtils.getZ(MinecraftUtils.getPlayer()), Float.parseFloat(var2[1]), Float.parseFloat(var2[2]), Float.parseFloat(var2[3]), 1.0F, 0));
                        break;
                    case "cp":
                        CopyWorldInfo.copy(var2[1], var2[2], var2[3], var2[4], var2[5], var2[6]);
                        break;
                    case "paste":
                        CopyWorldInfo.paste();
                        break;
                    case "questions":
                        AutoQuestion.display();
                        break;
                    case "tp":
                        var3 = Integer.parseInt(var2[1]);
                        var4 = Integer.parseInt(var2[2]);
                        var5 = Integer.parseInt(var2[3]);
                        XiaojiaAddons.mc.thePlayer.posX = var3;
                        XiaojiaAddons.mc.thePlayer.posY = var4;
                        XiaojiaAddons.mc.thePlayer.posZ = var5;
                        break;
                    default:
                        if (XiaojiaAddons.isDebug()) {
                            SkyblockUtils.setCurrentMap(String.join(" ", var2));
                        } else {
                            ChatLib.chat(this.getUsage());
                        }
                }
            }
        }
    }

    public ArrayList getCommandAliases() {
        ArrayList var1 = new ArrayList();
        var1.add("xj");
        return var1;
    }

    private String getUsage() {
        return "&c/xj s&b to open gui settings.\n&c/xj show dungarmor&b, &c/xj show dungtrash&b, and &c/xj show runes&b to show auto-sell dungeon armors/dungeon trash/runes seperately.\n&c/xj rename&b to rename items.\n&c/xj showitem&b to show held item.\n&c/xj keybind&b to set keybinds.\n&c/xj report&b to report a bug.";
    }
}
