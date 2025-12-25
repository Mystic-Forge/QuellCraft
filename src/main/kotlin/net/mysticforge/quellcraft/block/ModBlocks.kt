package net.mysticforge.quellcraft.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.core.BlockPos
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

    private fun register(blockFactory: (settings: BlockBehaviour.Properties) -> Block, name: String, shouldRegisterItem: Boolean = true): Block {
        val id = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, name)

        val settings = BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))
        val block = blockFactory(settings)

        if (shouldRegisterItem) {
            val blockItem = BlockItem(block, Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)))
            Registry.register(BuiltInRegistries.ITEM, id, blockItem)
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, block)
    }

    private fun <T : BlockEntity>registerBlockEntity(name: String, entityFactory: (blockPos: BlockPos, blockState: BlockState) -> T, vararg blocks: Block) : BlockEntityType<T> {
        return Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, name),
            FabricBlockEntityTypeBuilder.create(entityFactory, *blocks).build()
        )
    }
}