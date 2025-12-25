package net.mysticforge.quellcraft.entity.effect

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.asKotlinRandom


class DistortedEffect : MobEffect(MobEffectCategory.HARMFUL, 0x000000) {
    private var lastMobAppliedEffects = mutableMapOf<LivingEntity, Int>()

    private val speedModifierId = ResourceLocation.fromNamespaceAndPath("quellcraft", "distorted_speed_modifier")

    // A list of effects stored in a pair where the first element is the apply effect and the second element is the remove effect
    private val effects = listOf(
        // 2 Damage
        DistortionEffect(
            apply = {
                hurtServer(level() as ServerLevel, level().damageSources().generic(), 2.0f)
                playSound(SoundEvents.TURTLE_EGG_CRACK, 0.5f, 2f)
            },
            remove = { }
        ),
        // 2 Health
        DistortionEffect(
            apply = {
                heal(2.0f)
                playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 0.5f, 1.5f)
            },
            remove = { }
        ),
        // Random minor speed
        DistortionEffect(
            apply = {
                val effectInstance = attributes.getInstance(Attributes.MOVEMENT_SPEED)
                if (effectInstance != null) {
                    effectInstance.removeModifier(speedModifierId)
                    effectInstance.addTransientModifier(AttributeModifier(speedModifierId, random.triangle(0.0, 0.05), AttributeModifier.Operation.ADD_VALUE))
                }
                playSound(SoundEvents.LODESTONE_HIT, 0.8f, 1.5f)
            },
            remove = {
                attributes.getInstance(Attributes.MOVEMENT_SPEED)?.removeModifier(speedModifierId)
            }
        ),
        // Pushed in a random direction
        DistortionEffect(
            apply = {
                val randomDirection = random.nextFloat() * 2 * Math.PI
                val amount = 0.5 + it * 0.1
                push(cos(randomDirection) * amount, amount, sin(randomDirection) * amount)
                playSound(SoundEvents.BASALT_BREAK, 0.2f, 0.6f)
            },
            remove = { }
        ),
        // Gain or lose small xp
        DistortionEffect(
            apply = {
                if (this is Player) {
                    giveExperiencePoints(random.nextInt(4) - 2)
                    playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.2f, 1f)
                }
            },
            remove = { }
        ),
    )

    private data class DistortionEffect(
        val apply: LivingEntity.(amplifier: Int) -> Unit,
        val remove: LivingEntity.(amplifier: Int) -> Unit,
    )

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int) = true


    override fun applyEffectTick(world: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        if (!lastMobAppliedEffects.containsKey(entity))
            lastMobAppliedEffects[entity] = -1

        val amp = amplifier.coerceAtMost(4)

        val lastEffectIndex = lastMobAppliedEffects[entity]!!

        // Voices
//        if (entity.world.isClient()) {
//            val chance = entity.random.nextInt(100 - (amp * 15))
//            if (chance == 0) {
//                val randomAngle = entity.random.nextFloat() * 2 * Math.PI
//                val randomOffset = Vec3d(cos(randomAngle), 0.0, sin(randomAngle)).multiply(0.5)
//                entity.world.playSound(entity.x + randomOffset.x, entity.y + 1.0, entity.z + randomOffset.z,  BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.AMBIENT, 0.5f, 1.0f, true)
//            }
//        }

        val random = Random(entity.level().gameTime)

        val chance = 100 - (amp * 10)
        if (random.nextInt(chance) == 0) {
            if (lastEffectIndex != -1) {
                val (_, removeEffect) = effects[lastEffectIndex]
                removeEffect(entity, amp)
            }

            val effectIndex = applyEffect(entity, amp, random)
            lastMobAppliedEffects[entity] = effectIndex
        }

        return true
    }

    private fun applyEffect(entity: LivingEntity, amplifier: Int, random: Random): Int {
        val randomEffectIndex = random.nextInt(effects.size)
//        println("Applying effect $randomEffectIndex")
        effects.random(random.asKotlinRandom()).apply(entity, amplifier)
        return randomEffectIndex
    }

    fun onRemovedForEntity(livingEntity: LivingEntity, amplifier: Int) {
        try {
            if (lastMobAppliedEffects.containsKey(livingEntity)) {
                val lastEffectIndex = lastMobAppliedEffects[livingEntity]!!
                if (lastEffectIndex != -1) {
                    val (_, removeEffect) = effects[lastEffectIndex]
                    removeEffect(livingEntity, amplifier)
                }
                lastMobAppliedEffects.remove(livingEntity)
            }
        } catch (e: Exception) {
            println(e)
        }
    }
}