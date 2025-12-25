package net.mysticforge.quellcraft

import net.minecraft.world.effect.MobEffect
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.mysticforge.quellcraft.entity.effect.DistortedEffect

object ModStatusEffects {
    @JvmStatic
    val distortedEffect = register(DistortedEffect(), "distorted")

    private fun register(effect: MobEffect, id: String) = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, id), effect)
}