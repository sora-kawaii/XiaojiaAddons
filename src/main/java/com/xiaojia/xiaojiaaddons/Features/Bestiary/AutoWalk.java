package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.vecmath.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class AutoWalk {

   public boolean should = false;

   private final HashMap graph = new HashMap();

   private StringBuilder log = new StringBuilder();

   public boolean shouldShow = false;

   public Image defaultIcon = new Image("defaultPlayerIcon.png");

   private ArrayList positions;

   private final ConcurrentLinkedDeque indexes = new ConcurrentLinkedDeque();

   public int index = 0;

   public BlockPos goingTo = null;

   private boolean tryingEnable = false;

   private String name;

   private long lastForceClose = 0L;

   private Thread runningThread = null;

   private KeyBind keyBind;

   private BlockPos ghostBlockPos = new BlockPos(0, 0, 0);

   public HashSet toBeKilled = new HashSet();

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (!this.enabled() && this.should) {
            this.stop();
         }

         if (this.keyBind.isPressed()) {
            if (!this.should && this.tryingEnable) {
               this.lastForceClose = TimeUtils.curTime();
               ChatLib.chat("Stopped from re-enabling.");
               return;
            }

            this.should = !this.should;
            if (this.should) {
               if (!this.enabled()) {
                  this.stop();
               }

               ChatLib.chat(this.name + " &aactivated");
            } else {
               this.goingTo = null;
               this.stop();
               ChatLib.chat(this.name + " &cdeactivated");
            }
         }

         if (this.should) {
            if (this.runningThread == null || !this.runningThread.isAlive()) {
               this.runningThread = new Thread(() -> {
                  try {
                     ChatLib.chat("Starting a new Thread...");
                     if (this.goingTo == null) {
                        this.shouldShow = true;
                        this.goingTo = (BlockPos)this.positions.get(this.index);
                        ChatLib.chat("Start moving automatically.");
                        ControlUtils.stopMoving();
                        this.shouldShow = false;
                        Thread.sleep(1000L);
                        ControlUtils.face((BlockPos)this.positions.get(this.index));
                     }

                     while(this.should) {
                        this.goingTo = (BlockPos)this.positions.get(this.index);
                        ControlUtils.holdForward();
                        Thread var1_ = null;
                        BlockPos var2 = MinecraftUtils.getPlayer().getPosition();

                        for(long var3 = TimeUtils.curTime(); lIlIIlIllIIl(MathUtils.distanceSquareFromPlayer((double)this.goingTo.getX(), (double)(MathUtils.getY(MinecraftUtils.getPlayer()) + 1.5F), (double)this.goingTo.getZ()), this.getJudgeDistance() * this.getJudgeDistance()) > 0 && this.should; Thread.sleep(20L)) {
                           BlockPos var5 = MinecraftUtils.getPlayer().getPosition();
                           if (var5.getX() != var2.getX() || var2.getZ() != var5.getZ()) {
                              var2 = var5;
                              var3 = TimeUtils.curTime();
                           }

                           if (TimeUtils.curTime() - var3 > 4000L) {
                              ControlUtils.jump();
                           }

                           if (TimeUtils.curTime() - var3 > 6000L) {
                              Integer var6 = (Integer)this.indexes.pollLast();
                              if (var6 == null) {
                                 this.stop();
                                 MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
                                 return;
                              }

                              this.index = var6;
                              this.goingTo = (BlockPos)this.positions.get(this.index);
                              ChatLib.chat("Backwards 1 step!");
                              var3 = TimeUtils.curTime();
                           }

                           if (var1_ == null || !var1_.isAlive()) {
                              var1_ = new Thread(() -> {
                                 try {
                                    ControlUtils.faceSlowly((float)this.goingTo.getX(), MathUtils.getY(MinecraftUtils.getPlayer()) + MinecraftUtils.getPlayer().getEyeHeight(), (float)this.goingTo.getZ());
                                 } catch (Exception e) {
                                    this.stop();
                                    MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
                                    BlockPos var2_ = MinecraftUtils.getPlayer().getPosition();
                                    if (var2_.distanceSq(this.ghostBlockPos) < 4.0) {
                                       try {
                                          ControlUtils.moveBackward(200L);
                                       } catch (InterruptedException var4) {
                                          var4.printStackTrace();
                                       }
                                    }

                                    this.ghostBlockPos = var2_;
                                 }

                              });
                              var1_.start();
                           }

                           while((PacketRelated.getReceivedQueueLength() == 0 || this.existCreeperBeside()) && this.should) {
                              ControlUtils.stopMoving();
                              Thread.sleep(20L);
                           }

                           if (!this.should) {
                              return;
                           }

                           ControlUtils.holdForward();
                           if ((float)this.goingTo.getY() > MathUtils.getY(MinecraftUtils.getPlayer())) {
                              ControlUtils.jump();
                           } else {
                              ControlUtils.releaseJump();
                           }
                        }

                        if (this.should) {
                           this.indexes.offerLast(this.index);
                           if (this.indexes.size() > 100) {
                              this.indexes.pollFirst();
                           }

                           this.index = this.getNext(this.index);
                        }
                     }
                  } catch (Exception var7) {
                     var7.printStackTrace();
                  }

               });
               this.runningThread.start();
            }
         }
      }
   }

   private HashSet getCreepersAlong(int var1, int var2, HashSet var3, HashSet var4, List var5) {
      BlockPos var6 = (BlockPos)this.positions.get(var1);
      BlockPos var7 = (BlockPos)this.positions.get(var2);
      HashSet var8 = new HashSet();
      int var9 = Configs.SneakySplit;

      for(int var10 = 0; var10 <= var9; ++var10) {
         Vector3d var11 = new Vector3d((double)var6.getX() + 0.5, (double)var6.getY() + 0.5, (double)var6.getZ() + 0.5);
         Vector3d var12 = new Vector3d((double)(var7.getX() - var6.getX()), (double)(var7.getY() - var6.getY()), (double)(var7.getZ() - var6.getZ()));
         var12.scale((double)var10 * 1.0 / (double)var9);
         var11.add(var12);
         Iterator var13 = var5.iterator();

         while(var13.hasNext()) {
            EntityCreeper var14 = (EntityCreeper)var13.next();
            if (var14.getDistance(var11.x, var11.y, var11.z) < (double)((float)Configs.SneakySearchRadius / 10.0F)) {
               var8.add(var14.getEntityId());
            }
         }
      }

      return var8;
   }

   public abstract KeyBind getKeyBind();

   private static int lIlIIlIllIIl(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   private void stop() {
      ControlUtils.stopMoving();
      if (this.should) {
         this.should = false;
         ChatLib.chat(this.name + " &cdeactivated");
         if (!this.enabled()) {
            return;
         }

         (new Thread(() -> {
            try {
               this.tryingEnable = true;
               ChatLib.chat("Waiting 2s for re-enable...");
               Thread.sleep(2000L);
               if (TimeUtils.curTime() - this.lastForceClose > 2022L) {
                  ChatLib.chat("Re-enabling...");
                  this.should = true;
               } else {
                  this.should = false;
               }

               this.tryingEnable = false;
            } catch (Exception var2) {
               var2.printStackTrace();
            }

         })).start();
      }

   }

   public abstract double getJudgeDistance();

   private HashSet dfs(int var1, double var2, HashSet var4, List var5) {
      if (var2 < 0.0) {
         return new HashSet();
      } else {
         HashSet var6 = new HashSet(var4);
         Iterator var7 = ((ArrayList)this.graph.get(var1)).iterator();

         while(var7.hasNext()) {
            int var8 = (Integer)var7.next();
            double var9 = this.distanceBetween(var1, var8);
            HashSet var11 = this.dfs(var8, var2 - var9, var4, var5);
            HashSet var12 = this.getCreepersAlong(var1, var8, var4, var11, var5);
            if (var6.size() < var11.size() + var12.size()) {
               var11.addAll(var12);
               var6 = var11;
            }
         }

         return var6;
      }
   }

   private int getNext(int var1) {
      int var2 = -1;
      List var3 = this.getCreepers();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      double var6 = (double)Configs.SNMaxLen;
      this.log.append("Getting next: " + var1 + "\n");

      int var9;
      HashSet var10;
      HashSet var11;
      for(Iterator var8 = ((ArrayList)this.graph.get(var1)).iterator(); var8.hasNext(); this.log.append("   next: " + var9 + ", along: " + (var10.size() - var11.size()) + ", search: " + var11.size() + ", size " + var4.size() + "\n")) {
         var9 = (Integer)var8.next();
         var10 = this.getCreepersAlong(var1, var9, new HashSet(), new HashSet(), var3);
         var11 = this.dfs(var9, var6 - this.distanceBetween(var1, var9), var10, var3);
         var10.addAll(var11);
         if (var10.size() > var2) {
            var2 = var10.size();
            var4 = new ArrayList();
            var5 = new ArrayList();
            var4.add(var9);
            var5.add(var10);
         } else if (var10.size() == var2) {
            var4.add(var9);
            var5.add(var10);
         }
      }

      int var12 = (int)((double)var4.size() * Math.random());
      this.toBeKilled = (HashSet)var5.get(var12);
      return (Integer)var4.get(var12);
   }

   public void printLog() {
      System.err.println(this.name + " Log:");
      System.err.println(this.log);
      System.err.println();
   }

   public abstract String getName();

   public List getCreepers() {
      List var1 = EntityUtils.getEntities();
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Entity var4 = (Entity)var3.next();
         if (var4 instanceof EntityCreeper) {
            EntityCreeper var5 = (EntityCreeper)var4;
            if (lllIlIIllIIl(var5.getHealth(), 1.0F) >= 0) {
               var2.add(var5);
            }
         }
      }

      return var2;
   }

   public abstract ArrayList getEdges();

   public int getSize() {
      return this.indexes.size();
   }

   private static int lllIlIIllIIl(float var0, float var1) {
      float var2;
      return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
   }

   private boolean existCreeperBeside() {
      return this.getCreepers().stream().anyMatch((var0) -> {
         return MathUtils.distanceSquareFromPlayer((Entity)var0) < 25.0;
      });
   }

   public abstract boolean enabled();

   public abstract ArrayList getPositions();

   private double distanceBetween(int var1, int var2) {
      return Math.sqrt(((BlockPos)this.positions.get(var1)).distanceSq((Vec3i)this.positions.get(var2)));
   }

   public void init() {
      this.positions = this.getPositions();

      for(int var1 = 0; var1 < this.positions.size(); ++var1) {
         this.graph.put(var1, new ArrayList());
      }

      Iterator var3 = this.getEdges().iterator();

      while(var3.hasNext()) {
         Pair var2 = (Pair)var3.next();
         ((ArrayList)this.graph.get(var2.getKey())).add(var2.getValue());
      }

      this.name = this.getName();
      this.keyBind = this.getKeyBind();
   }

   @SubscribeEvent
   public void onLoad(WorldEvent.Load var1) {
      this.log = new StringBuilder();
      this.toBeKilled.clear();

      for(int var2 = 0; var2 < this.positions.size(); ++var2) {
         this.log.append("Graph log - " + var2 + " " + ((ArrayList)this.graph.get(var2)).size() + "\n");
      }

   }
}
