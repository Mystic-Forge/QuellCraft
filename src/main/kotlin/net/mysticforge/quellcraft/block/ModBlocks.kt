package net.mysticforge.quellcraft.block

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity


object ModBlocks {
    val crystalCluster = register(CrystalBlock, "crystal_cluster")

    val crystalBlockEntityType: BlockEntityType<CrystalBlockEntity> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Identifier(Quellcraft.MOD_ID, "crystal_block_entity"),
        BlockEntityType.Builder.create({ blockPos, blockState -> CrystalBlockEntity(blockPos, blockState) }, crystalCluster).build(null)
    )

    private fun register(block: Block, name: String, shouldRegisterItem: Boolean = true): Block {
        val id = Identifier.of(Quellcraft.MOD_ID, name)

        if (shouldRegisterItem) {
            val blockItem = BlockItem(block, Item.Settings())
            Registry.register(Registries.ITEM, id, blockItem)
        }

        return Registry.register(Registries.BLOCK, id, block)
    }
}