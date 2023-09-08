package com.overcontrol1.biomechanica.client;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.client.model.BiomechanicaDynamicModels;
import com.overcontrol1.biomechanica.client.model.DefaultedBiotechModel;
import com.overcontrol1.biomechanica.client.renderer.BiotechRenderer;
import com.overcontrol1.biomechanica.client.renderer.blockentity.BiotechCraftingStationBlockEntityRenderer;
import com.overcontrol1.biomechanica.client.screen.BiotechCoreInserterScreen;
import com.overcontrol1.biomechanica.client.screen.BiotechCraftingStationScreen;
import com.overcontrol1.biomechanica.network.ModMessages;
import com.overcontrol1.biomechanica.registry.BlockEntityRegistry;
import com.overcontrol1.biomechanica.registry.ScreenHandlerRegistry;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Pair;

public class BiomechanicaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMessages.registerS2C();

        for (Biotech biotech : CustomRegistries.BIOTECH.stream().toList()) {
            BiotechRenderer.addModel(biotech, new DefaultedBiotechModel<>(CustomRegistries.BIOTECH.getId(biotech)));
        }

        HandledScreens.register(ScreenHandlerRegistry.BIOTECH_CRAFTING_STATION, BiotechCraftingStationScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.BIOTECH_CORE_INSERTER, BiotechCoreInserterScreen::new);

        BlockEntityRendererFactories.register(BlockEntityRegistry.BIOTECH_CRAFTING_STATION, BiotechCraftingStationBlockEntityRenderer::new);

        BiomechanicaDynamicModels.register(() -> new Pair<>("item/core",
                new ModelIdentifier(Biomechanica.MOD_ID, "core/default", "inventory")));

        BiomechanicaDynamicModels.register(() -> new Pair<>("item/exoskeleton",
                new ModelIdentifier(Biomechanica.MOD_ID, "exoskeleton/default", "inventory")));

        PreparableModelLoadingPlugin.register(BiomechanicaDynamicModels::findModels, (data, pluginContext) -> pluginContext.addModels(data));
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(BiomechanicaDynamicModels.INSTANCE);
    }
}