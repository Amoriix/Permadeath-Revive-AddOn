package name.amori.permadeathrevive.network;

import name.amori.permadeathrevive.PermadeathRevive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TotemAnimationPayload(ResourceLocation itemID)
        implements CustomPacketPayload {

    public static final Type<TotemAnimationPayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            PermadeathRevive.MOD_ID,
                            "totem_animation"
            )
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TotemAnimationPayload> CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    TotemAnimationPayload::itemID,
                    TotemAnimationPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

/*
TotemAnimationPayload: is responsible for carrying information.
ModNetworking: is responsible for managing network registration and sending.
ClientPacketHandler: is responsible for handling the received information.
 */