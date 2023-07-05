package net.notcoded.nqt.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.network.ClientPlayerEntity;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Redirect(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasPermissionLevel(I)Z"))
    private boolean checkPermissionLevel(ClientPlayerEntity entity, int permissionLevel){
        if(NQT.clientModConfig.isEnabled && NQT.clientModConfig.qol.gamemodeSwitcherBypass) return true;
        return permissionLevel >= 4;
    }
}
