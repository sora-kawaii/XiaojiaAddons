package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import javax.vecmath.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoBuildFarmVertical {

   private static boolean picked = false;

   private static BlockPos currentBlockPos = null;

   private static final KeyBind keyBind = new KeyBind("Auto Build Farm", 0);

   private static int from;

   private static BlockPos startPos = null;

   private static int step = 1;

   private static int to;

   private static final ArrayList blocksOne = new ArrayList();

   private static final ArrayList blocksTwo = new ArrayList();

   private static boolean autoBuildThreadLock = false;

   private static final ArrayList toRemoveBlocks = new ArrayList();

   private static BlockUtils.Face currentFacing = null;

   private static boolean isBuilding = false;

   private static final ArrayList notTilled = new ArrayList();

   public static void setStep(int var0) {
      step = var0;
      notTilled.clear();
      ChatLib.chat("Successfully set step to " + var0 + ".");
   }

   private static void stop() {
      stop(true);
   }

   @SubscribeEvent
   public void onTickStep5(TickEndEvent var1) {
      if (Checker.enabled) {
         if (step == 5) {
            if (Configs.AutoBuildFarm5) {
               if (isBuilding) {
                  if (!autoBuildThreadLock) {
                     if (MinecraftUtils.getPlayer() != null) {
                        autoBuildThreadLock = true;
                        (new Thread(() -> {
                           try {
                              ControlUtils.changeDirection(MathUtils.getYaw() > 0.0F ? 130.0F : -130.0F, -25.0F);
                              ControlUtils.stopMoving();
                              ControlUtils.holdForward();

                              while(enabled()) {
                                 float var0 = MathUtils.getY(MinecraftUtils.getPlayer());

                                 for(long var1_ = 0L; MathUtils.getY(MinecraftUtils.getPlayer()) >= var0 && enabled(); Thread.sleep(20L)) {
                                    ControlUtils.holdForward();
                                    if (TimeUtils.curTime() - var1_ > (long)Configs.AutoBuildFarm5CD) {
                                       var1_ = TimeUtils.curTime();
                                       fillLightingBlock();
                                       ControlUtils.rightClick();
                                    }
                                 }

                                 if (!enabled()) {
                                    break;
                                 }

                                 ControlUtils.releaseForward();
                                 ControlUtils.faceSlowly((double)(-MathUtils.getYaw()), -25.0);

                                 while(MathUtils.getY(MinecraftUtils.getPlayer()) != var0 - 3.0F && IIlIIlIlIll(MathUtils.getY(MinecraftUtils.getPlayer()), var0 + 3.0F) < 0 && enabled()) {
                                    Thread.sleep(20L);
                                 }

                                 if (MathUtils.getY(MinecraftUtils.getPlayer()) > var0 + 3.0F) {
                                    break;
                                 }
                              }

                              if (enabled()) {
                                 stop(false);
                                 step = 3;
                              }
                           } catch (Exception var6) {
                              var6.printStackTrace();
                              stop();
                           } finally {
                              ControlUtils.releaseForward();
                              autoBuildThreadLock = false;
                           }

                        })).start();
                     }
                  }
               }
            }
         }
      }
   }

   private static int IIlIIlIlIll(float var0, float var1) {
      float var2;
      return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
   }

   public static void check(int var0, int var1, int var2, int var3, int var4, int var5) {
      ArrayList var6 = new ArrayList();
      ChatLib.chat(String.format("Checking from (%d, %d, %d) to (%d, %d, %d).", var0, var1, var2, var3, var4, var5));

      for(int var7 = var0; var7 <= var3; ++var7) {
         for(int var8 = var1; var8 <= var4; ++var8) {
            for(int var9 = var2; var9 <= var5; ++var9) {
               if (BlockUtils.isBlockAir((float)var7, (float)(var8 + 1), (float)var9) && BlockUtils.getBlockAt(var7, var8, var9) == Blocks.dirt) {
                  var6.add(new BlockPos(var7, var8, var9));
                  ChatLib.chat(String.format("Detected dirt: (%d %d %d)", var7, var8, var9));
               }
            }
         }
      }

      synchronized(notTilled) {
         notTilled.addAll(var6);
      }

      ChatLib.chat("Finished checking.");
   }

   private static int lIIlIlIlIll(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   private static int IllIllIlIll(float var0, float var1) {
      float var2;
      return (var2 = var0 - var1) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
   }

   @SubscribeEvent
   public void onRenderWorld(RenderWorldLastEvent var1) {
      if (Checker.enabled) {
         if (Configs.AutoBuildFarm1) {
            ArrayList var2 = new ArrayList();
            var2.addAll(blocksOne);
            var2.addAll(blocksTwo);
            var2.addAll(toRemoveBlocks);
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               BlockPos var4 = (BlockPos)var3.next();
               if (BlockUtils.isBlockAir((float)var4.getX(), (float)var4.getY(), (float)var4.getZ())) {
                  GuiUtils.drawBoxAtBlock(var4, new Color(72, 50, 34, 80), 1, 1, 0.0F);
               }
            }

            GuiUtils.enableESP();
            if (step == 1) {
               if (currentBlockPos != null) {
                  GuiUtils.drawBoxAtBlock(currentBlockPos, new Color(255, 0, 0, 80), 1, 1, 0.002F);
               }

               if (currentFacing != null) {
                  GuiUtils.drawFilledFace(currentFacing, new Color(224, 104, 51, 200));
               }
            }

            synchronized(notTilled) {
               Iterator var8 = notTilled.iterator();

               while(true) {
                  if (!var8.hasNext()) {
                     break;
                  }

                  BlockPos var5 = (BlockPos)var8.next();
                  GuiUtils.drawBoxAtBlock(var5, new Color(238, 85, 140, 200), 1, 1, 0.0F);
               }
            }

            GuiUtils.disableESP();
         }
      }
   }

   private static BlockUtils.Face getFaceFromCenter(Vector3d var0) {
      double var1 = 1.0E-4;
      if (Math.abs(var0.x - (double)Math.round(var0.x)) < var1) {
         return new BlockUtils.Face(var0.x, var0.y - 0.5, var0.z - 0.5, var0.x, var0.y + 0.5, var0.z + 0.5);
      } else if (Math.abs(var0.y - (double)Math.round(var0.y)) < var1) {
         return new BlockUtils.Face(var0.x - 0.5, var0.y, var0.z - 0.5, var0.x + 0.5, var0.y, var0.z + 0.5);
      } else {
         return Math.abs(var0.z - (double)Math.round(var0.z)) < var1 ? new BlockUtils.Face(var0.x - 0.5, var0.y - 0.5, var0.z, var0.x + 0.5, var0.y + 0.5, var0.z) : null;
      }
   }

   @SubscribeEvent
   public void onTickStep2(TickEndEvent var1) {
      if (Checker.enabled) {
         if (step == 2) {
            if (Configs.AutoBuildFarm2) {
               if (isBuilding) {
                  if (!autoBuildThreadLock) {
                     if (MinecraftUtils.getPlayer() != null) {
                        autoBuildThreadLock = true;
                        (new Thread(() -> {
                           try {
                              ControlUtils.changeDirection(MathUtils.getYaw() > 0.0F ? 120.0F : -120.0F, -10.0F);
                              ControlUtils.stopMoving();
                              ControlUtils.holdLeftClick();
                              ControlUtils.holdForward();

                              while(enabled()) {
                                 float var0 = MathUtils.getY(MinecraftUtils.getPlayer());

                                 while(MathUtils.getY(MinecraftUtils.getPlayer()) >= var0 && enabled()) {
                                    ControlUtils.holdLeftClick();
                                    ControlUtils.holdForward();
                                    Thread.sleep(20L);
                                 }

                                 if (!enabled()) {
                                    break;
                                 }

                                 ControlUtils.releaseLeftClick();
                                 ControlUtils.releaseForward();
                                 ControlUtils.faceSlowly((double)(-MathUtils.getYaw()), -10.0);

                                 while(MathUtils.getY(MinecraftUtils.getPlayer()) != var0 - 3.0F && IllIllIlIll(MathUtils.getY(MinecraftUtils.getPlayer()), var0 + 3.0F) < 0 && enabled()) {
                                    Thread.sleep(20L);
                                 }

                                 if (MathUtils.getY(MinecraftUtils.getPlayer()) > var0 + 3.0F) {
                                    break;
                                 }
                              }

                              if (enabled()) {
                                 stop(false);
                                 step = 3;
                              }
                           } catch (Exception var4) {
                              var4.printStackTrace();
                              stop();
                           } finally {
                              autoBuildThreadLock = false;
                           }

                        })).start();
                     }
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onTick(TickEndEvent event) {
      if (Checker.enabled) {
         if (keyBind.isPressed()) {
            isBuilding = !isBuilding;
            ChatLib.chat("Auto Build Farm - " + step + (isBuilding ? " &aactivated" : " &cdeactivated"));
         }

         if (step == 1) {
            if (Configs.AutoBuildFarm1) {
               if (isBuilding) {
                  if (!autoBuildThreadLock) {
                     if (MinecraftUtils.getPlayer() != null) {
                        autoBuildThreadLock = true;
                        (new Thread(() -> {
                           try {
                              ArrayList var0 = new ArrayList(blocksOne);
                              var0.addAll(blocksTwo);
                              Iterator var1 = var0.iterator();

                              label167:
                              while(var1.hasNext()) {
                                 BlockPos var2 = (BlockPos)var1.next();
                                 currentBlockPos = var2;

                                 while(BlockUtils.isBlockAir(var2) && enabled()) {
                                    ArrayList var3 = new ArrayList(Arrays.asList(var2.up(), var2.down(), var2.east(), var2.west(), var2.north(), var2.south()));
                                    boolean var4 = false;
                                    Vector3d var5 = null;
                                    Iterator var6 = var3.iterator();

                                    while(var6.hasNext()) {
                                       BlockPos var7 = (BlockPos)var6.next();
                                       if (!BlockUtils.isBlockAir(var7)) {
                                          var4 = true;
                                          var5 = new Vector3d(((double)var7.getX() * 1.000001 + (double)var2.getX() * 0.999999) / 2.0 + 0.5, ((double)var7.getY() * 1.000001 + (double)var2.getY() * 0.999999) / 2.0 + 0.5, ((double)var7.getZ() * 1.000001 + (double)var2.getZ() * 0.999999) / 2.0 + 0.5);
                                          break;
                                       }
                                    }

                                    if (!var4) {
                                       ChatLib.chat("Cannot find supporting block. Make sure you're near the red block and all horizontal blocks have been placed.");
                                       stop();
                                       break label167;
                                    }

                                    currentFacing = getFaceFromCenter(var5);
                                    int var18 = HotbarUtils.dirtWandSlot;
                                    if (var18 == -1) {
                                       ChatLib.chat("InfiniDirt Wand needed!");
                                       stop();
                                       break label167;
                                    }

                                    Vector3d var19 = BlockUtils.getNearestBlock(MinecraftUtils.getPlayer().getPositionEyes(MathUtils.partialTicks), currentFacing.mid, false);
                                    if (var19 == null) {
                                       ChatLib.chat("? currentFacing " + currentFacing + ", nearest null");
                                       break label167;
                                    }

                                    if (lIIlllIlIll(MathUtils.distanceSquareFromPlayer(var19), 484.0) > 0) {
                                       ChatLib.chat("Get Closer!");
                                       Thread.sleep(1000L);
                                    } else if (lIIlllIlIll(MathUtils.distanceSquaredFromPoints(var19, var5), 0.25) > 0) {
                                       ChatLib.chat("Face the orange face!");
                                       Thread.sleep(1000L);
                                    } else {
                                       ControlUtils.face(var5.x, var5.y, var5.z);
                                       float var8 = MathUtils.getYaw();
                                       float var9 = MathUtils.getPitch();
                                       float var10 = MathUtils.getX(MinecraftUtils.getPlayer());
                                       float var11 = MathUtils.getY(MinecraftUtils.getPlayer());
                                       float var12 = MathUtils.getZ(MinecraftUtils.getPlayer());
                                       ControlUtils.stopMoving();
                                       Thread.sleep(250L);
                                       if (!ControlUtils.differentDirection(var8, var9) && !MathUtils.differentPosition(var10, var11, var12)) {
                                          ControlUtils.setHeldItemIndex(var18);
                                          ControlUtils.rightClick();
                                          Thread.sleep((long)Configs.AutoBuildFarm1CD);
                                          if (!BlockUtils.isBlockAir(var2)) {
                                             break;
                                          }
                                       }
                                    }
                                 }
                              }

                              ChatLib.chat("Farm model built. Expand them with builder's wand, set side borders before pressing keybind again.");
                              stop(false);
                              step = 2;
                           } catch (Exception var16) {
                              var16.printStackTrace();
                              stop();
                           } finally {
                              autoBuildThreadLock = false;
                           }

                        })).start();
                     }
                  }
               }
            }
         }
      }
   }

   private static void stop(boolean var0) {
      if (isBuilding) {
         if (MinecraftUtils.getPlayer() != null && var0) {
            MinecraftUtils.getPlayer().playSound("random.successful_hit", 1000.0F, 1.0F);
         }

         isBuilding = false;
         ChatLib.chat("Auto Build Farm - " + step + " &cdeactivated");
      }
   }

   private static void fillLightingBlock() throws InterruptedException {
      ItemStack var0 = ControlUtils.getHeldItemStack();
      int var1 = ControlUtils.getHeldItemIndex();
      if (!isLightBlock(var0)) {
         Inventory var2 = ControlUtils.getOpenedInventory();
         ControlUtils.stopMoving();

         for(int var3 = 0; var3 < 45; ++var3) {
            if (isLightBlock(var2.getItemInSlot(var3))) {
               XiaojiaAddons.mc.playerController.windowClick(var2.getWindowId(), var3, var1, 2, MinecraftUtils.getPlayer());
               Thread.sleep(100L);
               ChatLib.chat("Got light block from " + var3 + ".");
               return;
            }
         }

         ChatLib.chat("Can't find any light block in inventory.");
         stop();
      }
   }

   private static int refillMaterial(ItemStack var0) throws InterruptedException {
      ChatLib.say("/pickupstash");
      picked = false;

      while(!picked && enabled()) {
         Thread.sleep(20L);
      }

      if (!picked) {
         return -1;
      } else {
         picked = false;
         ControlUtils.leftClick();

         Inventory var1;
         for(var1 = ControlUtils.getOpenedInventory(); enabled() && !var1.getName().contains("Basket of Seeds"); var1 = ControlUtils.getOpenedInventory()) {
            Thread.sleep(20L);
         }

         int var2;
         for(var2 = 45; var2 < 90; ++var2) {
            ItemStack var3 = var1.getItemInSlot(var2);
            if (var3 != null && var3.getItem() == var0.getItem()) {
               var1.click(var2, true, "LEFT");
               Thread.sleep(50L);
            }
         }

         var2 = 0;

         for(int var5 = 0; var5 < 45; ++var5) {
            ItemStack var4 = var1.getItemInSlot(var5);
            if (var4 != null && var4.getItem() == var0.getItem()) {
               var2 += var4.stackSize;
            }
         }

         return var2;
      }
   }

   @SubscribeEvent
   public void onReceive(ClientChatReceivedEvent var1) {
      if (Checker.enabled) {
         if (var1.type == 0) {
            if (ChatLib.removeFormatting(var1.message.getUnformattedText()).matches("You picked up \\d+ items from your item stash!|Couldn't unstash your items! Your inventory is full!")) {
               picked = true;
            }

         }
      }
   }

   @SubscribeEvent
   public void onTickStep4(TickEndEvent var1) {
      if (Checker.enabled) {
         if (step == 4) {
            if (Configs.AutoBuildFarm4) {
               if (isBuilding) {
                  if (!autoBuildThreadLock) {
                     if (MinecraftUtils.getPlayer() != null) {
                        autoBuildThreadLock = true;
                        (new Thread(() -> {
                           try {
                              ItemStack var0 = ControlUtils.getHeldItemStack();
                              if (!NBTUtils.getSkyBlockID(var0).equals("BASKET_OF_SEEDS")) {
                                 ChatLib.chat("Hold basket of seeds to continue.");
                                 stop();
                                 return;
                              }

                              ControlUtils.leftClick();

                              Inventory var1_;
                              for(var1_ = ControlUtils.getOpenedInventory(); enabled() && !var1_.getName().contains("Basket of Seeds"); var1_ = ControlUtils.getOpenedInventory()) {
                                 Thread.sleep(20L);
                              }

                              if (!enabled()) {
                                 return;
                              }

                              ItemStack var2 = null;
                              Iterator var3 = var1_.getItemStacks().iterator();

                              while(var3.hasNext()) {
                                 ItemStack var4 = (ItemStack)var3.next();
                                 if (var4 != null) {
                                    var2 = var4;
                                    break;
                                 }
                              }

                              if (var2 != null) {
                                 ChatLib.chat("Got material: " + var2.getUnlocalizedName());
                                 int var16 = 0;

                                 for(int var17 = 0; var17 < 45; ++var17) {
                                    ItemStack var5 = var1_.getItemInSlot(var17);
                                    if (var5 != null && var5.getItem() == var2.getItem()) {
                                       var16 += var5.stackSize;
                                    }
                                 }

                                 MinecraftUtils.getPlayer().closeScreen();
                                 Thread.sleep(1000L);
                                 float var18 = MathUtils.getX(MinecraftUtils.getPlayer());
                                 float var19 = MathUtils.getY(MinecraftUtils.getPlayer());
                                 float var6 = MathUtils.getZ(MinecraftUtils.getPlayer());
                                 BlockPos var7 = new BlockPos((double)var18, (double)var19 - 1.0E-4, (double)var6);
                                 BlockPos var8 = new BlockPos((double)var18, (double)var19 - 1.0E-4, (double)(var6 - 1.0F));
                                 BlockPos var9 = new BlockPos((double)var18, (double)var19 - 1.0E-4, (double)(var6 - 2.0F));
                                 BlockPos var10 = new BlockPos((double)var18, (double)var19 - 1.0E-4, (double)(var6 - 3.0F));
                                 ControlUtils.jump();
                                 ControlUtils.jump();
                                 ControlUtils.faceSlowly(MathUtils.getYaw() > 0.0F ? 90.0 : -90.0, 0.0);
                                 ControlUtils.moveBackward(250L);
                                 ControlUtils.sneak();

                                 while(lIIlIlIlIll((double)MathUtils.getY(MinecraftUtils.getPlayer()) + 1.5, (double)var19 - 0.5) > 0) {
                                    Thread.sleep(20L);
                                 }

                                 ControlUtils.unSneak();
                                 ControlUtils.moveRight(180L);
                                 ChatLib.chat("Found starting position.");
                                 ChatLib.chat("Set material per row to: " + Configs.IslandSize);

                                 while(var7.getY() > from && enabled()) {
                                    while(var16 < 4 * Configs.IslandSize && enabled()) {
                                       ChatLib.chat("Trying to fill up materials.");
                                       var16 = refillMaterial(var2);
                                    }

                                    var16 -= 4 * Configs.IslandSize;
                                    if (!enabled()) {
                                       break;
                                    }

                                    ChatLib.chat("Material Left after this layer: " + var16);
                                    ControlUtils.faceSlowly((double)var7.getX() + 0.5, (double)var7.getY() + 0.5, (double)var7.getZ() + 0.5);
                                    if (!enabled()) {
                                       break;
                                    }

                                    ControlUtils.rightClick();
                                    Thread.sleep(200L);
                                    ControlUtils.faceSlowly((double)var8.getX() + 0.5, (double)var8.getY() + 0.5, (double)var8.getZ() + 0.5);
                                    if (!enabled()) {
                                       break;
                                    }

                                    ControlUtils.rightClick();
                                    Thread.sleep(200L);
                                    ControlUtils.faceSlowly((double)var9.getX() + 0.5, (double)var9.getY() + 0.5, (double)var9.getZ() + 0.5);
                                    if (!enabled()) {
                                       break;
                                    }

                                    ControlUtils.rightClick();
                                    Thread.sleep(200L);
                                    ControlUtils.faceSlowly((double)var10.getX() + 0.5, (double)var10.getY() + 0.5, (double)var10.getZ() + 0.5);
                                    if (!enabled()) {
                                       break;
                                    }

                                    ControlUtils.rightClick();
                                    Thread.sleep(200L);
                                    var19 -= 3.0F;
                                    ControlUtils.sneak();

                                    while(lIIlIlIlIll((double)MathUtils.getY(MinecraftUtils.getPlayer()) + 1.5, (double)var19) > 0 && enabled()) {
                                       Thread.sleep(20L);
                                    }

                                    ControlUtils.unSneak();
                                    var7 = var7.down(3);
                                    var8 = var8.down(3);
                                    var9 = var9.down(3);
                                    var10 = var10.down(3);
                                 }

                                 if (enabled()) {
                                    stop(false);
                                    step = 5;
                                 }

                                 return;
                              }

                              ChatLib.chat("Put material in basket.");
                              stop();
                           } catch (Exception var14) {
                              var14.printStackTrace();
                              stop();
                              return;
                           } finally {
                              autoBuildThreadLock = false;
                           }

                        })).start();
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean enabled() {
      return isBuilding && Checker.enabled;
   }

   private static boolean isLightBlock(ItemStack var0) {
      return var0 != null && (Block.getBlockFromItem(var0.getItem()) == Blocks.glowstone || Block.getBlockFromItem(var0.getItem()) == Blocks.sea_lantern);
   }

   @SubscribeEvent
   public void onTickStep3(TickEndEvent var1) {
      if (Checker.enabled) {
         if (step == 3) {
            if (Configs.AutoBuildFarm3) {
               if (isBuilding) {
                  if (!autoBuildThreadLock) {
                     if (MinecraftUtils.getPlayer() != null) {
                        autoBuildThreadLock = true;
                        (new Thread(() -> {
                           try {
                              ControlUtils.changeDirection(MathUtils.getYaw() > 0.0F ? 90.0F : -90.0F, 45.0F);
                              ControlUtils.stopMoving();
                              ControlUtils.holdRightClick();
                              ControlUtils.holdForward();
                              float var0 = MathUtils.getY(MinecraftUtils.getPlayer());

                              while(MathUtils.getY(MinecraftUtils.getPlayer()) == var0 && enabled()) {
                                 ControlUtils.stopMoving();
                                 ControlUtils.holdRightClick();
                                 ControlUtils.holdForward();
                                 Thread.sleep(20L);
                              }

                              ControlUtils.stopMoving();
                              ControlUtils.releaseRightClick();
                              if (enabled()) {
                                 (new Thread(() -> {
                                    try {
                                       ControlUtils.faceSlowly(MathUtils.getYaw() > 0.0F ? -90.0 : 90.0, 45.0);
                                    } catch (InterruptedException e) {
                                       e.printStackTrace();
                                    }

                                 })).start();
                                 ControlUtils.jump();
                                 ChatLib.chat("Jump!");
                                 ControlUtils.jump();
                                 ChatLib.chat("Jump!");
                                 stop(false);
                              }
                           } catch (Exception var4) {
                              var4.printStackTrace();
                              stop();
                           } finally {
                              autoBuildThreadLock = false;
                           }

                        })).start();
                     }
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onBlockChange(BlockChangeEvent var1) {
      if (SkyblockUtils.getCurrentMap().equals("Your Island")) {
         if (step == 3) {
            if (var1.oldBlock.getBlock() == Blocks.dirt && var1.newBlock.getBlock() == Blocks.farmland) {
               synchronized(notTilled) {
                  notTilled.remove(var1.position);
               }
            }

            if (var1.newBlock.getBlock() == Blocks.dirt && var1.oldBlock.getBlock() == Blocks.farmland) {
               synchronized(notTilled) {
                  notTilled.add(var1.position);
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void onWorldLoad(WorldEvent.Load var1) {
      blocksOne.clear();
      blocksTwo.clear();
      toRemoveBlocks.clear();
      synchronized(notTilled) {
         notTilled.clear();
      }

      currentFacing = null;
      currentBlockPos = null;
      startPos = null;
   }

   private static int lIIlllIlIll(double var0, double var2) {
      double var4;
      return (var4 = var0 - var2) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1);
   }

   public static void setFarmingPoint(int var0, int var1) {
      float var2 = MathUtils.getX(MinecraftUtils.getPlayer());
      float var3 = MathUtils.getY(MinecraftUtils.getPlayer()) - 0.01F;
      float var4 = MathUtils.getZ(MinecraftUtils.getPlayer());
      step = 1;
      blocksOne.clear();
      blocksTwo.clear();
      toRemoveBlocks.clear();
      from = var0;
      to = var1;
      startPos = new BlockPos((double)var2, (double)var3, (double)var4);
      int var5 = startPos.getX();
      int var6 = startPos.getY();
      int var7 = startPos.getZ();
      int var8 = from;

      while(var8 < to) {
         int var9 = (var6 + 333333 - var8) % 3;
         if (var9 == 1) {
            blocksOne.add(new BlockPos(var5, var8, var7 + 1));
            toRemoveBlocks.add(new BlockPos(var5, var8, var7));
            blocksTwo.add(new BlockPos(var5, var8 + 1, var7 - 3));
            blocksTwo.add(new BlockPos(var5, var8 + 1, var7 - 2));
            blocksTwo.add(new BlockPos(var5, var8 + 1, var7 - 1));
            blocksTwo.add(new BlockPos(var5, var8 + 1, var7));
            toRemoveBlocks.add(new BlockPos(var5, var8 + 2, var7));
            var8 += 3;
         } else {
            ++var8;
         }
      }

      Collections.reverse(blocksTwo);
      ChatLib.chat("Successfully set farming point!");
      ChatLib.chat("Build the horizontal blocks yourself and make sure no connected blocks within range. After that, press keybind.");
   }
}
