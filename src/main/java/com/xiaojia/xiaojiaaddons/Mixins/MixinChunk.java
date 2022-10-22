package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Chunk.class})
public abstract class MixinChunk {
   @Shadow
   public abstract IBlockState getBlockState(BlockPos var1);

   @Inject(
      method = {"setBlockState"},
      at = {@At("HEAD")}
   )
   private void onBlockChange(BlockPos var1, IBlockState var2, CallbackInfoReturnable var3) {
      IBlockState var4 = this.getBlockState(var1);
      if (var4 != var2 && MinecraftUtils.getWorld() != null) {
         try {
            MinecraftForge.EVENT_BUS.post(new BlockChangeEvent(var1, var4, var2));
         } catch (Exception var6) {
         }
      }

   }
}
