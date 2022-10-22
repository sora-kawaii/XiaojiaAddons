package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;
import java.util.Iterator;

public class FolderSetting extends Setting {
    public FolderSetting(Property var1, Field var2) {
        super(var1, var2);
    }

    public boolean isSonEnabled() {
        Iterator var1 = this.sons.iterator();

        Setting var2;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            var2 = (Setting) var1.next();
            if (var2 instanceof BooleanSetting && (Boolean) var2.get(Boolean.class)) {
                return true;
            }
        } while (!(var2 instanceof FolderSetting) || !((FolderSetting) var2).isSonEnabled());

        return true;
    }

    public boolean setRecursively(boolean var1) {
        boolean var2 = this.set(var1);
        if (!var1) {
            Iterator var3 = this.sons.iterator();

            while (var3.hasNext()) {
                Setting var4 = (Setting) var3.next();
                if (var4 instanceof FolderSetting) {
                    var2 &= ((FolderSetting) var4).setRecursively(false);
                }
            }
        }

        return var2;
    }
}
