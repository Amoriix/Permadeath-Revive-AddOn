package name.amori.permadeathrevive.network;

import name.amori.permadeathrevive.PermadeathRevive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ReviveParticlesPayload(int entityID)
        implements CustomPacketPayload {

    public static final Type<ReviveParticlesPayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            PermadeathRevive.MOD_ID,
                            "particles_revive"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, ReviveParticlesPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ReviveParticlesPayload::entityID,
                    ReviveParticlesPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
