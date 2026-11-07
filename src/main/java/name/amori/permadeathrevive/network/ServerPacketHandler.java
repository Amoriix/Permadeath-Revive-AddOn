package name.amori.permadeathrevive.network;

import com.mojang.authlib.GameProfile;
import name.amori.permadeathrevive.PermadeathRevive;
import name.amori.permadeathrevive.item.ModItems;
import name.amori.permadeathrevive.player.BannedPlayerManager;
import name.amori.permadeathrevive.player.ReviveHealthManager;
import name.amori.permadeathrevive.player.ReviveRequestManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public class ServerPacketHandler {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                RevivePlayerPayload.TYPE,
                (payload, context) -> {
                    ServerPlayer requester = context.player();
                    MinecraftServer server = requester.getServer();


                    if(!requester.getInventory().contains(new ItemStack(ModItems.REVIVE))) {
                        ReviveRequestManager.revokeAuthorization(requester);
                        return;
                    }

                    if(!ReviveRequestManager.isAuthorized(requester)) {
                        return;
                    }

                    // Remove player Authorization after the player has been revived
                    if(!ReviveHealthManager.canAffordRevival(requester)) {
                        requester.sendSystemMessage(
                                Component.literal(
                                        "Ya no te quedan suficientes corazones permanentes que sacrificar"
                                )
                        );

                        ReviveRequestManager.revokeAuthorization(requester);
                        return;
                    }

                    // Get the profile of the player revived
                    GameProfile revivedProfile =
                            BannedPlayerManager.getBannedPlayerProfile(
                                    server, payload.playerUUID());

                    if(revivedProfile == null) {
                        ReviveRequestManager.revokeAuthorization(requester);
                    }

                    boolean success = BannedPlayerManager.unbanPlayer(
                            server,
                            payload.playerUUID()
                    );

                    if(success) {
                        String revivedName = revivedProfile.getName();

                        // Apply permanent heart penalty
                        boolean penaltyApplied =
                                ReviveHealthManager.applyRevivalPenalty(requester);

                        if (!penaltyApplied) {
                            // This should not happen after successful validation.
                            // Do not silently continue with a free revival.
                            PermadeathRevive.LOGGER.error(
                                    "Revival succeeded, but health penalty failed for {}",
                                    requester.getName().getString()
                            );
                        }

                        for(ItemStack stack : requester.getInventory().items) {
                            if(stack.is(ModItems.REVIVE)){
                                stack.shrink(1);
                                break;
                            }
                        }

                        // Send the particles to the requester after reviving the player
                        ModNetwork.sendReviveParticles(
                                requester,
                                requester.getId()
                        );

                        // Send the animation to the requester after reviving the player
                        ModNetwork.sendTotemAnimation(
                                requester,
                                BuiltInRegistries.ITEM.getKey(ModItems.REVIVE)
                        );

                        ReviveRequestManager.revokeAuthorization(requester);

                        requester.level().playSound(
                                null,
                                requester.blockPosition(),
                                SoundEvents.TOTEM_USE,
                                SoundSource.NEUTRAL,
                                10.0f, 1.0f);

                        requester.getServer().getPlayerList().broadcastSystemMessage(
                            Component.literal(
                                    "[!] " + requester.getName().getString() + " A Revivido a " + revivedName
                            ).withStyle(
                                    ChatFormatting.GOLD,
                                    ChatFormatting.ITALIC
                            ), false
                        );
                    }
                    else {
                        ReviveRequestManager.revokeAuthorization(requester);
                    }
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                CloseReviveScreenPayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();

                    ReviveRequestManager.revokeAuthorization(player);
                }
        );
    }
}
