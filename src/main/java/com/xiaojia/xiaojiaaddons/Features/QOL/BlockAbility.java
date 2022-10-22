package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.List;

public class BlockAbility {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent var1) {
        if (true) {
            try {
                ItemStack var2 = ControlUtils.getHeldItemStack();
                if (var2 == null) {
                    return;
                }

                String var3 = var2.getDisplayName();
                if (Configs.BlockGloomlock && var3.contains("Gloomlock Grimoire")) {
                    var1.setCanceled(true);
                    ChatLib.chat("&bBlocked Gloomlock Grimoire Right Click!");
                }

                if (Configs.BlockPickobulus && SkyblockUtils.getCurrentMap().equals("Your Island") && (var3.contains("Pickaxe") || var3.contains("Drill") || var3.contains("Stonk"))) {
                    List var4 = NBTUtils.getLore(var2);
                    Iterator var5 = var4.iterator();

                    while (var5.hasNext()) {
                        String var6 = (String) var5.next();
                        if (var6.contains("Pickobulus")) {
                            var1.setCanceled(true);
                            ChatLib.chat("&bBlocked Pickobulus Ability!");
                            return;
                        }
                    }
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }

        }
    }
}
