package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import java.awt.Color;
import net.minecraft.block.Block;

public class Door {

   public boolean normallyVisible;

   public String type;

   public int x;

   public boolean explored;

   public int z;

   public Color getColor() {
      switch (this.type) {
         case "wither":
            return ColorUtils.realColors[Configs.WitherDoorColor];
         case "blood":
            return new Color(231, 0, 0, 255);
         case "entrance":
            return new Color(20, 133, 0, 255);
         default:
            return new Color(92, 52, 14, 255);
      }
   }

   public void update() {
      int var1 = Block.getIdFromBlock(BlockUtils.getBlockAt(this.x, 70, this.z));
      if (var1 != 0 && var1 == 166) {
      }

   }

   public Door(int var1, int var2) {
      this.x = var1;
      this.z = var2;
      this.type = "normal";
      this.explored = true;
      this.normallyVisible = true;
   }
}
