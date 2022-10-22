package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class M7Dragon {

    private static final HashMap lastWarn = new HashMap();

    private static final ConcurrentHashMap dragonsMap = new ConcurrentHashMap();
    private static final HashSet done = new HashSet();
    private static final DragonInfo blueDragon = new DragonInfo(new BlockPos(84, 14, 94), "&bBlue Dragon&r: ", new Color(0.0F, 1.0F, 1.0F), "dragon_ice.png", "Corrupted Blue Relic");
    private static final DragonInfo redDragon = new DragonInfo(new BlockPos(27, 14, 59), "&cRed Dragon&r: ", new Color(1.0F, 0.0F, 0.0F), "dragon_power.png", "Corrupted Red Relic");
    private static final DragonInfo greenDragon = new DragonInfo(new BlockPos(27, 14, 94), "&aGreen Dragon&r: ", new Color(0.0F, 1.0F, 0.0F), "dragon_apex.png", "Corrupted Green Relic");
    private static final DragonInfo purpleDragon = new DragonInfo(new BlockPos(56, 14, 125), "&5Purple Dragon&r: ", new Color(0.5019608F, 0.0F, 0.5019608F), "dragon_soul.png", "Corrupted Purple Relic");
    private static final Deque particles = new ArrayDeque();
    private static final DragonInfo orangeDragon = new DragonInfo(new BlockPos(85, 14, 56), "&6Orange Dragon&r: ", new Color(1.0F, 0.64705884F, 0.0F), "dragon_flame.png", "Corrupted Orange Relic");
    private static final ArrayList dragonInfos = new ArrayList() {
        {
            this.add(M7Dragon.redDragon);
            this.add(M7Dragon.greenDragon);
            this.add(M7Dragon.purpleDragon);
            this.add(M7Dragon.blueDragon);
            this.add(M7Dragon.orangeDragon);
        }
    };
    private static long lastCheck = 0L;
    private final Display display = new Display();

    public M7Dragon() {
        this.display.setShouldRender(true);
        this.display.setBackground("full");
        this.display.setBackgroundColor(0);
        this.display.setAlign("left");
    }

    public static void onSpawnDragon(EntityDragon var0) {
        if (SkyblockUtils.isInDungeon()) {
            double var1 = 1.0E9;
            DragonInfo var3 = null;
            Iterator var4 = dragonInfos.iterator();

            while (var4.hasNext()) {
                DragonInfo var5 = (DragonInfo) var4.next();
                BlockPos var6 = var5.blockPos;
                double var7 = var0.getDistanceSq(var6);
                if (var7 < var1) {
                    var1 = var7;
                    var3 = var5;
                }
            }

            if (var3 != null) {
                ChatLib.debug("spawning drag: " + var3.prefix + ", lastwarn diff " + (TimeUtils.curTime() - (Long) lastWarn.getOrDefault(var3, 0L)));
                dragonsMap.put(var0, var3);
            }
        }
    }

    private static DragonInfo getDragonInfoFromHP(float var0) {
        if (var0 > 3.0E8F) {
            return greenDragon;
        } else {
            return var0 > 1.0E8F ? orangeDragon : redDragon;
        }
    }

    public static void getEntityTexture(EntityDragon var0, CallbackInfoReturnable var1) {
        if (Checker.enabled) {
            if (Configs.ReplaceDragonTexture != 0) {
                String var3;
                if (Configs.ReplaceDragonTexture == 1) {
                    if (!dragonsMap.containsKey(var0)) {
                        return;
                    }

                    var3 = ((DragonInfo) dragonsMap.get(var0)).textureName;
                } else {
                    var3 = getDragonInfoFromHP(var0.getHealth()).textureName;
                }

                ResourceLocation var2 = new ResourceLocation("xiaojiaaddons:" + var3);
                var1.setReturnValue(var2);
            }
        }
    }

    private static float getScale(float var0) {
        if (var0 > 30.0F) {
            return 1.0F;
        } else {
            return var0 > 15.0F ? 1.0F + (30.0F - var0) / 15.0F * 0.1F : 1.3F - var0 / 15.0F * 0.2F;
        }
    }

    private static DragonInfo getDragonInfoFromHelmName(String var0) {
        Iterator var1 = dragonInfos.iterator();

        DragonInfo var2;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            var2 = (DragonInfo) var1.next();
        } while (!var2.headName.equals(var0));

        return var2;
    }

    @SubscribeEvent
    public void onReceive(PacketReceivedEvent var1) {
        if (Checker.enabled) {
            if (SkyblockUtils.isInDungeon()) {
                if (SkyblockUtils.getDungeon().contains("7")) {
                    if (var1.packet instanceof S2APacketParticles) {
                        S2APacketParticles var2 = (S2APacketParticles) var1.packet;
                        if (var2.getParticleType() != EnumParticleTypes.SMOKE_LARGE) {
                            return;
                        }

                        if (var2.getYCoordinate() < 10.0 || var2.getYCoordinate() > 28.0) {
                            return;
                        }

                        DragonInfo var3 = null;
                        Iterator var4 = dragonInfos.iterator();

                        while (var4.hasNext()) {
                            DragonInfo var5 = (DragonInfo) var4.next();
                            if (var5.blockPos.distanceSq(var2.getXCoordinate(), var5.blockPos.getY(), var2.getZCoordinate()) < 1.0) {
                                var3 = var5;
                            }
                        }

                        if (var3 == null) {
                            return;
                        }

                        if (TimeUtils.curTime() - (Long) lastWarn.getOrDefault(var3, 0L) > 6000L) {
                            lastWarn.put(var3, TimeUtils.curTime());
                        }
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        dragonsMap.clear();
        done.clear();
        synchronized (particles) {
            particles.clear();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent var1) {
        if (Checker.enabled) {
            if (Configs.ShowStatueBox) {
                if (SkyblockUtils.isInDungeon() && SkyblockUtils.getDungeon().equals("M7")) {
                    Iterator var2 = dragonInfos.iterator();

                    while (var2.hasNext()) {
                        DragonInfo var3 = (DragonInfo) var2.next();
                        BlockPos var4 = var3.blockPos;
                        AxisAlignedBB var5 = new AxisAlignedBB(var4.add(-12, -2, -12), var4.add(12, 15, 12));
                        GuiUtils.drawBoundingBox(var5, 5, var3.color);
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void renderLivingPost(TickEndEvent var1) {
        this.display.clearLines();
        this.display.setRenderLoc(Configs.dragonInfoX, Configs.dragonInfoY);
        if (Checker.enabled) {
            if (Configs.dragonInfoDisplay) {
                if (MinecraftUtils.getWorld() != null) {
                    if (Configs.dragonInfoTest) {
                        this.display.addLine("dragon Info is here");
                        DisplayLine var2 = new DisplayLine("&aGreen Dragon&r: &a400M, &c7");
                        var2.setScale((float) Configs.dragonInfoScale / 20.0F * getScale(7.0F));
                        this.display.addLine(var2);
                        DisplayLine var3 = new DisplayLine("&cRed Dragon&r: &c512K, &a40");
                        var3.setScale((float) Configs.dragonInfoScale / 20.0F * getScale(45.0F));
                        this.display.addLine(var3);
                        DisplayLine var4 = new DisplayLine("&6Orange Dragon&r: &cDONE");
                        var4.setScale((float) Configs.dragonInfoScale / 20.0F * 0.9F);
                        this.display.addLine(var4);
                        DisplayLine var5 = new DisplayLine("&5Purple Dragon&r: &6150M, &620");
                        var5.setScale((float) Configs.dragonInfoScale / 20.0F * getScale(20.0F));
                        this.display.addLine(var5);
                        DisplayLine var6 = new DisplayLine("&bCyan Dragon&r: &6200M, &c13");
                        var6.setScale((float) Configs.dragonInfoScale / 20.0F * getScale(13.0F));
                        this.display.addLine(var6);
                    }

                    Iterator var16 = EntityUtils.getEntities().iterator();

                    String var8;
                    while (var16.hasNext()) {
                        Entity var17 = (Entity) var16.next();
                        if (var17 instanceof EntityDragon && dragonsMap.containsKey(var17)) {
                            DragonInfo var19 = (DragonInfo) dragonsMap.get(var17);
                            String var21 = "&a";
                            double var22 = ((EntityDragon) var17).getHealth();
                            if (var22 <= 1.0E8) {
                                var21 = "&c";
                            } else if (var22 <= 3.0E8) {
                                var21 = "&6";
                            }

                            var8 = var21 + DisplayUtils.hpToString(var22, true);
                            BlockPos var9 = var19.blockPos;
                            double var10 = Math.sqrt(var17.getDistanceSq(var9));
                            String var12 = "&a";
                            if (var22 <= 1.0 && var10 < 15.0) {
                                done.add(var9);
                            }

                            float var13 = getScale((float) var10);
                            if (var10 < 15.0) {
                                var12 = "&c";
                            } else if (var10 < 30.0) {
                                var12 = "&6";
                            }

                            String var14 = var19.prefix + var8 + "&r, " + var12 + String.format("%.2f", var10);
                            if (done.contains(var9)) {
                                var14 = var19.prefix + "&cDONE";
                                var13 = 0.9F;
                            }

                            DisplayLine var15 = new DisplayLine(var14);
                            var15.setScale((float) Configs.dragonInfoScale / 20.0F * var13);
                            this.display.addLine(var15);
                        }
                    }

                    var16 = lastWarn.keySet().iterator();

                    while (var16.hasNext()) {
                        DragonInfo var18 = (DragonInfo) var16.next();
                        long var20 = (Long) lastWarn.getOrDefault(var18, 0L);
                        long var23 = TimeUtils.curTime() - var20;
                        var23 = 5000L - var23;
                        if (var23 > 0L && var23 < 5000L) {
                            var8 = var23 < 1500L ? "&c" : (var23 < 3000L ? "&6" : "&a");
                            String var24 = var18.prefix + var8 + String.format("%.2fs", (float) var23 / 1000.0F);
                            DisplayLine var25 = new DisplayLine(var24);
                            var25.setScale((float) Configs.dragonInfoScale / 20.0F * 1.5F);
                            this.display.addLine(var25);
                        }
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Checker.enabled) {
            if (SkyblockUtils.isInDungeon()) {
                if (MinecraftUtils.getWorld() != null) {
                    if (TimeUtils.curTime() - lastCheck > (long) Configs.DragonCheckCD) {
                        lastCheck = TimeUtils.curTime();
                        (new Thread(() -> {
                            HashMap var0 = new HashMap();
                            ArrayList var1 = new ArrayList();
                            StringBuilder var2 = new StringBuilder();
                            Iterator var3 = EntityUtils.getEntities().iterator();

                            while (true) {
                                Entity var4;
                                while (true) {
                                    String var6;
                                    if (!var3.hasNext()) {
                                        HashMap var15 = new HashMap();
                                        boolean var16 = false;
                                        Iterator var17 = var0.keySet().iterator();

                                        while (var17.hasNext()) {
                                            var6 = (String) var17.next();
                                            var2.append("relic: ").append(var6).append("\n");
                                            ArrayList var7 = (ArrayList) var0.get(var6);
                                            Iterator var8 = var1.iterator();

                                            while (var8.hasNext()) {
                                                EntityDragon var9 = (EntityDragon) var8.next();
                                                int var10 = 0;
                                                var2.append(String.format("checking entity dragon: %.2f %.2f %.2f", MathUtils.getX(var9), MathUtils.getY(var9), MathUtils.getZ(var9))).append("\n");
                                                Iterator var11 = var7.iterator();

                                                while (var11.hasNext()) {
                                                    EntityArmorStand var12 = (EntityArmorStand) var11.next();
                                                    double var13 = Math.sqrt(var12.getDistanceSqToEntity(var9));
                                                    var2.append(String.format("relic: %.2f %.2f %.2f, %.2f", MathUtils.getX(var12), MathUtils.getY(var12), MathUtils.getZ(var12), var13)).append("\n");
                                                    if (var13 > 3.0 && var13 < 4.0) {
                                                        ++var10;
                                                    }
                                                }

                                                DragonInfo var18 = getDragonInfoFromHelmName(var6);
                                                if (var18 == null) {
                                                    var16 = true;
                                                } else if (var10 >= 8) {
                                                    if (var15.containsKey(var9)) {
                                                        var15.remove(var9);
                                                        var2.append(String.format("wtf this dragon is counted as %s and %s", ((DragonInfo) var15.get(var9)).headName, var6)).append("\n");
                                                        var16 = true;
                                                    } else {
                                                        var15.put(var9, var18);
                                                    }
                                                }
                                            }
                                        }

                                        dragonsMap.putAll(var15);
                                        return;
                                    }

                                    var4 = (Entity) var3.next();
                                    if (!(var4 instanceof EntityArmorStand)) {
                                        break;
                                    }

                                    ItemStack var5 = ((EntityArmorStand) var4).getEquipmentInSlot(4);
                                    if (var5 != null) {
                                        var6 = ChatLib.removeFormatting(var5.getDisplayName());
                                        if (var6.contains("Corrupted ")) {
                                            var0.putIfAbsent(var6, new ArrayList());
                                            ((ArrayList) var0.get(var6)).add(var4);
                                        }
                                        break;
                                    }
                                }

                                if (var4 instanceof EntityDragon) {
                                    var1.add(var4);
                                }
                            }
                        })).start();
                    }

                }
            }
        }
    }
}
