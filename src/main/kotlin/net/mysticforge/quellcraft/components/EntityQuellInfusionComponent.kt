package net.mysticforge.quellcraft.components

import org.ladysnake.cca.api.v3.component.ComponentV3
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.mysticforge.quellcraft.ModStatusEffects
import net.mysticforge.quellcraft.QuellcraftConfig

interface IntComponent : ComponentV3 {
    fun getValue(): Int
}

class EntityQuellInfusionComponent(private val entity: LivingEntity) : IntComponent, AutoSyncedComponent, ServerTickingComponent {
    companion object {
        const val KEY = "quell_infusion"
    }

    private var infusionAmount = 0

    override fun getValue() = infusionAmount

    fun setValue(value: Int) {
        infusionAmount = value
        ModComponents.quellInfusion.sync(entity)

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
        setValue(infusionAmount + value)
    }

    override fun serverTick() {
        if (infusionAmount > 0 && entity.random.nextFloat() < QuellcraftConfig.quellInfusionDecay) setValue(infusionAmount - 1)
    }

    override fun readData(p0: ValueInput) {
        infusionAmount = p0.getIntOr(KEY, 0)
    }

    override fun writeData(p0: ValueOutput) {
        p0.putInt(KEY, infusionAmount)
    }
}
