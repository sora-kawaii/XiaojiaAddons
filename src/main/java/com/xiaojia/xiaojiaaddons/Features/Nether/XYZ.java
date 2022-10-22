package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.*;

public class XYZ {

    private static Field entityIdField = null;

    static {
        try {
            entityIdField = C02PacketUseEntity.class.getDeclaredField("entityId");
        } catch (NoSuchFieldException var3) {
            try {
                entityIdField = C02PacketUseEntity.class.getDeclaredField("field_149567_a");
                entityIdField.setAccessible(true);
            } catch (NoSuchFieldException var2) {
                var2.printStackTrace();
            }
        }

    }

    private final HashMap hits = new HashMap();
    private final HashSet charmed = new HashSet();
    private long lastClick = 0L;

    private static boolean isZ(Entity var0) {
        String var1 = getId(var0);
        return var1 != null && var1.equals("df890e75-67c6-3119-be04-9a3175c2455d");
    }

    private static boolean isX(Entity var0) {
        String var1 = getId(var0);
        return var1 != null && var1.equals("25f7c8b4-3cc7-33df-a0c8-33f5e309036f");
    }

    private static String getId(Entity var0) {
        if (!(var0 instanceof EntityArmorStand)) {
            return null;
        } else {
            ItemStack var1 = ((EntityArmorStand) var0).getEquipmentInSlot(4);
            if (var1 == null) {
                return null;
            } else {
                return var1.getItem() == Items.skull && var1.getMetadata() == 3 ? var1.getTagCompound().getCompoundTag("SkullOwner").getString("Id") : null;
            }
        }
    }

    private static boolean isY(Entity var0) {
        String var1 = getId(var0);
        return var1 != null && var1.equals("954e4eac-fbed-3a5a-8ab7-091cb189cac1");
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent var1) {
        if (true) {
            if (Configs.XYZHelper) {
                if (SkyblockUtils.isInMysticMarsh()) {
                    if (var1.action == Action.RIGHT_CLICK_AIR) {
                        if (TimeUtils.curTime() - this.lastClick >= 30L) {
                            double var2 = 4.5;
                            if (Configs.XYZReach > var2) {
                                var2 = Configs.XYZReach;
                            }

                            AxisAlignedBB var4 = MinecraftUtils.getPlayer().getEntityBoundingBox().expand(var2, var2, var2);
                            List var5 = MinecraftUtils.getWorld().getEntitiesWithinAABBExcludingEntity(MinecraftUtils.getPlayer(), var4);
                            ArrayList var6 = new ArrayList();
                            ArrayList var7 = new ArrayList();
                            ArrayList var8 = new ArrayList();
                            Iterator var9 = var5.iterator();

                            while (var9.hasNext()) {
                                Entity var10 = (Entity) var9.next();
                                if (!this.charmed.contains(var10.getEntityId()) && var10 instanceof EntityLivingBase && ((EntityLivingBase) var10).getHealth() != 0.0F) {
                                    if (isX(var10)) {
                                        var6.add(var10);
                                    } else if (isY(var10)) {
                                        var7.add(var10);
                                    } else if (isZ(var10)) {
                                        var8.add(var10);
                                    }
                                }
                            }

                            var6.sort((var0, var1x) -> {
                                return (int) (MathUtils.distanceSquareFromPlayer((Entity) var0) - MathUtils.distanceSquareFromPlayer((Entity) var1x));
                            });
                            var7.sort((var0, var1x) -> {
                                return (int) (MathUtils.distanceSquareFromPlayer((Entity) var0) - MathUtils.distanceSquareFromPlayer((Entity) var1x));
                            });
                            var8.sort((var0, var1x) -> {
                                return (int) (MathUtils.distanceSquareFromPlayer((Entity) var0) - MathUtils.distanceSquareFromPlayer((Entity) var1x));
                            });

                            try {
                                Object var15 = null;
                                boolean var16 = false;
                                boolean var11 = false;
                                if (var8.size() > 0) {
                                    var15 = var8.get(0);
                                    var11 = true;
                                } else {
                                    Iterator var12;
                                    EntityArmorStand var13;
                                    if (var7.size() != 0) {
                                        var12 = var7.iterator();

                                        while (var12.hasNext()) {
                                            var13 = (EntityArmorStand) var12.next();
                                            if (var13.getHealth() > var13.getMaxHealth() / 2.0F && !this.hits.containsKey(var13.getEntityId())) {
                                                var15 = var13;
                                                var11 = true;
                                                break;
                                            }
                                        }

                                        if (var15 == null) {
                                            var12 = var7.iterator();

                                            while (var12.hasNext()) {
                                                var13 = (EntityArmorStand) var12.next();
                                                if (!this.charmed.contains(var13.getEntityId())) {
                                                    var15 = var13;
                                                    var16 = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    if (var15 == null && var6.size() != 0) {
                                        var12 = var6.iterator();

                                        while (var12.hasNext()) {
                                            var13 = (EntityArmorStand) var12.next();
                                            if (var13.getHealth() > var13.getMaxHealth() / 2.0F && !this.hits.containsKey(var13.getEntityId())) {
                                                var15 = var13;
                                                var11 = true;
                                                break;
                                            }
                                        }

                                        if (var15 == null) {
                                            var12 = var6.iterator();

                                            while (var12.hasNext()) {
                                                var13 = (EntityArmorStand) var12.next();
                                                if (!this.charmed.contains(var13.getEntityId())) {
                                                    var15 = var13;
                                                    var16 = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (var15 != null) {
                                    var1.setCanceled(true);
                                    this.lastClick = TimeUtils.curTime();
                                    if (var16) {
                                        this.swapCharm();
                                        this.charmed.add(((Entity) var15).getEntityId());
                                    }

                                    if (var11) {
                                        this.swapHarvest();
                                    }

                                    EntityUtils.tryRightClickEntity((Entity) var15, Configs.XYZReach);
                                }
                            } catch (Exception var14) {
                                var14.printStackTrace();
                            }

                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load var1) {
        this.charmed.clear();
        this.hits.clear();
    }

    private void swapHarvest() throws Exception {
        if (HotbarUtils.harvestSlot != -1) {
            ControlUtils.setHeldItemIndex(HotbarUtils.harvestSlot);
        } else {
            ChatLib.chat("Atominizer in hotbar is required.");
            throw new Exception("Atominizer Not Found");
        }
    }

    private void swapCharm() throws Exception {
        if (HotbarUtils.charmSlot != -1) {
            ControlUtils.setHeldItemIndex(HotbarUtils.charmSlot);
        } else {
            ChatLib.chat("Charminizer in hotbar is required.");
            throw new Exception("Charminizer Not Found");
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent var1) {
        if (entityIdField != null && var1.packet instanceof C02PacketUseEntity) {
            try {
                ItemStack var2 = ControlUtils.getHeldItemStack();
                if (!NBTUtils.getSkyBlockID(var2).equals("ATOMINIZER")) {
                    return;
                }

                int var3 = entityIdField.getInt(var1.packet);
                Entity var4 = MinecraftUtils.getWorld().getEntityByID(var3);
                if (!(var4 instanceof EntityArmorStand)) {
                    return;
                }

                boolean var5 = isX(var4);
                boolean var6 = isY(var4);
                boolean var7 = isZ(var4);
                if (!var5 && !var6 && !var7) {
                    return;
                }

                float var8 = ((EntityArmorStand) var4).getHealth();
                float var9 = ((EntityArmorStand) var4).getMaxHealth();
                if (var7 || var6 && Configs.XYZMode >= 1 || var5 && Configs.XYZMode >= 2) {
                    return;
                }

                if (2.0F * var8 - (float) ((Integer) this.hits.getOrDefault(var3, 0) + 1) * var9 > 0.0F) {
                    this.hits.put(var3, (Integer) this.hits.getOrDefault(var3, 0) + 1);
                    return;
                }

                var1.setCanceled(true);
                ChatLib.chat("XYZ Helper blocked your wrong click!");
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        }

    }
}
