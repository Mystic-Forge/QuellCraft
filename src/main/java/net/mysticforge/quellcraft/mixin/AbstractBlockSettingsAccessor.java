package net.mysticforge.quellcraft.mixin;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.Settings.class)
public interface AbstractBlockSettingsAccessor {
    @Accessor("offsetter")
    void setOffsetter(AbstractBlock.Offsetter offsetter);
}
