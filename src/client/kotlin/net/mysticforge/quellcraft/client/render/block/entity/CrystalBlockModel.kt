package net.mysticforge.quellcraft.client.render.block.entity

import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.JsonUnbakedModel
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.render.model.json.WeightedUnbakedModel
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import net.mysticforge.quellcraft.quellmanagement.QuellContent
import net.mysticforge.quellcraft.Quellcraft
import net.mysticforge.quellcraft.block.entity.CrystalBlockEntity
import net.mysticforge.quellcraft.quellmanagement.readQuellContent
import net.mysticforge.quellcraft.state.property.QuellType
import org.joml.Vector3f
import java.util.function.Function
import java.util.function.Supplier
import kotlin.math.roundToInt


class CrystalBlockModel(val id: ModelIdentifier, val original: UnbakedModel) : UnbakedModel, BakedModel, FabricBakedModel {
    // Automatically maps all quell types to crystal cluster sprite identifiers. Place crystal cluster textures in assets/quellcraft/block/<quell_type>_cluster.png
    private val spriteIds: Map<QuellType?, SpriteIdentifier> =
        (QuellType.entries + null).associateWith {
            SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("quellcraft:block/${it ?: "spectrite"}_cluster"))
        }

    private var quadsMap: MutableMap<QuellType?, List<BakedQuad>> = mutableMapOf()
    private val material = RendererAccess.INSTANCE.renderer!!.materialFinder().find()
    private var transformation: ModelTransformation = ModelTransformation.NONE

    override fun getModelDependencies(): Collection<Identifier> = listOf()

    override fun setParents(modelLoader: Function<Identifier?, UnbakedModel?>?) = Unit

    override fun bake(baker: Baker, textureGetter: Function<SpriteIdentifier?, Sprite?>, rotationContainer: ModelBakeSettings, modelId: Identifier): BakedModel {
        val modelRotation = if (original is WeightedUnbakedModel) {
            val euler = Vector3f(0.0f, 0.0f, 0.0f)
            original.variants.first().rotation.leftRotation.getEulerAnglesXYZ(euler)

            ModelRotation.get(
                -Math.toDegrees(euler.x().toDouble()).roundToInt(),
                -Math.toDegrees(euler.z().toDouble()).roundToInt()
            )
        } else {
            rotationContainer
        }

        println("Baking model $id with rotation $modelRotation")

        for (quellType in QuellType.entries + null) {
            val sprite = textureGetter.apply(spriteIds[quellType])
            val model = baker.getOrLoadModel(Identifier(Quellcraft.MOD_ID, "block/crystal_cluster_model"))
            val bakedModel = model.bake(baker, { _ -> sprite }, modelRotation, modelId)

            if (bakedModel is BasicBakedModel) {
                quadsMap[quellType] = bakedModel.getQuads(null, null, null)
            }

            if (original is JsonUnbakedModel) {
                transformation = original.getTransformations()
            }
        }

        return this
    }

    override fun getQuads(state: BlockState?, face: Direction?, random: Random?): List<BakedQuad> = listOf()

    override fun useAmbientOcclusion(): Boolean = true

    override fun isBuiltin(): Boolean = false

    override fun hasDepth(): Boolean = false

    override fun isSideLit(): Boolean = false

    override fun getParticleSprite(): Sprite = spriteIds[null]!!.sprite

    override fun getTransformation(): ModelTransformation = transformation

    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY

    override fun isVanillaAdapter(): Boolean = false

    override fun emitBlockQuads(blockRenderView: BlockRenderView, blockState: BlockState?, blockPos: BlockPos?, supplier: Supplier<Random?>?, renderContext: RenderContext) {
        val quellContent = blockRenderView.getBlockEntityRenderData(blockPos) as QuellContent
        val quellType = (quellContent as? QuellContent.Filled)?.quellType
        val quads = quadsMap[quellType]
        if (quads != null) {
            for (quad in quads) {
                renderContext.emitter.fromVanilla(quad, material, null).emit()
            }
        }
    }

    override fun emitItemQuads(itemStack: ItemStack, supplier: Supplier<Random?>?, renderContext: RenderContext) {
        val quellContent = itemStack.nbt?.getCompound("BlockEntityTag")?.readQuellContent(CrystalBlockEntity.NBT_KEY)
        val quellType = (quellContent as? QuellContent.Filled)?.quellType
        val quads = quadsMap[quellType]
        if (quads != null) {
            for (quad in quads) {
                renderContext.emitter.fromVanilla(quad, material, null).emit()
            }
        }
    }
}