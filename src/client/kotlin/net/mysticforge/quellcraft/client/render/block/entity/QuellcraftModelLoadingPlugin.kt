package net.mysticforge.quellcraft.client.render.block.entity

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier.OnLoad
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.mysticforge.quellcraft.Quellcraft

@Environment(EnvType.CLIENT)
class QuellcraftModelLoadingPlugin : ModelLoadingPlugin {
    override fun onInitializeModelLoader(pluginContext: ModelLoadingPlugin.Context) {
        println("Loading Quellcraft models")

        // We want to add our model when the models are loaded
        pluginContext.modifyModelOnLoad().register(OnLoad { original: UnbakedModel?, context: OnLoad.Context ->
            // This is called for every model that is loaded, so make sure we only target ours
            val id = context.id()
            if(context.id().namespace == "quellcraft" && id is ModelIdentifier) {
                println(id.variant)
                CrystalBlockModel()
            } else {
                original
            }
        })
    }

    companion object {
        val CRYSTAL_CLUSTER_MODEL: ModelIdentifier = ModelIdentifier(Quellcraft.MOD_ID, "crystal_cluster", "facing=up,waterlogged=false")
    }
}