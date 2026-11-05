package name.amori.permadeathrevive.network;

import name.amori.permadeathrevive.PermadeathRevive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CloseReviveScreenPayload ()
        implements CustomPacketPayload {

    public static final Type<CloseReviveScreenPayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            PermadeathRevive.MOD_ID,
                            "close_gui_screen"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, CloseReviveScreenPayload> CODEC =
            StreamCodec.unit(new CloseReviveScreenPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
