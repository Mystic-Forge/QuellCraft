package net.mysticforge.quellcraft.screenhandler

import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.mysticforge.quellcraft.block.entity.ProjectDeskEntity
import net.mysticforge.quellcraft.item.ModItems
import net.mysticforge.quellcraft.util.FilteredSlot

class ProjectDeskScreenHandler : AbstractContainerMenu {
    var inventory: Container

    constructor(syncId: Int, playerInventory: Inventory) : this(syncId, playerInventory, SimpleContainer(ProjectDeskEntity.INVENTORY_SIZE))

    constructor(syncId: Int, playerInventory: Inventory, inventory: Container) : super(ModScreenHandlers.projectDesk, syncId) {
        checkContainerSize(inventory, ProjectDeskEntity.INVENTORY_SIZE)
        this.inventory = inventory
        inventory.startOpen(playerInventory.player)

        addSlot(FilteredSlot(inventory, 0, 12, 12, { stack -> stack.item == ModItems.blueprint }, 1))

        for (x in 1 until ProjectDeskEntity.INVENTORY_SIZE)
            addSlot(Slot(inventory, x, 8 + (x - 1) * 18, 81))

        addStandardInventorySlots(playerInventory, 8, 111)
    }

    override fun quickMoveStack(player: Player, slotIndex: Int): ItemStack {
        val slot = this.slots.get(slotIndex)

        val movedStack = if (slot.hasItem()) {
            val slotStack = slot.item
            val stackCopy = slotStack.copy()

            if (slotIndex < ProjectDeskEntity.INVENTORY_SIZE) {
                if (!this.moveItemStackTo(slotStack, ProjectDeskEntity.INVENTORY_SIZE, this.slots.size, true))
                    ItemStack.EMPTY
            } else if (!this.moveItemStackTo(slotStack, 0, ProjectDeskEntity.INVENTORY_SIZE, false))
                ItemStack.EMPTY

            if (slotStack.isEmpty) slot.setByPlayer(ItemStack.EMPTY)
            else slot.setChanged()
            stackCopy
        } else ItemStack.EMPTY

        return movedStack
    }

    override fun stillValid(player: Player): Boolean {
        return true
    }
}