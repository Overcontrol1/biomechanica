package com.overcontrol1.biomechanica.client.model;

import com.overcontrol1.biomechanica.Biomechanica;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
public class BiomechanicaDynamicModels implements SimpleSynchronousResourceReloadListener {
    public static final Identifier FABRIC_ID = new Identifier(Biomechanica.MOD_ID, "dynamic_model_loader");
    public static final BiomechanicaDynamicModels INSTANCE = new BiomechanicaDynamicModels();
    private static final Map<Identifier, BakedModel> models = new HashMap<>();
    private static final Map<DynamicModelFinder, BakedModel> finders = new HashMap<>();
    private static BakedModel missingModel;
    @Override
    public Identifier getFabricId() {
        return FABRIC_ID;
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return Collections.singletonList(ResourceReloadListenerKeys.MODELS);
    }

    @Override
    public void reload(ResourceManager manager) {
        MinecraftClient client = MinecraftClient.getInstance();
        AtomicInteger modelCount = new AtomicInteger();
        long startingTime = System.nanoTime();

        for (DynamicModelFinder finder : finders.keySet()) {
            var resources = manager.findResources("models/" + finder.getPair().getLeft(), identifier -> identifier.toString().endsWith(".json"));

            resources.forEach((identifier, resource) -> {
                Identifier toRead = identifier.withPath(identifier.getPath().substring(7, identifier.getPath().length() - 5));
                models.put((toRead), client.getBakedModelManager().getModel(toRead));
                modelCount.getAndIncrement();
            });

            finders.put(finder, client.getBakedModelManager().getModel(finder.getPair().getRight())); // set default
            modelCount.getAndIncrement();
        }

        missingModel = client.getBakedModelManager().getMissingModel();
        long endTime = System.nanoTime() - startingTime;
        if (modelCount.get() > 0) {
            Biomechanica.LOGGER.info(String.format("Successfully reloaded %s models for %s %s in %.2fms.", modelCount, finders.size(),
                    finders.size() > 1 ? "DynamicModelFinders" : "DynamicModelFinder", (double) endTime / 1000000));
        } else {
            Biomechanica.LOGGER.warn("No new dynamic models reloaded. Something may be wrong! Proceed with caution.");
        }
    }

    public static BakedModel getModel(Identifier id) {
        if (models.containsKey(id)) {
            return models.get(id);
        }

        AtomicReference<BakedModel> model = new AtomicReference<>();

        finders.forEach((finder, defaultModel) -> {
            if (id.getPath().startsWith(finder.getPair().getLeft())) {
                model.set(defaultModel);
            }
        });

        return model.get() != null ? model.get() : missingModel;
    }

    /**
     * <p> Registers a new DynamicModelFinder to the list. </p>
     * <p> A DynamicModelFinder returns a Pair&lt;String, Identifier&gt; containing the base path of models to load, and the fallback model if none are found. </p>
     * <p> Paths are not namespaced. There is no consequence if you load additional models from another mod accidentally. </p>
     * @param finder The DynamicModelFinder to register.
     */
    public static void register(DynamicModelFinder finder) {
        finders.put(finder, null); // Default will be filled in on reload if found
    }

    public static CompletableFuture<List<Identifier>> findModels(ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            if (finders.isEmpty()) {
                return List.of();
            }

            AtomicInteger modelCount = new AtomicInteger();
            long startingTime = System.nanoTime();
            List<Identifier> loadableModels = new ArrayList<>();

            for (DynamicModelFinder finder : finders.keySet()) {
                String path = finder.getPair().getLeft();
                var resources = resourceManager.findResources(
                        path.startsWith("models/") ? path : "models/" + path,
                        identifier -> identifier.toString().endsWith(".json"));

                resources.forEach((identifier, resource) -> {
                        loadableModels.add(identifier.withPath(identifier.getPath().substring(7, identifier.getPath().length()-5)));
                        modelCount.getAndIncrement();
                });
            }

            long endTime = System.nanoTime() - startingTime;
            if (modelCount.get() > 0) {
                Biomechanica.LOGGER.info(String.format("Successfully found %s additional models to load in %.2fms.", modelCount, (double) endTime / 1000000));
            } else {
                Biomechanica.LOGGER.warn(String.format("Found no additional models from %s DynamicModelFinders. This may mean that something has gone wrong! Proceed with caution.", finders.size()));
            }

            return loadableModels;
        }, executor);
    }

    @FunctionalInterface
    public interface DynamicModelFinder {
        Pair<String, Identifier> getPair(); // FILEPATH, DEFAULT MODEL
    }
}
