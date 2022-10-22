package com.xiaojia.xiaojiaaddons.Tests;

import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

public class CopyWorldInfo {

    public static int dx;

    public static int dy;

    public static int y;

    public static int x;

    public static ArrayList blockStates;

    public static int dz;

    public static int z;

    public static void paste() {
        (new Thread(() -> {
            int var0 = 0;
            ChatLib.say(String.format("/fill %d %d %d %d %d %d air", x, y, z, x + dx, y + dy, z + dz));

            for (int var1 = x; var1 <= x + dx; ++var1) {
                for (int var2 = y; var2 <= y + dy; ++var2) {
                    for (int var3 = z; var3 <= z + dz; ++var3) {
                        if (((IBlockState) blockStates.get(var0)).getBlock() != Blocks.air) {
                            ChatLib.say(String.format("/setblock %d %d %d %s %d", var1, var2, var3, Block.blockRegistry.getNameForObject(((IBlockState) blockStates.get(var0)).getBlock()), ((IBlockState) blockStates.get(var0)).getBlock().getMetaFromState((IBlockState) blockStates.get(var0))));
                        }

                        ++var0;
                    }
                }
            }

            ChatLib.chat(String.format("Successfully pasted blocks from %d %d %d to %d %d %d (%d blocks).", x, y, z, x + dx, y + dy, z + dz, dx * dy * dz));
        })).start();
    }

    public static void copy(String var0, String var1, String var2, String var3, String var4, String var5) {
        blockStates = new ArrayList();
        (new Thread(() -> {
            x = Integer.parseInt(var0);
            y = Integer.parseInt(var1);
            z = Integer.parseInt(var2);
            dx = Integer.parseInt(var3);
            dy = Integer.parseInt(var4);
            dz = Integer.parseInt(var5);

            for (int var6 = x; var6 <= x + dx; ++var6) {
                for (int var7 = y; var7 <= y + dy; ++var7) {
                    for (int var8 = z; var8 <= z + dz; ++var8) {
                        blockStates.add(BlockUtils.getBlockStateAt(new BlockPos(var6, var7, var8)));
                    }
                }
            }

            ChatLib.chat(String.format("Successfully copied blocks from %d %d %d to %d %d %d (%d blocks).", x, y, z, x + dx, y + dy, z + dz, dx * dy * dz));
        })).start();
    }
}
