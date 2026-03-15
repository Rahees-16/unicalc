package com.rahees.unicalc.ui.screens.currency

data class CurrencyInfo(
    val code: String,
    val name: String,
    val flag: String
)

object CurrencyData {
    val currencies = listOf(
        CurrencyInfo("USD", "US Dollar", "\uD83C\uDDFA\uD83C\uDDF8"),
        CurrencyInfo("EUR", "Euro", "\uD83C\uDDEA\uD83C\uDDFA"),
        CurrencyInfo("GBP", "British Pound", "\uD83C\uDDEC\uD83C\uDDE7"),
        CurrencyInfo("JPY", "Japanese Yen", "\uD83C\uDDEF\uD83C\uDDF5"),
        CurrencyInfo("AUD", "Australian Dollar", "\uD83C\uDDE6\uD83C\uDDFA"),
        CurrencyInfo("CAD", "Canadian Dollar", "\uD83C\uDDE8\uD83C\uDDE6"),
        CurrencyInfo("CHF", "Swiss Franc", "\uD83C\uDDE8\uD83C\uDDED"),
        CurrencyInfo("CNY", "Chinese Yuan", "\uD83C\uDDE8\uD83C\uDDF3"),
        CurrencyInfo("INR", "Indian Rupee", "\uD83C\uDDEE\uD83C\uDDF3"),
        CurrencyInfo("MXN", "Mexican Peso", "\uD83C\uDDF2\uD83C\uDDFD"),
        CurrencyInfo("BRL", "Brazilian Real", "\uD83C\uDDE7\uD83C\uDDF7"),
        CurrencyInfo("ZAR", "South African Rand", "\uD83C\uDDFF\uD83C\uDDE6"),
        CurrencyInfo("KRW", "South Korean Won", "\uD83C\uDDF0\uD83C\uDDF7"),
        CurrencyInfo("SGD", "Singapore Dollar", "\uD83C\uDDF8\uD83C\uDDEC"),
        CurrencyInfo("HKD", "Hong Kong Dollar", "\uD83C\uDDED\uD83C\uDDF0"),
        CurrencyInfo("NOK", "Norwegian Krone", "\uD83C\uDDF3\uD83C\uDDF4"),
        CurrencyInfo("SEK", "Swedish Krona", "\uD83C\uDDF8\uD83C\uDDEA"),
        CurrencyInfo("DKK", "Danish Krone", "\uD83C\uDDE9\uD83C\uDDF0"),
        CurrencyInfo("NZD", "New Zealand Dollar", "\uD83C\uDDF3\uD83C\uDDFF"),
        CurrencyInfo("THB", "Thai Baht", "\uD83C\uDDF9\uD83C\uDDED"),
        CurrencyInfo("AED", "UAE Dirham", "\uD83C\uDDE6\uD83C\uDDEA"),
        CurrencyInfo("SAR", "Saudi Riyal", "\uD83C\uDDF8\uD83C\uDDE6"),
        CurrencyInfo("TRY", "Turkish Lira", "\uD83C\uDDF9\uD83C\uDDF7"),
        CurrencyInfo("RUB", "Russian Ruble", "\uD83C\uDDF7\uD83C\uDDFA"),
        CurrencyInfo("PLN", "Polish Zloty", "\uD83C\uDDF5\uD83C\uDDF1")
    )

    val popularCodes = listOf("USD", "EUR", "GBP", "JPY", "INR", "CAD", "AUD", "CHF")

    fun getInfo(code: String): CurrencyInfo? = currencies.find { it.code == code }
}
