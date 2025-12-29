package `in`.co.abdev.greenguardian

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform