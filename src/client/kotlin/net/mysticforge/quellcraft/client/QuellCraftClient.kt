package net.mysticforge.quellcraft.client

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexFormat
import me.shedaniel.math.Color
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gl.RenderPipelines.GLOBALS_SNIPPET
import net.minecraft.client.gl.RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET
import net.minecraft.client.gl.UniformType
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.ModStatusEffects
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.client.networking.PacketReceiver
import net.mysticforge.quellcraft.client.screens.ThaumicAssemblerScreen
import net.mysticforge.quellcraft.screenhandler.ModScreenHandlers
import org.joml.Math


object QuellCraftClient : ClientModInitializer {

    private val distortedOutlinePipeline by lazy {
        RenderPipeline.builder(GLOBALS_SNIPPET, TRANSFORMS_AND_PROJECTION_SNIPPET)
            .withVertexShader("core/distorted_outline")
            .withFragmentShader("core/distorted_outline")
            .withSampler("Sampler0")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
            .withLocation("pipeline/distorted_outline")
            .build()
    }

    private var previousEffectLevel = 0f

    override fun onInitializeClient() {
        PacketReceiver
        HandledScreens.register(ModScreenHandlers.THAUMIC_ASSEMBLER_SCREEN_HANDLER_TYPE, ::ThaumicAssemblerScreen)
//        ModelLoadingPlugin.register(QuellcraftModelLoadingPlugin)
    }

    fun drawDistortedEffect(context: DrawContext, tickCounter: RenderTickCounter) {
//        RenderSystem.setShaderGameTime(MinecraftClient.getInstance().world!!.time, tickDelta)

        val player = MinecraftClient.getInstance().player ?: return
        val effect = player.getStatusEffect(ModStatusEffects.distortedEffect)

        val targetEffectLevel = if (effect != null) effect.amplifier.toFloat() + 1 else 0f

        previousEffectLevel = Math.lerp(previousEffectLevel, targetEffectLevel, 0.05f)

        if (previousEffectLevel <= 0.01) return

        val aspectRatio = context.scaledWindowWidth.toFloat() / context.scaledWindowHeight.toFloat() / 3
        val noise = Identifier.of(Quellcraft.MOD_ID, "textures/misc/quell_noise.png")
        context.drawTexture(
            distortedOutlinePipeline,
            noise,
            0,
            0,
            0f,
            0f,
            context.scaledWindowWidth,
            context.scaledWindowHeight,
            context.scaledWindowWidth,
            context.scaledWindowHeight,
            Color.ofRGBA(previousEffectLevel / 5, aspectRatio, 0f, 0f).color
        )
    }
}
