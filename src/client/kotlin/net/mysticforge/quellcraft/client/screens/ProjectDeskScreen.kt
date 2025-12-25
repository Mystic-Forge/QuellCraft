package net.mysticforge.quellcraft.client.screens

import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.world.entity.player.Inventory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.screenhandler.ProjectDeskScreenHandler


class ProjectDeskScreen(handler: ProjectDeskScreenHandler, playerInventory: Inventory, title: Component) :
    AbstractContainerScreen<ProjectDeskScreenHandler>(handler, playerInventory, title) {
    val TEXTURE: ResourceLocation? = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "textures/gui/container/thaumic_assembler.png")

    private val backgroundWidth = 175
    private val backgroundHeight = 192

    override fun renderBg(context: GuiGraphics, delta: Float, mouseX: Int, mouseY: Int) {
        context.blit(
            RenderPipelines.GUI_TEXTURED,
            TEXTURE,
            leftPos,
            topPos,
            0f,
            0f,
            backgroundWidth, backgroundHeight,
            256,
            256
        )
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context, mouseX, mouseY, delta)
        super.render(context, mouseX, mouseY, delta)

//        val entity = handler.entity
//        if(entity != null) {
//            for(pos in entity.assemblerConfiguration)
//                context.fillGradient(
//                    x + 12 + pos.x * 18,
//                    y + 30 + pos.y * 18,
//                    x + 12 + pos.x * 18 + 16,
//                    y + 30 + pos.y * 18 + 16,
//                    0x80FF00FF.toInt(),
//                    0x80FF00FF.toInt()
//                )
//        }

        renderTooltip(context, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        titleLabelX = (backgroundWidth - font.width(title)) / 2
        inventoryLabelY = 100
    }
}