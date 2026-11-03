package name.amori.permadeathrevive.player;

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

    public static void unbanPlayer( MinecraftServer server, UUID playerUUID) {
        // Remove player from the ban list
    }
}
