package net.mysticforge.quellcraft

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

object ModSoundEvents {
    val springHammerHitId = ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "spring_hammer_hit")
    val springHammerHitEvent = SoundEvent.createVariableRangeEvent(springHammerHitId)
}