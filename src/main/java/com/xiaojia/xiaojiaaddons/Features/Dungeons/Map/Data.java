package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import java.util.ArrayList;

public class Data {

    public static Data blankRoom = new Data("Unknown", "normal", 0, new ArrayList());
    public String name;
    public String type;
    public int secrets;

    public ArrayList cores;

    public Data(String var1, String var2, int var3, ArrayList var4) {
        this.name = var1;
        this.type = var2;
        this.secrets = var3;
        this.cores = var4;
    }

    public Data() {
    }
}
