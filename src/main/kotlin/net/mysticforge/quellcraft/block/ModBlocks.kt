package net.mysticforge.quellcraft.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityType
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.command.argument.RegistryKeyArgumentType.registryKey
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity

object ModBlocks {
    val quellBlock = register(::QuellBlock, "quell")
    val crystalCluster = register(::CrystalBlock, "crystal_cluster")

    val crystalBlockEntityType: BlockEntityType<CrystalBlockEntity> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Identifier.of(Quellcraft.MOD_ID, "crystal_block_entity"),
        FabricBlockEntityTypeBuilder.create({ blockPos, blockState -> CrystalBlockEntity(blockPos, blockState) }, crystalCluster).build()
    )

    private fun register(blockFactory: (settings: AbstractBlock.Settings) -> Block, name: String, shouldRegisterItem: Boolean = true): Block {
        val id = Identifier.of(Quellcraft.MOD_ID, name)

        val settings = AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, id))
        val block = blockFactory(settings)

        if (shouldRegisterItem) {
            val blockItem = BlockItem(block, Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id)))
            Registry.register(Registries.ITEM, id, blockItem)
        }

        return Registry.register(Registries.BLOCK, id, block)
    }
}