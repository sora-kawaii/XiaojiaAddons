package com.xiaojia.xiaojiaaddons.utils;

import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

public class BlockUtils {

    private static final Block[] interactiveBlocks;

    static {
        interactiveBlocks = new Block[]{Blocks.chest, Blocks.trapped_chest, Blocks.lever, Blocks.skull, Blocks.stone_button, Blocks.wooden_button};
    }

    public static boolean canGhostBlock(Block var0) {
        if (isInteractive(var0)) {
            return false;
        } else {
            return var0 != Blocks.bedrock;
        }
    }

    public static Block getBlockAt(float var0, float var1, float var2) {
        return getBlockAt(MathUtils.floor(var0), MathUtils.floor(var1), MathUtils.floor(var2));
    }

    public static BlockPos getNearestBlockPos(Vector3d var0, Vector3d var1) {
        Vector3d var2 = getNearestBlock(var0, var1);
        return new BlockPos(var2.x, var2.y, var2.z);
    }

    public static boolean isBlockTeleportPad(int var0, int var1, int var2) {
        Block var3 = getBlockAt(var0, var1, var2);
        return var3 != null && Block.getIdFromBlock(var3) == 120;
    }

    public static Vector3d getNearestBlock(Vector3d var0, Vector3d var1) {
        return getNearestBlock(var0, var1, true);
    }

    public static Block getBlockAt(double var0, double var2, double var4) {
        return getBlockAt(MathUtils.floor(var0), MathUtils.floor(var2), MathUtils.floor(var4));
    }

    public static Block getBlockAt(int var0, int var1, int var2) {
        return getBlockAt(new BlockPos(var0, var1, var2));
    }

    public static boolean isBlockAir(double var0, double var2, double var4) {
        Block var6 = getBlockAt(var0, var2, var4);
        String var7 = var6.getUnlocalizedName().toLowerCase();
        return var7.contains("air") && !var7.matches(".*[_a-z]air[_a-z].*");
    }

    public static Vector3d getLookingAtVector(double var0) {
        EntityPlayerSP var2 = MinecraftUtils.getPlayer();
        Vec3 var3 = var2.getPositionEyes(MathUtils.partialTicks);
        Vec3 var4 = var2.getLook(MathUtils.partialTicks);
        Vec3 var5 = var3.addVector(var4.xCoord * var0, var4.yCoord * var0, var4.zCoord * var0);
        return getNearestBlock(var3, var5);
    }

    public static Block getBlockAt(Vector3d var0) {
        return getBlockAt(var0.x, var0.y, var0.z);
    }

    public static int getMetaAt(Vector3d var0) {
        IBlockState var1 = getBlockStateAt(new BlockPos(var0.x, var0.y, var0.z));
        return var1 == null ? -1 : var1.getBlock().getMetaFromState(var1);
    }

    public static Block getBlockAt(BlockPos var0) {
        return MinecraftUtils.getWorld() != null && var0 != null ? MinecraftUtils.getWorld().getBlockState(var0).getBlock() : null;
    }

    public static boolean isInteractive(Block var0) {
        Block[] var1 = interactiveBlocks;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Block var4 = var1[var3];
            if (var4 == var0) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList getSurfaceMid(Vec3 var0, BlockPos var1) {
        ArrayList var2 = new ArrayList();
        int var3 = var1.getX();
        int var4 = var1.getY();
        int var5 = var1.getZ();
        double var6 = 1.0E-6;
        if (var0.yCoord < (double) var4) {
            var2.add(new Face(var3, (double) var4 + var6, var5, var3 + 1, (double) var4 + var6, var5 + 1));
        }

        if (var0.yCoord > (var4 + 1)) {
            var2.add(new Face(var3, (double) (var4 + 1) - var6, var5, var3 + 1, (double) (var4 + 1) - var6, var5 + 1));
        }

        if (var0.xCoord < (double) var3) {
            var2.add(new Face((double) var3 + var6, var4, var5, (double) var3 + var6, var4 + 1, var5 + 1));
        }

        if (var0.xCoord > (var3 + 1)) {
            var2.add(new Face((double) (var3 + 1) - var6, var4, var5, (double) (var3 + 1) - var6, var4 + 1, var5 + 1));
        }

        if (var0.zCoord < (double) var5) {
            var2.add(new Face(var3, var4, (double) var5 + var6, var3 + 1, var4 + 1, (double) var5 + var6));
        }

        if (var0.zCoord > (var5 + 1)) {
            var2.add(new Face(var3, var4, (double) (var5 + 1) - var6, var3 + 1, var4 + 1, (double) (var5 + 1) - var6));
        }

        return var2;
    }

    public static int getMetaFromIBS(IBlockState var0) {
        return var0.getBlock().getMetaFromState(var0);
    }

    public static AxisAlignedBB getAABBOfBlock(BlockPos var0) {
        Block var1 = getBlockAt(var0);
        return var1 == null ? null : var1.getSelectedBoundingBox(MinecraftUtils.getWorld(), var0).offset(-XiaojiaAddons.mc.getRenderManager().viewerPosX, -XiaojiaAddons.mc.getRenderManager().viewerPosY, -XiaojiaAddons.mc.getRenderManager().viewerPosZ).expand(0.0010000000474974513, 0.0010000000474974513, 0.0010000000474974513);
    }

    public static String getBlockInfo(BlockPos var0) {
        IBlockState var1 = getBlockStateAt(var0);
        if (var1 != null) {
            int var2 = var1.getBlock().getMetaFromState(var1);
            return var1.getBlock() + ", meta: " + var2;
        } else {
            return "";
        }
    }

    public static BlockPos getLookingAtPos(int var0) {
        Vector3d var1 = getLookingAtVector(var0);
        return new BlockPos(var1.x, var1.y, var1.z);
    }

    public static IBlockState getBlockStateAt(BlockPos var0) {
        return MinecraftUtils.getWorld() != null && var0 != null ? MinecraftUtils.getWorld().getBlockState(var0) : null;
    }

    public static boolean isBlockAir(BlockPos var0) {
        return isBlockAir((float) var0.getX(), (float) var0.getY(), (float) var0.getZ());
    }

    public static String getTileProperty(BlockSkull var0, BlockPos var1) {
        TileEntitySkull var2 = (TileEntitySkull) MinecraftUtils.getWorld().getTileEntity(var1);
        if (var2 != null && var2.getSkullType() == 3) {
            Property var3 = (Property) Iterables.getFirst(var2.getPlayerProfile().getProperties().get("textures"), (Object) null);
            if (var3 != null) {
                String var4 = var3.getValue();
                return var4;
            } else {
                return "owo!";
            }
        } else {
            return "owo?";
        }
    }

    public static Vector3d getNearestBlock(Vector3d var0, Vector3d var1, boolean var2) {
        Vector3d var3 = new Vector3d();
        var3.normalize(MathUtils.diff(var0, var1));
        double var4 = 1.0E-5;
        Vector3d var6 = var0;
        if (!isBlockAir(var0.x, var0.y, var0.z)) {
            return var0;
        } else {
            GuiTest.clear();

            do {
                double var7 = (var4 + (double) MathUtils.ceil(var6.x) - var6.x) / var3.x;
                double var9 = (var4 + (double) MathUtils.ceil(var6.y) - var6.y) / var3.y;
                double var11 = (var4 + (double) MathUtils.ceil(var6.z) - var6.z) / var3.z;
                if (var3.x < 0.0) {
                    var7 = ((double) MathUtils.floor(var6.x) - var4 - var6.x) / var3.x;
                }

                if (var3.y < 0.0) {
                    var9 = ((double) MathUtils.floor(var6.y) - var4 - var6.y) / var3.y;
                }

                if (var3.z < 0.0) {
                    var11 = ((double) MathUtils.floor(var6.z) - var4 - var6.z) / var3.z;
                }

                double var13 = var7;
                if (Math.abs(var9) < Math.abs(var7)) {
                    var13 = var9;
                }

                if (Math.abs(var11) < Math.abs(var13)) {
                    var13 = var11;
                }

                var6 = MathUtils.add(var6, MathUtils.mul(var13, var3));
                GuiTest.append(var6);
                if (MathUtils.floor(var6.x) == MathUtils.floor(var1.x) && MathUtils.floor(var6.y) == MathUtils.floor(var1.y) && MathUtils.floor(var6.z) == MathUtils.floor(var1.z)) {
                    if (var2) {
                        return null;
                    }

                    return var6;
                }
            } while (isBlockAir(var6.x, var6.y, var6.z));

            return var6;
        }
    }

    public static boolean isBlockSapling(float var0, float var1, float var2) {
        Block var3 = getBlockAt(var0, var1, var2);
        return var3.getRegistryName().toLowerCase().contains("sapling");
    }

    public static Vector3d getNearestBlock(Vec3 var0, Vec3 var1, boolean var2) {
        return getNearestBlock(new Vector3d(var0.xCoord, var0.yCoord, var0.zCoord), new Vector3d(var1.xCoord, var1.yCoord, var1.zCoord), var2);
    }

    public static boolean isBlockAir(float var0, float var1, float var2) {
        return isBlockAir(var0, var1, (double) var2);
    }

    public static boolean isBlockWater(double var0, double var2, double var4) {
        Block var6 = getBlockAt(var0, var2, var4);
        return var6.getUnlocalizedName().toLowerCase().contains("water");
    }

    public static boolean isBlockBedRock(double var0, double var2, double var4) {
        Block var6 = getBlockAt(var0, var2, var4);
        return var6.getUnlocalizedName().toLowerCase().contains("bedrock");
    }

    public static MovingObjectPosition watchingAt() {
        return MinecraftUtils.getPlayer().rayTrace(XiaojiaAddons.mc.playerController.getBlockReachDistance(), 0.0F);
    }

    public static Vector3d getNearestBlock(Vec3 var0, Vec3 var1) {
        return getNearestBlock(new Vector3d(var0.xCoord, var0.yCoord, var0.zCoord), new Vector3d(var1.xCoord, var1.yCoord, var1.zCoord));
    }

    public static void showBlockAt(int var0, int var1, int var2) {
        Block var3 = getBlockAt(var0, var1, var2);
        if (var3 instanceof BlockSkull) {
            ChatLib.chat("Skull!");
            ChatLib.chat(getTileProperty((BlockSkull) var3, new BlockPos(var0, var1, var2)));
        }

    }

    public static class Face {

        public Vec3 v2;
        public Vec3 v1;
        public Vec3 v3;
        public Vec3 v4;
        public Vec3 mid;
        double sy;
        double sz;
        double tz;
        double ty;
        double tx;
        double sx;

        public Face(double var1, double var3, double var5, double var7, double var9, double var11) {
            this.sx = var1;
            this.sy = var3;
            this.sz = var5;
            this.tx = var7;
            this.ty = var9;
            this.tz = var11;
            this.mid = new Vec3((var1 + var7) / 2.0, (var3 + var9) / 2.0, (var5 + var11) / 2.0);
        }

        public String toString() {
            return this.mid.toString();
        }
    }
}
