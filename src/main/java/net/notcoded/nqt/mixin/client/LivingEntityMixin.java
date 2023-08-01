package net.notcoded.nqt.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "isInsideWall", at = @At("HEAD"), cancellable = true)
    private void noClientCheck(CallbackInfoReturnable<Boolean> cir) {
        if(this.getWorld().isClient && (NQT.clientModConfig.isEnabled && NQT.clientModConfig.performance.noClientSideEntityCollisionChecks)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "pushAway", at = @At("HEAD"), cancellable = true)
    private void noPushEntitiesClient(CallbackInfo ci) {
        if (this.getWorld().isClient && (NQT.clientModConfig.isEnabled && NQT.clientModConfig.performance.noClientSideEntityCollisionChecks)) {
            ci.cancel();
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void noPushEntitiesFromClient(CallbackInfo ci) {
        if (this.getWorld().isClient && (NQT.clientModConfig.isEnabled && NQT.clientModConfig.performance.noClientSideEntityCollisionChecks)) {
            ci.cancel();
        }
    }
}
