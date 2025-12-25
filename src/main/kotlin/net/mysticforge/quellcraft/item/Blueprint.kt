package net.mysticforge.quellcraft.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class Blueprint(settings: Settings) : Item(settings) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult {
        onUseEvent?.invoke(player.getStackInHand(hand), world, player, hand)
        return ActionResult.SUCCESS
    }

    companion object {
        var onUseEvent : (ItemStack.(world: World, player: PlayerEntity, hand: Hand) -> Unit)? = null
    }
}