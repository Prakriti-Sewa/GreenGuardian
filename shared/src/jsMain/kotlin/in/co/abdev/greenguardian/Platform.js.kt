package `in`.co.abdev.greenguardian

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()