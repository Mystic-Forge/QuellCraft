package net.mysticforge.quellcraft.util

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class FilteredSlot(inventory: Inventory, index: Int, x: Int, y: Int, val predicate: (item: ItemStack) -> Boolean, val stackSize: Int = 64) : Slot(inventory, index, x, y) {
    override fun canInsert(stack: ItemStack?): Boolean = stack?.let(predicate) ?: false

    override fun getMaxItemCount(stack: ItemStack?): Int = stackSize
}