package net.mysticforge.quellcraft.components

import dev.onyxstudios.cca.api.v3.component.ComponentV3
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.nbt.NbtCompound
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

        var statusEffectInstance = entity.getStatusEffect(ModStatusEffects.distortedEffect)
        if (statusEffectInstance == null && targetAmplifier > 0) {
            statusEffectInstance = StatusEffectInstance(ModStatusEffects.distortedEffect, -1, targetAmplifier - 1, false, false, true)
            entity.addStatusEffect(statusEffectInstance)
        } else if (statusEffectInstance != null && targetAmplifier == 0) {
            entity.removeStatusEffect(ModStatusEffects.distortedEffect)
        } else if (statusEffectInstance != null && statusEffectInstance.amplifier != targetAmplifier - 1) {
            entity.setStatusEffect(
                StatusEffectInstance(ModStatusEffects.distortedEffect, -1, targetAmplifier - 1, false, false, true),
                null
            )
        }
    }

    fun addValue(value: Int) {
        setValue(infusionAmount + value)
    }

    override fun readFromNbt(tag: NbtCompound) {
        infusionAmount = tag.getInt(KEY)
    }

    override fun writeToNbt(tag: NbtCompound) {
        tag.putInt(KEY, infusionAmount)
    }

    override fun serverTick() {
        if (infusionAmount > 0 && entity.random.nextFloat() < QuellcraftConfig.quellInfusionDecay) setValue(infusionAmount - 1)
    }
}
