package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class PrismarineESP extends BlockESP {
   public Color getColor() {
      return new Color(68, 232, 218, 120);
   }

   public boolean isEnabled() {
      return Configs.PrismarineESP;
   }

   public Block getBlock() {
      return Blocks.prismarine;
   }
}
