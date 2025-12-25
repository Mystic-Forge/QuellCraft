package net.mysticforge.quellcraft.client.render.armorrenderer

import net.mysticforge.quellcraft.client.render.model.SorcererHatModel
import net.mysticforge.quellcraft.item.equipment.SorcererHatItem
import software.bernie.geckolib.renderer.GeoArmorRenderer

class SorcererHatArmorRenderer<R: Nothing>
    : GeoArmorRenderer<SorcererHatItem, R> {
        constructor() : super(SorcererHatModel())
}