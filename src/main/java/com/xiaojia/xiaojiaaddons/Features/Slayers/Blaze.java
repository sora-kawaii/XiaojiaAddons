package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Blaze {

    private static final ArrayList states = new ArrayList(Arrays.asList("SPIRIT", "CRYSTAL", "ASHEN", "AURIC"));
    private static final Display display = new Display();
    private static long lastSwapSPICRY = 0L;
    private static long lastSwapASHAUR = 0L;
    private AxisAlignedBB box = null;

    public Blaze() {
        display.setShouldRender(false);
        display.setBackground("full");
        display.setBackgroundColor(0);
        display.setAlign("center");
    }

    public static void doSwap(String var0) {
        ChatLib.debug("Current state: " + var0);
        boolean var1 = false;
        int var2 = -1;
        boolean var4 = var0.equals("ASHEN") || var0.equals("AURIC");
        ArrayList var3;
        if (var4) {
            var3 = new ArrayList(Arrays.asList("FIREDUST_DAGGER", "BURSTFIRE_DAGGER", "HEARTFIRE_DAGGER"));
        } else {
            var3 = new ArrayList(Arrays.asList("MAWDUST_DAGGER", "BURSTMAW_DAGGER", "HEARTMAW_DAGGER"));
        }

        for (int var5 = 0; var5 < 8; ++var5) {
            ItemStack var6 = ControlUtils.getItemStackInSlot(var5 + 36, true);
            String var7 = NBTUtils.getSkyBlockID(var6);
            if (var3.stream().anyMatch((var1x) -> {
                return var1x.equals(var7);
            })) {
                List var8 = NBTUtils.getLore(var6);
                AttunedState var9 = null;
                Iterator var10 = var8.iterator();

                while (var10.hasNext()) {
                    String var11 = (String) var10.next();
                    String var12 = ChatLib.removeFormatting(var11);
                    if (var12.equals("Attuned: Ashen")) {
                        var9 = Blaze.AttunedState.ASHEN;
                    }

                    if (var12.equals("Attuned: Auric")) {
                        var9 = Blaze.AttunedState.AURIC;
                    }

                    if (var12.equals("Attuned: Spirit")) {
                        var9 = Blaze.AttunedState.SPIRIT;
                    }

                    if (var12.equals("Attuned: Crystal")) {
                        var9 = Blaze.AttunedState.CRYSTAL;
                    }
                }

                if (var9 != null) {
                    var2 = var5;
                    var1 = !var9.toString().equals(var0);
                    break;
                }
            }
        }

        ControlUtils.setHeldItemIndex(var2);
        if (var1) {
            long var13 = TimeUtils.curTime();
            long var14 = var13 - (var4 ? lastSwapASHAUR : lastSwapSPICRY);
            if (var14 < (long) Configs.BlazeHelperCD) {
                return;
            }

            if (var4) {
                lastSwapASHAUR = var13;
            } else {
                lastSwapSPICRY = var13;
            }

            NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, ControlUtils.getHeldItemStack(), 0.0F, 0.0F, 0.0F));
        }

    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Configs.PillarDisplay) {
            if (MinecraftUtils.getWorld() != null) {
                display.clearLines();
                display.setRenderLoc(Configs.PillarX, Configs.PillarY);
                display.setShouldRender(true);
                if (Configs.PillarTest) {
                    DisplayLine var2 = new DisplayLine("&fPillar: &6&l6s &c&l8 hits");
                    var2.setScale(1.51F * (float) Configs.PillarScale / 20.0F);
                    display.addLine(var2);
                }

                Iterator var8 = EntityUtils.getEntities().iterator();

                while (var8.hasNext()) {
                    Entity var3 = (Entity) var8.next();
                    String var4 = ChatLib.removeFormatting(var3.getName());
                    Pattern var5 = Pattern.compile("(\\d)s (\\d) hits");
                    Matcher var6 = var5.matcher(var4);
                    if (var6.find()) {
                        DisplayLine var7 = new DisplayLine("&fPillar: " + var3.getName());
                        var7.setScale(1.51F * (float) Configs.PillarScale / 20.0F);
                        display.addLine(var7);
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void onLeftClick(LeftClickEvent var1) {
        if (true) {
            if (Configs.BlazeSlayerHelper) {
                MovingObjectPosition var2 = XiaojiaAddons.mc.objectMouseOver;
                if (var2 != null) {
                    Entity var3 = var2.entityHit;
                    if (var3 != null) {
                        this.box = var3.getEntityBoundingBox().addCoord(0.0, 1.0, 0.0);
                        List var4 = MinecraftUtils.getWorld().getEntitiesWithinAABBExcludingEntity(var3, this.box);
                        Iterator var5 = var4.iterator();

                        while (true) {
                            Entity var6;
                            do {
                                if (!var5.hasNext()) {
                                    return;
                                }

                                var6 = (Entity) var5.next();
                            } while (!var6.hasCustomName());

                            String var7 = var6.getCustomNameTag();
                            Iterator var8 = states.iterator();

                            while (var8.hasNext()) {
                                String var9 = (String) var8.next();
                                if (var7.contains(var9)) {
                                    doSwap(var9);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    enum AttunedState {

        ASHEN,

        AURIC,

        CRYSTAL,

        SPIRIT
    }
}
