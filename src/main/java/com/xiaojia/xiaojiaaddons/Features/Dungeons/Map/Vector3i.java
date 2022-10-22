package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

public class Vector3i {

   public int y;

   public int z;

   public int x;

   public boolean equals(Object var1) {
      if (!(var1 instanceof Vector3i)) {
         return false;
      } else {
         Vector3i var2 = (Vector3i)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      }
   }

   public String toString() {
      return this.x + ", " + this.y + ", " + this.z;
   }

   public int hashCode() {
      return this.x * 1000000 + this.y * 1000 + this.z;
   }

   public Vector3i(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }
}
