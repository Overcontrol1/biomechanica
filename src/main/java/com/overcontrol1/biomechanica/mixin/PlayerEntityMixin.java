package com.overcontrol1.biomechanica.mixin;

import com.mojang.authlib.GameProfile;
import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.biotech.BiotechHolder;
import com.overcontrol1.biomechanica.cca.BiomechanicaComponents;
import com.overcontrol1.biomechanica.registry.BiotechRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements BiotechHolder {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public List<Biotech> getAttachedBiotech() {
        return BiomechanicaComponents.ATTACHED_BIOTECH.get(this).get();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectConstructor(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        if (!this.getAttachedBiotech().contains(BiotechRegistry.BACK_CLAW)) {
            this.getAttachedBiotech().add(BiotechRegistry.BACK_CLAW);
        }

        if (!this.getAttachedBiotech().contains(BiotechRegistry.GAS_MASK)) {
            this.getAttachedBiotech().add(BiotechRegistry.GAS_MASK);
        }
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


}
