package net.mysticforge.quellcraft

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment

@Config(name = "quellcraft")
object QuellcraftConfig : ConfigData {
    @Comment("How quickly does a Living Entity lose its Quell Infusion stat. Represents chance to lose 1 Quell Infusion per tick.")
    var quellInfusionDecay = 0.1f

    var turboTreadsMaxBoost = 10f
}