package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import net.minecraft.util.BlockPos;

class MazeGrid implements Comparable {

   public BlockPos pos = null;

   public Vector2i gridPos;

   public Type type;

   public int hashCode() {
      return this.gridPos.hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof MazeGrid)) {
         return false;
      } else {
         return this.pos == ((MazeGrid)var1).pos;
      }
   }

   public int compareTo(Object var1) {
      if (var1 instanceof MazeGrid) {
         MazeGrid var2 = (MazeGrid)var1;
         return var2.gridPos.x * 5 + var2.gridPos.y - (this.gridPos.x * 5 + this.gridPos.y);
      } else {
         return 0;
      }
   }

   public String toString() {
      return this.pos.toString() + ", " + this.type + ", " + this.gridPos;
   }

   MazeGrid(BlockPos var1, Type var2, Vector2i var3) {
      this.pos = var1;
      this.type = var2;
      this.gridPos = var3;
   }
}
