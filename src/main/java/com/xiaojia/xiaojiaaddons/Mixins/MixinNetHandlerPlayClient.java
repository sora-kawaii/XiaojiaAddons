package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.M7Dragon;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.Velocity;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetHandlerPlayClient.class})
public abstract class MixinNetHandlerPlayClient {

    @Shadow
    private WorldClient clientWorldController;

    @Inject(
            method = {"func_147283_a"},
            cancellable = true,
            remap = false,
            at = {@At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/Explosion;doExplosionB(B)V"
            )}
    )
    public void handleExplosion(S27PacketExplosion var1, CallbackInfo var2) {
        EntityPlayerSP var3 = MinecraftUtils.getPlayer();
        if (var3 != null && this.enabled()) {
            if (Configs.VelocityXZ != 0 || Configs.VelocityY != 0) {
                double var4 = (float) Configs.VelocityXZ / 100.0F;
                double var6 = (float) Configs.VelocityY / 100.0F;
                var3.motionX += (double) var1.func_149149_c() * var4;
                var3.motionY += (double) var1.func_149144_d() * var6;
                var3.motionZ += (double) var1.func_149147_e() * var4;
            }

            var2.cancel();
        }
    }

    @Inject(
            method = {"handleEntityVelocity"},
            cancellable = true,
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V"
            )}
    )
    public void handleEntityVelocity(S12PacketEntityVelocity var1, CallbackInfo var2) {
        if (this.enabled()) {
            Entity var3 = MinecraftUtils.getWorld().getEntityByID(var1.getEntityID());
            if (var3 instanceof EntityPlayerSP) {
                double var4 = var1.getMotionX();
                double var6 = var1.getMotionY();
                double var8 = var1.getMotionZ();
                double var10 = (float) Configs.VelocityXZ / 100.0F;
                double var12 = (float) Configs.VelocityY / 100.0F;
                if (Configs.VelocityXZ != 0 || Configs.VelocityY != 0) {
                    var3.setVelocity(var4 * var10 / 8000.0, var6 * var12 / 8000.0, var8 * var10 / 8000.0);
                }

                var2.cancel();
            }
        }
    }

    @Inject(
            method = {"handleSpawnMob"},
            at = {@At("TAIL")}
    )
    public void onHandleSpawnMobTail(S0FPacketSpawnMob var1, CallbackInfo var2) {
        Entity var3 = this.clientWorldController.getEntityByID(var1.getEntityID());
        if (var3 instanceof EntityDragon) {
            M7Dragon.onSpawnDragon((EntityDragon) var3);
        }

    }

    private boolean enabled() {
        return Checker.enabled && Velocity.canDisableKnockBack() && (SkyblockUtils.isInSkyblock() || !Configs.DisableOutofSkyBlock);
    }
}
