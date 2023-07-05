package net.notcoded.nqt.mixin.client;

import com.mojang.authlib.minecraft.SocialInteractionsService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.SocialInteractionsManager;
import net.notcoded.nqt.NQT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

/*
- Chat Lag Fix "borrowed" from:
- fantahund | https://github.com/fantahund/ChatLagRemover/blob/master/src/main/java/de/fanta/chatlagremover/mixin/MixinSocialInteractionsManager.java
*/

@Environment(EnvType.CLIENT)
@Mixin(SocialInteractionsManager.class)
public class SocialInteractionsManagerMixin {
    @Redirect(method = "isPlayerBlocked", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/minecraft/SocialInteractionsService;isBlockedPlayer(Ljava/util/UUID;)Z"))
    public boolean chatLagFix(SocialInteractionsService instance, UUID uuid) {
        if(NQT.clientModConfig.isEnabled && NQT.clientModConfig.fixes.chatLagFix) return false;
        return instance.isBlockedPlayer(uuid);
    }
}
