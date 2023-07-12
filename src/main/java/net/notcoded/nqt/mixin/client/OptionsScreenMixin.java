package net.notcoded.nqt.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.notcoded.nqt.NQT;
import net.notcoded.nqt.config.ClientModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;
import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
@Environment(EnvType.CLIENT)
public abstract class OptionsScreenMixin extends Screen {

    /*
    - Tweaked the code a bit, from:
    - RizeCookey | https://github.com/rizecookey/CookeyMod/blob/1.19.4/src/main/java/net/rizecookey/cookeymod/mixin/client/OptionsScreenMixin.java
    */
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;",
                    shift = At.Shift.AFTER, ordinal = 10),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addCustomButton(CallbackInfo ci) {
        if(NQT.clientModConfig.isEnabled && NQT.clientModConfig.optionsMenu) {
            this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("nqt.options"), (buttonWidget) -> NQT.client.setScreen(AutoConfig.getConfigScreen(ClientModConfig.class, this).get())).build());
        }

    }
}
