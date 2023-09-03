package com.overcontrol1.biomechanica.recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Set;

public class BiotechCraftingStationShapedRecipe implements Recipe<RecipeInputInventory> {
    // ALWAYS SHAPED
    private final int width;
    private final int height;
    private final DefaultedList<Ingredient> ingredients;
    private final ItemStack result;
    private final Identifier id;

    public BiotechCraftingStationShapedRecipe(Identifier id, DefaultedList<Ingredient> ingredients, ItemStack result, int width, int height) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.width = width;
        this.height = height;
    }

    private static int findFirstSymbol(String line) {
        int i;
        for(i = 0; i < line.length() && line.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int findLastSymbol(String pattern) {
        int i;
        for(i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] removePadding(String... pattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int m = 0; m < pattern.length; ++m) {
            String string = pattern[m];
            i = Math.min(i, findFirstSymbol(string));
            int n = findLastSymbol(string);
            j = Math.max(j, n);
            if (n < 0) {
                if (k == m) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (pattern.length == l) {
            return new String[0];
        } else {
            String[] strings = new String[pattern.length - l - k];

            for(int o = 0; o < strings.length; ++o) {
                strings[o] = pattern[o + k].substring(i, j + 1);
            }

            return strings;
        }
    }

    static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(symbols.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String string = pattern[i].substring(j, j + 1);
                Ingredient ingredient = (Ingredient)symbols.get(string);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
                }

                set.remove(string);
                defaultedList.set(j + width * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return defaultedList;
        }
    }

    static Map<String, Ingredient> readSymbols(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if ((entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson((JsonElement) entry.getValue(), false));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        for(int i = 0; i <= inventory.getWidth() - this.width; ++i) {
            for(int j = 0; j <= inventory.getHeight() - this.height; ++j) {
                if (this.matchesPattern(inventory, i, j, true)) {
                    return true;
                }

                if (this.matchesPattern(inventory, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matchesPattern(RecipeInputInventory inv, int offsetX, int offsetY, boolean flipped) {
        for(int i = 0; i < inv.getWidth(); ++i) {
            for(int j = 0; j < inv.getHeight(); ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < 3 && l < this.height) {
                    if (flipped) {
                        ingredient = this.ingredients.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.ingredients.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(inv.getStack(i + j * inv.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    static String[] getPattern(JsonArray json) {
        String[] strings = new String[json.size()];
        if (strings.length > 4) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 4 is maximum");
        } else if (strings.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < strings.length; ++i) {
                String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
                if (string.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (i > 0 && strings[0].length() != string.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                strings[i] = string;
            }

            return strings;
        }
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        return this.getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.result;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BiotechCraftingStationShapedRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "biotech_crafting_shaped";
        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<BiotechCraftingStationShapedRecipe> {
        public static final Serializer INSTANCE = new Serializer();


        private Serializer() {}

        @Override
        public BiotechCraftingStationShapedRecipe read(Identifier id, JsonObject json) {
            JsonFormat format = new Gson().fromJson(json, JsonFormat.class);

            Map<String, Ingredient> symbols = readSymbols(format.key);

            String[] pattern = BiotechCraftingStationShapedRecipe.removePadding(BiotechCraftingStationShapedRecipe.getPattern(format.pattern));

            DefaultedList<Ingredient> ingredientList = BiotechCraftingStationShapedRecipe.createPatternMatrix(pattern, symbols, 3, 4);
            ItemStack result = ShapedRecipe.outputFromJson(format.result);

            int width = pattern[0].length();
            int height = pattern.length;

            return new BiotechCraftingStationShapedRecipe(id, ingredientList, result, width, height);
        }

        @Override
        public BiotechCraftingStationShapedRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> ingredients = buf.readCollection(value -> DefaultedList.ofSize(value, Ingredient.EMPTY),
                    Ingredient::fromPacket);
            ItemStack result = buf.readItemStack();
            int width = buf.readInt();
            int height = buf.readInt();

            return new BiotechCraftingStationShapedRecipe(id, ingredients, result, width, height);
        }

        @Override
        public void write(PacketByteBuf buf, BiotechCraftingStationShapedRecipe recipe) {
            buf.writeCollection(recipe.ingredients, (packetByteBuf, ingredient) ->
                    ingredient.write(packetByteBuf));
            buf.writeItemStack(recipe.result);
            buf.writeInt(recipe.width);
            buf.writeInt(recipe.height);
        }

        private static class JsonFormat {
            JsonArray pattern;
            JsonObject key;
            JsonObject result;
        }
    }
}
