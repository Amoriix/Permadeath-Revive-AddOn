package name.amori.permadeathrevive.item.custom;

import com.mojang.authlib.GameProfile;
import name.amori.permadeathrevive.PermadeathRevive;
import name.amori.permadeathrevive.mixin.StoredUserEntryAccessor;
import name.amori.permadeathrevive.network.ModNetwork;
import name.amori.permadeathrevive.network.PlayerInfo;
import name.amori.permadeathrevive.player.BannedPlayerManager;
import name.amori.permadeathrevive.player.ReviveRequestManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
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

            // Get the Server and list of all players
            MinecraftServer server = serverPlayer.getServer();

            if(ReviveRequestManager.authorizePlayer(serverPlayer)){
                return InteractionResultHolder.fail(stack);
            }

            //BanPlayersTest.banPlayersTest(server);

            Collection<UserBanListEntry> banList = BannedPlayerManager.getPermadeathBans(server);
            List<PlayerInfo> players = createPlayersInfoList(banList);

            ModNetwork.sendBannedPlayerList(serverPlayer, players);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    // Helper Method to create a list of people banned by Permadeath
    private static @NotNull List<PlayerInfo> createPlayersInfoList(Collection<UserBanListEntry> banList) {
        List<PlayerInfo> players = new ArrayList<>(); // Empty List

        for(UserBanListEntry entry : banList) {
            StoredUserEntryAccessor accessor = (StoredUserEntryAccessor) entry;
            GameProfile profile = (GameProfile) accessor.permadeathRevive$invokeGetUser();

            if(profile == null ) {
                continue;
            }

            PlayerInfo info = new PlayerInfo(
                    profile.getId(),
                    profile.getName()
            );

            players.add(info);
        }
        return players;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.permadeath.revive"));
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }
}

//ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(stack.getItem());
//PermadeathRevive.LOGGER.info("Sending revive animation and particles!");

//System.out.println("Total bans: " + server.getPlayerList().getBans().getEntries().size());