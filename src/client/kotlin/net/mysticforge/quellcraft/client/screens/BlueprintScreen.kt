package net.mysticforge.quellcraft.client.screens

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.item.Blueprint
import net.mysticforge.quellcraft.item.ModItems
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.minus
import org.joml.plus
import kotlin.random.Random

object BlueprintScreen :
    Screen(Component.nullToEmpty("Blueprint")) {
    val TEXTURE: ResourceLocation? = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "textures/gui/blueprint.png")

    private val backgroundSize = Vector2i(224, 224)
    private val cellSize = 28

    enum class CellDirection(val vector: Vector2i, val spriteOffset: Vector2i) {
        NONE(Vector2i(0, 0), Vector2i(0, 0)),
        UP(Vector2i(0, -1), Vector2i(28, 224)),
        UP_RIGHT(Vector2i(1, -1), Vector2i(56, 224)),
        RIGHT(Vector2i(1, 0), Vector2i(84, 224)),
        DOWN_RIGHT(Vector2i(1, 1), Vector2i(112, 224)),
        DOWN(Vector2i(0, 1), Vector2i(140, 224)),
        DOWN_LEFT(Vector2i(-1, 1), Vector2i(168, 224)),
        LEFT(Vector2i(-1, 0), Vector2i(196, 224)),
        UP_LEFT(Vector2i(-1, -1), Vector2i(224, 224));

        companion object {
            fun fromVector(vector: Vector2i): CellDirection {
                for (direction in entries) {
                    if (direction.vector == vector)
                        return direction
                }
                return NONE
            }
        }
    }

    sealed interface CellType {
        val spriteOffset: Vector2i
        val direction: CellDirection
        val takesInput: Boolean
        val cost: Int get() = 0

        data object Empty : CellType {
            override val spriteOffset = Vector2i(0, 0)
            override val direction = CellDirection.NONE
            override val takesInput = false
            override fun draw(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float, position: Vector2i) {}
            override fun withDirection(direction: CellDirection): CellType = Empty
        }

        data class Arm(override val direction: CellDirection = CellDirection.NONE) : CellType {
            override val spriteOffset = if (direction == CellDirection.NONE) Vector2i(0, 0) else Vector2i(0, 28)
            override val takesInput = true
            override val cost: Int = if(direction == CellDirection.NONE) 0 else 1
            override fun withDirection(direction: CellDirection): CellType = Arm(direction)
        }

        data object Output : CellType {
            override val spriteOffset = Vector2i(0, 56)
            override val direction = CellDirection.NONE
            override val takesInput = true

            override fun withDirection(direction: CellDirection): CellType = Output
        }

        data class Input(val itemStack: ItemStack, override val direction: CellDirection = CellDirection.NONE) : CellType {
            override val spriteOffset = Vector2i(0, 84)
            override val takesInput = false
            override val cost: Int = if(direction == CellDirection.NONE) 0 else 1

            override fun draw(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float, position: Vector2i) {
                super.draw(context, mouseX, mouseY, delta, position)
                context.renderItem(itemStack, position.x + 6, position.y + 6)
            }

            override fun withDirection(direction: CellDirection): CellType = Input(itemStack, direction)
        }

        data class Forge(override val direction: CellDirection = CellDirection.NONE) : CellType {
            override val spriteOffset = Vector2i(0, 112)
            override val takesInput = true
            override val cost: Int = if(direction == CellDirection.NONE) 0 else 2
            override fun withDirection(direction: CellDirection): CellType = Forge(direction)
        }

        data class Infuser(override val direction: CellDirection = CellDirection.NONE) : CellType {
            override val spriteOffset = Vector2i(0, 140)
            override val takesInput = true
            override val cost: Int = if(direction == CellDirection.NONE) 0 else 3
            override fun withDirection(direction: CellDirection): CellType = Infuser(direction)
        }

        fun draw(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float, position: Vector2i) {
            context.blit(
                RenderPipelines.GUI_TEXTURED,
                TEXTURE,
                position.x,
                position.y,
                spriteOffset.x.toFloat(),
                spriteOffset.y.toFloat(),
                28,
                28,
                256,
                256
            )
        }

        fun withDirection(direction: CellDirection): CellType
    }

    val cells: Array<Array<CellType>> = Array(5) { Array(5) { CellType.Empty } }

    private var clicking = false
    private var clickedCellIndex = Vector2i(-1, -1)

    init {
        Blueprint.onUseEvent = { world, player, hand ->
            // The single player client runs this code twice. (Untested if server runs this). Either way only the rendering client opens the screen.
            if (player is LocalPlayer) {
                randomizeLayout()
                Minecraft.getInstance().setScreen(BlueprintScreen)
            }
        }
    }

    private fun randomizeLayout() {
        val available = mutableListOf<Vector2i>()
        for (x in 0 until 5)
            for (y in 0 until 5) {
                available.add(Vector2i(x, y))
                cells[x][y] = CellType.Arm()
            }

        val outputIndex = Random.nextInt(available.size)
        val outputCell = available[outputIndex]
        cells[outputCell.x][outputCell.y] = CellType.Output
        available.removeAt(outputIndex)

        for(i in 0 until 2) {
            val forgeIndex = Random.nextInt(available.size)
            val forgeCell = available[forgeIndex]
            cells[forgeCell.x][forgeCell.y] = CellType.Forge(CellDirection.NONE)
            available.removeAt(forgeIndex)
        }


        val infuserIndex = Random.nextInt(available.size)
        val infuserCell = available[infuserIndex]
        cells[infuserCell.x][infuserCell.y] = CellType.Infuser(CellDirection.NONE)
        available.removeAt(infuserIndex)

        val items = listOf(Items.IRON_INGOT, Items.STICK, ModItems.quell_capacitor)
        for (i in items) {
            val inputIndex = Random.nextInt(available.size)
            val inputCell = available[inputIndex]
            cells[inputCell.x][inputCell.y] = CellType.Input(ItemStack(i))
            available.removeAt(inputIndex)
        }
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)
        val margin = Vector2i(context.guiWidth() / 2 - backgroundSize.x / 2, context.guiHeight() / 2 - backgroundSize.y / 2)
        context.blit(
            RenderPipelines.GUI_TEXTURED,
            TEXTURE,
            margin.x,
            margin.y,
            32f,
            0f,
            backgroundSize.x, backgroundSize.y,
            256,
            256
        )

        val gridOffset = Vector2i(backgroundSize.x / 2 - cellSize * 5 / 2, 16)

        var score = 0
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                val cellPos = Vector2i(margin.x + gridOffset.x + x * cellSize, margin.y + gridOffset.y + y * cellSize)
//                val highlighted = mouseX >= cellPos.x && mouseX < cellPos.x + cellSize && mouseY >= cellPos.y && mouseY < cellPos.y + cellSize
                val cell = cells[x][y]
                score += cell.cost
                cell.draw(context, mouseX, mouseY, delta, cellPos)
            }
        }

        for (x in 0 until 5) {
            for (y in 0 until 5) {
                val cellPos = Vector2i(margin.x + gridOffset.x + x * cellSize, margin.y + gridOffset.y + y * cellSize)
                val cell = cells[x][y]
                if (cell.direction == CellDirection.NONE) continue

                context.blit(
                    RenderPipelines.GUI_TEXTURED,
                    TEXTURE,
                    cellPos.x + cell.direction.vector.x * cellSize / 2,
                    cellPos.y + cell.direction.vector.y * cellSize / 2,
                    cell.direction.spriteOffset.x.toFloat(),
                    cell.direction.spriteOffset.y.toFloat(),
                    cellSize,
                    cellSize,
                    256,
                    256
                )
            }
        }

        context.drawString(
            font,
            "Blueprint",
            margin.x + backgroundSize.x / 2 - font.width("Blueprint") / 2,
            margin.y + 8,
            -0x1,
            false
        )

        context.drawString(
            font,
            "$score",
            margin.x + backgroundSize.x / 2 - font.width("$score") / 2,
            margin.y + backgroundSize.y - 16,
            -0x1,
            false
        )
    }

    private fun getCellIndexOfPos(xPos: Double, yPos: Double): Vector2i? {
        val margin = Vector2i(width / 2 - backgroundSize.x / 2, height / 2 - backgroundSize.y / 2)
        val gridOffset = margin + Vector2i(backgroundSize.x / 2 - cellSize * 5 / 2, 16)

        val mousePos = Vector2f(xPos.toFloat(), yPos.toFloat()) - Vector2f(gridOffset)
        val gridMousePos = mousePos / cellSize.toFloat()

        if (gridMousePos.x >= 0 && gridMousePos.x < 5 && gridMousePos.y >= 0 && gridMousePos.y < 5) {
            val cellMousePos = gridMousePos - gridMousePos.floor(Vector2f())
            val dist = (cellMousePos - Vector2f(0.5f, 0.5f)).lengthSquared()
            val maxDist = 0.4f
            if (dist <= maxDist * maxDist)
                return Vector2i(gridMousePos.x.toInt(), gridMousePos.y.toInt())
        }

        return null
    }


    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val cellIndex = getCellIndexOfPos(mouseX, mouseY)
        if (cellIndex != null) {
            clicking = true
            clickedCellIndex = cellIndex
            return true
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (clicking) {
            val newCellIndex = getCellIndexOfPos(mouseX, mouseY)
            if (newCellIndex != null && (newCellIndex.x != clickedCellIndex.x || newCellIndex.y != clickedCellIndex.y)) {
                val offset = Vector2i(newCellIndex.x - clickedCellIndex.x, newCellIndex.y - clickedCellIndex.y)
                var draggedDirection: CellDirection? = null
                for (direction in CellDirection.entries) {
                    if (direction.vector == offset) {
                        draggedDirection = direction
                        break
                    }
                }

                if (draggedDirection != null) {
                    tryConnect(clickedCellIndex, newCellIndex)
                    clickedCellIndex = newCellIndex
                }
            }
            return true
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    fun tryConnect(fromIndex: Vector2i, toIndex: Vector2i): Boolean {
        val fromCell = cells[fromIndex.x][fromIndex.y]
        val toCell = cells[toIndex.x][toIndex.y]
        val direction = CellDirection.fromVector(toIndex - fromIndex)

        if (fromCell.direction == direction) {
            cells[fromIndex.x][fromIndex.y] = fromCell.withDirection(CellDirection.NONE)
            return true
        }

        if (toIndex + toCell.direction.vector == fromIndex) {
            cells[toIndex.x][toIndex.y] = toCell.withDirection(CellDirection.NONE)
            return true
        }

        if (!toCell.takesInput) return false

        cells[fromIndex.x][fromIndex.y] = fromCell.withDirection(direction)
        return true
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        clicking = false
        clickedCellIndex = Vector2i(-1, -1)
        return super.mouseReleased(mouseX, mouseY, button)
    }
}