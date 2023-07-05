package net.notcoded.nqt.mixin.client;

import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilSocialInteractionsService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/*
- Chat Lag Fix "borrowed" from:
- Crec0 | https://github.com/adryd325/chat-lag-fix/blob/1.16/src/main/java/com/adryd/chatlagfix/mixin/MixinYggdrasilSocialInteractionsService_chatLagFix.java
*/

@Mixin(value = YggdrasilSocialInteractionsService.class, remap = false)
public abstract class YggdrasilSocialInteractionsServiceMixin implements SocialInteractionsService {
    @Shadow
    private Set<UUID> blockList;

    @Shadow
    protected abstract Set<UUID> fetchBlockList();

    @Redirect(method = "isBlockedPlayer", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/yggdrasil/YggdrasilSocialInteractionsService;fetchBlockList()Ljava/util/Set;"))
    private Set<UUID> asyncFetchBlockList(YggdrasilSocialInteractionsService service) {
        CompletableFuture.runAsync(() -> this.blockList = fetchBlockList());
        return null;
    }
}
