package net.notcoded.nqt.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.notcoded.nqt.NQT;
import net.notcoded.nqt.config.ClientModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
@Environment(EnvType.CLIENT)
public abstract class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void addCustomButton(CallbackInfo ci) {
        if(NQT.clientModConfig.optionsMenu){
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20, new TranslatableText("nqt.options"), (buttonWidget) -> NQT.client.setScreen(AutoConfig.getConfigScreen(ClientModConfig.class, this).get())));
        }
    }
}
