package net.mysticforge.quellcraft.quellmanagement

import io.wispforest.accessories.api.AccessoriesCapability
import io.wispforest.accessories.api.slot.SlotEntryReference
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.mysticforge.quellcraft.components.ModComponents
import net.mysticforge.quellcraft.state.property.QuellType
import net.mysticforge.quellcraft.util.getEntitiesOfType
import net.mysticforge.quellcraft.util.nextDouble
import kotlin.math.floor
import kotlin.random.Random

fun World.emitQuell(quellContent: QuellContent, point: Vec3d, range: Double) {
    if (quellContent !is QuellContent.Filled) return

    data class QuellAbsorbentStack(val stack: ItemStack, val item: QuellAbsorbentItem)

    val entities = getEntitiesOfType<LivingEntity>(Box(point, point).expand(range))
        .filter { it.pos.subtract(point).lengthSquared() <= range * range }

    val quellAbsorbentItems = entities.flatMap {
        var accessories = AccessoriesCapability.get(it)?.allEquipped
        if(accessories == null) accessories = emptyList<SlotEntryReference>()
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
        if(amount == 0) continue
        absorbentStack.item.doAbsorbQuell(absorbentStack.stack, QuellContent.Filled(quellContent.quellType, amount))
    }

    entities.forEach { ModComponents.quellInfusion.get(it).addValue(remaining) }

    println("Absorbed $absorbed thaum into items, remaining $remaining thaum was infused into entities")
}

fun World.doQuellExplosion(quellContent: QuellContent, point: Vec3d, range: Double) {
    if (quellContent !is QuellContent.Filled) return

    for (i in 0..<(20 * quellContent.storedThaum).coerceAtMost(10000)) {
        when ((quellContent as? QuellContent.Filled)?.quellType) {
            QuellType.VOID -> {
                val randomOffset = Vec3d(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)
                val direction = randomOffset.normalize().multiply(random.nextDouble(0.5..<range))
                val position = point.add(randomOffset.normalize().multiply(0.4))
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
            QuellType.THERMAL -> {
                val randomOffset = Vec3d(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)
                val position = point.add(randomOffset.normalize().multiply(0.4))
                val direction = randomOffset.normalize().multiply(random.nextDouble(0.0..<range * 0.15))

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
            else -> { }
        }
    }

    playSound(point.x, point.y, point.z, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f, true)
}