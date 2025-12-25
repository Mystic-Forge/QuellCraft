package net.mysticforge.quellcraft.util

import net.minecraft.world.entity.Entity
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.AABB
import net.minecraft.world.level.Level

inline fun <reified T : Entity> Level.getEntitiesOfType(box: AABB): List<T> =
    getEntities(EntityTypeTest.forClass(T::class.java), box) { true }
