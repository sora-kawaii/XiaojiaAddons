package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NetUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlowdown {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent var1) {
        if (true) {
            if (SkyblockUtils.isInSkyblock() || !Configs.DisableNoSlowOutofSkyBlock) {
                try {
                    ItemStack var2 = ControlUtils.getHeldItemStack();
                    if (var1.action == Action.RIGHT_CLICK_AIR && var2 != null && var2.getItem().getRegistryName().toLowerCase().contains("sword")) {
                        String var3 = var2.getDisplayName();
                        if (!Configs.NoSlowdownAll) {
                            boolean var4 = Configs.NoSlowdownRogue && var3.contains("Rogue");

                            if (Configs.NoSlowdownWitherBlade && (var3.contains("Hyperion") || var3.contains("Astraea") || var3.contains("Scylla") || var3.contains("Valkyrie"))) {
                                var4 = true;
                            }

                            if (Configs.NoSlowdownKatana && var3.contains("Katana")) {
                                var4 = true;
                            }

                            if (!var4) {
                                return;
                            }
                        }

                        var1.setCanceled(true);
                        if (XiaojiaAddons.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                            NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, var2, 0.0F, 0.0F, 0.0F));
                        }
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }

            }
        }
    }
}
