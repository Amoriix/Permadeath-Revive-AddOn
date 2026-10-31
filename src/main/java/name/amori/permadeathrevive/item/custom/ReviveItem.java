package name.amori.permadeathrevive.item.custom;

import name.amori.permadeathrevive.PermadeathRevive;
import name.amori.permadeathrevive.network.ModNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ReviveItem extends Item {

    public ReviveItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        PermadeathRevive.LOGGER.info("ReviveItem use() called!");
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            //Remove later debugging text
            PermadeathRevive.LOGGER.info("Sending revive animation and particles!");
            ModNetwork.sentTotemAnimation(serverPlayer, BuiltInRegistries.ITEM.getKey(this)); // Or could I use this for getKey argument?
            ModNetwork.sentReviveParticles(serverPlayer, serverPlayer.getId());

            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.TOTEM_USE,
                    SoundSource.NEUTRAL,
                    10.0f, 1.0f);

            stack.shrink(1);

            player.getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal(
                            "[!] " + player.getName().getString() + " A utilizado objeto de resurrección!"
                    ).withStyle(
                            ChatFormatting.GOLD,
                            ChatFormatting.ITALIC
                    ),
                    false
            );
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.permadeath.revive"));
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }
}

//ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(stack.getItem());
