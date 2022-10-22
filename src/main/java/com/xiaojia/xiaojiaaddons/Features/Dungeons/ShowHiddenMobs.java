package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShowHiddenMobs {
    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre var1) {
        if (true) {
            if (SkyblockUtils.isInDungeon()) {
                EntityLivingBase var2 = var1.entity;
                String var3 = var2.getCommandSenderEntity().getName();
                if (var2.isInvisible()) {
                    if (Configs.ShowFels && var2 instanceof EntityEnderman) {
                        var2.setInvisible(false);
                    }

                    if (var2 instanceof EntityPlayer) {
                        if (Configs.ShowShadowAssassin && var3.contains("Shadow Assassin")) {
                            var2.setInvisible(false);
                        }

                        if (Configs.ShowStealthy) {
                            String[] var4 = AutoBlood.bloodMobs;
                            int var5 = var4.length;

                            for (int var6 = 0; var6 < var5; ++var6) {
                                String var7 = var4[var6];
                                if (var3.contains(var7)) {
                                    var2.setInvisible(false);
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
