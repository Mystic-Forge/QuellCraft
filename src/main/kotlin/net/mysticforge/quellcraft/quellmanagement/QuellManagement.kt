package net.mysticforge.quellcraft.quellmanagement

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.mysticforge.quellcraft.components.ModComponents
import net.mysticforge.quellcraft.util.getEntitiesOfType
import kotlin.math.floor

fun World.emitQuell(quellContent: QuellContent, point: Vec3d, range: Double) {
    if (quellContent !is QuellContent.Filled) return

    data class QuellAbsorbentStack(val stack: ItemStack, val item: QuellAbsorbentItem)

    val entities = getEntitiesOfType<LivingEntity>(Box(point, point).expand(range))
        .filter { it.pos.subtract(point).lengthSquared() <= range * range }

    val quellAbsorbentItems = entities.flatMap {
        it.itemsEquipped.mapNotNull { stack -> (stack.item as? QuellAbsorbentItem)?.let { QuellAbsorbentStack(stack, it) } }
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