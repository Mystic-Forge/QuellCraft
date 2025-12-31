package net.mysticforge.quellcraft.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.*
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.BiConsumer

/**
 * Base class for `BigBlock`s that use `QuellCraftProxyBlock`s to occupy multiple positions.
 */
abstract class QuellCraftBigBlock(settings: Properties) : BaseEntityBlock(settings) {
    abstract val proxyOffsets: List<Vec3i>
        get

    abstract val shape: VoxelShape
        get

    abstract fun getProxyBlock(): QuellcraftProxyBlock

    /** Remove all proxy blocks associated to this BigBlock. This does not "destroy" them, it just silently removes them. */
    fun removeProxies(levelAccess: LevelAccessor, blockPos: BlockPos) {
        for (offset in proxyOffsets) {
            if (offset == Vec3i.ZERO) continue
            val pos = blockPos.offset(offset)
            val state = levelAccess.getBlockState(pos)
            if (state.block is QuellcraftProxyBlock) levelAccess.removeBlock(pos, true)
        }
    }

    // Check that all proxy positions can be replaced
    override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState? {
        val level = blockPlaceContext.level
        val pos = blockPlaceContext.clickedPos
        for (offset in proxyOffsets) {
            val blockPos = pos.offset(offset)
            if (!level.getBlockState(blockPos).canBeReplaced()) return null
        }
        return super.getStateForPlacement(blockPlaceContext)
    }

    // Also place proxy blocks. This also works with the `/setblock` command
    override fun onPlace(blockState: BlockState, level: Level, blockPos: BlockPos, blockState2: BlockState, bl: Boolean) {
        for (offset in proxyOffsets) {
            if (offset == Vec3i.ZERO) continue
            val state = getProxyBlock().withOffsetToSource(offset)
            level.setBlock(blockPos.offset(offset), state, UPDATE_NEIGHBORS or UPDATE_CLIENTS)
        }
    }

    override fun getShape(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos, collisionContext: CollisionContext): VoxelShape = shape

    // Called by proxies to handle pick block
    fun getCloneItemStackWrapper(levelReader: LevelReader, blockPos: BlockPos, blockState: BlockState, bl: Boolean): ItemStack? {
        return getCloneItemStack(levelReader, blockPos, blockState, bl)
    }

    // Handle all destruction cases
    fun explosionHitWrapper(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, explosion: Explosion, biConsumer: BiConsumer<ItemStack?, BlockPos?>) {
        onExplosionHit(blockState, serverLevel, blockPos, explosion, biConsumer)
    }

    override fun wasExploded(serverLevel: ServerLevel, blockPos: BlockPos, explosion: Explosion) {
        removeProxies(serverLevel, blockPos)
        super.wasExploded(serverLevel, blockPos, explosion)
    }

    override fun destroy(levelAccessor: LevelAccessor, blockPos: BlockPos, blockState: BlockState) {
        removeProxies(levelAccessor, blockPos)
        super.destroy(levelAccessor, blockPos, blockState)
    }
}