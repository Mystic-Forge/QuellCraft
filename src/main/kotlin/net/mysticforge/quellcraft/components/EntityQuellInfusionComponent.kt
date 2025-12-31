package net.mysticforge.quellcraft.components

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.mysticforge.quellcraft.ModStatusEffects
import net.mysticforge.quellcraft.QuellcraftConfig
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent

class EntityQuellInfusionComponent(private val entity: LivingEntity) :
    ActuallyAutoSyncedComponent(entity, ModComponents.quellInfusion),
    AutoSyncedComponent,
    ServerTickingComponent
{
    private var infusionAmount by autoSyncedOptionalValue<Int>("infusion")

    fun getValue() = infusionAmount

    fun setValue(value: Int) {
        infusionAmount = OptionalValue.Set(value)

        val targetAmplifier = (value / 100).coerceAtMost(5)

        var statusEffectInstance = entity.getEffect(ModStatusEffects.distortedEffect)
        if (statusEffectInstance == null && targetAmplifier > 0) {
            statusEffectInstance = MobEffectInstance(ModStatusEffects.distortedEffect, -1, targetAmplifier - 1, false, false, true)
            entity.addEffect(statusEffectInstance)
        } else if (statusEffectInstance != null && targetAmplifier == 0) {
            entity.removeEffect(ModStatusEffects.distortedEffect)
        } else if (statusEffectInstance != null && statusEffectInstance.amplifier != targetAmplifier - 1) {
            entity.forceAddEffect(
                MobEffectInstance(ModStatusEffects.distortedEffect, -1, targetAmplifier - 1, false, false, true),
                null
            )
        }
    }

    fun addValue(value: Int) {
        setValue(infusionAmount.getOrDefault(0) + value)
    }

    override fun serverTick() {
        if (infusionAmount !is OptionalValue.Unset && entity.random.nextFloat() < QuellcraftConfig.quellInfusionDecay) {
            addValue(-1)
        }
    }
}
