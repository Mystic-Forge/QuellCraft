package net.mysticforge.quellcraft.mixinimpl

import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.world.BlockView
import net.mysticforge.quellcraft.item.TurboTreadsItem

object EntityMixinImpl {
    @JvmStatic
    fun Entity.move(instance: Block, world: BlockView, entity: Entity) {
        val turboTreadsStack = armorItems.firstOrNull { it.item is TurboTreadsItem }
        if (turboTreadsStack == null || !TurboTreadsItem.tryActivateTurboTreads(turboTreadsStack, this))
            instance.onEntityLand(world, entity)
    }
}