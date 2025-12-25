package net.mysticforge.quellcraft.client.screens

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.screenhandler.ProjectDeskScreenHandler


class ProjectDeskScreen(handler: ProjectDeskScreenHandler, playerInventory: PlayerInventory, title: Text) :
    HandledScreen<ProjectDeskScreenHandler>(handler, playerInventory, title) {
    val TEXTURE: Identifier? = Identifier.of(Quellcraft.MOD_ID, "textures/gui/container/thaumic_assembler.png")

    private val backgroundWidth = 175
    private val backgroundHeight = 192

    override fun drawBackground(context: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            TEXTURE,
            x,
            y,
            0f,
            0f,
            backgroundWidth, backgroundHeight,
            256,
            256
        )
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
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

        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
        playerInventoryTitleY = 100
    }
}