package net.mysticforge.quellcraft.client

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import me.shedaniel.math.Color
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.renderer.RenderPipelines.GLOBALS_SNIPPET
import net.minecraft.client.renderer.RenderPipelines.MATRICES_PROJECTION_SNIPPET
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.resources.ResourceLocation
import net.mysticforge.quellcraft.ModStatusEffects
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.ModBlocks
import net.mysticforge.quellcraft.block.entity.GravityExtractorEntity
import net.mysticforge.quellcraft.client.networking.PacketReceiver
import net.mysticforge.quellcraft.client.render.accessory.AccessoryRenderers
import net.mysticforge.quellcraft.client.render.renderer.GravityExtractorRenderer
import net.mysticforge.quellcraft.client.screens.BlueprintScreen
import net.mysticforge.quellcraft.client.screens.ProjectDeskScreen
import net.mysticforge.quellcraft.screenhandler.ModScreenHandlers
import org.joml.Math


object QuellCraftClient : ClientModInitializer {

    private val distortedOutlinePipeline by lazy {
        RenderPipeline.builder(GLOBALS_SNIPPET, MATRICES_PROJECTION_SNIPPET)
            .withVertexShader("core/distorted_outline")
            .withFragmentShader("core/distorted_outline")
            .withSampler("Sampler0")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .withLocation("pipeline/distorted_outline")
            .build()
    }

    private var previousEffectLevel = 0f

    override fun onInitializeClient() {
        PacketReceiver
        BlueprintScreen

        MenuScreens.register(ModScreenHandlers.projectDesk, ::ProjectDeskScreen)
        AccessoryRenderers

        BlockEntityRenderers.register<GravityExtractorEntity>(ModBlocks.gravityExtractorEntityType, ::GravityExtractorRenderer)

//        ModelLoadingPlugin.register(QuellcraftModelLoadingPlugin)

//        ModItems.sorcererHat.onCreateGeoRenderer = { it.accept(SorcererHatGeoRenderer()) }


//        Thread {
//            Thread.sleep(5000)
//            val modelManager = Minecraft.getInstance().modelManager as FabricBakedModelManager
//            val model = Minecraft.getInstance().modelManager.getItemModel(ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "sorcerer_hat_penis"))
//            println("WE FOUND THE MODEL!: $model")
//        }.start()

//        LivingEntityFeatureRendererRegistrationCallback.EVENT
//            .register(LivingEntityFeatureRendererRegistrationCallback { entityType, entityRenderer, registrationHelper, context ->
//                if (entityRenderer is PlayerRenderer) {
//                    registrationHelper!!.register(object : RenderLayer<PlayerRenderState, PlayerModel>(entityRenderer) {
//                        override fun render(
//                            poseStack: PoseStack,
//                            multiBufferSource: MultiBufferSource,
//                            i: Int,
//                            entityRenderState: PlayerRenderState,
//                            f: Float,
//                            g: Float
//                        ) {
//                            val entityModel = getParentModel()
//                            entityModel.root().translateAndRotate(poseStack)
//                            entityModel.getHead().translateAndRotate(poseStack)
//                            entityRenderState.mainHandItem?.render(poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY)
//                        }
//                    })
//                }
//            })

//        EntityRenderers.createPlayerRenderers(EntityRendererProvider)
    }

    fun drawDistortedEffect(context: GuiGraphics, tickCounter: DeltaTracker) {
//        RenderSystem.setShaderGameTime(MinecraftClient.getInstance().world!!.time, tickDelta)

        val player = Minecraft.getInstance().player ?: return
        val effect = player.getEffect(ModStatusEffects.distortedEffect)

        val targetEffectLevel = if (effect != null) effect.amplifier.toFloat() + 1 else 0f

        previousEffectLevel = Math.lerp(previousEffectLevel, targetEffectLevel, 0.05f)

        if (previousEffectLevel <= 0.01) return


        val aspectRatio = context.guiWidth().toFloat() / context.guiHeight().toFloat() / 3
        val noise = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "textures/misc/quell_noise.png")
        context.blit(
            distortedOutlinePipeline,
            noise,
            0,
            0,
            0f,
            0f,
            context.guiWidth(),
            context.guiHeight(),
            context.guiWidth(),
            context.guiHeight(),
            Color.ofRGBA(previousEffectLevel / 5, aspectRatio, 0f, 0f).color
        )
    }
}
