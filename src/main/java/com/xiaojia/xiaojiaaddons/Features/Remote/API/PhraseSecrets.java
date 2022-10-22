package com.xiaojia.xiaojiaaddons.Features.Remote.API;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xiaojia.xiaojiaaddons.utils.JsonUtils;

public class PhraseSecrets {

    private int secrets;

    public PhraseSecrets(JsonObject var1) {
        if (var1 != null) {
            JsonElement var2 = var1.get("achievements");
            if (var2 != null) {
                JsonObject var3 = var2.getAsJsonObject();
                this.secrets = JsonUtils.getInt(var3, "skyblock_treasure_hunter");
            }
        }
    }

    public int getSecrets() {
        return this.secrets;
    }
}
