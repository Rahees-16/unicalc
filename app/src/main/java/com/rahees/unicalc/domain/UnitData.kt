package com.rahees.unicalc.domain

object UnitData {

    val units: Map<UnitCategory, List<UnitDef>> = mapOf(

        UnitCategory.LENGTH to listOf(
            UnitDef("Meter", "m", { it }, { it }),
            UnitDef("Kilometer", "km", { it * 1000.0 }, { it / 1000.0 }),
            UnitDef("Centimeter", "cm", { it * 0.01 }, { it / 0.01 }),
            UnitDef("Millimeter", "mm", { it * 0.001 }, { it / 0.001 }),
            UnitDef("Mile", "mi", { it * 1609.344 }, { it / 1609.344 }),
            UnitDef("Yard", "yd", { it * 0.9144 }, { it / 0.9144 }),
            UnitDef("Foot", "ft", { it * 0.3048 }, { it / 0.3048 }),
            UnitDef("Inch", "in", { it * 0.0254 }, { it / 0.0254 }),
            UnitDef("Nautical Mile", "nmi", { it * 1852.0 }, { it / 1852.0 })
        ),

        UnitCategory.WEIGHT to listOf(
            UnitDef("Kilogram", "kg", { it }, { it }),
            UnitDef("Gram", "g", { it * 0.001 }, { it / 0.001 }),
            UnitDef("Milligram", "mg", { it * 0.000001 }, { it / 0.000001 }),
            UnitDef("Metric Ton", "t", { it * 1000.0 }, { it / 1000.0 }),
            UnitDef("Pound", "lb", { it * 0.45359237 }, { it / 0.45359237 }),
            UnitDef("Ounce", "oz", { it * 0.028349523125 }, { it / 0.028349523125 }),
            UnitDef("Stone", "st", { it * 6.35029318 }, { it / 6.35029318 })
        ),

        UnitCategory.TEMPERATURE to listOf(
            UnitDef("Celsius", "\u00B0C", { it }, { it }),
            UnitDef("Fahrenheit", "\u00B0F", { (it - 32.0) * 5.0 / 9.0 }, { it * 9.0 / 5.0 + 32.0 }),
            UnitDef("Kelvin", "K", { it - 273.15 }, { it + 273.15 })
        ),

        UnitCategory.AREA to listOf(
            UnitDef("Square Meter", "m\u00B2", { it }, { it }),
            UnitDef("Square Kilometer", "km\u00B2", { it * 1_000_000.0 }, { it / 1_000_000.0 }),
            UnitDef("Square Centimeter", "cm\u00B2", { it * 0.0001 }, { it / 0.0001 }),
            UnitDef("Hectare", "ha", { it * 10_000.0 }, { it / 10_000.0 }),
            UnitDef("Acre", "ac", { it * 4046.8564224 }, { it / 4046.8564224 }),
            UnitDef("Square Foot", "ft\u00B2", { it * 0.09290304 }, { it / 0.09290304 }),
            UnitDef("Square Inch", "in\u00B2", { it * 0.00064516 }, { it / 0.00064516 }),
            UnitDef("Square Yard", "yd\u00B2", { it * 0.83612736 }, { it / 0.83612736 }),
            UnitDef("Square Mile", "mi\u00B2", { it * 2_589_988.110336 }, { it / 2_589_988.110336 })
        ),

        UnitCategory.VOLUME to listOf(
            UnitDef("Liter", "L", { it }, { it }),
            UnitDef("Milliliter", "mL", { it * 0.001 }, { it / 0.001 }),
            UnitDef("Cubic Meter", "m\u00B3", { it * 1000.0 }, { it / 1000.0 }),
            UnitDef("Gallon (US)", "gal", { it * 3.785411784 }, { it / 3.785411784 }),
            UnitDef("Quart (US)", "qt", { it * 0.946352946 }, { it / 0.946352946 }),
            UnitDef("Pint (US)", "pt", { it * 0.473176473 }, { it / 0.473176473 }),
            UnitDef("Cup (US)", "cup", { it * 0.2365882365 }, { it / 0.2365882365 }),
            UnitDef("Fluid Ounce (US)", "fl oz", { it * 0.0295735295625 }, { it / 0.0295735295625 }),
            UnitDef("Tablespoon", "tbsp", { it * 0.01478676478125 }, { it / 0.01478676478125 }),
            UnitDef("Teaspoon", "tsp", { it * 0.00492892159375 }, { it / 0.00492892159375 })
        ),

        UnitCategory.SPEED to listOf(
            UnitDef("Meters per Second", "m/s", { it }, { it }),
            UnitDef("Kilometers per Hour", "km/h", { it / 3.6 }, { it * 3.6 }),
            UnitDef("Miles per Hour", "mph", { it * 0.44704 }, { it / 0.44704 }),
            UnitDef("Knot", "kn", { it * 0.514444 }, { it / 0.514444 }),
            UnitDef("Feet per Second", "ft/s", { it * 0.3048 }, { it / 0.3048 })
        ),

        UnitCategory.TIME to listOf(
            UnitDef("Second", "s", { it }, { it }),
            UnitDef("Minute", "min", { it * 60.0 }, { it / 60.0 }),
            UnitDef("Hour", "h", { it * 3600.0 }, { it / 3600.0 }),
            UnitDef("Day", "d", { it * 86400.0 }, { it / 86400.0 }),
            UnitDef("Week", "wk", { it * 604800.0 }, { it / 604800.0 }),
            UnitDef("Month (30 days)", "mo", { it * 2_592_000.0 }, { it / 2_592_000.0 }),
            UnitDef("Year (365 days)", "yr", { it * 31_536_000.0 }, { it / 31_536_000.0 }),
            UnitDef("Millisecond", "ms", { it * 0.001 }, { it / 0.001 })
        ),

        UnitCategory.DATA to listOf(
            UnitDef("Byte", "B", { it }, { it }),
            UnitDef("Kilobyte", "KB", { it * 1024.0 }, { it / 1024.0 }),
            UnitDef("Megabyte", "MB", { it * 1_048_576.0 }, { it / 1_048_576.0 }),
            UnitDef("Gigabyte", "GB", { it * 1_073_741_824.0 }, { it / 1_073_741_824.0 }),
            UnitDef("Terabyte", "TB", { it * 1_099_511_627_776.0 }, { it / 1_099_511_627_776.0 }),
            UnitDef("Bit", "bit", { it / 8.0 }, { it * 8.0 }),
            UnitDef("Kilobit", "Kbit", { it * 128.0 }, { it / 128.0 }),
            UnitDef("Megabit", "Mbit", { it * 131_072.0 }, { it / 131_072.0 }),
            UnitDef("Gigabit", "Gbit", { it * 134_217_728.0 }, { it / 134_217_728.0 })
        ),

        UnitCategory.PRESSURE to listOf(
            UnitDef("Pascal", "Pa", { it }, { it }),
            UnitDef("Kilopascal", "kPa", { it * 1000.0 }, { it / 1000.0 }),
            UnitDef("Bar", "bar", { it * 100_000.0 }, { it / 100_000.0 }),
            UnitDef("Atmosphere", "atm", { it * 101_325.0 }, { it / 101_325.0 }),
            UnitDef("PSI", "psi", { it * 6894.757293168 }, { it / 6894.757293168 }),
            UnitDef("mmHg", "mmHg", { it * 133.322387415 }, { it / 133.322387415 }),
            UnitDef("Torr", "Torr", { it * 133.322368421 }, { it / 133.322368421 })
        ),

        UnitCategory.ENERGY to listOf(
            UnitDef("Joule", "J", { it }, { it }),
            UnitDef("Kilojoule", "kJ", { it * 1000.0 }, { it / 1000.0 }),
            UnitDef("Calorie", "cal", { it * 4.184 }, { it / 4.184 }),
            UnitDef("Kilocalorie", "kcal", { it * 4184.0 }, { it / 4184.0 }),
            UnitDef("Watt-hour", "Wh", { it * 3600.0 }, { it / 3600.0 }),
            UnitDef("Kilowatt-hour", "kWh", { it * 3_600_000.0 }, { it / 3_600_000.0 }),
            UnitDef("BTU", "BTU", { it * 1055.05585262 }, { it / 1055.05585262 }),
            UnitDef("Electronvolt", "eV", { it * 1.602176634e-19 }, { it / 1.602176634e-19 })
        ),

        UnitCategory.POWER to listOf(
            UnitDef("Watt", "W", { it }, { it }),
            UnitDef("Kilowatt", "kW", { it * 1000.0 }, { it / 1000.0 }),
            UnitDef("Megawatt", "MW", { it * 1_000_000.0 }, { it / 1_000_000.0 }),
            UnitDef("Horsepower", "hp", { it * 745.69987158 }, { it / 745.69987158 }),
            UnitDef("BTU/hour", "BTU/h", { it * 0.29307107017 }, { it / 0.29307107017 })
        ),

        UnitCategory.FUEL to listOf(
            UnitDef("km/L", "km/L", { it }, { it }),
            UnitDef("L/100km", "L/100km",
                { if (it != 0.0) 100.0 / it else 0.0 },
                { if (it != 0.0) 100.0 / it else 0.0 }
            ),
            UnitDef("mpg (US)", "mpg",
                { it * 0.425143707 },
                { it / 0.425143707 }
            ),
            UnitDef("mpg (UK)", "mpg (UK)",
                { it * 0.354006190 },
                { it / 0.354006190 }
            )
        ),

        UnitCategory.ANGLE to listOf(
            UnitDef("Degree", "\u00B0", { it }, { it }),
            UnitDef("Radian", "rad", { it * 180.0 / Math.PI }, { it * Math.PI / 180.0 }),
            UnitDef("Gradian", "grad", { it * 0.9 }, { it / 0.9 }),
            UnitDef("Arcminute", "\u2032", { it / 60.0 }, { it * 60.0 }),
            UnitDef("Arcsecond", "\u2033", { it / 3600.0 }, { it * 3600.0 })
        ),

        UnitCategory.FREQUENCY to listOf(
            UnitDef("Hertz", "Hz", { it }, { it }),
            UnitDef("Kilohertz", "kHz", { it * 1000.0 }, { it / 1000.0 }),
            UnitDef("Megahertz", "MHz", { it * 1_000_000.0 }, { it / 1_000_000.0 }),
            UnitDef("Gigahertz", "GHz", { it * 1_000_000_000.0 }, { it / 1_000_000_000.0 }),
            UnitDef("RPM", "RPM", { it / 60.0 }, { it * 60.0 })
        ),

        UnitCategory.COOKING to listOf(
            UnitDef("Milliliter", "mL", { it }, { it }),
            UnitDef("Teaspoon", "tsp", { it * 4.92892 }, { it / 4.92892 }),
            UnitDef("Tablespoon", "tbsp", { it * 14.7868 }, { it / 14.7868 }),
            UnitDef("Cup", "cup", { it * 236.588 }, { it / 236.588 }),
            UnitDef("Fluid Ounce", "fl oz", { it * 29.5735 }, { it / 29.5735 }),
            UnitDef("Pinch", "pinch", { it * 0.308057 }, { it / 0.308057 })
        )
    )
}
