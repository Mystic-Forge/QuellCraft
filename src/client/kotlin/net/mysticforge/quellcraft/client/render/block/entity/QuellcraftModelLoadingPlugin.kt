package net.mysticforge.quellcraft.client.render.block.entity

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.mysticforge.quellcraft.Quellcraft

@Environment(EnvType.CLIENT)
object QuellcraftModelLoadingPlugin : ModelLoadingPlugin {
    private val unbakedModelsById = listOf(
        UnbakedModelGetter(predicate = { it.namespace == Quellcraft.MOD_ID && it.path == "crystal_cluster" }) { id, original ->
            CrystalBlockModel(id, original)
        },
    )

    data class UnbakedModelGetter(
        val predicate: (ModelIdentifier) -> Boolean,
        val getter: (ModelIdentifier, UnbakedModel) -> UnbakedModel
    )

    override fun onInitializeModelLoader(pluginContext: ModelLoadingPlugin.Context) {
        println("Loading Quellcraft models")

        pluginContext.modifyModelOnLoad().register { original, context ->
            (context.id() as? ModelIdentifier)?.let { modelIdentifier ->
                unbakedModelsById
                    .find { it.predicate(modelIdentifier) }
                    ?.getter
                    ?.invoke(modelIdentifier, original)
            } ?: original
        }
    }
}