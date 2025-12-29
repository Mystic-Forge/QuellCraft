package net.mysticforge.quellcraft.client.render.accessory

import com.mojang.blaze3d.vertex.PoseStack
import io.wispforest.accessories.api.client.renderers.AccessoryRenderer
import io.wispforest.accessories.api.slot.SlotPath
import net.minecraft.client.Minecraft
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.PlayerModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class HatAccessoryRenderer : AccessoryRenderer {
    override fun <S : LivingEntityRenderState> render(
        stack: ItemStack,
        path: SlotPath,
        matrices: PoseStack,
        model: EntityModel<S>,
        renderState: S,
        multiBufferSource: MultiBufferSource,
        light: Int,
        partialTicks: Float
    ) {
        if(model !is PlayerModel) return
        model.head.translateAndRotate(matrices)
        CustomHeadLayer.translateToHead(matrices, CustomHeadLayer.Transforms(0f, 0f, 0f))
        Minecraft.getInstance().getItemRenderer().renderStatic(
            stack, ItemDisplayContext.HEAD, light, OverlayTexture.NO_OVERLAY, matrices, multiBufferSource, null, 0)
    }
}