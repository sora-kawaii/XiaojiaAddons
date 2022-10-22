package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.PacketUtils;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoBack {
   @SubscribeEvent
   public void onTitlePacketReceived(PacketReceivedEvent var1) {
      if (Checker.enabled) {
         if (Configs.AutoBack) {
            if (var1.packet instanceof S45PacketTitle) {
               S45PacketTitle var2 = (S45PacketTitle)var1.packet;
               IChatComponent var3 = PacketUtils.getMessage(var2);
               if (var3 == null) {
                  return;
               }

               String var4 = ChatLib.removeFormatting(var3.getUnformattedText());
               if (var4.contains("你失败了")) {
                  CommandsUtils.addCommand("/back");
               }
            }

         }
      }
   }
}
