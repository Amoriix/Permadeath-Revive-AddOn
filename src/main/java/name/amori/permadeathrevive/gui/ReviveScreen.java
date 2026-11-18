package name.amori.permadeathrevive.gui;

import name.amori.permadeathrevive.client.PlayerSkinCache;
import name.amori.permadeathrevive.network.CloseReviveScreenPayload;
import name.amori.permadeathrevive.network.PlayerInfo;
import name.amori.permadeathrevive.network.RevivePlayerPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
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
    private PlayerInfo selectedPlayer = null;


    private Button reviveButton;

    private int scrollOffset = 0;

    private int panelWidth;
    private int panelHeight;
    private int panelX;
    private int panelY;

    private static final int HEADER_HEIGHT = 45;
    private static final int FOOT_HEADER = 45;
    private static final int ROW_HEIGHT = 24;
    private static final int PADDING = 10;

    private int listX;
    private int listY;
    private int listWidth;
    private int listHeight;

    private boolean requestSubmitted = false;

    @Override
    protected void init() {
        super.init();

        panelWidth = (int)(width * 0.40);
        panelHeight = (int)(height * 0.50);

        panelX = (width - panelWidth) / 2;
        panelY = (height - panelHeight) / 2;

        listX = panelX + PADDING;
        listY = panelY + HEADER_HEIGHT;

        listWidth = panelWidth - (PADDING * 2);
        listHeight = panelHeight - HEADER_HEIGHT - FOOT_HEADER;

        int buttonWidth = 90;
        int buttonHeight = 20;

        int buttonX = panelX + (panelWidth - buttonWidth) / 2;
        int buttonY = panelY + panelHeight - buttonHeight - 10;

        reviveButton = Button.builder(
                Component.literal("REVIVR"),
                button -> confirmRevival()
        ).bounds(
                buttonX,
                buttonY,
                buttonWidth,
                buttonHeight
        ).build();

        reviveButton.active = selectedPlayer != null;
        addRenderableWidget(reviveButton);

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

        // Draws black rectangle
        guiGraphics.fill(panelX, panelY,panelX + panelWidth, panelY + panelHeight, 0x80000000);

        // Draws Title inside the rectangle
        guiGraphics.drawCenteredString(
                minecraft.font,
                "JUGADORES MUERTOS",
                panelX + (panelWidth / 2),
                panelY + 10,
                0xFFFFFFFF);

        // Draws Counter of people dead
        guiGraphics.drawCenteredString(
                minecraft.font,
                "Numero de muertos: " + players.size(),
                panelX + panelWidth / 2,
                panelY + 23,
                0xFFAAAAAA
        );

        // Draws Line separation from title and usernames
        guiGraphics.fill(
                panelX + 10,
                panelY + 35,
                panelX + panelWidth - 10,
                panelY + 36,
                0xFFBFBFBF
        );

        int textX = listX + 5;

        // Prevent rows from rendering outside the list
        guiGraphics.enableScissor(
                listX,
                listY,
                listX + listWidth,
                listY + listHeight
        );

        int starIndex = scrollOffset;
        int endIndex = Math.min(players.size(), starIndex + getVisibleRows() + 1);

        for(int i = starIndex; i < endIndex; i++) {
            PlayerInfo player = players.get(i);

            int playerSlotY = getPlayerSlotY(i);

            int rowLeft = listX;
            int rowRight = listX + listWidth;

            int rowTop = getPlayerSlotY(i);
            int rowBottom = rowTop + ROW_HEIGHT;

            boolean isHovered = mouseX >= rowLeft && mouseX < rowRight &&
                            mouseY >= rowTop && mouseY < rowBottom &&
                            mouseY >= listY && mouseY < listY + listHeight;

            boolean isSelected =
                    selectedPlayer != null && selectedPlayer.uuid().equals(player.uuid());

            // Draw hover highlight
            if (isSelected) {
                guiGraphics.fill(
                        rowLeft,
                        rowTop,
                        rowRight,
                        rowBottom,
                        0x8066AA66
                );
            } else if(isHovered) {
                guiGraphics.fill(
                        rowLeft,
                        rowTop,
                        rowRight,
                        rowBottom,
                        0x40FFFFFF
                );
            }

            int textY = playerSlotY + (ROW_HEIGHT - minecraft.font.lineHeight) / 2;
            // Draws Player names
            guiGraphics.drawString(
                    minecraft.font,
                    player.name(),
                    textX + 25,
                    textY,
                    0xFFFFFFFF
            );

            Supplier<PlayerSkin> skin = playerSkins.get(player.uuid());

            int headSize = 16;
            int headY = playerSlotY + (ROW_HEIGHT - headSize) / 2;

            if (skin != null) {
                ResourceLocation texture = skin.get().texture();
                guiGraphics.blit(texture, textX, headY,
                        16, 16, 8f, 8f, 8, 8, 64, 64);
                // Outer layer: 18×18
                guiGraphics.blit(texture, textX - 1, headY - 1,
                        18, 18,
                        40f, 8f,
                        8, 8,
                        64, 64
                );
            }

        }
        guiGraphics.disableScissor();

        String selectedName = selectedPlayer != null ? selectedPlayer.name() : "Ninguno";

        guiGraphics.drawCenteredString(
                minecraft.font,
                "Seleccionado: " + selectedName,
                panelX + panelWidth / 2,
                panelY + panelHeight - 40,
                0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0) {
            int textX = panelX + 20;
            boolean insideList =
                    mouseX >= listY &&
                    mouseX < listX + listWidth &&
                    mouseY >= listY &&
                    mouseY < listY + listHeight;

            if(!insideList) {
                return super.mouseClicked(mouseX, mouseY, button);
            }

            int startIndex = scrollOffset;
            int endIndex = Math.min(players.size(), startIndex + getVisibleRows() + 1);

            for (int i = startIndex; i < endIndex; i++) {
                PlayerInfo player = players.get(i);

                int rowLeft = textX;
                int rowRight = panelX + panelWidth - 10;

                int rowTop = getPlayerSlotY(i);
                int rowBottom = rowTop + 20;

                if (mouseX >= rowLeft && mouseX <= rowRight &&
                        mouseY >= rowTop && mouseY <= rowBottom) {

                    selectedPlayer = player;
                    reviveButton.active = true;

                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if(mouseX >= listY &&
                mouseX < listX + listWidth &&
                mouseY >= listY &&
                mouseY < listY + listHeight) {
            if(verticalAmount > 0) {
                scrollOffset--;
            } else if(verticalAmount < 0) {
                scrollOffset++;
            }

            scrollOffset = Math.clamp(scrollOffset, 0, getMaxScrollOffset());
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
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

    private void confirmRevival() {
        if(selectedPlayer == null || requestSubmitted) {
            return;
        }

        requestSubmitted = true;

        ClientPlayNetworking.send(
                new RevivePlayerPayload(selectedPlayer.uuid())
        );

        minecraft.setScreen(null);
    }

    private int getPlayerSlotY(int index) {
        return listY + ((index - scrollOffset) * ROW_HEIGHT);
    }

    private int getVisibleRows() {
        return  Math.max(1, listHeight / ROW_HEIGHT);
    }

    private int getMaxScrollOffset() {
        return Math.max(0, players.size() - getVisibleRows());
    }
}
