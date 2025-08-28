package net.mysticforge.quellcraft

import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object ModSoundEvents {
    val springHammerHitId = Identifier.of(Quellcraft.MOD_ID, "spring_hammer_hit")
    val springHammerHitEvent = SoundEvent.of(springHammerHitId)
}