package com.rahees.unicalc.domain

object UnitConverter {
    fun convert(value: Double, from: UnitDef, to: UnitDef): Double {
        val baseValue = from.toBase(value)
        return to.fromBase(baseValue)
    }
}
