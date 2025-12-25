package net.mysticforge.quellcraft.client.render.accessory

import io.wispforest.accessories.api.client.renderers.AccessoryRenderer
import io.wispforest.accessories.api.slot.SlotPath
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.world.item.ItemStack

class HatAccessoryRenderer : AccessoryRenderer {

    override fun <S : LivingEntityRenderState?> render(
        stack: ItemStack,
        path: SlotPath,
        matrices: PoseStack,
        model: EntityModel<S?>,
        renderState: S,
        multiBufferSource: MultiBufferSource?,
        light: Int,
        partialTicks: Float
    ) {

    }
}