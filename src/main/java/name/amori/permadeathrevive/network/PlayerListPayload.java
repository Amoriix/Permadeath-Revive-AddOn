package name.amori.permadeathrevive.network;

import name.amori.permadeathrevive.PermadeathRevive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record PlayerListPayload(List<PlayerInfo> players)
        implements CustomPacketPayload {

    public static final Type<PlayerListPayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            PermadeathRevive.MOD_ID,
                            "player_list"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerListPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.collection(
                            ArrayList::new,
                            PlayerInfo.CODEC
                    ),
                    PlayerListPayload::players,
                    PlayerListPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
