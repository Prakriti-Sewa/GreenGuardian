package `in`.co.abdev.greenguardian.data.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null
)
