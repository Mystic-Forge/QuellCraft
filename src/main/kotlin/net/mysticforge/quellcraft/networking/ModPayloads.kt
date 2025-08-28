package net.mysticforge.quellcraft.networking

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import net.mysticforge.quellcraft.Quellcraft

object ModPayloads {
    val openMistikTolisPayload = PayloadTypeRegistry.playS2C().register(OpenMistikTolisPayload.payloadId, OpenMistikTolisPayload.codec)
}

object OpenMistikTolisPayload : CustomPayload {
    val openMistikTolisId: Identifier = Identifier.of(Quellcraft.MOD_ID, "open_mistik_tolis")
    val payloadId: CustomPayload.Id<OpenMistikTolisPayload?> = CustomPayload.Id<OpenMistikTolisPayload?>(openMistikTolisId)
    val codec = PacketCodec.unit<RegistryByteBuf, OpenMistikTolisPayload>(OpenMistikTolisPayload)

    override fun getId(): CustomPayload.Id<out CustomPayload?> = payloadId
}