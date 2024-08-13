package net.mysticforge.quellcraft.item

import io.wispforest.accessories.api.AccessoryItem
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.mysticforge.quellcraft.quellmanagement.QuellAbsorbentItem
import net.mysticforge.quellcraft.quellmanagement.QuellContent
import net.mysticforge.quellcraft.quellmanagement.readQuellContent
import net.mysticforge.quellcraft.quellmanagement.writeQuellContent

object LuckyCrystal : AccessoryItem(Settings().maxCount(1)), QuellAbsorbentItem {
    private const val QUELL_CONTENT_KEY = "quell_content"
    private const val MAX_QUELL_CONTENT = 100
    private const val ABSORPTION_RATIO = 0.5
    private const val DECAY_CHANCE = 0.1

    override fun getPossibleQuellAbsorption(itemStack: ItemStack, quellContent: QuellContent.Filled): QuellContent {
        val nbt = itemStack.orCreateNbt;
        val currentQuellContent = nbt.readQuellContent(QUELL_CONTENT_KEY)
        if (currentQuellContent is QuellContent.Filled) {
            if (currentQuellContent.quellType == quellContent.quellType) {
                val absorbedAmount =
                    (currentQuellContent.storedThaum + quellContent.storedThaum * ABSORPTION_RATIO).toInt().coerceAtMost(MAX_QUELL_CONTENT) - currentQuellContent.storedThaum
                return if (absorbedAmount > 0) QuellContent.Filled(quellContent.quellType, absorbedAmount) else QuellContent.Empty
            } else return QuellContent.Empty
        }

        val absorbedAmount = (quellContent.storedThaum * ABSORPTION_RATIO).toInt().coerceAtMost(MAX_QUELL_CONTENT)
        return if (absorbedAmount > 0) QuellContent.Filled(quellContent.quellType, absorbedAmount) else QuellContent.Empty
    }

    override fun doAbsorbQuell(itemStack: ItemStack, quellContent: QuellContent.Filled) {
        val nbt = itemStack.orCreateNbt
        val currentQuellContent = nbt.readQuellContent(QUELL_CONTENT_KEY)
        val newQuellContent = if (currentQuellContent is QuellContent.Filled && currentQuellContent.quellType == quellContent.quellType)
            currentQuellContent + quellContent.storedThaum else quellContent

        nbt.writeQuellContent(QUELL_CONTENT_KEY, newQuellContent)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
//        println("Inventory tick")
        tryDecayQuellContent(stack, world.random)
    }

    private fun tryDecayQuellContent(itemStack: ItemStack, random: Random) {
        val nbt = itemStack.orCreateNbt
        val currentQuellContent = nbt.readQuellContent(QUELL_CONTENT_KEY)
        if (currentQuellContent is QuellContent.Filled && random.nextDouble() < DECAY_CHANCE) {
            nbt.writeQuellContent(QUELL_CONTENT_KEY, currentQuellContent - 1)
        }
    }
}