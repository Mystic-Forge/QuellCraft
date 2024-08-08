package net.mysticforge.quellcraft.client.screens

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.toast.SystemToast
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.Matrix4f

object MistikTolisScreen: Screen(Text.of("Mistik Tolis")) {

    var shader = FabricShaderProgram(MinecraftClient.getInstance().resourceManager, Identifier.of("minecraft", "mistik_tolis_background"), VertexFormats.POSITION_TEXTURE)

    override fun init() {
        val buttonWidget: ButtonWidget = ButtonWidget.builder(Text.of("Hello World")) { btn ->
            client!!.toastManager.add(
                SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
            )
        }.dimensions(40, 40, 120, 20).build()

        // Register the button widget.
        this.addDrawableChild(buttonWidget)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context) // Tint the background.
        super.render(context, mouseX, mouseY, delta)

        context.drawText(this.textRenderer, "Special Button", 40, 40 - textRenderer.fontHeight - 10, -0x1, true)

        val transformationMatrix: Matrix4f = context.matrices.peek().positionMatrix
        val tessellator = Tessellator.getInstance()

        val buffer = tessellator.buffer
        buffer.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_TEXTURE)

        buffer.vertex(transformationMatrix, 0f, 0f, 5f).texture(0f, 0f).next()
        buffer.vertex(transformationMatrix, 0f, 40f, 5f).texture(0f, 1f).next()
        buffer.vertex(transformationMatrix, 40f, 0f, 5f).texture(1f, 0f).next()

        buffer.vertex(transformationMatrix, 0f, 40f, 5f).texture(0f, 1f).next()
        buffer.vertex(transformationMatrix, 40f, 40f, 5f).texture(1f, 1f).next()
        buffer.vertex(transformationMatrix, 40f, 0f, 5f).texture(1f, 0f).next()

        RenderSystem.setShader { FabricShaderProgram(MinecraftClient.getInstance().resourceManager, Identifier.of("minecraft", "mistik_tolis_background"), VertexFormats.POSITION_TEXTURE) }

        BufferRenderer.drawWithGlobalProgram(buffer.end())
    }

    override fun shouldPause() = false
}