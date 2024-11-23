import java.util.Properties

object Config{
    const val BASE_URL = "\"https://api.themoviedb.org/3/\""
    const val IMAGE_URL = "\"https://image.tmdb.org/t/p/\""
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.tmdb"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tmdb"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        } else {
            throw GradleException("local.properties 文件未找到，請確保文件存在並包含apiKey設置")
        }

        val apiKey = properties.getProperty("apiKey") ?: throw GradleException("local.properties中的apiKey未設置")


        debug {
            buildConfigField("String", "BASE_URL", Config.BASE_URL)
            buildConfigField("String", "IMAGE_URL", Config.IMAGE_URL)
            buildConfigField("String", "API_KEY", "$apiKey")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "BASE_URL", Config.BASE_URL)
            buildConfigField("String", "IMAGE_URL", Config.IMAGE_URL)
            buildConfigField("String", "API_KEY", "$apiKey")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.coil.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit2)
    implementation(libs.okhttp3)
    implementation(libs.okhttp.logging)
    implementation(libs.okio)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2.adapter.rxjava2)
    implementation(libs.gson)
    implementation(libs.androidx.navigation.compose)
}