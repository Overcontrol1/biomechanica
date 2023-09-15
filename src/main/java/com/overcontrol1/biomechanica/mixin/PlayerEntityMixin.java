package com.overcontrol1.biomechanica.mixin;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.biotech.BiotechHolder;
import com.overcontrol1.biomechanica.item.additions.MiningLevelModifyingItem;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements BiotechHolder {
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public List<Biotech> getAttachedBiotech() {
//        return BiomechanicaComponents.ATTACHED_BIOTECH.get(this).get();
        return List.of();
    }

    @Unique
    private boolean doesBiotechMakeInvulnerableTo(DamageSource source) {
        for (Biotech biotech : this.getAttachedBiotech()) {
            for (RegistryKey<DamageType> key : biotech.getInvulnerabilities()) {
                if (source.isOf(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Inject(method = "isInvulnerableTo", at = @At("TAIL"), cancellable = true)
    public void injectInvulnerabilities(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (doesBiotechMakeInvulnerableTo(damageSource)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canHarvest", at = @At("RETURN"), cancellable = true)
    public void injectHarvestLevel(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }
        // EMPTY HAND
        int miningLevel = MiningLevelManager.getRequiredMiningLevel(state);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = this.getEquippedStack(slot);

            if (isValidMiningModifier(stack, slot)) {
                if (((MiningLevelModifyingItem) stack.getItem()).canMine(miningLevel, this.getMainHandStack(), this)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }

        cir.setReturnValue(false);
    }

    // MINING SPEED IS ADDITIVE
    @ModifyVariable(method = "getBlockBreakingSpeed", at = @At(value = "STORE"), ordinal = 0)
    public float overrideMiningSpeed(float value, BlockState state) {
        float miningMult = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = this.getEquippedStack(slot);
            if (isValidMiningModifier(stack, slot)) {
                miningMult += ((MiningLevelModifyingItem) stack.getItem()).getMiningSpeedMult(state, this.getMainHandStack(), this);
            }
        }

        return value + miningMult;
    }

    @Unique
    private static boolean isValidMiningModifier(ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();

        if (item instanceof ArmorItem armorItem) {
            if (!(armorItem.getSlotType() == slot)) { // Not correct slot, don't take it into account
                return false;
            }
        }

        return item instanceof MiningLevelModifyingItem;
    }
}