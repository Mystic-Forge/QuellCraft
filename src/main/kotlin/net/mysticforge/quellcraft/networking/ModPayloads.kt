package net.mysticforge.quellcraft.networking

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.mysticforge.quellcraft.Quellcraft

object ModPayloads {
    val openMistikTolisPayload = PayloadTypeRegistry.playS2C().register(OpenMistikTolisPayload.payloadId, OpenMistikTolisPayload.codec)
}

object OpenMistikTolisPayload : CustomPacketPayload {
    val openMistikTolisId: ResourceLocation = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "open_mistik_tolis")
    val payloadId: CustomPacketPayload.Type<OpenMistikTolisPayload?> = CustomPacketPayload.Type<OpenMistikTolisPayload?>(openMistikTolisId)
    val codec = StreamCodec.unit<RegistryFriendlyByteBuf, OpenMistikTolisPayload>(OpenMistikTolisPayload)

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> = payloadId
}