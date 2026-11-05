package name.amori.permadeathrevive.player;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReviveRequestManager {
    private static Set<UUID> players = new HashSet<>();

    public static boolean authorizePlayer(ServerPlayer player) {
        return !players.add(player.getUUID());
    }

    public static boolean isAuthorized(ServerPlayer player) {
        return players.contains(player.getUUID());
    }

    public static void revokeAuthorization(ServerPlayer player) {
        players.remove(player.getUUID());
    }
}
