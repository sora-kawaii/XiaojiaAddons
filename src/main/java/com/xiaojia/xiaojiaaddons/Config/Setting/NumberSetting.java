package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;
import net.minecraft.util.MathHelper;

import java.lang.reflect.Field;

public class NumberSetting extends Setting {

    public int step;

    public String suffix;

    public String prefix;

    public int min;

    public int max;

    public NumberSetting(Property var1, Field var2) {
        super(var1, var2);
        this.step = var1.step();
        this.min = var1.min();
        this.max = var1.max();
        this.prefix = var1.prefix();
        this.suffix = var1.suffix();
    }

    public boolean set(Object var1) {
        return super.set(MathHelper.clamp_int((Integer) var1, this.min, this.max));
    }

    public int compareTo(Integer var1) {
        try {
            return Integer.compare((Integer) this.get(Integer.TYPE), var1);
        } catch (Exception var3) {
            return 0;
        }
    }
}
