package name.amori.permadeathrevive.item;

import name.amori.permadeathrevive.PermadeathRevive;
import name.amori.permadeathrevive.item.custom.ReviveItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final Item REVIVE = register(new ReviveItem(new Item.Properties().stacksTo(1)), "revive");

    private static Item register(Item item, String id) {
        ResourceLocation itemID = ResourceLocation.fromNamespaceAndPath(PermadeathRevive.MOD_ID, id);
        return Registry.register(BuiltInRegistries.ITEM, itemID, item);
    }

    public static void registerModItems() {
        PermadeathRevive.LOGGER.info("Registering mod Items for " + PermadeathRevive.MOD_ID);
    }
}
