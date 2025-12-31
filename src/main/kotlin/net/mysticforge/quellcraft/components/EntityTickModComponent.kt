package net.mysticforge.quellcraft.components

import net.minecraft.world.entity.Entity
import org.ladysnake.cca.api.v3.component.ComponentV3

class EntityTickModComponent(private val entity: Entity) : ActuallyAutoSyncedComponent(entity, ModComponents.tickMod),
    ComponentV3 {
    private var tickMod by autoSyncedOptionalValue<Int>("mod")
    private var timeSinceTick = 0f

    fun runTicks(deltaTime: Float, tickFunction: (Entity) -> Unit) {
        when (val localTickMod = tickMod) {
            is OptionalValue.Unset -> {
                entity.setOldPosAndRot();
                tickFunction(entity)
            }

            is OptionalValue.Set -> {
                timeSinceTick += deltaTime

                val tickDelta = 1f / (20f + localTickMod.value)

                if (timeSinceTick >= tickDelta) entity.setOldPosAndRot()

                while (timeSinceTick >= tickDelta) {
                    tickFunction(entity)
                    timeSinceTick -= tickDelta
                }
            }
        }
    }

    fun getPartialTickTime(realPartialTick: Float): Float {
        // Running at 20 tps or higher we cannot interpolate faster since ticks are always dispatched 20 times a second
        val localTickMod = tickMod
        if (localTickMod !is OptionalValue.Set || localTickMod.value >= 0) return realPartialTick

        // Running less than 20 ticks we want to interpolate slower
        val tickDelta = 1f / (20f + localTickMod.value)
        return (timeSinceTick + (realPartialTick / 20f)) / tickDelta
    }
}
