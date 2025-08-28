//package net.mysticforge.quellcraft.client.render.block.entity
//
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
//import net.minecraft.client.render.model.UnbakedModel
//import net.minecraft.util.Identifier
//import net.mysticforge.quellcraft.Quellcraft
//
//@Environment(EnvType.CLIENT)
//object QuellcraftModelLoadingPlugin : ModelLoadingPlugin {
//    private val unbakedModelsById = listOf(
//        UnbakedModelGetter(predicate = { it.namespace == Quellcraft.MOD_ID && it.path == "crystal_cluster" }) { id, original ->
//            CrystalBlockModel(id, original)
//        },
//    )
//
//    data class UnbakedModelGetter(
//        val predicate: (Identifier) -> Boolean,
//        val getter: (Identifier, UnbakedModel) -> UnbakedModel
//    )
//
//    override fun initialize(context: ModelLoadingPlugin.Context) {
//        println("Loading Quellcraft models")
//
//        context.modifyModelOnLoad().register { original, context ->
//            unbakedModelsById
//                .find { it.predicate(context.id()) }
//                ?.getter
//                ?.invoke(context.id(), original)
//        }
//    }
//}