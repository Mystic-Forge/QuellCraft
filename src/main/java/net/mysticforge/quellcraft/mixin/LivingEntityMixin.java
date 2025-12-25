package net.mysticforge.quellcraft.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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
        method = "onEffectUpdated",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V"
        )
    )
    private void redirectOnRemoved1(
            MobEffect instance,
            AttributeMap attributeContainer,
            MobEffectInstance effect,
            boolean reapplyEffect,
            @Nullable Entity source
    ) {
        if (instance instanceof DistortedEffect distortedEffect) {
            distortedEffect.onRemovedForEntity((LivingEntity) (Object) this, effect.getAmplifier());
        } else {
            instance.removeAttributeModifiers(attributeContainer);
        }
    }

    @Redirect(
        method = "onEffectsRemoved",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V"
        )
    )
    private void redirectOnRemoved2(MobEffect instance, AttributeMap attributeContainer, Collection<MobEffectInstance> effects) {
        if (instance instanceof DistortedEffect distortedEffect) {
            @SuppressWarnings("OptionalGetWithoutIsPresent") 
            final var effectInstance = effects
                .stream()
                .filter(e -> e.getEffect() == ModStatusEffects.getDistortedEffect())
                .findFirst()
                .get();

            distortedEffect.onRemovedForEntity((LivingEntity) (Object) this, effectInstance.getAmplifier());
        } else {
            instance.removeAttributeModifiers(attributeContainer);
        }
    }
}
