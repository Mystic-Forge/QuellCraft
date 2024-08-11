package net.mysticforge.quellcraft.util

import net.minecraft.entity.Entity
import net.minecraft.util.TypeFilter
import net.minecraft.util.math.Box
import net.minecraft.world.World

inline fun <reified T : Entity> World.getEntitiesOfType(box: Box): List<T> =
    getEntitiesByType(TypeFilter.instanceOf(T::class.java), box) { true }
