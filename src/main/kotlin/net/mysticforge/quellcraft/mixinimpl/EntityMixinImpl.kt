package net.mysticforge.quellcraft.mixinimpl

import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.world.BlockView
import net.mysticforge.quellcraft.item.TurboTreadsItem

object EntityMixinImpl {
    @JvmStatic
    fun Entity.onEntityLand() : Boolean {
        if(this !is LivingEntity) return false
        if(!this.hasStackEquipped(EquipmentSlot.FEET)) return false
        val boots = this.getEquippedStack(EquipmentSlot.FEET)
        val bootsItem = boots.item
        if(bootsItem !is TurboTreadsItem) return false
        return TurboTreadsItem.tryActivateTurboTreads(boots, this)
    }
}