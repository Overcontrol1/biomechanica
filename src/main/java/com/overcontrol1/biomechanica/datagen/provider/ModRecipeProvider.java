package com.overcontrol1.biomechanica.datagen.provider;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.datagen.provider.json.BiotechCraftingShapedJsonBuilder;
import com.overcontrol1.biomechanica.registry.BlockRegistry;
import com.overcontrol1.biomechanica.registry.ItemRegistry;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        BiotechCraftingShapedJsonBuilder.create(ItemRegistry.BIOTECH_EXOSKELETON)
                .input('I', Blocks.IRON_BLOCK).input('R', Blocks.REDSTONE_BLOCK).input('A', Items.AMETHYST_SHARD)
                .pattern("IAI")
                .pattern(" R ")
                .pattern("IAI")
                .pattern(" R ")
                .offerTo(exporter, new Identifier(Biomechanica.MOD_ID, "biotech_exoskeleton"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.BIOTECH_CRAFTING_STATION)
                .input('O', Blocks.OBSIDIAN).input('D', Items.DIAMOND).input('I', Blocks.IRON_BLOCK).input('S', Blocks.SMITHING_TABLE)
                .pattern("OSO")
                .pattern("IDI")
                .pattern("OIO")
                .criterion("has_smithing_table", conditionsFromItem(Blocks.SMITHING_TABLE))
                .offerTo(exporter, new Identifier(Biomechanica.MOD_ID, "biotech_crafting_station"));
    }
}
