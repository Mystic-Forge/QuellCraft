package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import net.mysticforge.quellcraft.networking.OpenMistikTolisPayload


class MistikTolisItem(settings: Settings) : Item(settings.maxCount(1)) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult {
        if (!world.isClient && player is ServerPlayerEntity) {
            ServerPlayNetworking.send(player, OpenMistikTolisPayload)
        }

        return ActionResult.SUCCESS
    }
}