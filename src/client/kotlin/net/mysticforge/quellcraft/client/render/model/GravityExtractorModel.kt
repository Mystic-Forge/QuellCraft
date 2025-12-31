package net.mysticforge.quellcraft.client.render.model

import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.*
import net.minecraft.client.renderer.RenderType
import net.mysticforge.quellcraft.block.entity.GravityExtractorEntity
import org.joml.AxisAngle4f
import org.joml.Quaternionf
import org.joml.Vector3f

class GravityExtractorModel(root: ModelPart) : Model(root, RenderType::entitySolid) {
    private val body: ModelPart
    private val arm_middle: ModelPart
    private val arm1: ModelPart
    private val arm2: ModelPart
    private val arm3: ModelPart
    private val arm4: ModelPart
    private val leg1: ModelPart
    private val leg2: ModelPart
    private val leg3: ModelPart
    private val leg4: ModelPart
    private val bb_main: ModelPart

    init {
        this.body = root.getChild("body")
        this.arm_middle = root.getChild("arm_middle")
        this.arm1 = root.getChild("arm1")
        this.arm2 = root.getChild("arm2")
        this.arm3 = root.getChild("arm3")
        this.arm4 = root.getChild("arm4")
        this.leg1 = root.getChild("leg1")
        this.leg2 = root.getChild("leg2")
        this.leg3 = root.getChild("leg3")
        this.leg4 = root.getChild("leg4")
        this.bb_main = root.getChild("bb_main")
    }

    fun doAnim(tickTime: Float, renderState: GravityExtractorEntity.GravityExtractorRenderState) {
        resetPose()
        bb_main.rotateBy(Quaternionf(AxisAngle4f(renderState.getRotation(tickTime), 0f, 1f, 0f)))
        bb_main.offsetPos(Vector3f(0f, -renderState.getYOffset(tickTime), 0f))
    }

    companion object {
        val texturedModelData: LayerDefinition
            get() {
                val modelData = MeshDefinition()
                val PartDefinition: PartDefinition = modelData.getRoot()
                val body: PartDefinition? = PartDefinition.addOrReplaceChild(
                    "body", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0f, -4.0f, -10.0f, 20.0f, 5.0f, 20.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-12.0f, 20.0f, -12.0f, 24.0f, 4.0f, 24.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-8.0f, 16.0f, -8.0f, 16.0f, 4.0f, 16.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-8.0f, 1.0f, -8.0f, 16.0f, 3.0f, 16.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-5.0f, 4.0f, -5.0f, 10.0f, 12.0f, 10.0f, CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, 0.0f)
                )

                val arm_middle: PartDefinition? = PartDefinition.addOrReplaceChild(
                    "arm_middle", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0f, -2.0f, -3.0f, 6.0f, 2.0f, 6.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-2.0f, -5.0f, -2.0f, 4.0f, 3.0f, 4.0f, CubeDeformation(0.0f)), PartPose.offset(0.0f, -4.0f, 0.0f)
                )

                val arm1: PartDefinition = PartDefinition.addOrReplaceChild("arm1", CubeListBuilder.create(), PartPose.offset(-17.35f, -3.75f, 8.0f))

                val cube_r1: PartDefinition? = arm1.addOrReplaceChild(
                    "cube_r1",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-6.9479f, -11.5748f, -2.0f, 2.0f, 4.0f, 4.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-10.9479f, -11.5748f, -2.0f, 4.0f, 5.0f, 4.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-11.9479f, -6.5748f, -3.0f, 6.0f, 5.0f, 6.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(17.35f, -3.0409f, -8.0f, 1.5708f, -0.829f, -1.5708f)
                )

                val arm2: PartDefinition = PartDefinition.addOrReplaceChild("arm2", CubeListBuilder.create(), PartPose.offset(-17.35f, -3.75f, 8.0f))

                val cube_r2: PartDefinition? = arm2.addOrReplaceChild(
                    "cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(2.0f, -7.5f, -2.0f, 2.0f, 4.0f, 4.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-2.0f, -7.5f, -2.0f, 4.0f, 5.0f, 4.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-3.0f, -2.5f, -3.0f, 6.0f, 5.0f, 6.0f, CubeDeformation(0.0f)), PartPose.offsetAndRotation(8.0f, 0.0f, -8.0f, 0.0f, 0.0f, -0.7418f)
                )

                val arm3: PartDefinition = PartDefinition.addOrReplaceChild("arm3", CubeListBuilder.create(), PartPose.offset(1.35f, -3.75f, 8.0f))

                val cube_r3: PartDefinition? = arm3.addOrReplaceChild(
                    "cube_r3",
                    CubeListBuilder.create().texOffs(0, 0).addBox(4.9479f, -11.5748f, -2.0f, 2.0f, 4.0f, 4.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(6.9479f, -11.5748f, -2.0f, 4.0f, 5.0f, 4.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(5.9479f, -6.5748f, -3.0f, 6.0f, 5.0f, 6.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(-1.35f, -3.0409f, -8.0f, -1.5708f, -0.829f, 1.5708f)
                )

                val arm4: PartDefinition = PartDefinition.addOrReplaceChild("arm4", CubeListBuilder.create(), PartPose.offset(1.35f, -3.75f, 8.0f))

                val cube_r4: PartDefinition? = arm4.addOrReplaceChild(
                    "cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -7.5f, -2.0f, 2.0f, 4.0f, 4.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-2.0f, -7.5f, -2.0f, 4.0f, 5.0f, 4.0f, CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-3.0f, -2.5f, -3.0f, 6.0f, 5.0f, 6.0f, CubeDeformation(0.0f)), PartPose.offsetAndRotation(8.0f, 0.0f, -8.0f, 0.0f, 0.0f, 0.7418f)
                )

                val leg1: PartDefinition = PartDefinition.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offset(-21.0f, 18.0f, 8.0f))

                val cube_r5: PartDefinition? = leg1.addOrReplaceChild(
                    "cube_r5",
                    CubeListBuilder.create().texOffs(0, 0).addBox(10.0919f, 9.2073f, -2.0f, 20.0f, 4.0f, 4.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(21.0f, 1.3433f, -8.0f, -2.6486f, 0.639f, 0.7333f)
                )

                val cube_r6: PartDefinition? = leg1.addOrReplaceChild(
                    "cube_r6",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-15.1422f, 5.2426f, -3.0f, 10.0f, 6.0f, 6.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(21.0f, 1.3433f, -8.0f, -2.5261f, -0.5236f, 2.1863f)
                )

                val leg2: PartDefinition = PartDefinition.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offset(-21.0f, 18.0f, 8.0f))

                val cube_r7: PartDefinition? = leg2.addOrReplaceChild(
                    "cube_r7",
                    CubeListBuilder.create().texOffs(0, 0).addBox(10.0919f, 9.2073f, -2.0f, 20.0f, 4.0f, 4.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(21.0f, 1.3433f, -8.0f, -0.493f, 0.639f, 2.4083f)
                )

                val cube_r8: PartDefinition? = leg2.addOrReplaceChild(
                    "cube_r8",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-15.1422f, 5.2426f, -3.0f, 10.0f, 6.0f, 6.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(21.0f, 1.3433f, -8.0f, -0.6155f, -0.5236f, 0.9553f)
                )

                val leg3: PartDefinition = PartDefinition.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offset(5.0f, 18.0f, 8.0f))

                val cube_r9: PartDefinition? = leg3.addOrReplaceChild(
                    "cube_r9",
                    CubeListBuilder.create().texOffs(0, 0).addBox(5.1422f, 5.2426f, -3.0f, 10.0f, 6.0f, 6.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(-5.0f, 1.3433f, -8.0f, 2.5261f, -0.5236f, -2.1863f)
                )

                val cube_r10: PartDefinition? = leg3.addOrReplaceChild(
                    "cube_r10",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-30.0919f, 9.2073f, -2.0f, 20.0f, 4.0f, 4.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(-5.0f, 1.3433f, -8.0f, 2.6486f, 0.639f, -0.7333f)
                )

                val leg4: PartDefinition = PartDefinition.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offset(5.0f, 18.0f, 8.0f))

                val cube_r11: PartDefinition? = leg4.addOrReplaceChild(
                    "cube_r11",
                    CubeListBuilder.create().texOffs(0, 0).addBox(5.1422f, 5.2426f, -3.0f, 10.0f, 6.0f, 6.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(-5.0f, 1.3433f, -8.0f, 0.6155f, -0.5236f, -0.9553f)
                )

                val cube_r12: PartDefinition? = leg4.addOrReplaceChild(
                    "cube_r12",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-30.0919f, 9.2073f, -2.0f, 20.0f, 4.0f, 4.0f, CubeDeformation(0.0f)),
                    PartPose.offsetAndRotation(-5.0f, 1.3433f, -8.0f, 0.493f, 0.639f, -2.4083f)
                )

                val bb_main: PartDefinition? = PartDefinition.addOrReplaceChild(
                    "bb_main",
                    CubeListBuilder.create().texOffs(0, 0).addBox(-7.0f, -48.0f, -7.0f, 14.0f, 14.0f, 14.0f, CubeDeformation(0.0f)),
                    PartPose.offset(0.0f, 24.0f, 0.0f)
                )
                return LayerDefinition.create(modelData, 16, 16)
            }
    }
}