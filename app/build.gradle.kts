import java.util.Properties
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
val signProps = Properties().apply {
    val f = file(System.getenv("SHARPQR_SIGNING_PROPS") ?: "/workspace/keys/sharpqrlauncher-signing.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
android {
    namespace = "com.github.toober77.sharpqrlauncher"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.github.toober77.sharpqrlauncher"
        minSdk = 28
        targetSdk = 35
        versionCode = 4
        versionName = "1.0.3"
    }
    signingConfigs {
        create("release") {
            val sf = signProps.getProperty("storeFile") ?: System.getenv("SHARPQR_STORE_FILE")
            if (sf != null) {
                storeFile = file(sf)
                storePassword = signProps.getProperty("storePassword") ?: System.getenv("SHARPQR_STORE_PASSWORD")
                keyAlias = signProps.getProperty("keyAlias") ?: System.getenv("SHARPQR_KEY_ALIAS")
                keyPassword = signProps.getProperty("keyPassword") ?: System.getenv("SHARPQR_KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            val rc = signingConfigs.getByName("release")
            if (rc.storeFile != null) signingConfig = rc
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
