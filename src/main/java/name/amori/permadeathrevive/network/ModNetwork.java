package name.amori.permadeathrevive.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

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
    }

    public static void sentTotemAnimation(ServerPlayer player, ResourceLocation itemID) {
        TotemAnimationPayload payload = new TotemAnimationPayload(itemID);

        for(ServerPlayer viewer : PlayerLookup.tracking(player)) {
            ServerPlayNetworking.send(viewer, payload);
        }

        ServerPlayNetworking.send(player, payload);
    }

    public static void sentReviveParticles(ServerPlayer player, int entityID) {
        ReviveParticlesPayload payload = new ReviveParticlesPayload(entityID);

        for(ServerPlayer viewer : PlayerLookup.tracking(player)) {
            ServerPlayNetworking.send(viewer, payload);
        }

        ServerPlayNetworking.send(player, payload);
    }
}
