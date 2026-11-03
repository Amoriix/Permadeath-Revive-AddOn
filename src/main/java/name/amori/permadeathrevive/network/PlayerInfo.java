package name.amori.permadeathrevive.network;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record PlayerInfo(UUID uuid, String name) {

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerInfo> CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC,
                    PlayerInfo::uuid,
                    ByteBufCodecs.STRING_UTF8,
                    PlayerInfo::name,
                    PlayerInfo::new
                    );
}
