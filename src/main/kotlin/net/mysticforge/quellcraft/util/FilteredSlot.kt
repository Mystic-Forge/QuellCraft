package net.mysticforge.quellcraft.util

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.inventory.Slot

class FilteredSlot(inventory: Container, index: Int, x: Int, y: Int, val predicate: (item: ItemStack) -> Boolean, val stackSize: Int = 64) : Slot(inventory, index, x, y) {
    override fun mayPlace(stack: ItemStack): Boolean = predicate(stack)

    override fun getMaxStackSize(stack: ItemStack): Int = stackSize
}