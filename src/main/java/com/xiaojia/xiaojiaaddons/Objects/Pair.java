package com.xiaojia.xiaojiaaddons.Objects;

import java.io.Serializable;
import java.util.Objects;

public class Pair implements Serializable {

   private final Object value;

   private final Object key;

   public Pair(Object var1, Object var2) {
      this.key = var1;
      this.value = var2;
   }

   public int hashCode() {
      return this.key.hashCode() * 13 + (this.value == null ? 0 : this.value.hashCode());
   }

   public Object getValue() {
      return this.value;
   }

   public String toString() {
      return this.key + "=" + this.value;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof Pair) {
         Pair var2 = (Pair)var1;
         return !Objects.equals(this.key, var2.key) ? false : Objects.equals(this.value, var2.value);
      } else {
         return false;
      }
   }

   public Object getKey() {
      return this.key;
   }
}
