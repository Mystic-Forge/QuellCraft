package net.mysticforge.quellcraft.client.render.renderer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import net.mysticforge.quellcraft.block.entity.GravityExtractorEntity
import net.mysticforge.quellcraft.client.render.model.GravityExtractorModel
import org.joml.Quaternionf

class GravityExtractorRenderer(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<GravityExtractorEntity> {
    val model: GravityExtractorModel
    val renderType: RenderType

    init {
        model = GravityExtractorModel(GravityExtractorModel.texturedModelData.bakeRoot())
        renderType = model.renderType(ResourceLocation.withDefaultNamespace("textures/block/stone.png"))
    }

    override fun render(
        blockEntity: GravityExtractorEntity,
        f: Float,
        poseStack: PoseStack,
        multiBufferSource: MultiBufferSource,
        i: Int,
        j: Int,
        vec3: Vec3
    ) {
        val vertexConsumer = multiBufferSource.getBuffer(renderType)
        poseStack.pushPose()
        poseStack.translate(0.5f, 1.5f, 0.5f)
        poseStack.mulPose(Quaternionf(1f, 0f, 0f, 0f))
        model.renderToBuffer(poseStack, vertexConsumer, i, j)
        poseStack.popPose()
    }
}