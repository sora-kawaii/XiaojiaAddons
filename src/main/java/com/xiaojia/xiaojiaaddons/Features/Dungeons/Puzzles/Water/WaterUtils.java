package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.vecmath.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class WaterUtils {

   private static final ArrayList yToFlag = new ArrayList() {
      {
         this.add(new Pair(1, 0));
         this.add(new Pair(5, 1));
         this.add(new Pair(10, 2));
         this.add(new Pair(15, 3));
         this.add(new Pair(19, 4));
      }
   };

   public static Vector3d cv;

   public static Vector3d ev;

   private static final ArrayList da = new ArrayList();

   private static final HashMap stateMap = new HashMap() {
      {
         this.put(EnumOperation.g, WaterUtils.ga);
         this.put(EnumOperation.c, WaterUtils.ca);
         this.put(EnumOperation.e, WaterUtils.ea);
         this.put(EnumOperation.d, WaterUtils.da);
         this.put(EnumOperation.cl, WaterUtils.cla);
         this.put(EnumOperation.q, WaterUtils.qa);
      }
   };

   public static final int width = 21;

   private static final ArrayList ga = new ArrayList();

   public static Vector3d dv;

   public static boolean raw = true;

   public static Vector3d gv;

   public static final int gap = 15;

   public static String boardString = "";

   private static final ArrayList ca = new ArrayList();

   private static final ArrayList cla = new ArrayList();

   public static TreeMap operations = new TreeMap();

   public static Vector3d trigV;

   private static final ArrayList ea = new ArrayList();

   public static int bestTime;

   private static final ArrayList qa = new ArrayList();

   public static Vector3d clv;

   public static ArrayList points;

   public static Vector3d qv;

   public static final int height = 24;

   public static int compare(EnumState var0, EnumState var1) {
      int var2 = getWaterValueOf(var0);
      int var3 = getWaterValueOf(var1);
      return var2 - var3;
   }

   private static Pair getTimeAndFlag(EnumState[][] var0) {
      int var1 = 0;
      int var2 = 0;

      for(Pair var3 = simulate(var0); !Arrays.deepEquals(var0, (Object[])var3.getKey()); var3 = simulate(var0)) {
         var0 = (EnumState[][])var3.getKey();
         var1 ^= (Integer)var3.getValue();
         ++var2;
      }

      return new Pair(var2, var1);
   }

   public static boolean canExtendRight(EnumState[][] var0, int var1, int var2) {
      return !isBlock(var0[var1][var2 + 1]) && isBlock(var0[var1 - 1][var2]);
   }

   public static EnumState getStateFromBlock(Block var0, Block var1) {
      if (Blocks.air.equals(var0)) {
         if (Blocks.gold_block.equals(var1)) {
            return EnumState.g;
         } else if (Blocks.emerald_block.equals(var1)) {
            return EnumState.e;
         } else if (Blocks.hardened_clay.equals(var1)) {
            return EnumState.cl;
         } else if (Blocks.diamond_block.equals(var1)) {
            return EnumState.d;
         } else if (Blocks.coal_block.equals(var1)) {
            return EnumState.c;
         } else {
            return Blocks.quartz_block.equals(var1) ? EnumState.q : EnumState.E;
         }
      } else if (Blocks.gold_block.equals(var0)) {
         return EnumState.cg;
      } else if (Blocks.emerald_block.equals(var0)) {
         return EnumState.ce;
      } else if (Blocks.hardened_clay.equals(var0)) {
         return EnumState.ccl;
      } else if (Blocks.diamond_block.equals(var0)) {
         return EnumState.cd;
      } else if (Blocks.coal_block.equals(var0)) {
         return EnumState.cc;
      } else {
         return Blocks.quartz_block.equals(var0) ? EnumState.cq : EnumState.B;
      }
   }

   public static Vector3d getPosFor(EnumOperation var0) {
      if (var0 == EnumOperation.c) {
         return cv;
      } else if (var0 == EnumOperation.q) {
         return qv;
      } else if (var0 == EnumOperation.cl) {
         return clv;
      } else if (var0 == EnumOperation.e) {
         return ev;
      } else if (var0 == EnumOperation.d) {
         return dv;
      } else if (var0 == EnumOperation.g) {
         return gv;
      } else if (var0 == EnumOperation.trig) {
         return trigV;
      } else {
         ChatLib.chat(var0 + " ???");
         return null;
      }
   }

   public static int getFlag(Room var0, EnumFacing var1) {
      boolean var2 = false;
      int var3;
      if (var1 != EnumFacing.xn && var1 != EnumFacing.xp) {
         if (var1 == EnumFacing.zn) {
            var3 = getFlag(var0.x + 2, var0.z + 4, var0.x + 2, var0.z);
         } else {
            var3 = getFlag(var0.x + 2, var0.z - 4, var0.x + 2, var0.z);
         }
      } else if (var1 == EnumFacing.xn) {
         var3 = getFlag(var0.x + 4, var0.z + 2, var0.x, var0.z + 2);
      } else {
         var3 = getFlag(var0.x - 4, var0.z + 2, var0.x, var0.z + 2);
      }

      return var3;
   }

   public static EnumState[][] getStatesFromOperation(EnumState[][] var0, EnumOperation var1) {
      EnumState[][] var2 = new EnumState[24][21];

      for(int var3 = 0; var3 < 24; ++var3) {
         System.arraycopy(var0[var3], 0, var2[var3], 0, 21);
      }

      if (var1 == EnumOperation.trig) {
         var2[22][10] = var2[22][10] == EnumState.w ? EnumState.B : EnumState.w;
      } else if (var1 != EnumOperation.empty) {
         ArrayList var9 = (ArrayList)stateMap.get(var1);
         Iterator var4 = var9.iterator();

         while(true) {
            while(var4.hasNext()) {
               Pair var5 = (Pair)var4.next();
               int var6 = (Integer)var5.getKey();
               int var7 = (Integer)var5.getValue();
               EnumState var8 = var2[var6][var7];
               if (var8 != EnumState.cc && var8 != EnumState.ce && var8 != EnumState.cd && var8 != EnumState.ccl && var8 != EnumState.cq && var8 != EnumState.cg && var8 != EnumState.B) {
                  if (var1 == EnumOperation.c) {
                     var2[var6][var7] = EnumState.cc;
                  } else if (var1 == EnumOperation.d) {
                     var2[var6][var7] = EnumState.cd;
                  } else if (var1 == EnumOperation.e) {
                     var2[var6][var7] = EnumState.ce;
                  } else if (var1 == EnumOperation.g) {
                     var2[var6][var7] = EnumState.cg;
                  } else if (var1 == EnumOperation.q) {
                     var2[var6][var7] = EnumState.cq;
                  } else if (var1 == EnumOperation.cl) {
                     var2[var6][var7] = EnumState.ccl;
                  }
               } else {
                  var2[var6][var7] = EnumState.E;
               }
            }

            return var2;
         }
      }

      return var2;
   }

   public static void calculateVectors(Room var0, EnumFacing var1) {
      int var2 = var0.x;
      int var3 = var0.z;
      points = new ArrayList();
      raw = true;
      if (var1 != EnumFacing.xn && var1 != EnumFacing.xp) {
         if (var1 == EnumFacing.zp) {
            trigV = new Vector3d((double)var2 + 0.5, 60.2, (double)(var3 + 10) + 0.5);
            qv = new Vector3d((double)(var2 - 5) - 0.2 + 0.5, 61.5, (double)(var3 - 5) + 0.5);
            gv = new Vector3d((double)(var2 - 5) - 0.2 + 0.5, 61.5, (double)var3 + 0.5);
            cv = new Vector3d((double)(var2 - 5) - 0.2 + 0.5, 61.5, (double)(var3 + 5) + 0.5);
            dv = new Vector3d((double)(var2 + 5) + 0.2 + 0.5, 61.5, (double)(var3 - 5) + 0.5);
            ev = new Vector3d((double)(var2 + 5) + 0.2 + 0.5, 61.5, (double)var3 + 0.5);
            clv = new Vector3d((double)(var2 + 5) + 0.2 + 0.5, 61.5, (double)(var3 + 5) + 0.5);
            points.add(new Vector3d((double)(var2 + 2) + 0.5, 59.0, (double)(var3 + 7) + 0.5));
            points.add(new Vector3d((double)(var2 - 2) + 0.5, 59.0, (double)(var3 + 7) + 0.5));
            points.add(new Vector3d((double)(var2 + 3) + 0.5, 59.0, (double)(var3 - 3) + 0.5));
            points.add(new Vector3d((double)(var2 - 3) + 0.5, 59.0, (double)(var3 - 3) + 0.5));
         } else {
            trigV = new Vector3d((double)var2 + 0.5, 60.2, (double)(var3 - 10) + 0.5);
            qv = new Vector3d((double)(var2 + 5) + 0.2 + 0.5, 61.5, (double)(var3 + 5) + 0.5);
            gv = new Vector3d((double)(var2 + 5) + 0.2 + 0.5, 61.5, (double)var3 + 0.5);
            cv = new Vector3d((double)(var2 + 5) + 0.2 + 0.5, 61.5, (double)(var3 - 5) + 0.5);
            dv = new Vector3d((double)(var2 - 5) - 0.2 + 0.5, 61.5, (double)(var3 + 5) + 0.5);
            ev = new Vector3d((double)(var2 - 5) - 0.2 + 0.5, 61.5, (double)var3 + 0.5);
            clv = new Vector3d((double)(var2 - 5) - 0.2 + 0.5, 61.5, (double)(var3 - 5) + 0.5);
            points.add(new Vector3d((double)(var2 + 2) + 0.5, 59.0, (double)(var3 - 7) + 0.5));
            points.add(new Vector3d((double)(var2 - 2) + 0.5, 59.0, (double)(var3 - 7) + 0.5));
            points.add(new Vector3d((double)(var2 + 3) + 0.5, 59.0, (double)(var3 + 3) + 0.5));
            points.add(new Vector3d((double)(var2 - 3) + 0.5, 59.0, (double)(var3 + 3) + 0.5));
         }
      } else if (var1 == EnumFacing.xp) {
         trigV = new Vector3d((double)(var2 + 10) + 0.5, 60.2, (double)var3 + 0.5);
         qv = new Vector3d((double)(var2 - 5) + 0.5, 61.5, (double)(var3 + 5) + 0.2 + 0.5);
         gv = new Vector3d((double)var2 + 0.5, 61.5, (double)(var3 + 5) + 0.2 + 0.5);
         cv = new Vector3d((double)(var2 + 5) + 0.5, 61.5, (double)(var3 + 5) + 0.2 + 0.5);
         dv = new Vector3d((double)(var2 - 5) + 0.5, 61.5, (double)(var3 - 5) - 0.2 + 0.5);
         ev = new Vector3d((double)var2 + 0.5, 61.5, (double)(var3 - 5) - 0.2 + 0.5);
         clv = new Vector3d((double)(var2 + 5) + 0.5, 61.5, (double)(var3 - 5) - 0.2 + 0.5);
         points.add(new Vector3d((double)(var2 + 7) + 0.5, 59.0, (double)(var3 + 2) + 0.5));
         points.add(new Vector3d((double)(var2 + 7) + 0.5, 59.0, (double)(var3 - 2) + 0.5));
         points.add(new Vector3d((double)(var2 - 3) + 0.5, 59.0, (double)(var3 + 3) + 0.5));
         points.add(new Vector3d((double)(var2 - 3) + 0.5, 59.0, (double)(var3 - 3) + 0.5));
      } else {
         trigV = new Vector3d((double)(var2 - 10) + 0.5, 60.2, (double)var3 + 0.5);
         qv = new Vector3d((double)(var2 + 5) + 0.5, 61.5, (double)(var3 - 5) - 0.2 + 0.5);
         gv = new Vector3d((double)var2 + 0.5, 61.5, (double)(var3 - 5) - 0.2 + 0.5);
         cv = new Vector3d((double)(var2 - 5) + 0.5, 61.5, (double)(var3 - 5) - 0.2 + 0.5);
         dv = new Vector3d((double)(var2 + 5) + 0.5, 61.5, (double)(var3 + 5) + 0.2 + 0.5);
         ev = new Vector3d((double)var2 + 0.5, 61.5, (double)(var3 + 5) + 0.2 + 0.5);
         clv = new Vector3d((double)(var2 - 5) + 0.5, 61.5, (double)(var3 + 5) + 0.2 + 0.5);
         points.add(new Vector3d((double)(var2 - 7) + 0.5, 59.0, (double)(var3 - 2) + 0.5));
         points.add(new Vector3d((double)(var2 - 7) + 0.5, 59.0, (double)(var3 + 2) + 0.5));
         points.add(new Vector3d((double)(var2 + 3) + 0.5, 59.0, (double)(var3 - 3) + 0.5));
         points.add(new Vector3d((double)(var2 + 3) + 0.5, 59.0, (double)(var3 + 3) + 0.5));
      }

      if (BlockUtils.getMetaAt(qv) > 5) {
         raw = false;
      }

      if (BlockUtils.getMetaAt(dv) > 5) {
         raw = false;
      }

      if (BlockUtils.getMetaAt(ev) > 5) {
         raw = false;
      }

      if (BlockUtils.getMetaAt(cv) > 5) {
         raw = false;
      }

      if (BlockUtils.getMetaAt(clv) > 5) {
         raw = false;
      }

      if (BlockUtils.getMetaAt(gv) > 5) {
         raw = false;
      }

   }

   public static void print(EnumState[][] var0) {
      for(int var1 = 23; var1 >= 0; --var1) {
         StringBuilder var2 = new StringBuilder();

         for(int var3 = 0; var3 < 21; ++var3) {
            String var4 = var0[var1][var3].toString();
            char var5 = var4.charAt(var4.length() - 1);
            if (var5 == 'E') {
               var5 = ' ';
            }

            var2.append(var5);
         }

         System.out.println(var2);
      }

      System.out.println();
   }

   public static int getFlag(int var0, int var1, int var2, int var3) {
      int var4 = 0;
      int var5 = 0;
      int var6 = var0;

      while(true) {
         int var7 = var1;

         while(true) {
            var5 |= (1 << var4) * (BlockUtils.getBlockAt(var6, 57, var7) == Blocks.piston_head ? 1 : 0);
            ++var4;
            if (var7 == var3) {
               if (var6 == var2) {
                  return var5;
               }

               var6 += var0 < var2 ? 1 : -1;
               break;
            }

            var7 += var1 < var3 ? 1 : -1;
         }
      }
   }

   public static EnumFacing getFacing(Room var0) {
      EnumFacing var1 = null;
      if (BlockUtils.getBlockAt(var0.x, 68, var0.z - 9).equals(Blocks.stone_brick_stairs)) {
         var1 = EnumFacing.zn;
      }

      if (BlockUtils.getBlockAt(var0.x, 68, var0.z + 9).equals(Blocks.stone_brick_stairs)) {
         var1 = EnumFacing.zp;
      }

      if (BlockUtils.getBlockAt(var0.x - 9, 68, var0.z).equals(Blocks.stone_brick_stairs)) {
         var1 = EnumFacing.xn;
      }

      if (BlockUtils.getBlockAt(var0.x + 9, 68, var0.z).equals(Blocks.stone_brick_stairs)) {
         var1 = EnumFacing.xp;
      }

      return var1;
   }

   public static void getBoardString(EnumState[][] var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < 24; ++var2) {
         for(int var3 = 0; var3 < 21; ++var3) {
            String var4 = var0[var2][var3].toString();
            var1.append(String.format("board[%d][%d]=EnumState.%s;", var2, var3, var4));
         }
      }

      boardString = var1.toString();
   }

   public static int getWaterValueOf(EnumState var0) {
      byte var1 = 0;
      if (var0 != EnumState.w && var0 != EnumState.w8) {
         if (var0 == EnumState.w7) {
            return 7;
         } else if (var0 == EnumState.w6) {
            return 6;
         } else if (var0 == EnumState.w5) {
            return 5;
         } else if (var0 == EnumState.w4) {
            return 4;
         } else if (var0 == EnumState.w3) {
            return 3;
         } else if (var0 == EnumState.w2) {
            return 2;
         } else {
            return var0 == EnumState.w1 ? 1 : var1;
         }
      } else {
         return 8;
      }
   }

   public static boolean isWater(EnumState var0) {
      return var0 == EnumState.w || var0 == EnumState.w1 || var0 == EnumState.w2 || var0 == EnumState.w3 || var0 == EnumState.w4 || var0 == EnumState.w5 || var0 == EnumState.w6 || var0 == EnumState.w7 || var0 == EnumState.w8;
   }

   public static EnumState[][] getBoard(Room var0, EnumFacing var1) {
      EnumState[][] var2 = new EnumState[24][21];
      byte var3 = 60;
      byte var4 = 83;
      int var5;
      int var6;
      int var7;
      int var8;
      int var9;
      int var10;
      if (var1 != EnumFacing.xn && var1 != EnumFacing.xp) {
         var5 = var1 == EnumFacing.zp ? var0.z - 11 : var0.z + 11;
         var6 = var1 == EnumFacing.zp ? var0.z - 12 : var0.z + 12;
         if (var1 == EnumFacing.zn) {
            var7 = var0.x + 10;
            var8 = var0.x - 10;

            for(var9 = var7; var9 >= var8; --var9) {
               for(var10 = var3; var10 <= var4; ++var10) {
                  var2[var10 - var3][var7 - var9] = getStateFromBlock(BlockUtils.getBlockAt(var9, var10, var5), BlockUtils.getBlockAt(var9, var10, var6));
               }
            }
         } else {
            var7 = var0.x - 10;
            var8 = var0.x + 10;

            for(var9 = var7; var9 <= var8; ++var9) {
               for(var10 = var3; var10 <= var4; ++var10) {
                  var2[var10 - var3][var9 - var7] = getStateFromBlock(BlockUtils.getBlockAt(var9, var10, var5), BlockUtils.getBlockAt(var9, var10, var6));
               }
            }
         }
      } else {
         var5 = var1 == EnumFacing.xp ? var0.x - 11 : var0.x + 11;
         var6 = var1 == EnumFacing.xp ? var0.x - 12 : var0.x + 12;
         if (var1 == EnumFacing.xp) {
            var7 = var0.z + 10;
            var8 = var0.z - 10;

            for(var9 = var7; var9 >= var8; --var9) {
               for(var10 = var3; var10 <= var4; ++var10) {
                  var2[var10 - var3][var7 - var9] = getStateFromBlock(BlockUtils.getBlockAt(var5, var10, var9), BlockUtils.getBlockAt(var6, var10, var9));
               }
            }
         } else {
            var7 = var0.z - 10;
            var8 = var0.z + 10;

            for(var9 = var7; var9 <= var8; ++var9) {
               for(var10 = var3; var10 <= var4; ++var10) {
                  var2[var10 - var3][var9 - var7] = getStateFromBlock(BlockUtils.getBlockAt(var5, var10, var9), BlockUtils.getBlockAt(var6, var10, var9));
               }
            }
         }
      }

      var2[23][10] = EnumState.w;
      getBoardString(var2);
      return var2;
   }

   public static Pair simulate(EnumState[][] var0) {
      EnumState[][] var1 = new EnumState[24][21];

      int var2;
      for(var2 = 0; var2 < 24; ++var2) {
         for(int var3 = 0; var3 < 21; ++var3) {
            if (!isWater(var0[var2][var3])) {
               var1[var2][var3] = var0[var2][var3];
            } else if (isFlowWater(var0[var2][var3])) {
               var1[var2][var3] = var0[var2][var3];
            } else if ((var3 <= 1 || !isWater(var0[var2][var3 - 1])) && (var3 >= 20 || !isWater(var0[var2][var3 + 1])) && (var2 + 1 >= 24 || !isWater(var0[var2 + 1][var3]))) {
               var1[var2][var3] = EnumState.E;
            } else {
               var1[var2][var3] = var0[var2][var3];
            }
         }
      }

      var1[23][10] = EnumState.w;

      for(var2 = 1; var2 < 23; ++var2) {
         boolean var15 = false;
         int var4 = -1;
         int var5 = -1;

         for(int var6 = 0; var6 < 21; ++var6) {
            EnumState var7 = var0[var2][var6];
            if (isWater(var7)) {
               var15 |= isWater(var0[var2 + 1][var6]);
               if (var4 == -1) {
                  var4 = var6;
               }

               var5 = var6;
               if (!isBlock(var1[var2 - 1][var6])) {
                  if (var2 > 1 && isBlock(var1[var2 - 2][var6])) {
                     var1[var2 - 1][var6] = EnumState.w8;
                  } else {
                     var1[var2 - 1][var6] = EnumState.w;
                  }
               }
            } else if (var4 != -1) {
               boolean var8 = false;
               boolean var9 = false;
               boolean var10 = !var15 && var4 == var5 && var1[var2][var4] == EnumState.w8;
               int var12;
               if ((!var15 || var4 != var5) && !var10) {
                  var8 = canExtendLeft(var1, var2, var4) && var1[var2][var4] != EnumState.w1 && compare(var1[var2][var4], var1[var2][var4 + 1]) < 0;
                  var9 = canExtendRight(var1, var2, var5) && var1[var2][var5] != EnumState.w1 && compare(var1[var2][var5], var1[var2][var5 - 1]) < 0;
               } else {
                  int var11 = 0;
                  var12 = 0;
                  boolean var13 = true;

                  boolean var14;
                  for(var14 = true; canExtendLeft(var1, var2, var4 - var11) && var11 < 5; ++var11) {
                  }

                  if (isBlock(var0[var2 - 1][var4 - var11])) {
                     var13 = false;
                  }

                  while(canExtendRight(var1, var2, var4 + var12) && var12 < 5) {
                     ++var12;
                  }

                  if (isBlock(var0[var2 - 1][var4 + var12])) {
                     var14 = false;
                  }

                  if (var11 == 0) {
                     var11 = 1000;
                  }

                  if (var12 == 0) {
                     var12 = 1000;
                  }

                  var8 = (var13 && !var14 || var13 == var14 && var11 <= var12) && canExtendLeft(var1, var2, var4);
                  var9 = (!var13 && var14 || var13 == var14 && var11 >= var12) && canExtendRight(var1, var2, var4);
                  if (var11 == var12 && var11 == 1000) {
                     var9 = false;
                     var8 = false;
                  }
               }

               if (var8) {
                  var1[var2][var4 - 1] = getLowerFormOfWater(var1[var2][var4]);
                  if (var1[var2][var4 - 1] != EnumState.E) {
                     --var4;
                  }
               }

               if (var9) {
                  var1[var2][var5 + 1] = getLowerFormOfWater(var1[var2][var5]);
                  if (var1[var2][var5 + 1] != EnumState.E) {
                     ++var5;
                  }
               }

               if (!var15 && !var10) {
                  EnumState var18 = EnumState.E;

                  for(var12 = var4; var12 <= var5; ++var12) {
                     if (compare(var18, var1[var2][var12]) < 0) {
                        var18 = var1[var2][var12];
                     }
                  }

                  for(var12 = var4; var12 <= var5; ++var12) {
                     if (compare(var18, var1[var2][var12]) == 0) {
                        var1[var2][var12] = getLowerFormOfWater(var1[var2][var12]);
                     }
                  }
               }

               var15 = false;
               var5 = -1;
               var4 = -1;
            }
         }
      }

      var2 = 0;
      Iterator var16 = yToFlag.iterator();

      while(var16.hasNext()) {
         Pair var17 = (Pair)var16.next();
         if (!isWater(var0[0][(Integer)var17.getKey()]) && isWater(var1[0][(Integer)var17.getKey()])) {
            var2 |= 1 << (Integer)var17.getValue();
         }
      }

      return new Pair(var1, var2);
   }

   public static void dfs(EnumState[][] var0, int var1, HashMap var2, int var3, boolean var4) {
      int var6;
      int var19;
      if (var4 && var2.size() == 0) {
         EnumOperation[] var16 = EnumOperation.values();
         var6 = var16.length;

         for(var19 = 0; var19 < var6; ++var19) {
            EnumOperation var21 = var16[var19];
            if (var21 != EnumOperation.trig && var21 != EnumOperation.empty) {
               EnumState[][] var22 = getStatesFromOperation(var0, var21);
               var2.put(0, var21);
               EnumOperation[] var23 = EnumOperation.values();
               int var11 = var23.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  EnumOperation var13 = var23[var12];
                  if (var13 != EnumOperation.trig && var13 != EnumOperation.empty && var21 != var13) {
                     EnumState[][] var14 = getStatesFromOperation(var22, var13);
                     var2.put(2, var13);
                     var2.put(4, EnumOperation.trig);
                     EnumState[][] var15 = getStatesFromOperation(var14, EnumOperation.trig);
                     dfs(var15, 4, var2, var3, var4);
                     var2.remove(4);
                     var2.remove(2);
                  }
               }

               var2.remove(0);
            }
         }

      } else {
         if (var4) {
            if (var2.values().stream().filter((var0x) -> {
               return !var0x.equals(EnumOperation.empty) && !var0x.equals(EnumOperation.trig);
            }).count() > 5L || var2.size() > 8) {
               return;
            }
         } else if (var2.values().stream().filter((var0x) -> {
            return !var0x.equals(EnumOperation.empty) && !var0x.equals(EnumOperation.trig);
         }).count() >= 4L || var2.size() > 6) {
            return;
         }

         if (var1 < bestTime) {
            Pair var5 = getTimeAndFlag(var0);
            if (var3 == (Integer)var5.getValue()) {
               bestTime = var1;
               operations = new TreeMap(var2);
               System.out.println("Found a solution: " + var1);
               Iterator var18 = operations.entrySet().iterator();

               while(var18.hasNext()) {
                  Map.Entry var20 = (Map.Entry)var18.next();
                  System.out.println(var20.getKey() + ": " + var20.getValue());
               }

            } else {
               for(var6 = 0; var6 < 15; ++var6) {
                  Pair var7 = simulate(var0);
                  var0 = (EnumState[][])var7.getKey();
                  var3 ^= (Integer)var7.getValue();
               }

               var1 += 15;
               EnumOperation[] var17 = EnumOperation.values();
               var19 = var17.length;

               for(int var8 = 0; var8 < var19; ++var8) {
                  EnumOperation var9 = var17[var8];
                  if ((var9 == EnumOperation.trig || var2.size() != (var4 ? 2 : 0)) && (var9 != EnumOperation.trig || var2.size() <= (var4 ? 2 : 0))) {
                     EnumState[][] var10 = getStatesFromOperation(var0, var9);
                     var2.put(var1, var9);
                     dfs(var10, var1, var2, var3, var4);
                     var2.remove(var1);
                  }
               }

            }
         }
      }
   }

   public static boolean isFlowWater(EnumState var0) {
      return isWater(var0) && var0 != EnumState.w;
   }

   public static Vector3d getEtherwarpPointFor(EnumOperation var0) {
      Vector3d var1 = getPosFor(var0);

      assert var1 != null;

      double var2 = 1.0E9;
      Vector3d var4 = null;
      Iterator var5 = points.iterator();

      while(var5.hasNext()) {
         Vector3d var6 = (Vector3d)var5.next();
         double var7 = MathUtils.distanceSquaredFromPoints(var6, var1);
         if (var7 < var2) {
            var2 = var7;
            var4 = var6;
         }
      }

      return var4;
   }

   public static boolean canExtendLeft(EnumState[][] var0, int var1, int var2) {
      return !isBlock(var0[var1][var2 - 1]) && isBlock(var0[var1 - 1][var2]);
   }

   public static void processBoard(EnumState[][] var0) {
      ca.clear();
      ea.clear();
      da.clear();
      cla.clear();
      ga.clear();
      qa.clear();

      for(int var1 = 0; var1 < 24; ++var1) {
         for(int var2 = 0; var2 < 21; ++var2) {
            EnumState var3 = var0[var1][var2];
            if (var3 == EnumState.c || var3 == EnumState.cc) {
               ca.add(new Pair(var1, var2));
            }

            if (var3 == EnumState.e || var3 == EnumState.ce) {
               ea.add(new Pair(var1, var2));
            }

            if (var3 == EnumState.d || var3 == EnumState.cd) {
               da.add(new Pair(var1, var2));
            }

            if (var3 == EnumState.cl || var3 == EnumState.ccl) {
               cla.add(new Pair(var1, var2));
            }

            if (var3 == EnumState.g || var3 == EnumState.cg) {
               ga.add(new Pair(var1, var2));
            }

            if (var3 == EnumState.q || var3 == EnumState.cq) {
               qa.add(new Pair(var1, var2));
            }

            if (var3 == EnumState.c || var3 == EnumState.d || var3 == EnumState.e || var3 == EnumState.q || var3 == EnumState.g || var3 == EnumState.cl) {
               var0[var1][var2] = EnumState.E;
            }
         }
      }

   }

   public static boolean isBlock(EnumState var0) {
      return var0 == EnumState.B || var0 == EnumState.cc || var0 == EnumState.cg || var0 == EnumState.ccl || var0 == EnumState.cq || var0 == EnumState.cd || var0 == EnumState.ce;
   }

   public static EnumState getLowerFormOfWater(EnumState var0) {
      if (var0 != EnumState.w && var0 != EnumState.w8) {
         if (var0 == EnumState.w7) {
            return EnumState.w6;
         } else if (var0 == EnumState.w6) {
            return EnumState.w5;
         } else if (var0 == EnumState.w5) {
            return EnumState.w4;
         } else if (var0 == EnumState.w4) {
            return EnumState.w3;
         } else if (var0 == EnumState.w3) {
            return EnumState.w2;
         } else if (var0 == EnumState.w2) {
            return EnumState.w1;
         } else {
            return var0 == EnumState.w1 ? EnumState.E : EnumState.E;
         }
      } else {
         return EnumState.w7;
      }
   }
}
