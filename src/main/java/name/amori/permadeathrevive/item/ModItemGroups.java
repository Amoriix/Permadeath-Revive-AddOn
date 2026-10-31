package name.amori.permadeathrevive.item;

import name.amori.permadeathrevive.PermadeathRevive;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static final ResourceKey<CreativeModeTab> REVIVE_ITEM_GROUP_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ResourceLocation.fromNamespaceAndPath(PermadeathRevive.MOD_ID, "revive_player_items")
    );

    public static final CreativeModeTab REVIVE_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.REVIVE))
            .title(Component.translatable("itemgroup.permadeath.revive_items"))
            .build();

    public static void registerItemGroups() {
        PermadeathRevive.LOGGER.info("Registering Item Groups for " + PermadeathRevive.MOD_ID);

        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                REVIVE_ITEM_GROUP_KEY,
                REVIVE_ITEM_GROUP
        );

        ItemGroupEvents.modifyEntriesEvent(REVIVE_ITEM_GROUP_KEY)
                .register(itemGroup -> {
                    itemGroup.accept(ModItems.REVIVE);
                });
    }
}
