@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.appDistribution)
}

android {
    namespace = "kiwi.orbit.compose.catalog"

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "kiwi.orbit.compose.catalog"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionName = project.findProperty("VERSION_NAME").toString()

        val bits = versionName!!.split('.').map { it.toInt() }
        check(bits.size == 3)
        versionCode = bits[0] * 1_00_00 + bits[1] * 1_00 + bits[2]

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val propertiesFile = File("$projectDir/../release/signing.properties")
            if (!propertiesFile.exists()) return@create

            val properties = loadProperties(propertiesFile.absolutePath)
            storeFile = properties.getProperty("store.path")?.let { file(it) }
            storePassword = properties.getProperty("store.password")
            keyAlias = properties.getProperty("key.alias")
            keyPassword = properties.getProperty("key.password")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        create("ci") {
            matchingFallbacks.add("release")
            applicationIdSuffix = ".ci"
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            firebaseAppDistribution {
                appId = "1:442962334472:android:3a8ac71491745d2d37d2bd"
                artifactType = "APK"
                groups = "main"
                serviceCredentialsFile = "release/service-account.json"
            }
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    applicationVariants.configureEach {
        outputs.configureEach {
            if (buildType.name == "ci") {
                this as ApkVariantOutputImpl
                versionNameOverride = "${versionName}-${System.getenv("GITHUB_REF_NAME")}"
                versionCodeOverride = System.getenv("GITHUB_RUN_NUMBER")?.toInt() ?: 1
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    lint {
        disable.add("ObsoleteLintCustomCheck")
        baseline = file("lint-baseline.xml")
        abortOnError = true
        warningsAsErrors = true
    }
}

kotlinter {
    reporters = arrayOf("json")
    experimentalRules = true
}

dependencies {
    implementation(projects.ui)
    implementation(projects.icons)
    implementation(projects.illustrations)

    implementation(libs.androidx.core)
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.activityComposeCatalog)

    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    implementation(libs.compose.layout)
    implementation(libs.compose.material)
    implementation(libs.compose.materialIconsExtended)
    implementation(libs.compose.navigation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtimeLivedata)
    implementation(libs.compose.toolingPreview)

    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    implementation(libs.coil)
    implementation(libs.accompanist.systemController)
    implementation(libs.accompanist.pager)
    implementation(libs.kiwi.navigationComposeTyped)

    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.androidx.customView)
    debugImplementation(libs.androidx.customViewPoolingContainer)

    lintChecks(project(":lint"))
}
