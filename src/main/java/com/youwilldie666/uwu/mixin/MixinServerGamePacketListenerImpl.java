package com.youwilldie666.uwu.mixin;

import com.youwilldie666.uwu.util.UwUify;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {

    @Unique
    private static final SecureRandom uwu$secureRandom = new SecureRandom();
    @Shadow
    public ServerPlayer player;

    protected MixinServerGamePacketListenerImpl(ServerPlayer player) {
        this.player = player;
    }

    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    public void onChatMessage(@NotNull ServerboundChatPacket packet, @NotNull CallbackInfo ci) {
        String orig = packet.message();

        byte[] salt = new byte[32];
        uwu$secureRandom.nextBytes(salt);

        byte[] hashed = uwu$Hash(salt, orig);

        SignedMessageBody signedBody = new SignedMessageBody(
                UwUify.uwuify(orig),
                Instant.now(),
                uwu$btl(hashed),
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

    /**
     * Helper method to convert byte array to long
     */
    @Unique
    @Contract(pure = true)
    private long uwu$btl(byte @NotNull [] bytes) {
        long result = 0;
        for (int i = 0; i < Math.min(bytes.length, Long.BYTES); i++) {
            result |= ((long) bytes[i] & 0xFF) << (i * 8);
        }
        return result;
    }

    /**
     * Hash the salt with the original message using SHA-256
     */
    @Unique
    private byte[] uwu$Hash(byte[] salt, @NotNull String orig) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            md.update(orig.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found");
        }
    }
}
