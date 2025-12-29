package net.mysticforge.quellcraft.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity
import net.mysticforge.quellcraft.block.entity.GravityExtractorEntity
import net.mysticforge.quellcraft.block.entity.ProjectDeskEntity

object ModBlocks {
    val quellBlock = register(::QuellBlock, "quell")
    val crystalCluster = register(::CrystalBlock, "crystal_cluster")
    val thaumicAssembler = register(::ThaumicAssemblerBlock, "thaumic_assembler")
    val projectDesk = register(::ProjectDeskBlock, "project_desk")
    val gravityExtractor = register(::GravityExtractorBlock, "gravity_extractor")
    val gravityExtractorProxy = register(::GravityExtractorProxyBlock, "gravity_extractor_proxy", registerItem = false)

    val crystalBlockEntityType = registerBlockEntity("crystal_block_entity", ::CrystalBlockEntity, crystalCluster)
    val projectDeskEntityType = registerBlockEntity("project_desk_entity", ::ProjectDeskEntity, projectDesk)
    val gravityExtractorEntityType = registerBlockEntity("gravity_extractor_entity", ::GravityExtractorEntity, gravityExtractor)

    private fun <T : Block> register(blockFactory: (settings: BlockBehaviour.Properties) -> T, name: String, registerItem: Boolean = true): T {
        val id = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, name)

        val settings = BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))
        val block = blockFactory(settings)

        if (registerItem) {
            val blockItem = BlockItem(block, Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)))
            Registry.register(BuiltInRegistries.ITEM, id, blockItem)
        }

        return Registry.register(BuiltInRegistries.BLOCK, id, block)
    }

    private fun <T : BlockEntity> registerBlockEntity(name: String, entityFactory: (blockPos: BlockPos, blockState: BlockState) -> T, vararg blocks: Block): BlockEntityType<T> {
        return Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, name),
            FabricBlockEntityTypeBuilder.create(entityFactory, *blocks).build()
        )
    }
}