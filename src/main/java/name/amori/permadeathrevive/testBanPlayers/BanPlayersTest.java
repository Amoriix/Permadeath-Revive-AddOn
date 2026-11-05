package name.amori.permadeathrevive.testBanPlayers;

import com.mojang.authlib.GameProfile;
import name.amori.permadeathrevive.player.BannedPlayerManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.UserBanListEntry;

import java.util.UUID;

public class BanPlayersTest {
    public static void banPlayersTest(MinecraftServer server) {

        // Create a fake user for now
        UUID testPlayer = UUID.fromString("ba684907-5591-4bb7-a59e-b237b50095c1");
        GameProfile testProfile = new GameProfile(testPlayer, "Selqyx_");

        UUID testPlayer2 = UUID.fromString("ca4be97e-8daa-40a6-9f0f-27689d7ddce7");
        GameProfile testProfile2 = new GameProfile(testPlayer2, "Xhinii");

        UUID testPlayer3 = UUID.fromString("20388a51-97a6-41df-bc39-80cd66af1a44");
        GameProfile testProfile3 = new GameProfile(testPlayer3, "_Amori");

        UUID testPlayer4 = UUID.fromString("73313ec9-9a4e-4b19-a284-087b59224991");
        GameProfile testProfile4 = new GameProfile(testPlayer4, "Nath_sis");

        UserBanListEntry testEntry = new UserBanListEntry(testProfile, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry2 = new UserBanListEntry(testProfile2, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry3 = new UserBanListEntry(testProfile3, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry4 = new UserBanListEntry(testProfile4, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);

        server.getPlayerList().getBans().add(testEntry);
        server.getPlayerList().getBans().add(testEntry2);
        server.getPlayerList().getBans().add(testEntry3);
        server.getPlayerList().getBans().add(testEntry4);
    }
}
