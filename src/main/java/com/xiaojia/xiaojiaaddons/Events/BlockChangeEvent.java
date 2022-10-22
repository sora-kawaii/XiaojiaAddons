package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockChangeEvent extends Event {

    public BlockPos position;

    public IBlockState newBlock;

    public IBlockState oldBlock;

    public BlockChangeEvent(BlockPos var1, IBlockState var2, IBlockState var3) {
        this.position = var1;
        this.oldBlock = var2;
        this.newBlock = var3;
    }
}
