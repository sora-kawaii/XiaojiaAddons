package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MimicWarn {
    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent var1) {
        if (Checker.enabled) {
            if (SkyblockUtils.isInDungeon()) {
                if (var1.entity instanceof EntityZombie && ((EntityZombie) var1.entity).isChild()) {
                    if (Configs.MimicWarn) {
                        CommandsUtils.addCommand("/pc Mimic dead!");
                    }

                    Dungeon.isMimicDead = true;
                }

            }
        }
    }
}
