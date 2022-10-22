package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

public class Vector2i {

    public int x;

    public int y;

    public Vector2i(int var1, int var2) {
        this.x = var1;
        this.y = var2;
    }

    public int hashCode() {
        return this.x * 1000 + this.y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean equals(Object var1) {
        if (!(var1 instanceof Vector2i)) {
            return false;
        } else {
            return ((Vector2i) var1).x == this.x && ((Vector2i) var1).y == this.y;
        }
    }
}
