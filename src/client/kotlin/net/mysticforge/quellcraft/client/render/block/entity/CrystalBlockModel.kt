package net.mysticforge.quellcraft.client.render.block.entity

import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.state.property.QuellType
import java.util.function.Function
import java.util.function.Supplier


class CrystalBlockModel : UnbakedModel, BakedModel, FabricBakedModel {
    private val spriteIds: Map<QuellType, SpriteIdentifier> = mapOf(
        QuellType.NONE to SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("quellcraft:block/spectrite_cluster")),
        QuellType.VOID to SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("quellcraft:block/void_cluster"))
    )

    private var quadsMap : MutableMap<QuellType, List<BakedQuad>> = mutableMapOf()
    private val material = RendererAccess.INSTANCE.renderer!!.materialFinder().find()
    private var transformation: ModelTransformation = ModelTransformation.NONE

    override fun getModelDependencies(): Collection<Identifier> {
        return listOf() // This model does not depend on other models.
    }

    override fun setParents(modelLoader: Function<Identifier?, UnbakedModel?>?) {
        // This is related to model parents, it's not required for our use case
    }

    override fun bake(baker: Baker, textureGetter: Function<SpriteIdentifier?, Sprite?>, rotationContainer: ModelBakeSettings, modelId: Identifier): BakedModel {
        // Get the sprites
        for (quellType in QuellType.entries) {
            val sprite = textureGetter.apply(spriteIds[quellType])
            val model = baker.getOrLoadModel(Identifier(Quellcraft.MOD_ID, "block/crystal_cluster_model"))
            val bakedModel = model.bake(baker, { _ -> sprite }, rotationContainer, modelId)

            if(bakedModel is BasicBakedModel) {
                quadsMap[quellType] = bakedModel.getQuads(null, null, null)
                transformation = bakedModel.transformation
            }
        }

        return this
    }

    override fun getQuads(state: BlockState?, face: Direction?, random: Random?): List<BakedQuad> {
        return listOf()
    }

    override fun useAmbientOcclusion(): Boolean {
        return true
    }

    override fun isBuiltin(): Boolean {
        return false
    }

    override fun hasDepth(): Boolean {
        return false
    }

    override fun isSideLit(): Boolean {
        return false
    }

    override fun getParticleSprite(): Sprite {
        return spriteIds[QuellType.NONE]!!.sprite
    }

    override fun getTransformation(): ModelTransformation {
        return transformation
    }

    override fun getOverrides(): ModelOverrideList {
        return ModelOverrideList.EMPTY
    }

    override fun isVanillaAdapter(): Boolean {
        return false
    }

    override fun emitBlockQuads(blockRenderView: BlockRenderView, blockState: BlockState?, blockPos: BlockPos?, supplier: Supplier<Random?>?, renderContext: RenderContext) {
        val quellType = blockRenderView.getBlockEntityRenderData(blockPos)
        println("Quell type: $quellType") // Still always outputs: Quell type: none
        val quads = quadsMap[quellType]
        if(quads != null) {
            for(quad in quads) {
                renderContext.emitter.fromVanilla(quad, material, null).emit()
            }
        }
    }

    override fun emitItemQuads(itemStack: ItemStack, supplier: Supplier<Random?>?, renderContext: RenderContext) {
//        val entity =  blockRenderView?.getBlockEntity(blockPos)
//        if(entity is CrystalBlockEntity) {
            val quellType = QuellType.VOID
            val quads = quadsMap[quellType]
            if(quads != null) {
                for(quad in quads) {
                    renderContext.emitter.fromVanilla(quad, material, null).emit()
                }
            }
//        }
    }
}