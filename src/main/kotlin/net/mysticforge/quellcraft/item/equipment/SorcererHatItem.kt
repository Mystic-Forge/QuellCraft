package net.mysticforge.quellcraft.item.equipment

import net.minecraft.world.item.equipment.ArmorType
import net.mysticforge.quellcraft.item.QuellcraftItem


class SorcererHatItem(settings: QuellcraftItemSettings)
    : QuellcraftItem(settings.humanoidArmor(ModArmorMaterials.sorcerer, ArmorType.HELMET).stacksTo(1)){
//    val cache = SingletonAnimatableInstanceCache(this)

//    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {}

//    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

//    var onCreateGeoRenderer: ((Consumer<GeoRenderProvider>) -> Unit)? = null

//    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
//        onCreateGeoRenderer?.invoke(consumer)
//    }
}