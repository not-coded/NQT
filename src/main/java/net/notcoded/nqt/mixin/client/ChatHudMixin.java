package net.notcoded.nqt.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Deque;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Shadow @Final private Deque<Text> messageQueue;

    @Shadow @Final private List<ChatHudLine<OrderedText>> visibleMessages;

    @Shadow @Final private List<ChatHudLine<Text>> messages;

    @Shadow @Final private List<String> messageHistory;

    /**
     * @author NotCoded
     * @reason Disable clearing of chat history.
     */
    @Overwrite
    public void clear(boolean clearHistory) {
        this.visibleMessages.clear();
        this.messages.clear();
        if(!NQT.clientModConfig.isEnabled || !NQT.clientModConfig.qol.dontClearChatHistory){
            this.messageQueue.clear();
            if (clearHistory) {
                this.messageHistory.clear();
            }
        }

    }
}
