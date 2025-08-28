package net.mysticforge.quellcraft.mixinimpl

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.mysticforge.quellcraft.item.QuellcraftItem

object PlayerEntityMixinImpl {
    @JvmStatic
    fun PlayerEntity.getKnockback(entity: Entity, damageSource: DamageSource): Float {
        val f = this.getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK).toFloat()
        val world = this.getWorld()
        var knockback = if (world is ServerWorld)
            EnchantmentHelper.modifyKnockback(world, this.getWeaponStack(), entity, damageSource, f)
        else
            f

        val stack = mainHandStack
        if (!stack.isEmpty && stack.item is QuellcraftItem) knockback += (mainHandStack.item as QuellcraftItem).knockbackBoost

        return knockback
    }
}