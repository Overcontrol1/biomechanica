package com.overcontrol1.biomechanica.datagen.provider;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.registry.BlockRegistry;
import com.overcontrol1.biomechanica.registry.ItemRegistry;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerParented(Blocks.IRON_BLOCK, BlockRegistry.BIOTECH_CRAFTING_STATION);
        blockStateModelGenerator.registerParented(Blocks.QUARTZ_BLOCK, BlockRegistry.BIOTECH_CORE_INSERTER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemRegistry.BIOTECH_ANIMATABLE, Items.BARRIER, Models.GENERATED);

        generateExoskeletonModels(itemModelGenerator);
    }

    private static void generateExoskeletonModels(ItemModelGenerator generator) {
        Identifier defaultModelId = new Identifier(Biomechanica.MOD_ID, "item/exoskeleton/default");
        Models.GENERATED.upload(defaultModelId, TextureMap.layer0(defaultModelId), generator.writer);

        for (CoreType coreType : CustomRegistries.CORE_TYPES.stream().toList()) {
            Identifier coreTypeId = Objects.requireNonNull(CustomRegistries.CORE_TYPES.getId(coreType));
            Identifier modelId = coreTypeId.withPrefixedPath("item/exoskeleton/");

            Models.GENERATED.upload(modelId, TextureMap.layer0(modelId), generator.writer);
        }
    }
}
