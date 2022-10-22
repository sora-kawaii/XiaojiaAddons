package com.xiaojia.xiaojiaaddons.Sounds;

import java.util.ArrayList;

public class Sounds {

    public static ArrayList sounds = new ArrayList();

    public static Sound icyFill() {
        return (new CustomSound("icyFill")).setRepeat(false);
    }

    public static Sound destiny() {
        return (new CustomSound("destiny")).setRepeat(false);
    }

    public static Sound bk() {
        return (new CustomSound("bk")).setRepeat(false);
    }
}
