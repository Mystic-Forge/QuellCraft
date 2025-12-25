package net.mysticforge.quellcraft.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.Properties.class)
public interface AbstractBlockSettingsAccessor {
    @Accessor("offsetFunction")
    void setOffsetFunction(BlockBehaviour.OffsetFunction offsetter);
}
