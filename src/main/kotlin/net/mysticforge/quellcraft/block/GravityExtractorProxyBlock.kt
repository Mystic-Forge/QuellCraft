package net.mysticforge.quellcraft.block

import net.minecraft.world.level.block.state.properties.IntegerProperty

class GravityExtractorProxyBlock(settings: Properties) : QuellcraftProxyBlock(settings) {
    override val sourceBlock: QuellcraftBigBlock
        get() = ModBlocks.gravityExtractor

    override val offsetProperty: IntegerProperty
        get() = originOffsetProperty

    companion object {
        val originOffsetProperty: IntegerProperty = IntegerProperty.create("origin_offset", 0, ModBlocks.gravityExtractor.proxyOffsets.size - 1)
    }
}