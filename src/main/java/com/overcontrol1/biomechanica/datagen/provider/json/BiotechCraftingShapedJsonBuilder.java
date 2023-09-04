package com.overcontrol1.biomechanica.datagen.provider.json;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.overcontrol1.biomechanica.registry.RecipeRegistry;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class BiotechCraftingShapedJsonBuilder extends RecipeJsonBuilder {
    private final Item result;
    private final int count;
    private final List<String> pattern = new ArrayList<>();
    private final Map<Character, Ingredient> inputs = new LinkedHashMap<>();

    public BiotechCraftingShapedJsonBuilder(ItemConvertible result, int count) {
        this.result = result.asItem();
        this.count = count;
    }

    public static BiotechCraftingShapedJsonBuilder create(ItemConvertible result) {
        return create(result, 1);
    }

    public static BiotechCraftingShapedJsonBuilder create(ItemConvertible result, int count) {
        return new BiotechCraftingShapedJsonBuilder(result, count);
    }

    public BiotechCraftingShapedJsonBuilder input(Character c, TagKey<Item> tag) {
        return this.input(c, Ingredient.fromTag(tag));
    }

    public BiotechCraftingShapedJsonBuilder input(Character c, ItemConvertible itemProvider) {
        return this.input(c, Ingredient.ofItems(itemProvider));
    }

    public BiotechCraftingShapedJsonBuilder input(Character c, Ingredient ingredient) {
        if (this.inputs.containsKey(c)) {
            throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
        } else if (c == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.inputs.put(c, ingredient);
            return this;
        }
    }

    public BiotechCraftingShapedJsonBuilder pattern(String patternStr) {
        if (!this.pattern.isEmpty() && patternStr.length() != (this.pattern.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(patternStr);
            return this;
        }
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        this.validate(recipeId);
        exporter.accept(new BiotechCraftingShapedJsonProvider(recipeId, this.result, this.count, this.pattern, this.inputs));
    }

    private void validate(Identifier recipeId) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + recipeId + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.inputs.keySet());
            set.remove(' ');

            for (String string : this.pattern) {
                for (int i = 0; i < string.length(); ++i) {
                    char c = string.charAt(i);
                    if (!this.inputs.containsKey(c) && c != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + recipeId + " uses undefined symbol '" + c + "'");
                    }

                    set.remove(c);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + recipeId);
            } else if (this.pattern.size() == 1 && (this.pattern.get(0)).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + recipeId + " only takes in a single item - should it be a shapeless recipe instead?");
            }
        }
    }

    public static class BiotechCraftingShapedJsonProvider implements RecipeJsonProvider {
        private final Identifier recipeId;
        private final Item result;
        private final int count;
        private final List<String> pattern;
        private final Map<Character, Ingredient> inputs;

        public BiotechCraftingShapedJsonProvider(Identifier recipeId, Item result, int count, List<String> pattern, Map<Character, Ingredient> inputs) {
            this.recipeId = recipeId;
            this.result = result;
            this.count = count;
            this.pattern = pattern;

            this.inputs = inputs;
        }
        @Override
        public void serialize(JsonObject json) {

            JsonArray jsonArray = new JsonArray();

            for (String string : this.pattern) {
                jsonArray.add(string);
            }

            json.add("pattern", jsonArray);
            JsonObject key = new JsonObject();

            for (Map.Entry<Character, Ingredient> characterIngredientEntry : this.inputs.entrySet()) {
                key.add(String.valueOf(characterIngredientEntry.getKey()), characterIngredientEntry.getValue().toJson());
            }

            json.add("key", key);
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", Registries.ITEM.getId(this.result).toString());
            if (this.count > 1) {
                resultJson.addProperty("count", this.count);
            }

            json.add("result", resultJson);
        }

        @Override
        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return RecipeRegistry.Serializers.BIOTECH_CRAFTING;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return null;
        }
    }
}
