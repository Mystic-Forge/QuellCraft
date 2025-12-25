package net.mysticforge.quellcraft.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity
import net.mysticforge.quellcraft.block.entity.ProjectDeskEntity

object ModBlocks {
    val thaumicAssembler = register(::ThaumicAssemblerBlock, "thaumic_assembler")
    val projectDesk = register(::ProjectDeskBlock, "project_desk")
    val quellBlock = register(::QuellBlock, "quell")
    val crystalCluster = register(::CrystalBlock, "crystal_cluster")

    val crystalBlockEntityType = registerBlockEntity("crystal_block_entity", ::CrystalBlockEntity, crystalCluster)
    val projectDeskEntityType = registerBlockEntity("project_desk_entity", ::ProjectDeskEntity, projectDesk)

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

    private fun <T : BlockEntity>registerBlockEntity(name: String, entityFactory: (blockPos: BlockPos, blockState: BlockState) -> T, vararg blocks: Block) : BlockEntityType<T> {
        return Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Quellcraft.MOD_ID, name),
            FabricBlockEntityTypeBuilder.create(entityFactory, *blocks).build()
        )
    }
}