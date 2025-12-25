package net.mysticforge.quellcraft.client.networking

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.packet.CustomPayload
import net.mysticforge.quellcraft.client.screens.MistikTolisScreen
import net.mysticforge.quellcraft.networking.ModPayloads.openMistikTolisPayload


object PacketReceiver {
    init {
        ClientPlayNetworking.registerGlobalReceiver<CustomPayload?>(
            openMistikTolisPayload.id() as CustomPayload.Id<CustomPayload?>?
        ) { _: CustomPayload?, context: ClientPlayNetworking.Context ->
            context.client().setScreen(MistikTolisScreen)
        }
    }
}