package name.amori.permadeathrevive.network;

import name.amori.permadeathrevive.PermadeathRevive;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ClientPacketHandler {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                TotemAnimationPayload.TYPE,
                (payload, context) -> {
                    PermadeathRevive.LOGGER.info("Received Totem Animation Payload!");
                    context.client().execute(() -> {
                        // Retrieve the item using its ResourceLocation
                        Item item = BuiltInRegistries.ITEM.get(payload.itemID());

                        // Make sure the item exists
                        if (item == Items.AIR) {
                            return;
                        }

                        // Create an ItemStack for the animation
                        ItemStack stack = new ItemStack(item);

                        // Display the item activation animation
                        context.client().gameRenderer.displayItemActivation(stack);
                    });
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(
                ReviveParticlesPayload.TYPE,
                (payload, context) -> {
                    PermadeathRevive.LOGGER.info("Received Totem Particles Payload!");
                    context.client().execute(() -> {
                        int entityID = payload.entityID();

                        if(context.client().level != null) {
                            Entity entity = context.client().level.getEntity(entityID);

                            if(entity != null) {
                                context.client().particleEngine.createTrackingEmitter(
                                        entity,
                                        ParticleTypes.TOTEM_OF_UNDYING,
                                        30
                                );
                            }
                        }

                    });
                }
        );
    }
}
