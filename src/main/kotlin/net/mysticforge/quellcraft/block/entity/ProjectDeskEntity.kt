package net.mysticforge.quellcraft.block.entity

import io.wispforest.owo.util.ImplementedInventory
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.level.block.Rotation
import net.minecraft.core.NonNullList
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.screenhandler.ProjectDeskScreenHandler
import org.joml.Vector2i

class ProjectDeskEntity(blockPos: BlockPos, blockState: BlockState) : BlockEntity(ModBlocks.projectDeskEntityType, blockPos, blockState), MenuProvider,
    ImplementedInventory {
    companion object {
        const val INVENTORY_SIZE = 10
    }

    private val inventory: NonNullList<ItemStack> = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY)
    var assemblerConfiguration: HashSet<Vector2i> = HashSet()
        private set

    private val forwardDirection = blockState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING)
    private val rightDirection = forwardDirection.clockWise

    override fun startOpen(player: Player?) {
        Quellcraft.LOGGER.info("Project Desk at $worldPosition opened by ${player?.name?.string}")
        updateAssemblerConfiguration()
    }

    fun updateAssemblerConfiguration() {
        val open = HashSet<BlockPos>()
        val positions = HashSet<BlockPos>()
        open.add(worldPosition)

        while (open.isNotEmpty()) {
            val currentPos = open.first()
            open.remove(currentPos)

            for (direction in Direction.Plane.HORIZONTAL) {
                val neighborPos = currentPos.relative(direction)
                val neighborState = level!!.getBlockState(neighborPos)
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
        return BlockPos(worldPosition).offset(forwardDirection.unitVec3i.multiply(localPos.y)).offset(rightDirection.unitVec3i.multiply(localPos.x))
    }

    fun blockPosToLocal(blockPos: BlockPos): Vector2i {
        val relativePos = blockPos.subtract(worldPosition)
        val rotatedPos = relativePos.rotate(
            when (forwardDirection) {
                Direction.NORTH -> Rotation.NONE
                Direction.EAST -> Rotation.CLOCKWISE_90
                Direction.SOUTH -> Rotation.CLOCKWISE_180
                Direction.WEST -> Rotation.COUNTERCLOCKWISE_90
                else -> Rotation.NONE
            }
        )
        return Vector2i(rotatedPos.x, rotatedPos.z)
    }

    override fun getItems(): NonNullList<ItemStack> = inventory

    override fun setChanged() {}

    override fun getDisplayName(): Component = Component.translatable(blockState.getBlock().descriptionId)

    override fun createMenu(
        syncId: Int,
        playerInventory: Inventory,
        player: Player
    ): AbstractContainerMenu = ProjectDeskScreenHandler(syncId, playerInventory, this)
}