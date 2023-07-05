package net.notcoded.nqt.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.Window;
import net.notcoded.nqt.NQT;
import net.notcoded.nqt.config.ClientModConfig;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public abstract Window getWindow();

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci){
        while (NQT.keyBinding.wasPressed()) {
            NQT.client.openScreen(AutoConfig.getConfigScreen(ClientModConfig.class, null).get());
        }
    }

    @Inject(method = "openScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;unlockCursor()V"))
    public void setMouseMode(Screen screen, CallbackInfo ci) {
        if(!(screen instanceof HandledScreen || screen instanceof GameMenuScreen) || (!NQT.clientModConfig.isEnabled || !NQT.clientModConfig.fixes.cursorCenteredFix)) return;

        GLFW.glfwSetInputMode(getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }
}
