package name.amori.permadeathrevive.gui;

import name.amori.permadeathrevive.client.PlayerSkinCache;
import name.amori.permadeathrevive.network.CloseReviveScreenPayload;
import name.amori.permadeathrevive.network.PlayerInfo;
import name.amori.permadeathrevive.network.RevivePlayerPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ReviveScreen extends Screen {
    private final Map<UUID, Supplier<PlayerSkin>> playerSkins = new HashMap<>();
    private final List<PlayerInfo> players;

    private int panelWidth;
    private int panelHeight;

    private int panelX;
    private int panelY;

    private boolean requestSubmitted = false;

    @Override
    protected void init() {
        super.init();

        loadPlayerSkins();
    }
    @Override
    public void onClose() {
        if(!requestSubmitted) {
            ClientPlayNetworking.send(new CloseReviveScreenPayload());
        }
        super.onClose();
    }

    public ReviveScreen(List<PlayerInfo> players) {
        super(Component.literal("The Departed"));
        this.players = players;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        panelWidth = (int)(width * 0.40);
        panelHeight = (int)(height * 0.50);

        panelX = (width - panelWidth) / 2;
        panelY = (height - panelHeight) / 2;

        // Draws black rectangle
        guiGraphics.fill(panelX, panelY,panelX + panelWidth, panelY + panelHeight, 0x80000000);

        // Draws Title inside the rectangle
        guiGraphics.drawCenteredString(minecraft.font, "JUGADORES MUERTOS", panelX + (panelWidth / 2), panelY + 10, 0xFFFFFFFF);

        // Draws Line separation from title and usernames
        guiGraphics.fill(
                panelX + 10,
                panelY + 25,
                panelX + panelWidth - 10,
                panelY + 26,
                0xFFBFBFBF
        );

        int textX = panelX + 20;

        for(int i = 0; i < players.size(); i++) {
            PlayerInfo player = players.get(i);

            int playerSlotY = getPlayerSlotY(i);

            int rowLeft = textX;
            int rowRight = panelX + panelWidth - 10;

            int rowTop = playerSlotY;
            int rowBottom = rowTop + 20;

            // Draw hover highlight
            if (mouseX >= rowLeft && mouseX <= rowRight &&
                    mouseY >= rowTop && mouseY <= rowBottom) {

                guiGraphics.fill(
                        rowLeft,
                        rowTop,
                        rowRight,
                        rowBottom,
                        0x40FFFFFF
                );
            }

            // Draws Player names
            guiGraphics.drawString(
                    minecraft.font,
                    player.name(),
                    textX + 25,
                    playerSlotY + 3,
                    0xFFFFFFFF
            );

            Supplier<PlayerSkin> skin = playerSkins.get(player.uuid());

            if (skin != null) {
                ResourceLocation texture = skin.get().texture();
                guiGraphics.blit(texture, textX, playerSlotY,
                        16, 16, 8f, 8f, 8, 8, 64, 64);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0) {
            int textX = panelX + 20;
            for (int i = 0; i < players.size(); i++) {
                PlayerInfo player = players.get(i);

                int rowLeft = textX;
                int rowRight = panelX + panelWidth - 10;

                int rowTop = getPlayerSlotY(i);
                int rowBottom = rowTop + 20;

                if (mouseX >= rowLeft && mouseX <= rowRight &&
                        mouseY >= rowTop && mouseY <= rowBottom) {

                    requestSubmitted = true;
                    ClientPlayNetworking.send(new RevivePlayerPayload(player.uuid()));
                    minecraft.setScreen(null);

                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void loadPlayerSkins() {
        for(PlayerInfo player : players) {
            if(PlayerSkinCache.hasSkin(player.uuid())) {
                Supplier<PlayerSkin> skin = PlayerSkinCache.getSkin(player.uuid());
                playerSkins.put(player.uuid(), skin);

            } else {
                CompletableFuture.supplyAsync(() ->
                        minecraft.getMinecraftSessionService()
                                .fetchProfile(player.uuid(), true)
                ).thenAccept(result -> {
                    minecraft.execute(() -> {
                        if (result != null) {
                            Supplier<PlayerSkin> skin = minecraft.getSkinManager()
                                    .lookupInsecure(result.profile());

                            PlayerSkinCache.addSkin(player.uuid(), skin);
                            playerSkins.put(player.uuid(), skin);
                        }
                    });
                });
            }
        }
    }

    private int getPlayerSlotY(int index) {
        return panelY + 35 + (index * 20);
    }
}
