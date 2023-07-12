package net.notcoded.nqt.mixin.client;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

/*
- Tooltip Fix "borrowed" from
- kyrptonaught | https://github.com/kyrptonaught/tooltipfix/blob/1.17/src/main/java/net/kyrptonaught/tooltipfix/mixin/FixToolTipMixin.java
*/
@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow
    protected TextRenderer textRenderer;
    @Shadow
    public int width;

    @Shadow
    public abstract void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y);

    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;II)V", at = @At(value = "HEAD"), cancellable = true)
    public void fix(MatrixStack matrices, Text text, int x, int y, CallbackInfo ci) {
        if(NQT.clientModConfig.isEnabled && NQT.clientModConfig.fixes.toolTipFix){
            net.notcoded.nqt.utils.fixes.ToolTipFix.set(x,width);
            this.renderOrderedTooltip(matrices, Lists.transform(net.notcoded.nqt.utils.fixes.ToolTipFix.doFix(Collections.singletonList(text), textRenderer), Text::asOrderedText), net.notcoded.nqt.utils.fixes.ToolTipFix.x, y);
            ci.cancel();
        }
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", at = @At("HEAD"), cancellable = true)
    public void fix(MatrixStack matrices, List<Text> lines, int x, int y, CallbackInfo ci) {
        if(NQT.clientModConfig.isEnabled && NQT.clientModConfig.fixes.toolTipFix){
            net.notcoded.nqt.utils.fixes.ToolTipFix.set(x,width);
            this.renderOrderedTooltip(matrices, Lists.transform(net.notcoded.nqt.utils.fixes.ToolTipFix.doFix(lines, textRenderer), Text::asOrderedText), net.notcoded.nqt.utils.fixes.ToolTipFix.x, y);
            ci.cancel();
        }
    }
}
