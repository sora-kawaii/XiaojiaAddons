package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PacketRelated {

    public static final Deque sendQueue = new ConcurrentLinkedDeque();

    public static final Deque receiveQueue = new ConcurrentLinkedDeque();

    public static int getReceivedQueueLength() {
        long var0 = TimeUtils.curTime();
        synchronized (receiveQueue) {
            while (!receiveQueue.isEmpty() && var0 - (Long) receiveQueue.getFirst() > 1000L) {
                receiveQueue.pollFirst();
            }

            return receiveQueue.size();
        }
    }

    public static int getSentQueueLength() {
        long var0 = TimeUtils.curTime();
        synchronized (sendQueue) {
            while (!sendQueue.isEmpty() && var0 - (Long) sendQueue.getFirst() > 1000L) {
                sendQueue.pollFirst();
            }

            return sendQueue.size();
        }
    }

    @SubscribeEvent
    public void onReceive(PacketReceivedEvent var1) {
        synchronized (receiveQueue) {
            receiveQueue.add(TimeUtils.curTime());
        }
    }

    @SubscribeEvent
    public void onSend(PacketSendEvent var1) {
        synchronized (sendQueue) {
            sendQueue.add(TimeUtils.curTime());
        }
    }
}
