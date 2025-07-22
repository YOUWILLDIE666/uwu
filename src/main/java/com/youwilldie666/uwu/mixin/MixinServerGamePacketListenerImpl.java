package com.youwilldie666.uwu.mixin;

import com.youwilldie666.uwu.util.UwUify;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;
import java.util.UUID;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {

    @Shadow
    public ServerPlayer player;

    protected MixinServerGamePacketListenerImpl(ServerPlayer player) {
        this.player = player;
    }

    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    public void onChatMessage(@NotNull ServerboundChatPacket packet, @NotNull CallbackInfo ci) {
        String orig = packet.message();

        SignedMessageBody signedBody = new SignedMessageBody(
                UwUify.uwuify(orig),
                Instant.now(),
                0L,
                LastSeenMessages.EMPTY
        );

        SignedMessageLink signedMessageLink = new SignedMessageLink(
                0,
                player.getUUID(),
                UUID.randomUUID()
        );

        PlayerChatMessage playerChatMessage = new PlayerChatMessage(
                signedMessageLink,
                null,
                signedBody,
                null,
                FilterMask.PASS_THROUGH
        );

        OutgoingChatMessage outgoingMessage = OutgoingChatMessage.create(playerChatMessage);

        player.sendChatMessage(outgoingMessage, false, ChatType.bind(ChatType.CHAT, player));

        ci.cancel();
    }
}
