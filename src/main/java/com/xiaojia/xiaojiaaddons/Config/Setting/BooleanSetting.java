package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;
import java.util.Iterator;

public class BooleanSetting extends Setting {

    public Property.Type type;

    public BooleanSetting(Property var1, Field var2, Property.Type var3) {
        super(var1, var2);
        this.type = var3;
    }

    public boolean set(Object var1) {
        try {
            Iterator var2 = this.sons.iterator();

            while (var2.hasNext()) {
                Setting var3 = (Setting) var2.next();
                var3.set(Boolean.FALSE);
            }

            return super.set(var1);
        } catch (Exception var4) {
            System.out.println("Failed to set " + this.name + " to " + var1);
            var4.printStackTrace();
            return false;
        }
    }
}
