import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = AppVersion.versionName

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
        else -> ::iosX64
    }

    iosTarget("ios") {}

    cocoapods {
        summary = "The domain module for the crypto app"
        homepage = "https://github.com/OHoussein/android-crypto-app"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "domain"
        }
        //podfile = project.file("../iosApp/Podfile")
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.kotlinStdlib)
                implementation(Libs.coroutinesCore)
            }
        }
/*        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
                implementation(kotlin(TestLibs.junit))
                implementation(kotlin(TestLibs.mockito))
                implementation(kotlin(TestLibs.mockitoInline))
            }
        }*/
        val androidMain by getting
/*        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation(TestLibs.junit)
                implementation(TestLibs.mockito)
                implementation(TestLibs.mockitoInline)
            }
        }*/
        val iosMain by getting
       // val iosTest by getting
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            isStatic = false
        }
    }
}

android {
    compileSdk = AndroidSdk.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk
    }
}