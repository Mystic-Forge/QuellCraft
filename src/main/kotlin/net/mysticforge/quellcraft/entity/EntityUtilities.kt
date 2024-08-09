package net.mysticforge.quellcraft.entity

import net.minecraft.entity.Entity
import net.minecraft.util.TypeFilter
import net.minecraft.util.math.Box
import net.minecraft.world.World

object EntityUtilities {
    fun getEntities(world: World, box: Box): Iterable<Entity> {
        return world.getOtherEntities(null, box).asIterable()
    }

    inline fun<reified T: Entity> getEntitiesOfType(world: World, box: Box): Iterable<T> {
        return world.getEntitiesByType(TypeFilter.instanceOf(T::class.java), box) { true }.asIterable()
    }
}