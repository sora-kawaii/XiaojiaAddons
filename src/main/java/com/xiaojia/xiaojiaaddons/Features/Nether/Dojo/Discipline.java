package com.xiaojia.xiaojiaaddons.Features.Nether.Dojo;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Discipline {
    @SubscribeEvent
    public void onLeftClick(LeftClickEvent var1) {
        if (true) {
            if (Configs.DisciplineHelper) {
                if (DojoUtils.getTask() == EnumDojoTask.DISCIPLINE) {
                    MovingObjectPosition var2 = XiaojiaAddons.mc.objectMouseOver;
                    Entity var3 = var2.entityHit;
                    if (var3 != null) {
                        double var4 = MathUtils.getX(var3);
                        double var6 = MathUtils.getY(var3);
                        double var8 = MathUtils.getZ(var3);
                        AxisAlignedBB var10 = (new AxisAlignedBB(var4, var6, var8, var4, var6, var8)).expand(0.1, 0.0, 0.1).addCoord(0.0, 2.5, 0.0);
                        List var11 = MinecraftUtils.getWorld().getEntitiesWithinAABBExcludingEntity(var3, var10);
                        HashMap var12 = new HashMap();
                        var12.put("Wood", 0);
                        var12.put("Iron", 1);
                        var12.put("Gold", 2);
                        var12.put("Diamond", 3);
                        Iterator var13 = var11.iterator();

                        while (true) {
                            Entity var14;
                            do {
                                if (!var13.hasNext()) {
                                    return;
                                }

                                var14 = (Entity) var13.next();
                            } while (!var14.hasCustomName());

                            String var15 = ChatLib.removeFormatting(var14.getCustomNameTag());
                            ChatLib.debug(var15);
                            Iterator var16 = var12.keySet().iterator();

                            while (var16.hasNext()) {
                                String var17 = (String) var16.next();
                                if (var15.contains(var17)) {
                                    ControlUtils.setHeldItemIndex((Integer) var12.get(var17));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
