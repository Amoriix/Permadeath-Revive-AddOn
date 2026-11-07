package name.amori.permadeathrevive.player;

import name.amori.permadeathrevive.PermadeathRevive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ReviveHealthManager {
    private static final ResourceLocation HEALTH_PENALTY_ID =
            ResourceLocation.fromNamespaceAndPath(
                    PermadeathRevive.MOD_ID,
                    "revival_health_penalty"
            );

    private static final double HEALTH_COST = 2.0;
    private static final double MINIMUM_HEALTH_COST = 2.0;

    private static final ResourceLocation ARMOR_BONUS_ID =
            ResourceLocation.fromNamespaceAndPath(
                    "permadeath",
                    "armor_health_bonus"
            );

    public static double getPermanentHealth(ServerPlayer player) {
        AttributeInstance healthAttribute =
                player.getAttribute(Attributes.MAX_HEALTH);

        if(healthAttribute == null) {
            return 0;
        }

        double totalHealth = healthAttribute.getValue();

        AttributeModifier armorModifier =
                healthAttribute.getModifier(ARMOR_BONUS_ID);

        if(armorModifier != null) {
            totalHealth -= armorModifier.amount();
        }

        return  totalHealth;
    }

    public static boolean canAffordRevival(ServerPlayer player) {
        return getPermanentHealth(player) >= MINIMUM_HEALTH_COST + HEALTH_COST;
    }

    public static boolean applyRevivalPenalty(ServerPlayer player) {
        if(!canAffordRevival(player)) {
            return false;
        }

        AttributeInstance healthAttribute =
                player.getAttribute(Attributes.MAX_HEALTH);

        if(healthAttribute == null) {
            return false;
        }

        AttributeModifier existingModifier =
                healthAttribute.getModifier(HEALTH_PENALTY_ID);

        double currentPenalty = existingModifier != null ? existingModifier.amount() : 0.0;
        double newPenalty = currentPenalty - HEALTH_COST;

        healthAttribute.removeModifier(HEALTH_PENALTY_ID);

        AttributeModifier penaltyModifier = new AttributeModifier(
                HEALTH_PENALTY_ID,
                newPenalty,
                AttributeModifier.Operation.ADD_VALUE
        );

        healthAttribute.addPermanentModifier(penaltyModifier);

        if(player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
        return true;
    }

    public static void copyRevivalPenalty(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        AttributeInstance oldAttribute = oldPlayer.getAttribute(Attributes.MAX_HEALTH);

        AttributeInstance newAttribute = newPlayer.getAttribute(Attributes.MAX_HEALTH);

        if(oldAttribute == null || newAttribute == null){
            return;
        }

        AttributeModifier penalty = oldAttribute.getModifier(HEALTH_PENALTY_ID);

        if(penalty == null) {
            return;
        }

        newAttribute.addPermanentModifier(new AttributeModifier(HEALTH_PENALTY_ID, penalty.amount(), AttributeModifier.Operation.ADD_VALUE));

        if (newPlayer.getHealth() > newPlayer.getMaxHealth()) {
            newPlayer.setHealth(newPlayer.getMaxHealth());
        }
    }
}
