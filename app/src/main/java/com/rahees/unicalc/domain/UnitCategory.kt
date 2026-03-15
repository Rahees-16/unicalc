package com.rahees.unicalc.domain

enum class UnitCategory(val displayName: String, val icon: String) {
    LENGTH("Length", "straighten"),
    WEIGHT("Weight", "fitness_center"),
    TEMPERATURE("Temperature", "thermostat"),
    AREA("Area", "square_foot"),
    VOLUME("Volume", "water_drop"),
    SPEED("Speed", "speed"),
    TIME("Time", "schedule"),
    DATA("Digital Storage", "storage"),
    PRESSURE("Pressure", "compress"),
    ENERGY("Energy", "bolt"),
    POWER("Power", "power"),
    FUEL("Fuel Economy", "local_gas_station"),
    ANGLE("Angle", "architecture"),
    FREQUENCY("Frequency", "graphic_eq"),
    COOKING("Cooking", "restaurant"),
    TORQUE("Torque", "build"),
    DENSITY("Density", "science"),
    FORCE("Force", "fitness_center"),
    ILLUMINANCE("Illuminance", "light_mode")
}
