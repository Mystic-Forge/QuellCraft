package net.mysticforge.quellcraft.mixinimpl

import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player
import net.minecraft.server.level.ServerLevel
import net.mysticforge.quellcraft.item.QuellcraftItem

object PlayerEntityMixinImpl {
    @JvmStatic
    fun Player.getKnockback(entity: Entity, damageSource: DamageSource): Float {
        val f = this.getAttributeValue(Attributes.ATTACK_KNOCKBACK).toFloat()
        val world = this.level()
        var knockback = if (world is ServerLevel)
            EnchantmentHelper.modifyKnockback(world, this.weaponItem, entity, damageSource, f)
        else
            f

        val stack = mainHandItem
        if (!stack.isEmpty && stack.item is QuellcraftItem) knockback += (mainHandItem.item as QuellcraftItem).knockbackBoost

        return knockback
    }
}