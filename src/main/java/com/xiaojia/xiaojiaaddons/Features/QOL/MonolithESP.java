package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MonolithESP {

    private final ArrayList spawns = new ArrayList(Arrays.asList(new BlockPos(-15, 236, -92), new BlockPos(49, 202, -162), new BlockPos(56, 214, -25), new BlockPos(1, 170, 0), new BlockPos(150, 196, 190), new BlockPos(-64, 206, -63), new BlockPos(-91, 221, -53), new BlockPos(-94, 201, -30), new BlockPos(-10, 162, 109), new BlockPos(1, 183, 25), new BlockPos(61, 204, 181), new BlockPos(77, 160, 162), new BlockPos(91, 187, 131), new BlockPos(128, 187, 58)));

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent var1) {
        if (true) {
            if (Configs.MonolithESP) {
                if (SkyblockUtils.isInDwarven()) {
                    BlockPos var2 = null;
                    Iterator var3 = this.spawns.iterator();

                    BlockPos var4;
                    while (var3.hasNext()) {
                        var4 = (BlockPos) var3.next();
                        Iterator var5 = BlockPos.getAllInBox(var4.add(-4, -2, -4), var4.add(4, 2, 4)).iterator();

                        while (var5.hasNext()) {
                            BlockPos var6 = (BlockPos) var5.next();
                            if (BlockUtils.getBlockAt(var6) == Blocks.dragon_egg) {
                                var2 = var6;
                                break;
                            }
                        }

                        if (var2 != null) {
                            break;
                        }
                    }

                    GuiUtils.enableESP();
                    if (var2 == null) {
                        var3 = this.spawns.iterator();

                        while (var3.hasNext()) {
                            var4 = (BlockPos) var3.next();
                            GuiUtils.drawBoxAtBlock(var4, new Color(0, 255, 0, 120), 1, 1, 0.002F);
                        }
                    } else {
                        GuiUtils.drawBoxAtBlock(var2, new Color(255, 0, 0, 120), 1, 1, 0.002F);
                    }

                    GuiUtils.disableESP();
                }
            }
        }
    }
}
