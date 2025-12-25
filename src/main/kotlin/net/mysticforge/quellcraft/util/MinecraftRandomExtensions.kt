package net.mysticforge.quellcraft.util

import net.minecraft.util.RandomSource

fun RandomSource.nextDouble(range: OpenEndRange<Double>) = nextDouble() * (range.endExclusive - range.start) + range.start