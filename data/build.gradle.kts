import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.5.0"
    id("com.squareup.sqldelight")
}

version = "1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
        else -> ::iosX64
    }

    iosTarget("ios") {}

    cocoapods {
        summary = "The data module for the crypto app"
        homepage = "https://github.com/OHoussein/android-crypto-app"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "kmmdata"
        }
        //podfile = project.file("../iosApp/Podfile")
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(path = ":domain"))
                implementation(Libs.ktorCore)
                implementation(Libs.coroutinesCore)
                implementation(Libs.ktorSerialization)
                implementation(Libs.ktorLogging)
                implementation(Libs.napier)
                implementation(Libs.sqlDelightRuntime)
                implementation(Libs.sqlDelightCoroutine)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Libs.ktorAndroid)
                implementation(Libs.coroutinesAndroid)
                implementation(Libs.sqlDelightAndroid)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Libs.ktorIOS)
                implementation(Libs.sqlDelightIOS)
            }
        }
        val iosTest by getting
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            isStatic = false
        }
    }
}

sqldelight {
    database("CryptoDB") {
        packageName = "dev.ohoussein.cryptoapp.kmmdata"
    }
    //linkSqlite = true
}

android {
    compileSdk = AndroidSdk.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk
    }
}