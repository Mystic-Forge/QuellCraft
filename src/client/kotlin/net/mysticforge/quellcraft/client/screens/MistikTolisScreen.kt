package net.mysticforge.quellcraft.client.screens

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.Vector2i
import org.joml.Vector4i

object MistikTolisScreen : Screen(Text.of("Mistik Tolis")) {
//    var shader = FabricShaderProgram(MinecraftClient.getInstance().resourceManager, Identifier.of("minecraft", "mistik_tolis_background"), VertexFormats.POSITION_TEXTURE)

    override fun init() {
//        val buttonWidget = ButtonWidget.builder(Text.of("Hello World")) {
//            client!!.toastManager.add(
//                SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
//            )
//        }.dimensions(40, 40, 120, 20).build()
//
//        // Register the button widget.
//        addDrawableChild(buttonWidget)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

//        context.drawText(this.textRenderer, "Special Button", 40, 40 - textRenderer.fontHeight - 10, -0x1, true)
        val mistikTolisResolution = Vector2i(332, 242)
        val borderPadding = 16;
        val marginX = context.scaledWindowWidth / 2 - 332 / 2
        val marginY = context.scaledWindowHeight / 2 - 242 / 2
        val leftPageRegion = Vector4i(marginX + borderPadding, marginY + borderPadding, marginX + mistikTolisResolution.x / 2 - borderPadding, marginY + mistikTolisResolution.y - borderPadding)
        context.drawTexture(RenderPipelines.GUI_TEXTURED,
            Identifier.of("quellcraft", "textures/gui/mistik_tolis_border.png"),
            marginX,
            marginY,
            0f,
            0f,
            mistikTolisResolution.x,
            mistikTolisResolution.y,
            332,
            242
        )
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("quellcraft", "textures/gui/mistik_tolis_pages.png"),
            marginX,
            marginY,
            0f,
            0f,
            mistikTolisResolution.x,
            mistikTolisResolution.y,
            332,
            242
        )
        context.enableScissor(leftPageRegion.x, leftPageRegion.y, leftPageRegion.z, leftPageRegion.w)
        context.drawText(textRenderer, "Mistik Tolis", leftPageRegion.x, leftPageRegion.y, -0x1, false)
        context.disableScissor()
    }

    override fun shouldPause() = false
}