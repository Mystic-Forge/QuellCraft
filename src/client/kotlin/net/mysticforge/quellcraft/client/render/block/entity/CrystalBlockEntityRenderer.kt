package net.mysticforge.quellcraft.client.render.block.entity

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity

class CrystalBlockEntityRenderer(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<CrystalBlockEntity> {
    private val renderManager = context.renderManager

    override fun render(blockEntity: CrystalBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
        val offset = blockEntity.cachedState.getModelOffset(blockEntity.world, blockEntity.pos)
        matrices.translate(offset.x, offset.y, offset.z)
//        val bakedModel = BasicBakedModel.Builder(JsonUnbakedModel.deserialize(), ModelOverrideList.EMPTY, true).build()

//        renderManager.modelRenderer.render(
//            matrices.peek(),
//            vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Identifier.of("quellcraft", "textures/block/void_cluster.png"))),
//            blockEntity.cachedState,
//            bakedModel,
//            1.0f,
//            1.0f,
//            1.0f,
//            light,
//            overlay
//        )
    }
}