package net.mysticforge.quellcraft.client.networking

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.mysticforge.quellcraft.client.screens.MistikTolisScreen
import net.mysticforge.quellcraft.networking.ModPayloads.openMistikTolisPayload


object PacketReceiver {
    init {
        ClientPlayNetworking.registerGlobalReceiver<CustomPacketPayload?>(
            openMistikTolisPayload.type() as CustomPacketPayload.Type<CustomPacketPayload?>?
        ) { _: CustomPacketPayload?, context: ClientPlayNetworking.Context ->
            context.client().setScreen(MistikTolisScreen)
        }
    }
}