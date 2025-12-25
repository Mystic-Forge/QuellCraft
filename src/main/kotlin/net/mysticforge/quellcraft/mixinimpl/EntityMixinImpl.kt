package net.mysticforge.quellcraft.mixinimpl

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.mysticforge.quellcraft.item.equipment.TurboTreadsItem

object EntityMixinImpl {
    @JvmStatic
    fun Entity.onEntityLand() : Boolean {
        if(this !is LivingEntity) return false
        if(!this.hasItemInSlot(EquipmentSlot.FEET)) return false
        val boots = this.getItemBySlot(EquipmentSlot.FEET)
        val bootsItem = boots.item
        if(bootsItem !is TurboTreadsItem) return false
        return TurboTreadsItem.tryActivateTurboTreads(boots, this)
    }
}