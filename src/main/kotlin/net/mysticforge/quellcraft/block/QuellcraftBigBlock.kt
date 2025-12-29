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
 * Base class for "big blocks" that use proxy blocks to occupy multiple block spaces.
 */
abstract class QuellcraftBigBlock(settings: Properties) : BaseEntityBlock(settings) {
    abstract val proxyOffsets: List<Vec3i>
        get

    abstract val shape: VoxelShape
        get

    abstract fun getProxyBlock(): QuellcraftProxyBlock

    override fun getShape(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos, collisionContext: CollisionContext): VoxelShape = shape

    override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState? {
        val level = blockPlaceContext.level
        val pos = blockPlaceContext.clickedPos
        for (offset in proxyOffsets) {
            val blockPos = pos.offset(offset)
            if (!level.getBlockState(blockPos).canBeReplaced()) return null
        }
        return super.getStateForPlacement(blockPlaceContext)
    }

    override fun onPlace(blockState: BlockState, level: Level, blockPos: BlockPos, blockState2: BlockState, bl: Boolean) {
        for (offset in proxyOffsets) {
            if (offset == Vec3i.ZERO) continue
            val state = getProxyBlock().withOriginOffset(offset)
            level.setBlock(blockPos.offset(offset), state, UPDATE_NEIGHBORS or UPDATE_CLIENTS)
        }
    }

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

    fun removeProxies(levelAccess: LevelAccessor, blockPos: BlockPos) {
        for (offset in proxyOffsets) {
            if (offset == Vec3i.ZERO) continue
            val pos = blockPos.offset(offset)
            val state = levelAccess.getBlockState(pos)
            if (state.block is QuellcraftProxyBlock) levelAccess.removeBlock(pos, true)
        }
    }

    fun getCloneItemStackWrapper(levelReader: LevelReader, blockPos: BlockPos, blockState: BlockState, bl: Boolean): ItemStack? {
        return getCloneItemStack(levelReader, blockPos, blockState, bl)
    }
}