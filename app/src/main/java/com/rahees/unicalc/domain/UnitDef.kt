package com.rahees.unicalc.domain

data class UnitDef(
    val name: String,
    val symbol: String,
    val toBase: (Double) -> Double,
    val fromBase: (Double) -> Double
)
