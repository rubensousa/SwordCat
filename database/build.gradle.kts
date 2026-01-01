import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rubensousa.swordcat.database"
    compileSdk {
        version = release(36)
    }
    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.generateKotlin", "true")
    }
}

dependencies {
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.room)
    ksp(libs.androidx.room.compiler)
    implementation(project(":domain"))
    testImplementation(libs.bundles.test.unit)
    androidTestImplementation(testFixtures(project(":domain")))
    androidTestImplementation(libs.bundles.test.unit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.room.testing)
}