package com.xiaojia.xiaojiaaddons.Config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.Config.Setting.*;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Config {

    private static final String fileName = "config/XiaojiaAddons.cfg";

    public static void save() {
        try {
            HashMap var0 = new HashMap();
            Iterator var1 = XiaojiaAddons.settings.iterator();

            while (var1.hasNext()) {
                Setting var2 = (Setting) var1.next();
                if (!(var2 instanceof FolderSetting)) {
                    var0.put(var2.name, var2.get(Object.class));
                }
            }

            String var4 = (new Gson()).toJson(var0);
            Files.write(Paths.get("config/XiaojiaAddons.cfg"), var4.getBytes(StandardCharsets.UTF_8));
        } catch (Exception var3) {
            System.out.println("Error while saving config file");
            var3.printStackTrace();
        }

    }

    public static ArrayList collect(Class var0) {
        Field[] var1 = var0.getDeclaredFields();
        ArrayList var2 = new ArrayList();
        Field[] var3 = var1;
        int var4 = var1.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Field var6 = var3[var5];
            Property var7 = var6.getAnnotation(Property.class);
            if (var7 != null) {
                switch (var7.type()) {
                    case BOOLEAN:
                        var2.add(new BooleanSetting(var7, var6, var7.type()));
                        break;
                    case NUMBER:
                        var2.add(new NumberSetting(var7, var6));
                        break;
                    case SELECT:
                        var2.add(new SelectSetting(var7, var6));
                        break;
                    case FOLDER:
                        var2.add(new FolderSetting(var7, var6));
                        break;
                    case TEXT:
                        var2.add(new TextSetting(var7, var6));
                }
            }
        }

        Iterator var8 = var2.iterator();

        while (var8.hasNext()) {
            Setting var10 = (Setting) var8.next();
            if (!var10.annotation.parent().equals("")) {
                var10.parent = getSetting(var10.annotation.parent(), var2);
                if (var10.parent != null) {
                    var10.parent.sons.add(var10);
                }
            }
        }

        ArrayList var9 = new ArrayList();
        Iterator var11 = var2.iterator();

        while (var11.hasNext()) {
            Setting var12 = (Setting) var11.next();
            if (var12.parent == null) {
                dfs(var9, var12);
            }
        }

        return var9;
    }

    public static void load() {
        try {
            File var0 = new File("config/XiaojiaAddons.cfg");
            if (var0.exists()) {
                BufferedReader var1 = Files.newBufferedReader(Paths.get("config/XiaojiaAddons.cfg"));
                Type var2 = (new TypeToken() {
                }).getType();
                HashMap<String, Object> var3 = (new Gson()).fromJson(var1, var2);
                for (Map.Entry<String, Object> entry : var3.entrySet()) {
                    Setting setting = Config.getSetting(entry.getKey(), XiaojiaAddons.settings);
                    System.out.println("ENT " + entry.getValue() + "\t" + entry.getValue().getClass());
                    if (setting != null) {
                        if (setting instanceof NumberSetting || setting instanceof SelectSetting) {
                            setting.set(((Double) entry.getValue()).intValue());
                        } else {
                            setting.set(entry.getValue());
                        }
                    }
                    return;
                }
            }
        } catch (Exception var7) {
            System.out.println("Error while loading config file");
            var7.printStackTrace();
        }

    }

    public static Setting getSetting(String var0, ArrayList var1) {
        Iterator var2 = var1.iterator();

        Setting var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (Setting) var2.next();
        } while (!var3.name.equals(var0));

        return var3;
    }

    private static void dfs(ArrayList var0, Setting var1) {
        var0.add(var1);
        Iterator var2 = var1.sons.iterator();

        Setting var3;
        while (var2.hasNext()) {
            var3 = (Setting) var2.next();
            if (!(var3 instanceof FolderSetting)) {
                dfs(var0, var3);
            }
        }

        var2 = var1.sons.iterator();

        while (var2.hasNext()) {
            var3 = (Setting) var2.next();
            if (var3 instanceof FolderSetting) {
                dfs(var0, var3);
            }
        }

    }
}
