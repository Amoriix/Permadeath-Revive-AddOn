package name.amori.permadeathrevive.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class ModNetwork {

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(
                TotemAnimationPayload.TYPE,
                TotemAnimationPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                ReviveParticlesPayload.TYPE,
                ReviveParticlesPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                PlayerListPayload.TYPE,
                PlayerListPayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                RevivePlayerPayload.TYPE,
                RevivePlayerPayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                CloseReviveScreenPayload.TYPE,
                CloseReviveScreenPayload.CODEC
        );
    }

    public static void sendTotemAnimation(ServerPlayer player, ResourceLocation itemID) {
        TotemAnimationPayload payload = new TotemAnimationPayload(itemID);
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendReviveParticles(ServerPlayer player, int entityID) {
        ReviveParticlesPayload payload = new ReviveParticlesPayload(entityID);

        for(ServerPlayer viewer : PlayerLookup.tracking(player)) {
            ServerPlayNetworking.send(viewer, payload);
        }

        ServerPlayNetworking.send(player, payload);
    }

    public static void sendBannedPlayerList(ServerPlayer player, List<PlayerInfo> players) {
        PlayerListPayload payload = new PlayerListPayload(players);
        ServerPlayNetworking.send(player, payload);
    }
}
