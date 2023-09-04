package com.overcontrol1.biomechanica.datagen.provider;

import com.overcontrol1.biomechanica.registry.BlockEntityRegistry;
import com.overcontrol1.biomechanica.registry.BlockRegistry;
import com.overcontrol1.biomechanica.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemRegistry.BIOTECH_ANIMATABLE, "Unused Item");
        translationBuilder.add(ItemRegistry.BIOTECH_EXOSKELETON, "Biotech Exoskeleton");

        translationBuilder.add(BlockRegistry.BIOTECH_CRAFTING_STATION, "Biotech Crafting Station");

        addBlockEntity(translationBuilder, BlockEntityRegistry.BIOTECH_CRAFTING_STATION, "Biotech Crafting Station");

        translationBuilder.add("itemGroup.biomechanica.main", "Biomechanica");

        translationBuilder.add("death.attack.bio_virus", "%1$s had their body melted away");
    }

    private static void addBlockEntity(TranslationBuilder builder, BlockEntityType<?> blockEntityType, String translation) {
        Identifier id = Registries.BLOCK_ENTITY_TYPE.getId(blockEntityType);

        if (id != null) {
            builder.add("blockentity." + id.getNamespace() + "." + id.getPath(), translation);
        }
    }
}
