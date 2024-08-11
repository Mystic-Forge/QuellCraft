package net.mysticforge.quellcraft.util

import net.minecraft.util.math.random.Random

fun Random.nextDouble(range: OpenEndRange<Double>) = nextDouble() * (range.endExclusive - range.start) + range.start