package com.overcontrol1.biomechanica.datagen.provider.tag;

import com.overcontrol1.biomechanica.registry.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.setToolAndMiningLevel(BlockRegistry.BIOTECH_CRAFTING_STATION, ToolType.PICKAXE, 2);
    }

    private void setToolAndMiningLevel(Block block, ToolType type, int level) {
        this.setMineable(block, type);
        this.setMiningLevel(block, level);
    }

    private void setMiningLevel(Block block, int level) {
        TagKey<Block> tagKey;
        switch (level) {
            case 1 -> tagKey = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "needs_stone_tool"));
            case 2 -> tagKey = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "needs_iron_tool"));
            case 3 -> tagKey = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "needs_diamond_tool"));
            default -> tagKey = TagKey.of(RegistryKeys.BLOCK, new Identifier("fabric", "needs_tool_level_" + level));
        }

        this.getOrCreateTagBuilder(tagKey).add(block);
    }

    private void setMineable(Block block, ToolType type) {
        this.getOrCreateTagBuilder(type.tag).add(block);
    }

    public enum ToolType {
        PICKAXE(TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "mineable/pickaxe"))),
        AXE(TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "mineable/axe"))),
        SHOVEL(TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "mineable/shovel"))),
        HOE(TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft", "mineable/hoe"))),
        SWORD(FabricMineableTags.SWORD_MINEABLE),
        SHEARS(FabricMineableTags.SHEARS_MINEABLE);

        public final TagKey<Block> tag;
        ToolType(TagKey<Block> tag) {
            this.tag = tag;
        }
    }
}
