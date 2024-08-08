package net.mysticforge.quellcraft.client

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.ModEffects
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.client.screens.MistikTolisScreen
import net.mysticforge.quellcraft.items.MistikTolisItem
import org.joml.Matrix4f


object QuellcraftClient : ClientModInitializer {
    private var shader = lazy { FabricShaderProgram(MinecraftClient.getInstance().resourceManager, Identifier.of("minecraft", "distorted_outline"), VertexFormats.POSITION_COLOR_TEXTURE)}

    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(MistikTolisItem.openMistikTolisPacketID) { client, _, _, _ ->
            client.execute {
                client.setScreen(MistikTolisScreen)
            }
        }
    }

    fun drawDistortedEffect(context: DrawContext, tickDelta: Float) {
        RenderSystem.setShaderGameTime(MinecraftClient.getInstance().world!!.time, tickDelta)

        val player = MinecraftClient.getInstance().player ?: return
        val effect = player.getStatusEffect(ModEffects.distortedEffect) ?: return

        val noise = Identifier.of(Quellcraft.MOD_ID, "textures/misc/quell_noise.png")
        RenderSystem.disableDepthTest()
        val matrix4f: Matrix4f = context.matrices.peek().positionMatrix
        val bufferBuilder = Tessellator.getInstance().buffer
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE)
        val amplifierRedChannel = effect.amplifier / 4f
        bufferBuilder.vertex(matrix4f, 0f, 0f, 0f)
            .color(amplifierRedChannel, 0.0f, 0.0f, 0.0f)
            .texture(0f, 0f)
            .next()
        bufferBuilder.vertex(matrix4f, 0f, context.scaledWindowHeight.toFloat(), 0f)
            .color(amplifierRedChannel, 0.0f, 0.0f, 0.0f)
            .texture(0f, 1f)
            .next()
        bufferBuilder.vertex(matrix4f, context.scaledWindowWidth.toFloat(), context.scaledWindowHeight.toFloat(), 0f)
            .color(amplifierRedChannel, 0.0f, 0.0f, 0.0f)
            .texture(1f, 1f)
            .next()
        bufferBuilder.vertex(matrix4f, context.scaledWindowWidth.toFloat(), 0f, 0f)
            .color(amplifierRedChannel, 0.0f, 0.0f, 0.0f)
            .texture(1f, 0f)
            .next()

        RenderSystem.setShader { shader.value }
        RenderSystem.setShaderTexture(0, noise)
        RenderSystem.depthMask(false)
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end())

        RenderSystem.depthMask(true)
        RenderSystem.enableDepthTest()
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    }
}
