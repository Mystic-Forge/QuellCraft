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
import net.minecraft.util.BlockRotation
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.screenhandler.ProjectDeskScreenHandler
import org.joml.Vector2i

class ProjectDeskEntity(blockPos: BlockPos, blockState: BlockState) : BlockEntity(ModBlocks.projectDeskEntityType, blockPos, blockState), NamedScreenHandlerFactory,
    ImplementedInventory {
    companion object {
        const val INVENTORY_SIZE = 10
    }

    private val inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY)
    var assemblerConfiguration: HashSet<Vector2i> = HashSet()
        private set

    private val forwardDirection = blockState.get(net.minecraft.state.property.Properties.HORIZONTAL_FACING)
    private val rightDirection = forwardDirection.rotateYClockwise()

    override fun onOpen(player: PlayerEntity?) {
        Quellcraft.LOGGER.info("Project Desk at $pos opened by ${player?.name?.string}")
        updateAssemblerConfiguration()
    }

    fun updateAssemblerConfiguration() {
        val open = HashSet<BlockPos>()
        val positions = HashSet<BlockPos>()
        open.add(pos)

        while (open.isNotEmpty()) {
            val currentPos = open.first()
            open.remove(currentPos)

            for (direction in Direction.Type.HORIZONTAL) {
                val neighborPos = currentPos.offset(direction)
                val neighborState = world!!.getBlockState(neighborPos)
                if (neighborState != null && neighborState.block == ModBlocks.thaumicAssembler) {
                    if (!positions.contains(neighborPos)) {
                        positions.add(neighborPos)
                        open.add(neighborPos)
                    }
                }
            }
        }

        assemblerConfiguration.clear()
        for (assemblerPos in positions) {
            val localPos = blockPosToLocal(assemblerPos)
            assemblerConfiguration.add(localPos)
        }

        Quellcraft.LOGGER.info("Updated assembler configuration: ${assemblerConfiguration.size}")
    }

    fun localToBlockPos(localPos: Vector2i): BlockPos {
        return BlockPos(pos).add(forwardDirection.vector.multiply(localPos.y)).add(rightDirection.vector.multiply(localPos.x))
    }

    fun blockPosToLocal(blockPos: BlockPos): Vector2i {
        val relativePos = blockPos.subtract(pos)
        val rotatedPos = relativePos.rotate(
            when (forwardDirection) {
                Direction.NORTH -> BlockRotation.NONE
                Direction.EAST -> BlockRotation.CLOCKWISE_90
                Direction.SOUTH -> BlockRotation.CLOCKWISE_180
                Direction.WEST -> BlockRotation.COUNTERCLOCKWISE_90
                else -> BlockRotation.NONE
            }
        )
        return Vector2i(rotatedPos.x, rotatedPos.z)
    }

    override fun getItems(): DefaultedList<ItemStack> = inventory

    override fun markDirty() {}

    override fun getDisplayName(): Text = Text.translatable(getCachedState().getBlock().getTranslationKey())

    override fun createMenu(
        syncId: Int,
        playerInventory: PlayerInventory,
        player: PlayerEntity
    ): ScreenHandler = ProjectDeskScreenHandler(syncId, playerInventory, this)
}