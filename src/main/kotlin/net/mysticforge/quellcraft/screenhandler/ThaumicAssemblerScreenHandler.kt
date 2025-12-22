package net.mysticforge.quellcraft.screenhandler

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.mysticforge.quellcraft.item.ModItems
import net.mysticforge.quellcraft.util.FilteredSlot

class ThaumicAssemblerScreenHandler : ScreenHandler {
    var inventory: Inventory

    constructor(syncId: Int, playerInventory: PlayerInventory) : this(syncId, playerInventory, SimpleInventory(28))

    constructor(syncId: Int, playerInventory: PlayerInventory, inventory: Inventory) : super(ModScreenHandlers.THAUMIC_ASSEMBLER_SCREEN_HANDLER_TYPE, syncId) {
        checkSize(inventory, 28)
        this.inventory = inventory
        inventory.onOpen(playerInventory.player)

        this.addSlot(FilteredSlot(inventory, 0, 80, 18, { item -> item.item == ModItems.blueprint }, 1))

        for (y in 0 until 3) {
            for (x in 0 until 9) {
                this.addSlot(Slot(inventory, x + y * 9 + 1, 8 + x * 18, 45 + y * 18))
            }
        }

        addPlayerSlots(playerInventory, 8, 111)
    }

    override fun quickMove(player: PlayerEntity, inventorySlot: Int): ItemStack {
        var newStack = ItemStack.EMPTY
        val slot = this.slots.get(inventorySlot)
        if (slot.hasStack()) {
            val originalStack = slot.getStack()
            newStack = originalStack.copy()
            if (inventorySlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY
            }

            if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY) else slot.markDirty()
        }

        return newStack!!
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return true
    }
}