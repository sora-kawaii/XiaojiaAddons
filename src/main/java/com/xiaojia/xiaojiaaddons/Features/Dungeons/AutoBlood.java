package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class AutoBlood {

    public static final String[] bloodMobs = new String[]{"Revoker", "Psycho", "Reaper", "Cannibal", "Mute", "Ooze", "Putrid", "Freak", "Leech", "Tear", "Parasite", "Flamer", "Skull", "Mr. Dead", "Vader", "Frost", "Walker", "WanderingSoul", "Bonzo", "Scarf", "Livid"};
    private static final KeyBind keyBind = new KeyBind("Auto Blood", 0);
    private static boolean enabled = false;
    private final ArrayList killed = new ArrayList();
    private long faceTime = 0L;
    private Entity target;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load var1) {
        this.killed.clear();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) {
            if (keyBind.isPressed()) {
                enabled = !enabled;
                ChatLib.chat(enabled ? "Auto Blood &aactivated" : "Auto Blood &cdeactivated");
            }

            if (enabled) {
                if (Configs.AutoBlood && SkyblockUtils.isInDungeon()) {
                    if (Dungeon.bossEntry <= Dungeon.runStarted) {
                        if (this.target == null || this.killed.contains(this.target) || this.target.getDistanceToEntity(MinecraftUtils.getPlayer()) > 20.0F || this.target.isDead) {
                            this.target = null;
                            this.faceTime = 0L;
                            Iterator var2 = EntityUtils.getEntities().iterator();

                            label89:
                            while (true) {
                                Entity var3;
                                do {
                                    do {
                                        do {
                                            do {
                                                if (!var2.hasNext()) {
                                                    break label89;
                                                }

                                                var3 = (Entity) var2.next();
                                            } while (!(var3 instanceof EntityPlayer));
                                        } while (var3.isDead);
                                    } while (this.killed.contains(var3));
                                } while (var3.getDistanceToEntity(MinecraftUtils.getPlayer()) > 20.0F);

                                String[] var4 = bloodMobs;
                                int var5 = var4.length;

                                for (int var6 = 0; var6 < var5; ++var6) {
                                    String var7 = var4[var6];
                                    if (var3.getName().contains(var7)) {
                                        this.target = var3;
                                    }
                                }
                            }
                        }

                        if (this.target != null) {
                            MinecraftUtils.getPlayer().closeScreen();
                            ControlUtils.face(this.target.posX, this.target.posY - (double) ((float) Configs.AutoBloodYoffset * 0.1F), this.target.posZ);
                            if (this.faceTime == 0L) {
                                this.faceTime = TimeUtils.curTime();
                            }

                            if (TimeUtils.curTime() - this.faceTime > (long) Configs.AutoBloodCD) {
                                this.faceTime = TimeUtils.curTime();
                                ControlUtils.leftClick();
                                this.killed.add(this.target);
                            }
                        }

                    }
                }
            }
        }
    }
}
