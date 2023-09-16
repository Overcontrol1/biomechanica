package com.overcontrol1.biomechanica.item.additions;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface MiningLevelModifyingItem {
    boolean canMine(BlockState state, int miningLevel, ItemStack stack, LivingEntity holder);
    float getMiningSpeedMult(BlockState state, ItemStack stack, LivingEntity holder);
}