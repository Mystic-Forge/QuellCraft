package net.mysticforge.quellcraft.item

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder.Living
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.mysticforge.quellcraft.QuellcraftConfig
import net.mysticforge.quellcraft.quellmanagement.QuellContent
import net.mysticforge.quellcraft.quellmanagement.doQuellExplosion
import net.mysticforge.quellcraft.state.property.QuellType

object TurboTreadsItem : ArmorItem(TurboTreadsArmorMaterial, Type.BOOTS, Settings().maxCount(1)) {
    private const val CHARGE_KEY = "quell_content"

    fun tryActivateTurboTreads(itemStack: ItemStack, entity: Entity): Boolean {
        if(!entity.isSneaking || !entity.isOnGround || entity.velocity.y > -0.2f) return false

        if(entity is PlayerEntity && entity.abilities.flying) return false

        val nbt = itemStack.getOrCreateNbt()
        val storedThaum = nbt.getShort(CHARGE_KEY).toInt()
        if(storedThaum < QuellcraftConfig.turboTreadsChargeTime) return false

        val launchPower = QuellcraftConfig.turboTreadsBoost.toDouble()
        nbt.putShort(CHARGE_KEY, 0)
        itemStack.nbt = nbt

        val velocity = entity.velocity
        val horizontalDirection = velocity.withAxis(Direction.Axis.Y, 0.0)
        val horizontalBoosting = horizontalDirection.length() > 0.05
        val horizontalBoost = if (horizontalBoosting)
            velocity.withAxis(Direction.Axis.Y, 0.0).normalize().multiply(launchPower)
        else Vec3d.ZERO
        val finalVelocity = Vec3d(horizontalBoost.x, if(horizontalBoosting) launchPower * 0.7 else launchPower, horizontalBoost.z)

        entity.velocity = finalVelocity

        if(entity is LivingEntity) {
            entity.damage(entity.damageSources.generic(), 3f)
            entity.world.doQuellExplosion(QuellContent.Filled(QuellType.VOID, 20), entity.pos, 5.0)
        }
        return true
    }

    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val nbt = itemStack.getOrCreateNbt()
        val currentCharge = nbt.getShort(CHARGE_KEY).toInt()
        if(currentCharge < QuellcraftConfig.turboTreadsChargeTime) {
            if(currentCharge == QuellcraftConfig.turboTreadsChargeTime - 1) {
                world.playSound(entity.x, entity.y, entity.z, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1f, 1f, false)
            }
            nbt.putShort(CHARGE_KEY, (currentCharge + 1).toShort())
        }
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        val nbt = stack.getOrCreateNbt();
        if(nbt.contains(CHARGE_KEY)) {
            val charge = nbt.getShort(CHARGE_KEY).toInt()
            tooltip.add(Text.of("Charge: ${(charge / QuellcraftConfig.turboTreadsChargeTime.toFloat() * 100).toInt()}%"))
        }

        tooltip.add(Text.of("Once charged, landing while sneaking"))
        tooltip.add(Text.of("will cause a violent explosion beneath"))
        tooltip.add(Text.of("you, sending you flying into the air!"))
    }
}

object TurboTreadsArmorMaterial : ArmorMaterial {
    override fun getDurability(type: ArmorItem.Type) = 128

    override fun getProtection(type: ArmorItem.Type?) = 1

    override fun getEnchantability() = 1

    override fun getEquipSound(): SoundEvent = SoundEvents.ITEM_ARMOR_EQUIP_LEATHER

    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.LEATHER)

    override fun getName() = "turbo_treads"

    override fun getToughness() = 0f

    override fun getKnockbackResistance() = 0f
}