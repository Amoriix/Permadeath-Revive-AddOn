package name.amori.permadeathrevive.item.custom;

import name.amori.permadeathrevive.player.ReviveHealthManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PermanentHeart extends Item {
    public PermanentHeart(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);

        if(!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            boolean success = ReviveHealthManager.canRestoreHeart(serverPlayer);

            if(!success) {
                return InteractionResultHolder.fail(stack);
            }
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if(!level.isClientSide && livingEntity instanceof ServerPlayer serverPlayer) {
            boolean success = ReviveHealthManager.restoreHeart(serverPlayer);

            if(!success) {
                return itemStack;
            }
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }
}
