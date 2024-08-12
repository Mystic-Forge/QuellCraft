package net.mysticforge.quellcraft.quellmanagement

import net.minecraft.item.ItemStack

interface QuellAbsorbent {

}

interface QuellAbsorbentItem: QuellAbsorbent {
    /**
     * Used for calculating how much quell a particular object could absorb for later use in distributing QuellContent
     * @param quellContent Quell content available to absorb
     * @return Quell content absorbed
     */
    fun getPossibleQuellAbsorption(itemStack: ItemStack, quellContent: QuellContent.Filled): QuellContent

    /**
     * Used for actually absorbing quell content.
     * Ideally you should always absorb as much as is given to this function as it was calculated from getPossibleQuellAbsorption
     * and will never supply more than this function absorbed
     * @param quellContent Quell content to absorb
     */
    fun doAbsorbQuell(itemStack: ItemStack, quellContent: QuellContent.Filled)
}