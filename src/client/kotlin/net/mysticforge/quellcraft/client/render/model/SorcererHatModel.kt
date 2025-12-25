package net.mysticforge.quellcraft.client.render.model

import net.minecraft.resources.ResourceLocation
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.item.equipment.SorcererHatItem
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.model.DefaultedGeoModel
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.base.GeoRenderState

class SorcererHatModel
    : DefaultedGeoModel<SorcererHatItem>(ResourceLocation.fromNamespaceAndPath(Quellcraft.MOD_ID, "sorcerer_hat")) {
    override fun subtype(): String = "armor"
}