package net.notcoded.nqt.mixin.client.fog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(method = "applyFog", at = @At("RETURN"), cancellable = true)
    private static void noFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
        if(NQT.clientModConfig.isEnabled && NQT.clientModConfig.fog.disableAllFog) ci.cancel();

        if(fogType.equals(BackgroundRenderer.FogType.FOG_SKY) && NQT.clientModConfig.isEnabled && (NQT.clientModConfig.fog.disableSkyFog || NQT.clientModConfig.fog.disableAllFog)) ci.cancel();
        if(fogType.equals(BackgroundRenderer.FogType.FOG_TERRAIN) && NQT.clientModConfig.isEnabled && (NQT.clientModConfig.fog.disableTerrainFog || NQT.clientModConfig.fog.disableAllFog))  ci.cancel();
    }
}
