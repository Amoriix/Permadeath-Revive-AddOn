package name.amori.permadeathrevive.network;

import name.amori.permadeathrevive.PermadeathRevive;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record RevivePlayerPayload(UUID playerUUID)
        implements CustomPacketPayload {

    public static final Type<RevivePlayerPayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            PermadeathRevive.MOD_ID,
                            "revive_player"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, RevivePlayerPayload> CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC,
                    RevivePlayerPayload::playerUUID,
                    RevivePlayerPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
