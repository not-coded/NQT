package net.notcoded.nqt.mixin.client;

import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.YggdrasilSocialInteractionsService;
import com.mojang.authlib.yggdrasil.response.BlockListResponse;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.URL;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/*
- Chat Lag Fix "borrowed" from:
- Crec0 | https://github.com/adryd325/chat-lag-fix/blob/1.17/src/main/java/com/adryd/chatlagfix/mixin/MixinYggdrasilSocialInteractionsService_chatLagFix.java
*/

@Mixin(value = YggdrasilSocialInteractionsService.class, remap = false)
public abstract class YggdrasilSocialInteractionsServiceMixin implements SocialInteractionsService {
    @Final
    @Shadow
    private URL routeBlocklist;

    @Final
    @Shadow
    private MinecraftClient minecraftClient;

    @Shadow
    private Set<UUID> blockList;


    @Inject(method = "fetchBlockList", at = @At("HEAD"), cancellable = true)
    private void safeForceFetchBlockList(CallbackInfoReturnable<Set<UUID>> cir) {
        // Don't fetch block list multiple times
        if (this.blockList != null || (!NQT.clientModConfig.isEnabled || !NQT.clientModConfig.fixes.chatLagFix)) {
            cir.setReturnValue(this.blockList);
            return;
        }
        // Return an empty set immediately and update the list once the request has finished
        CompletableFuture.runAsync(() -> {
            try {
                final BlockListResponse response = minecraftClient.get(routeBlocklist, BlockListResponse.class);
                this.blockList = response.getBlockedProfiles();
            } catch (final MinecraftClientException ignored) {
                this.blockList = Set.of(new UUID(0, 0));
            }
        });
        cir.setReturnValue(Set.of());
    }
}
