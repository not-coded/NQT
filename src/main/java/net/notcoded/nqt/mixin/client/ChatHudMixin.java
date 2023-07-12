package net.notcoded.nqt.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.notcoded.nqt.NQT.client;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Shadow @Final private List<ChatHudLine> messages;

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @Inject(method = "clear", at = @At("HEAD"), cancellable = true)
    private void dontClearChatHistory(boolean clearHistory, CallbackInfo ci) {

        if(!NQT.clientModConfig.isEnabled || !NQT.clientModConfig.qol.dontClearChatHistory) return;

        if (!clearHistory) {
            client.getMessageHandler().processAll();
            messages.clear();
            visibleMessages.clear();
        }

        ci.cancel();
    }
}
