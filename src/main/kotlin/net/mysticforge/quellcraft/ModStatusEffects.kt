package net.mysticforge.quellcraft

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.entity.effect.DistortedEffect

object ModStatusEffects {
    val distortedEffect: StatusEffect = register(DistortedEffect(), "distorted")

    private fun register(effect: StatusEffect, id: String): StatusEffect = Registry.register(Registries.STATUS_EFFECT, Identifier.of(Quellcraft.MOD_ID, id), effect)
}