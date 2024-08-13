package net.mysticforge.quellcraft.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.mysticforge.quellcraft.mixinimpl.EntityMixinImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V"
            ),
            method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
    )
    private void move(Block instance, BlockView world, Entity entity) {
        EntityMixinImpl.move((Entity) (Object) this, instance, world, entity);
    }
}