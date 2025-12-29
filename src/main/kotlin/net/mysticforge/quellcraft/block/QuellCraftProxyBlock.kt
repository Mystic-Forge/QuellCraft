package net.mysticforge.quellcraft.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.*
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.BiConsumer

/**
 * Base class for proxy blocks used by QuellcraftBigBlock to occupy multiple block spaces.
 */
abstract class QuellcraftProxyBlock(settings: Properties) : Block(settings.noOcclusion()) {
    abstract val sourceBlock: QuellcraftBigBlock
        get

    abstract val offsetProperty: IntegerProperty
        get

    val shapes: List<VoxelShape> = sourceBlock.proxyOffsets.map { offset -> sourceBlock.shape.move(offset.multiply(-1)) }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(offsetProperty)
    }

    // Handle pick block
    override fun getCloneItemStack(levelReader: LevelReader, blockPos: BlockPos, blockState: BlockState, bl: Boolean): ItemStack? {
        val offset = getOriginOffset(blockState)
        val originPos = blockPos.offset(offset)
        val originState = levelReader.getBlockState(originPos)
        return sourceBlock.getCloneItemStackWrapper(levelReader, originPos, originState, bl)
    }

    override fun getShape(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos, collisionContext: CollisionContext): VoxelShape =
        shapes[blockState.getValue(offsetProperty)]

    fun getOriginOffset(state: BlockState): Vec3i {
        val index = state.getValue(offsetProperty)
        return sourceBlock.proxyOffsets[index].multiply(-1)
    }

    override fun onExplosionHit(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, explosion: Explosion, biConsumer: BiConsumer<ItemStack?, BlockPos?>) {
        val offset = getOriginOffset(blockState)
        val originPos = blockPos.offset(offset)
        val originState = serverLevel.getBlockState(originPos)
        sourceBlock.explosionHitWrapper(originState, serverLevel, originPos, explosion, biConsumer)
    }

    override fun playerWillDestroy(level: Level, blockPos: BlockPos, blockState: BlockState, player: Player): BlockState? {
        return blockState // Removes break effects and sounds
    }

    override fun destroy(levelAccessor: LevelAccessor, blockPos: BlockPos, blockState: BlockState) {
        if(levelAccessor.isClientSide) return

        val offset = getOriginOffset(blockState)
        val originPos = blockPos.offset(offset)
        val originState = levelAccessor.getBlockState(originPos)
        if (originState.block is QuellcraftBigBlock) {
            sourceBlock.removeProxies(levelAccessor, originPos)
            levelAccessor.destroyBlock(originPos, true)
        }
    }

    fun withOriginOffset(offset: Vec3i): BlockState {
        val index = sourceBlock.proxyOffsets.indexOf(offset)
        return defaultBlockState().setValue(offsetProperty, index)
    }
}