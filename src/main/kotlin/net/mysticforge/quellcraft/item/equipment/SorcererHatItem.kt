package net.mysticforge.quellcraft.item.equipment

import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.item.equipment.EquipmentType
import net.mysticforge.quellcraft.item.QuellcraftItem
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.animatable.manager.AnimatableManager
import java.util.function.Consumer


class SorcererHatItem(settings: QuellcraftItemSettings)
    : QuellcraftItem(settings.armor(ModArmorMaterials.sorcerer, EquipmentType.HELMET).maxCount(1)),
    GeoItem {
    val cache = SingletonAnimatableInstanceCache(this)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {}

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        consumer.accept(object : GeoRenderProvider {
//            private var renderer: SorcererHatR<*>? = null
//
//            @Nullable
//            public override fun <S : HumanoidRenderState?> getGeoArmorRenderer(
//                @Nullable renderState: S?, itemStack: ItemStack?, equipmentSlot: EquipmentSlot?,
//                type: EquipmentClientInfo.LayerType?, @Nullable original: HumanoidModel<S?>?
//            ): ExampleArmorRenderer<*>? {
//                // Important that we do this. If we just instantiate it directly in the field it can cause incompatibilities with some mods.
//                if (this.renderer == null) this.renderer = ExampleArmorRenderer()
//
//                return this.renderer
//            }
        })
    }
}