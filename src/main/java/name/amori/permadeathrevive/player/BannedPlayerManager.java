package name.amori.permadeathrevive.player;

import com.mojang.authlib.GameProfile;
import name.amori.permadeathrevive.mixin.StoredUserEntryAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;

import java.util.Collection;
import java.util.UUID;

public class BannedPlayerManager {
    public static final String PERMADEATH_BAN_REASON = "Has muerto en Permadeath. Baneo permanente.";

    public static Collection<UserBanListEntry> getBannedPlayers(MinecraftServer server ) {
        UserBanList banList = server.getPlayerList().getBans();
        return banList.getEntries();
    }

    public static Collection<UserBanListEntry> getPermadeathBans(MinecraftServer server) {
        UserBanList banList = server.getPlayerList().getBans();
        return banList.getEntries().stream()
                .filter(entry -> entry.getReason().equals(
                        PERMADEATH_BAN_REASON))
                .toList();
    }

    public static GameProfile getBannedPlayerProfile(MinecraftServer server, UUID uuid) {
        for(UserBanListEntry entry : getPermadeathBans(server)) {

            StoredUserEntryAccessor accessor = (StoredUserEntryAccessor) entry;
            GameProfile profile = (GameProfile) accessor.permadeathRevive$invokeGetUser();

            if(profile.getId().equals(uuid)) {
                return profile;
            }
        }
        return  null;
    }

    public static boolean unbanPlayer( MinecraftServer server, UUID playerUUID) {

        UserBanList banList = server.getPlayerList().getBans();

        for(UserBanListEntry entry : getPermadeathBans(server)) {
            StoredUserEntryAccessor accessor = (StoredUserEntryAccessor) entry;

            GameProfile profile = (GameProfile) accessor.permadeathRevive$invokeGetUser();

            if(profile == null) {
                continue;
            }

            if(profile.getId().equals(playerUUID)) {
                banList.remove(profile);
                return true;
            }
        }
        return false;
    }
}
