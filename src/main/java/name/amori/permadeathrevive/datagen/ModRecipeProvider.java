package name.amori.permadeathrevive.datagen;

import name.amori.permadeathrevive.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.REVIVE)
                .pattern("BEX")
                .pattern("ATA")
                .pattern("XGB")
                .define('B', Items.BONE)
                .define('E', Items.ENDER_EYE)
                .define('X', Items.BLAZE_POWDER)
                .define('T', Items.TOTEM_OF_UNDYING)
                .define('A', Items.GOLDEN_APPLE)
                .define('G', Items.GHAST_TEAR)
                .unlockedBy("has_totem_of_undying", has(Items.TOTEM_OF_UNDYING))
                .save(exporter);
    }
}
