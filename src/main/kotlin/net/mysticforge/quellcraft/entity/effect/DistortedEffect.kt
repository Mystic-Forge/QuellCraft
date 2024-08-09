package net.mysticforge.quellcraft.entity.effect

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.sound.SoundEvents.BLOCK_SCULK_CATALYST_BLOOM
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class DistortedEffect : StatusEffect(StatusEffectCategory.HARMFUL, 0x000000) {
    private var lastMobAppliedEffects = mutableMapOf<LivingEntity, Int>() // Pair of time until next effect and the index of the last effect

    private val speedModifierUUID = UUID.fromString("7bc001ee-31e2-438a-9b52-2a8480769fa1")

    // A list of effects stored in a pair where the first element is the apply effect and the second element is the remove effect
    private val effects: List<Pair<(LivingEntity, Int) -> Unit, (LivingEntity, Int) -> Unit>> = listOf(
        // 2 Damage
        Pair({ entity, _ ->
            entity.damage(entity.world.damageSources.generic(), 2.0f)
            entity.playSound(SoundEvents.ENTITY_TURTLE_EGG_CRACK, .5f, 2f)
         }, { _, _ -> }),
        // 2 Health
        Pair({entity, _ ->
              entity.heal(2.0f)
                entity.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 0.5f, 1.5f)
             }, { _, _ -> }),
        // Random minor speed
        Pair({ entity, _ ->

            val effectInstance = entity.attributes.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
            if (effectInstance != null) {
                effectInstance.removeModifier(speedModifierUUID)
                effectInstance.addTemporaryModifier(EntityAttributeModifier(speedModifierUUID, ({ this.translationKey }), entity.random.nextTriangular(0.0, 0.05), EntityAttributeModifier.Operation.ADDITION))
            }
            entity.playSound(SoundEvents.BLOCK_LODESTONE_HIT, .8f, 1.5f)
        }, { entity, _ ->
            entity.attributes.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)?.removeModifier(speedModifierUUID)
        }),
        // Pushed in a random direction
        Pair({ entity, amplifier ->
            val randomDirection = entity.random.nextFloat() * 2 * Math.PI
            val amount =  0.5 + amplifier * 0.1
            entity.addVelocity(cos(randomDirection) * amount, amount, sin(randomDirection) * amount)
            entity.playSound(SoundEvents.BLOCK_BASALT_BREAK, 0.2f, 0.6f)
        }, { _, _ -> }),
        // Gain or lose small xp
        Pair({ entity, _ ->
            if(entity is PlayerEntity) {
                entity.addExperience(entity.random.nextInt(4) - 2)
                entity.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2f, 1f)
            }
        }, { _, _ -> }),
    )

    override fun onApplied(entity: LivingEntity?, attributes: AttributeContainer?, amplifier: Int) {

    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int) = true


    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        if (!lastMobAppliedEffects.containsKey(entity))
            lastMobAppliedEffects[entity] = -1

        val amp = amplifier.coerceAtMost(4)

        val lastEffectIndex = lastMobAppliedEffects[entity]!!

        // Voices
        if(entity.world.isClient()) {
            val chance = entity.random.nextInt(100 - (amp * 15))
            if (chance == 0) {
                val randomAngle = entity.random.nextFloat() * 2 * Math.PI
                val randomOffset = Vec3d(cos(randomAngle), 0.0, sin(randomAngle)).multiply(0.5)
                entity.world.playSound(entity.x + randomOffset.x, entity.y + 1.0, entity.z + randomOffset.z,  BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.AMBIENT, 0.5f, 1.0f, true)
            }
        }

        val random = Random(entity.world.time)

        val chance = 100 - (amp * 10)
        if (random.nextInt(chance) == 0) {
            if (lastEffectIndex != -1) {
                val (_, removeEffect) = effects[lastEffectIndex]
                removeEffect(entity, amp)
            }

            val effectIndex = applyEffect(entity, amp, random)
            lastMobAppliedEffects[entity] = effectIndex
        }
    }

    private fun applyEffect(entity: LivingEntity, amplifier: Int, random: Random): Int {
        val randomEffectIndex = random.nextInt(effects.size)
//        println("Applying effect $randomEffectIndex")
        val (applyEffect, _) = effects[randomEffectIndex]
        applyEffect(entity, amplifier)
        return randomEffectIndex
    }

    override fun onRemoved(entity: LivingEntity, attributes: AttributeContainer?, amplifier: Int) {
        try {
            if (lastMobAppliedEffects.containsKey(entity)) {
                val lastEffectIndex = lastMobAppliedEffects[entity]!!
                if (lastEffectIndex != -1) {
                    val (_, removeEffect) = effects[lastEffectIndex]
                    removeEffect(entity, amplifier)
                }
                lastMobAppliedEffects.remove(entity)
            }
        }
        catch (e: Exception) {
            println(e)
        }
    }
}