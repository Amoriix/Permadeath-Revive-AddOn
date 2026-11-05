package name.amori.permadeathrevive.client;

import net.minecraft.client.resources.PlayerSkin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerSkinCache {
    private static final Map<UUID, Supplier<PlayerSkin>> skins
            = new HashMap<>();

    public static Supplier<PlayerSkin> getSkin(UUID uuid) {
        return skins.get(uuid);
    }

    public static void addSkin(UUID uuid, Supplier<PlayerSkin> skin) {
        skins.put(uuid, skin);
    }

    public static boolean hasSkin(UUID uuid) {
        return skins.containsKey(uuid);
    }
}
