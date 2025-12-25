package net.mysticforge.quellcraft.quellmanagement

import io.wispforest.accessories.api.AccessoriesCapability
import io.wispforest.accessories.api.slot.SlotEntryReference
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.mysticforge.quellcraft.components.ModComponents
import net.mysticforge.quellcraft.state.property.QuellType
import net.mysticforge.quellcraft.util.getEntitiesOfType
import net.mysticforge.quellcraft.util.nextDouble
import kotlin.math.floor
import kotlin.random.Random

fun Level.emitQuell(quellContent: QuellContent, point: Vec3, range: Double) {
    if (quellContent !is QuellContent.Filled) return

    data class QuellAbsorbentStack(val stack: ItemStack, val item: QuellAbsorbentItem)

    val entities = getEntitiesOfType<LivingEntity>(AABB(point, point).inflate(range))
        .filter { it.position().subtract(point).lengthSqr() <= range * range }

    val quellAbsorbentItems = entities.flatMap {
        var accessories = AccessoriesCapability.get(it)?.allEquipped
        if (accessories == null) accessories = emptyList<SlotEntryReference>()
        accessories.mapNotNull { capability -> (capability.stack.item as? QuellAbsorbentItem)?.let { item -> QuellAbsorbentStack(capability.stack, item) } }
    }

    val ratios = quellAbsorbentItems.map {
        val absorbed = it.item.getPossibleQuellAbsorption(it.stack, quellContent)
        val ratio = absorbed.storedThaum.toDouble() / quellContent.storedThaum.toDouble()
        check(ratio in 0.0..1.0) { "Invalid ratio $ratio found on ${it.item}" }
        ratio
    }

    val remaining = floor(ratios.fold(1.0) { acc, next -> acc * (1.0 - next) } * quellContent.storedThaum).toInt()
    val absorbed = quellContent.storedThaum - remaining

    val totalWeight = ratios.sum()
    for ((ratio, absorbentStack) in ratios.zip(quellAbsorbentItems)) {
        val amount = floor(absorbed * (ratio / totalWeight)).toInt()
        if (amount == 0) continue
        absorbentStack.item.doAbsorbQuell(absorbentStack.stack, QuellContent.Filled(quellContent.quellType, amount))
    }

    entities.forEach { ModComponents.quellInfusion.get(it).addValue(remaining) }

    println("Absorbed $absorbed thaum into items, remaining $remaining thaum was infused into entities")
}

fun Level.doQuellExplosion(quellContent: QuellContent, point: Vec3, range: Double) {
    if (quellContent !is QuellContent.Filled) return

    when ((quellContent).quellType) {
        QuellType.Void -> {
            repeat((20 * quellContent.storedThaum).coerceAtMost(10000)) {
                val randomOffset = Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)
                val direction = randomOffset.normalize().scale(random.nextDouble(0.5..<range))
                val position = point.add(randomOffset.normalize().scale(random.nextDouble(0.2..<.5)))
                addParticle(
                    ParticleTypes.REVERSE_PORTAL,
                    position.x,
                    position.y,
                    position.z,
                    direction.x,
                    direction.y,
                    direction.z
                )
            }

            playLocalSound(point.x, point.y, point.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f, true)
        }

        QuellType.Thermal -> {
            repeat((20 * quellContent.storedThaum).coerceAtMost(10000)) {
                val randomOffset = Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)
                val position = point.add(randomOffset.normalize().scale(0.4))
                val direction = randomOffset.normalize().scale(random.nextDouble(0.0..<range * 0.15))

                addParticle(
                    if (Random.nextDouble() < 0.4) ParticleTypes.FLAME else ParticleTypes.SMOKE,
                    position.x,
                    position.y,
                    position.z,
                    direction.x,
                    direction.y,
                    direction.z
                )
            }

            playLocalSound(point.x, point.y, point.z, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0f, 1.0f, true)
        }

        QuellType.Life -> {
            repeat((20 * quellContent.storedThaum).coerceAtMost(10000)) {
                val randomOffset = Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)
                val position = point.add(randomOffset.normalize().scale(random.nextDouble(0.0..<range)))
                val direction = randomOffset.normalize().scale(random.nextDouble(0.0..<range))

                addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    position.x,
                    position.y,
                    position.z,
                    direction.x,
                    direction.y,
                    direction.z
                )
            }

            playLocalSound(point.x, point.y, point.z, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f, 1.0f, true)
        }
    }
}