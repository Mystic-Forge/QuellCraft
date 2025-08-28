package net.mysticforge.quellcraft.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.mysticforge.quellcraft.ModStatusEffects;
import net.mysticforge.quellcraft.entity.effect.DistortedEffect;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Redirect(
        method = "onStatusEffectUpgraded",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"
        )
    )
    private void redirectOnRemoved1(
        StatusEffect instance,
        AttributeContainer attributeContainer,
        StatusEffectInstance effect,
        boolean reapplyEffect,
        @Nullable Entity source
    ) {
        if (instance instanceof DistortedEffect distortedEffect) {
            distortedEffect.onRemovedForEntity((LivingEntity) (Object) this, effect.getAmplifier());
        } else {
            instance.onRemoved(attributeContainer);
        }
    }

    @Redirect(
        method = "onStatusEffectsRemoved",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"
        )
    )
    private void redirectOnRemoved2(StatusEffect instance, AttributeContainer attributeContainer, Collection<StatusEffectInstance> effects) {
        if (instance instanceof DistortedEffect distortedEffect) {
            @SuppressWarnings("OptionalGetWithoutIsPresent") 
            final var effectInstance = effects
                .stream()
                .filter(e -> e.getEffectType() == ModStatusEffects.getDistortedEffect())
                .findFirst()
                .get();

            distortedEffect.onRemovedForEntity((LivingEntity) (Object) this, effectInstance.getAmplifier());
        } else {
            instance.onRemoved(attributeContainer);
        }
    }
}
