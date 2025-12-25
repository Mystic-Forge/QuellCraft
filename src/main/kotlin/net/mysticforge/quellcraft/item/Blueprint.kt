package net.mysticforge.quellcraft.item

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionHand
import net.minecraft.world.level.Level

class Blueprint(settings: Properties) : Item(settings) {
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResult {
        onUseEvent?.invoke(player.getItemInHand(hand), world, player, hand)
        return InteractionResult.SUCCESS
    }

    companion object {
        var onUseEvent : (ItemStack.(world: Level, player: Player, hand: InteractionHand) -> Unit)? = null
    }
}