package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;

public class MapUtils {
    public static boolean includes(String[] var0, String var1) {
        String[] var2 = var0;
        int var3 = var0.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (var1.equals(var5)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isDoor(int var0, int var1) {
        return isColumnAir(var0 + 4, var1) && isColumnAir(var0 - 4, var1) && !isColumnAir(var0, var1 + 4) && !isColumnAir(var0, var1 - 4) || !isColumnAir(var0 + 4, var1) && !isColumnAir(var0 - 4, var1) && isColumnAir(var0, var1 + 4) && isColumnAir(var0, var1 - 4);
    }

    public static boolean chunkLoaded(Vector3i var0) {
        return MinecraftUtils.getWorld().getChunkFromBlockCoords(new BlockPos(var0.x, var0.y, var0.z)).isLoaded();
    }

    public static boolean includes(ArrayList var0, int var1) {
        Iterator var2 = var0.iterator();

        int var3;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            var3 = (Integer) var2.next();
        } while (var1 != var3);

        return true;
    }

    public static boolean isColumnAir(int var0, int var1) {
        for (int var2 = 140; var2 > 11; --var2) {
            Block var3 = BlockUtils.getBlockAt(var0, var2, var1);
            if (var3 != null && Block.getIdFromBlock(var3) != 0) {
                return false;
            }
        }

        return true;
    }
}
