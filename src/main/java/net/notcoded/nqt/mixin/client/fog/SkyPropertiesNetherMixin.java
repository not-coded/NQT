package net.notcoded.nqt.mixin.client.fog;

import net.minecraft.client.render.DimensionEffects;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionEffects.Nether.class)
public class SkyPropertiesNetherMixin {
    @Inject(method = "useThickFog", at = @At("HEAD"), cancellable = true)
    private void disableFog(CallbackInfoReturnable<Boolean> cir) {
        if((NQT.clientModConfig.fog.disableNetherFog || NQT.clientModConfig.fog.disableAllFog) && NQT.clientModConfig.isEnabled) {
            cir.setReturnValue(false);
        }
    }
}
