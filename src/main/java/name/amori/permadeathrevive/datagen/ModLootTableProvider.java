
package name.amori.permadeathrevive.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(
            FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(
                output,
                Set.of(),
                List.of(
                        new SubProviderEntry(
                                ModChestLootTables::new,
                                LootContextParamSets.CHEST
                        )
                ),
                registriesFuture
        );
    }
}