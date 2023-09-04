package com.overcontrol1.biomechanica.datagen;

import com.overcontrol1.biomechanica.datagen.provider.ModLanguageProvider;
import com.overcontrol1.biomechanica.datagen.provider.ModModelProvider;
import com.overcontrol1.biomechanica.datagen.provider.ModRecipeProvider;
import com.overcontrol1.biomechanica.datagen.provider.loot.ModBlockLootTableProvider;
import com.overcontrol1.biomechanica.datagen.provider.tag.ModBlockTagProvider;
import com.overcontrol1.biomechanica.datagen.provider.tag.ModItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BiomechanicaDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLanguageProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider((FabricDataGenerator.Pack.Factory<ModRecipeProvider>) ModRecipeProvider::new);
        pack.addProvider(ModBlockLootTableProvider::new);
    }
}
