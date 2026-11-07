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

        UUID testPlayer5 = UUID.fromString("d1de18ef-fbf8-444c-be1d-2e7ff7d3791a");
        GameProfile testProfile5 = new GameProfile(testPlayer5, "Legendarioam");

        UUID testPlayer6 = UUID.fromString("02d952ba-184b-40b1-87f2-6046e31e0513");
        GameProfile testProfile6 = new GameProfile(testPlayer6, "AlxMad");

        UUID testPlayer7 = UUID.fromString("b2b92d05-c281-4614-b42c-39a0d05d6ac4");
        GameProfile testProfile7 = new GameProfile(testPlayer7, "MelooC");

        UUID testPlayer8 = UUID.fromString("6ffceb72-b1cb-4ee3-b160-fa433f4ecc45");
        GameProfile testProfile8 = new GameProfile(testPlayer8, "DecadenciaDark");

        UUID testPlayer9 = UUID.fromString("fdbd12fb-1fa2-453b-b535-c1bf6d0e84fc");
        GameProfile testProfile9 = new GameProfile(testPlayer9, "conyDJ2462");

        UUID testPlayer10 = UUID.fromString("d4920e49-36fa-4340-a624-2e19b015176e");
        GameProfile testProfile10 = new GameProfile(testPlayer10, "MyaHS");

        UserBanListEntry testEntry = new UserBanListEntry(testProfile, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry2 = new UserBanListEntry(testProfile2, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry3 = new UserBanListEntry(testProfile3, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry4 = new UserBanListEntry(testProfile4, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry5 = new UserBanListEntry(testProfile5, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);

        UserBanListEntry testEntry6 = new UserBanListEntry(testProfile6, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry7 = new UserBanListEntry(testProfile7, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry8 = new UserBanListEntry(testProfile8, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry9 = new UserBanListEntry(testProfile9, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);
        UserBanListEntry testEntry10 = new UserBanListEntry(testProfile10, null, "Permadeath", null, BannedPlayerManager.PERMADEATH_BAN_REASON);

        server.getPlayerList().getBans().add(testEntry);
        server.getPlayerList().getBans().add(testEntry2);
        server.getPlayerList().getBans().add(testEntry3);
        server.getPlayerList().getBans().add(testEntry4);
        server.getPlayerList().getBans().add(testEntry5);

        server.getPlayerList().getBans().add(testEntry6);
        server.getPlayerList().getBans().add(testEntry7);
        server.getPlayerList().getBans().add(testEntry8);
        server.getPlayerList().getBans().add(testEntry9);
        server.getPlayerList().getBans().add(testEntry10);
    }
}
