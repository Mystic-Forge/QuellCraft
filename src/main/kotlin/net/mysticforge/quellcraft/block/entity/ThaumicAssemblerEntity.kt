package net.mysticforge.quellcraft.block.entity

import io.wispforest.owo.util.ImplementedInventory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.screenhandler.ThaumicAssemblerScreenHandler

class ThaumicAssemblerEntity(blockPos: BlockPos, blockState: BlockState)
    : BlockEntity(ModBlocks.thaumicAssemblerEntityType, blockPos, blockState), NamedScreenHandlerFactory, ImplementedInventory {
    private val inventory: DefaultedList<ItemStack?> = DefaultedList.ofSize<ItemStack?>(28, ItemStack.EMPTY)

    override fun getItems(): DefaultedList<ItemStack?> = inventory

    override fun markDirty() {}

    override fun getDisplayName(): Text = Text.translatable(getCachedState().getBlock().getTranslationKey())

    override fun createMenu(
        syncId: Int,
        playerInventory: PlayerInventory,
        player: PlayerEntity
    ): ScreenHandler = ThaumicAssemblerScreenHandler(syncId, playerInventory, this)
}