package net.notcoded.nqt.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public class EntityMixin {

    @Shadow public World world;

    @Inject(method = "isInsideWall", at = @At("HEAD"), cancellable = true)
    private void noClientCheck(CallbackInfoReturnable<Boolean> cir) {
        if(this.world.isClient && (NQT.clientModConfig.isEnabled && NQT.clientModConfig.performance.noClientSideEntityCollisionChecks)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
