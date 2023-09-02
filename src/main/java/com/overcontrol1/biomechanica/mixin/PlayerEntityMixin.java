package com.overcontrol1.biomechanica.mixin;

import com.mojang.authlib.GameProfile;
import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.biotech.BiotechHolder;
import com.overcontrol1.biomechanica.registry.BiotechRegistry;
import com.overcontrol1.biomechanica.util.NbtUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements BiotechHolder {
    @Unique
    private final List<Biotech> tech = new ArrayList<>();
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectConstructor(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        this.getAttachedTech().add(BiotechRegistry.BACK_CLAW);
        this.getAttachedTech().add(BiotechRegistry.GAS_MASK);
    }

    @Override
    public List<Biotech> getAttachedTech() {
        return this.tech;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        NbtCompound modNbt = new NbtCompound();
        this.writeBiotechToNbt(modNbt);
        NbtUtils.writeToModNbt(nbt, modNbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        NbtUtils.readModNbt(nbt).ifPresent(modNbt -> {
            BiotechHolder.fillFromNbt(modNbt, this);

            if (!this.getAttachedTech().contains(BiotechRegistry.BACK_CLAW)) {
                this.getAttachedTech().add(BiotechRegistry.BACK_CLAW);
            }

            if (!this.getAttachedTech().contains(BiotechRegistry.GAS_MASK)) {
                this.getAttachedTech().add(BiotechRegistry.GAS_MASK);
            }
        });
    }

    @Unique
    private boolean doesBiotechMakeInvulnerableTo(DamageSource source) {
        for (Biotech biotech : this.getAttachedTech()) {
            for (RegistryKey<DamageType> key : biotech.getInvulnerabilities()) {
                if (source.isOf(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || doesBiotechMakeInvulnerableTo(damageSource);
    }
}
