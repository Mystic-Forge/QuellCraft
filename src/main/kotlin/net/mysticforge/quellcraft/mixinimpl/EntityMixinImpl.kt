package net.mysticforge.quellcraft.mixinimpl

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.mysticforge.quellcraft.item.TurboTreadsItem
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object EntityMixinImpl {
    @JvmStatic
    fun Entity.fall(heightDifference: Double, onGround: Boolean, state: BlockState, landedPosition: BlockPos, callbackInfo: CallbackInfo) {
        var turboTreadsStack = armorItems.firstOrNull { it.item is TurboTreadsItem } ?: return
        if (onGround && fallDistance > 0.5f && isSneaking) {
            TurboTreadsItem.activateTurboTreads(turboTreadsStack, this, fallDistance)
        }
    }
}