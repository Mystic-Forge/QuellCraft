package net.mysticforge.quellcraft.screenhandler

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.mysticforge.quellcraft.block.entity.ProjectDeskEntity
import net.mysticforge.quellcraft.item.ModItems
import net.mysticforge.quellcraft.util.FilteredSlot

class ProjectDeskScreenHandler : ScreenHandler {
    var inventory: Inventory

    constructor(syncId: Int, playerInventory: PlayerInventory) : this(syncId, playerInventory, SimpleInventory(ProjectDeskEntity.INVENTORY_SIZE))

    constructor(syncId: Int, playerInventory: PlayerInventory, inventory: Inventory) : super(ModScreenHandlers.projectDesk, syncId) {
        checkSize(inventory, ProjectDeskEntity.INVENTORY_SIZE)
        this.inventory = inventory
        inventory.onOpen(playerInventory.player)

        addSlot(FilteredSlot(inventory, 0, 12, 12, { stack -> stack.item == ModItems.blueprint }, 1))

        for (x in 1 until ProjectDeskEntity.INVENTORY_SIZE)
            addSlot(Slot(inventory, x, 8 + (x - 1) * 18, 81))

        addPlayerSlots(playerInventory, 8, 111)
    }

    override fun quickMove(player: PlayerEntity, slotIndex: Int): ItemStack {
        val slot = this.slots.get(slotIndex)

        val movedStack = if (slot.hasStack()) {
            val slotStack = slot.getStack()
            val stackCopy = slotStack.copy()

            if (slotIndex < ProjectDeskEntity.INVENTORY_SIZE) {
                if (!this.insertItem(slotStack, ProjectDeskEntity.INVENTORY_SIZE, this.slots.size, true))
                    ItemStack.EMPTY
            } else if (!this.insertItem(slotStack, 0, ProjectDeskEntity.INVENTORY_SIZE, false))
                ItemStack.EMPTY

            if (slotStack.isEmpty()) slot.setStack(ItemStack.EMPTY)
            else slot.markDirty()
            stackCopy
        } else ItemStack.EMPTY

        return movedStack
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return true
    }
}