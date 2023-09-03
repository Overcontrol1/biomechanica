package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.recipe.BiotechCraftingStationShapedRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RecipeRegistry {
    public static class Types {
        public static final RecipeType<BiotechCraftingStationShapedRecipe> BIOTECH_CRAFTING =
                registerRecipeType(BiotechCraftingStationShapedRecipe.Type.ID, BiotechCraftingStationShapedRecipe.Type.INSTANCE);
        public static void register() {

        }

        private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String id, RecipeType<T> recipeType) {
            return Registry.register(Registries.RECIPE_TYPE, new Identifier(Biomechanica.MOD_ID, id), recipeType);
        }
    }

    public static class Serializers {
        public static final RecipeSerializer<BiotechCraftingStationShapedRecipe> BIOTECH_CRAFTING =
                registerRecipeSerializer(BiotechCraftingStationShapedRecipe.Type.ID, BiotechCraftingStationShapedRecipe.Serializer.INSTANCE);

        public static void register() {

        }

        private static <T extends Recipe<?>> RecipeSerializer<T> registerRecipeSerializer(String id, RecipeSerializer<T> recipeSerializer) {
            return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Biomechanica.MOD_ID, id), recipeSerializer);
        }
    }

    public static void register() {
        Types.register();
        Serializers.register();
    }
}
