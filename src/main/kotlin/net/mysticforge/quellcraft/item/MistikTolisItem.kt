package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionHand
import net.minecraft.core.Vec3i
import net.minecraft.world.level.portal.TeleportTransition
import net.minecraft.world.level.Level
import net.mysticforge.quellcraft.networking.OpenMistikTolisPayload
import net.mysticforge.quellcraft.realm.RealmGenerator


class MistikTolisItem(settings: Properties) : Item(settings.stacksTo(1)) {
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (!world.isClientSide && player is ServerPlayer) {
            ServerPlayNetworking.send(player, OpenMistikTolisPayload)

            val newWorld = RealmGenerator.create(world.server!!, world, player.blockPosition(), 64)
            player.teleport(TeleportTransition(newWorld, player.position(), player.deltaMovement, player.yRot, player.xRot, TeleportTransition.DO_NOTHING))
        }

        return InteractionResult.SUCCESS
    }
}