package net.mysticforge.quellcraft.client.render.accessory

import io.wispforest.accessories.api.client.renderers.AccessoryRenderer
import io.wispforest.accessories.api.slot.SlotPath
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.state.LivingEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

class HatAccessoryRenderer : AccessoryRenderer {

    override fun <S : LivingEntityRenderState?> render(
        stack: ItemStack,
        path: SlotPath,
        matrices: MatrixStack,
        model: EntityModel<S?>,
        renderState: S,
        multiBufferSource: VertexConsumerProvider?,
        light: Int,
        partialTicks: Float
    ) {

    }
}