package net.notcoded.nqt.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.notcoded.nqt.NQT;
import org.objectweb.asm.Opcodes;
import net.notcoded.nqt.utils.string.Strings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


/*
- Lower Case Commands "borrowed" from:
- JustAlittleWolf | https://github.com/JustAlittleWolf/fabricForceLowercase/blob/1.19/src/main/java/me/wolfii/fabricforcelowercase/mixin/TextFieldWidgetMixin.java
*/

@Environment(EnvType.CLIENT)
@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {
    @Shadow
    private String text;

    @Inject(method = "getText", at = @At("HEAD"), cancellable = true)
    private void modifiedGetText(CallbackInfoReturnable<String> cir) {
        if(NQT.clientModConfig.isEnabled && NQT.clientModConfig.qol.lowerCaseCommands) {
            if (this.text.startsWith("/")) {
                cir.setReturnValue(Strings.firstArgumentToLowerCase(this.text));
            }
        }
    }

    @Redirect(method = "renderButton", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;text:Ljava/lang/String;", opcode = Opcodes.GETFIELD))
    private String modifiedGetText(TextFieldWidget instance) {
        String finalText = this.text;
        if(NQT.clientModConfig.isEnabled && NQT.clientModConfig.qol.lowerCaseCommands) {
            if (this.text.startsWith("/")) {
                finalText = Strings.firstArgumentToLowerCase(text);
            }
        }
        return finalText;
    }
}