package net.mysticforge.quellcraft.items

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.mysticforge.quellcraft.Quellcraft

class MistikTolisItem(settings: Settings) : Item(settings.maxCount(1)) {
    companion object {
        val openMistikTolisPacketID = Identifier.of(Quellcraft.MOD_ID, "open_mistik_tolis")
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient && player is ServerPlayerEntity) {
            ServerPlayNetworking.send(player, openMistikTolisPacketID, PacketByteBufs.empty())
        }

        return TypedActionResult.success(player.getStackInHand(hand))
    }
}