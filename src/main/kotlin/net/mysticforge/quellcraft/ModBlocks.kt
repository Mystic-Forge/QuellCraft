package net.mysticforge.quellcraft

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.blocks.CrystalBlock


object ModBlocks {
    val crystal = register(CrystalBlock, "crystal")

    private fun register(block: Block, name: String, shouldRegisterItem: Boolean = true): Block {
        val id = Identifier.of(Quellcraft.MOD_ID, name)

        if (shouldRegisterItem) {
            val blockItem = BlockItem(block, Item.Settings())
            Registry.register(Registries.ITEM, id, blockItem)
        }

        return Registry.register(Registries.BLOCK, id, block)
    }
}