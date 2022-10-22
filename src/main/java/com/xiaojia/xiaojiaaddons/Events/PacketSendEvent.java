package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketSendEvent extends Event {

   public final Packet packet;

   public PacketSendEvent(Packet var1) {
      this.packet = var1;
   }
}
