
package name.amori.permadeathrevive.datagen;

import name.amori.permadeathrevive.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ModChestLootTables implements LootTableSubProvider {

    public ModChestLootTables(HolderLookup.Provider registries) {
        // We don't need the registries yet.
    }

    @Override
    public void generate(
            BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter
    ) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
                "permadeath-revive",
                "chests/nether_heart_shrine"
        );

        ResourceKey<LootTable> shrineLoot = ResourceKey.create(
                Registries.LOOT_TABLE,
                location
        );

        exporter.accept(
                shrineLoot,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(
                                                        ModItems.PERMANENT_HEART
                                                )
                                        )
                        )
                        // Pool 2: Random bonus treasures
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(UniformGenerator.between(2, 3))

                                        .add(
                                                LootItem.lootTableItem(Items.GOLDEN_APPLE)
                                                        .setWeight(20)
                                                        .apply(SetItemCountFunction.setCount(
                                                                UniformGenerator.between(1, 2)
                                                        ))
                                        )

                                        .add(
                                                LootItem.lootTableItem(Items.GOLD_INGOT)
                                                        .setWeight(25)
                                                        .apply(SetItemCountFunction.setCount(
                                                                UniformGenerator.between(3, 8)
                                                        ))
                                        )

                                        .add(
                                                LootItem.lootTableItem(Items.DIAMOND)
                                                        .setWeight(15)
                                                        .apply(SetItemCountFunction.setCount(
                                                                UniformGenerator.between(1, 3)
                                                        ))
                                        )

                                        .add(
                                                LootItem.lootTableItem(Items.ANCIENT_DEBRIS)
                                                        .setWeight(5)
                                        )

                                        .add(
                                                LootItem.lootTableItem(Items.NETHERITE_SCRAP)
                                                        .setWeight(5)
                                        )

                                        .add(
                                                LootItem.lootTableItem(Items.OBSIDIAN)
                                                        .setWeight(15)
                                                        .apply(SetItemCountFunction.setCount(
                                                                UniformGenerator.between(2, 5)
                                                        ))
                                        )

                                        .add(
                                                LootItem.lootTableItem(Items.CRYING_OBSIDIAN)
                                                        .setWeight(15)
                                                        .apply(SetItemCountFunction.setCount(
                                                                UniformGenerator.between(1, 3)
                                                        ))
                                        )
                        )
        );
    }
}
