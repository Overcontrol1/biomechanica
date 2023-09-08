package com.overcontrol1.biomechanica.datagen.provider;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.registry.BlockEntityRegistry;
import com.overcontrol1.biomechanica.registry.BlockRegistry;
import com.overcontrol1.biomechanica.registry.CoreTypeRegistry;
import com.overcontrol1.biomechanica.registry.ItemRegistry;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemRegistry.BIOTECH_ANIMATABLE, "Unused Item");
        translationBuilder.add(ItemRegistry.BIOTECH_EXOSKELETON, "Biotech Exoskeleton");
        translationBuilder.add(ItemRegistry.BIOTECH_CORE, "Biotech Core");

        translationBuilder.add(BlockRegistry.BIOTECH_CRAFTING_STATION, "Biotech Crafting Station");
        translationBuilder.add(BlockRegistry.BIOTECH_CORE_INSERTER, "Biotech Core Inserter");

        addBlockEntity(translationBuilder, BlockEntityRegistry.BIOTECH_CRAFTING_STATION, "Biotech Crafting Station");
        addBlockEntity(translationBuilder, BlockEntityRegistry.BIOTECH_CORE_INSERTER, "Biotech Core Inserter");

        addItemGroup(translationBuilder, "main", "Biomechanica");

        addDamageSource(translationBuilder, "bio_virus", "%1$s had their body melted away");

        translationBuilder.add("coreType.biomechanica.empty", "Empty");

        addCoreType(translationBuilder, CoreTypeRegistry.CUSTOS, "Custos");
        addCoreType(translationBuilder, CoreTypeRegistry.IGNIS, "Ignis");
        addCoreType(translationBuilder, CoreTypeRegistry.CALOR, "Calor");
        addCoreType(translationBuilder, CoreTypeRegistry.OCEANUS, "Oceanus");
        addCoreType(translationBuilder, CoreTypeRegistry.SALTU, "Saltu");
    }

    private static void addBlockEntity(TranslationBuilder builder, BlockEntityType<?> blockEntityType, String translation) {
        addTranslation(builder, "blockEntity", Registries.BLOCK_ENTITY_TYPE, blockEntityType, translation);
    }

    private static void addItemGroup(TranslationBuilder builder, String name, String translation) {
        builder.add("itemGroup." + Biomechanica.MOD_ID + "." + name, translation);
    }

    private static void addDamageSource(TranslationBuilder builder, String name, String translation) {
        builder.add("death.attack." + name, translation);
    }

    private static void addCoreType(TranslationBuilder builder, CoreType type, String translation) {
        addTranslation(builder, "coreType", CustomRegistries.CORE_TYPES, type, translation);
    }

    private static <T> void addTranslation(TranslationBuilder builder, String base, Registry<T> registry, T value, String translation) {
        Identifier id = registry.getId(value);

        if (id != null) {
            builder.add(id.toTranslationKey(base), translation);
        }
    }
}
