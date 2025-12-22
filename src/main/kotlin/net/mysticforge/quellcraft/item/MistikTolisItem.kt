package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3i
import net.minecraft.world.TeleportTarget
import net.minecraft.world.World
import net.mysticforge.quellcraft.networking.OpenMistikTolisPayload
import net.mysticforge.quellcraft.realm.RealmGenerator


class MistikTolisItem(settings: Settings) : Item(settings.maxCount(1)) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult {
        if (!world.isClient && player is ServerPlayerEntity) {
            ServerPlayNetworking.send(player, OpenMistikTolisPayload)

            val newWorld = RealmGenerator.create(world.server!!, world, player.blockPos, 64)
            player.teleportTo(TeleportTarget(newWorld, player.getPos(), player.getVelocity(), player.yaw, player.pitch, TeleportTarget.NO_OP))
        }

        return ActionResult.SUCCESS
    }
}