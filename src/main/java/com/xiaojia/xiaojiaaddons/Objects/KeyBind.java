package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.utils.KeyBindUtils;
import net.minecraft.client.settings.KeyBinding;

public class KeyBind {

    private final KeyBinding keyBinding;

    public KeyBind(String var1, int var2) {
        this.keyBinding = new KeyBinding(var1, var2, "Addons - Xiaojia Addons");
        KeyBindUtils.addKeyBind(this);
    }

    public KeyBind(KeyBinding var1) {
        this.keyBinding = var1;
    }

    public boolean isKeyDown() {
        return this.keyBinding.isKeyDown();
    }

    public boolean isPressed() {
        return this.keyBinding.isPressed();
    }

    public KeyBinding mcKeyBinding() {
        return this.keyBinding;
    }
}
