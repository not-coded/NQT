package net.notcoded.nqt.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.notcoded.nqt.NQT;
import net.notcoded.nqt.utils.fixes.titlefix.TitleFix;
import net.notcoded.nqt.utils.fixes.titlefix.TitleRenderInfo;
import net.notcoded.nqt.utils.fixes.tooltipfix.ToolTipFix;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


/*
 - Title Fix "borrowed" from:
 - TheGameratorT | https://github.com/TheGameratorT/McTitleFixer/blob/1.18/src/main/java/com/thegameratort/titlefixer/mixin/InGameHudMixin.java
 */

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Final @Shadow private MinecraftClient client;
    @Shadow private int titleFadeInTicks;
    @Shadow private int titleRemainTicks;
    @Shadow private int titleFadeOutTicks;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow private int titleStayTicks;

    @Shadow protected abstract void drawTextBackground(DrawContext context, TextRenderer textRenderer, int yOffset, int width, int color);

    @Shadow private @Nullable Text subtitle;
    @Shadow private @Nullable Text title;
    @Unique
    private Text titlec;

    @Unique
    private int scoreboardWidth = -1;
    @Unique
    private int scoreboardOpacityGain = 0;

    @Unique
    public boolean renderTitle = false;
    @Unique
    public boolean hideScoreboard = false;
    @Unique
    public final TitleRenderInfo titleRI = new TitleRenderInfo();
    @Unique
    public final TitleRenderInfo subtitleRI = new TitleRenderInfo();

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At("HEAD"))
    private void preRenderHud(CallbackInfo ci) {
        if(!TitleFix.canRun()) return;
        scoreboardWidth = -1; // reset variable
        hideScoreboard = false; // reset variable
        titlec = title; // keep a reference for the title
        title = null; // prevent operation of the original title code
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At("TAIL"))
    private void postRenderHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        if(!TitleFix.canRun()) return;
        /* Calculate title stuff */
        collectRenderInfo();
        /* Render the title */
        executeRenderInfo(context, tickDelta);

        title = titlec; // restore the title
    }

    @Unique
    private void collectRenderInfo() {
        renderTitle = titlec != null && titleStayTicks > 0;
        if (renderTitle) {
            TextRenderer textRenderer = getTextRenderer();

            int titleWidth = textRenderer.getWidth(titlec);
            collectTitleRenderInfo(titleRI, 4.0F, titleWidth);

            if (subtitle != null) {
                int subtitleWidth = textRenderer.getWidth(subtitle);
                collectTitleRenderInfo(subtitleRI, 2.0F, subtitleWidth);
            }
        }
    }

    @Unique
    private void collectTitleRenderInfo(TitleRenderInfo ri, float titleScale, int titleWidth) {
        float renderScale = titleScale;
        int renderAreaWidth = scaledWidth - 8 - 8;
        int renderAreaWidthSB = renderAreaWidth - scoreboardWidth;
        renderAreaWidthSB -= scoreboardWidth;


        int renderTextWidth = (int)(renderScale * titleWidth);
        if (renderTextWidth > renderAreaWidthSB) {
            hideScoreboard = true;
            if (renderTextWidth > renderAreaWidth) {
                renderScale = (float) renderAreaWidth / titleWidth;
            }

        }

        float titlePosX = (float)scaledWidth / 2;
        float titlePosY = (float)scaledHeight / 2;

        ri.posX = titlePosX;
        ri.posY = titlePosY;
        ri.scale = renderScale;
    }

    @Unique
    private void executeRenderInfo(DrawContext context, float tickDelta) {
        if (renderTitle) {
            Profiler profiler = client.getProfiler();
            TextRenderer textRenderer = getTextRenderer();

            profiler.push("titleAndSubtitle");

            float ticksLeft = (float)titleStayTicks - tickDelta;
            int alpha = 255;
            if (titleStayTicks > titleFadeOutTicks + titleRemainTicks) {
                float r = (float)(titleFadeInTicks + titleRemainTicks + titleFadeOutTicks) - ticksLeft;
                alpha = (int)(r * 255.0F / titleFadeInTicks);
            }

            if (titleRemainTicks <= titleFadeOutTicks) {
                alpha = (int)(ticksLeft * 255.0F / titleFadeOutTicks);
            }

            alpha = MathHelper.clamp(alpha, 0, 255);
            if (alpha > 8) {
                context.getMatrices().push();
                context.getMatrices().translate(titleRI.posX, titleRI.posY, 0.0F);
                RenderSystem.enableBlend();
                //RenderSystem.defaultBlendFunc();
                context.getMatrices().push();
                // context.getMatrices().scale(4.0F, 4.0F, 4.0F);
                context.getMatrices().scale(titleRI.scale, titleRI.scale, 1.0F);
                //int titleColor = alpha << 24 & -0x1000000;
                int titleColor = alpha << 24 & 16777216;
                int titleWidth = textRenderer.getWidth(titlec);
                drawTextBackground(context, textRenderer, -10, titleWidth, 16777215 | titleColor);
                context.drawTextWithShadow(textRenderer, titlec, -titleWidth / 2, -10, 16777215 | titleColor);
                context.getMatrices().pop();
                if (subtitle != null) {
                    context.getMatrices().push();
                    context.getMatrices().scale(subtitleRI.scale, subtitleRI.scale, 1.0F);
                    int subtitleWidth = textRenderer.getWidth(subtitle);
                    drawTextBackground(context, textRenderer, 5, subtitleWidth, 16777215 | titleColor);
                    context.drawTextWithShadow(textRenderer, subtitle, -subtitleWidth / 2, 5, 16777215 | titleColor);
                    context.getMatrices().pop();
                }
                RenderSystem.disableBlend();
                context.getMatrices().pop();
            }

            profiler.pop();
        }
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    void tick_hook(CallbackInfo ci) {
        if(!TitleFix.canRun()) return;
        if (hideScoreboard) {
            if (scoreboardOpacityGain > -255) {
                scoreboardOpacityGain -= 13;
                if (scoreboardOpacityGain < -255) {
                    scoreboardOpacityGain = -255;
                }
            }
        }
        else {
            if (scoreboardOpacityGain < 0) {
                scoreboardOpacityGain += 13;
                if (scoreboardOpacityGain > 0) {
                    scoreboardOpacityGain = 0;
                }
            }
        }
    }

    @ModifyArgs(
            method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"
            )
    )
    private void renderScoreboardSidebar_hook1(Args args) {
        if(!TitleFix.canRun()) return;
        if (scoreboardWidth == -1) {
            int x1 = args.get(1);
            int x2 = args.get(3);
            scoreboardWidth = x2 - x1;
        }
        int color = args.get(5);
        args.set(5, getNewScoreboardColor(color));
    }

    @Redirect(
            method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"
            )
    )
    private int renderScoreboardSidebar_hook2(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow) {
        if(!TitleFix.canRun()) return instance.drawText(textRenderer, text, x, y, color, shadow);
        int newColor = getNewScoreboardColor(color);
        int alpha = newColor >>> 24;
        if (alpha <= 8) {
            return 0;
        }
        return instance.drawText(textRenderer, text, x, y, newColor, shadow);
    }

    @Redirect(
            method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"
            )
    )
    private int renderScoreboardSidebar_hook3(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow) {
        if(!TitleFix.canRun()) return instance.drawText(textRenderer, text, x, y, color, shadow);
        int newColor = getNewScoreboardColor(color);
        int alpha = newColor >>> 24;
        if (alpha <= 8) {
            return 0;
        }
        return instance.drawText(textRenderer, text, x, y, newColor, shadow);
    }

    @Unique
    private int getNewScoreboardColor(int color) {
        int alpha = color >>> 24;
        alpha += scoreboardOpacityGain;
        if (alpha > 255) {
            alpha = 255;
        }
        color &= ~0xFF000000;
        color |= alpha << 24;
        return color;
    }
}