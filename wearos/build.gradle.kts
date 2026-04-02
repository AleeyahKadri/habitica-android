@file:Suppress("PropertyName")

import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val rootExtra = rootProject.extra

val target_sdk: Int by rootExtra
val wearos_target_sdk: Int by rootExtra
val app_version_code: Int by rootExtra
val app_version_name: String by rootExtra

val core_ktx_version: String by rootExtra
val play_wearables_version: String by rootExtra
val recyclerview_version: String by rootExtra
val okhttp_version: String by rootExtra
val retrofit_version: String by rootExtra
val moshi_version: String by rootExtra
val firebase_bom: String by rootExtra
val lifecycle_version: String by rootExtra
val coroutines_version: String by rootExtra
val preferences_version: String by rootExtra
val play_auth_version: String by rootExtra
val appcompat_version: String by rootExtra
val daggerhilt_version: String by rootExtra
val kotlin_version: String by rootExtra
val mockk_version: String by rootExtra
val kotest_version: String by rootExtra

android {
    compileSdk = target_sdk
    namespace = "com.habitrpg.android.habitica"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.useJUnitPlatform()
            }
        }
        animationsDisabled = true
    }

    defaultConfig {
        applicationId = "com.habitrpg.android.habitica"
        minSdk = 26
        targetSdk = wearos_target_sdk
        versionCode = app_version_code + 1
        versionName = "${app_version_name}w"
        buildConfigField("String", "TESTING_LEVEL", "\"production\"")

        val formattedDate = SimpleDateFormat("yyMMdd", Locale.US).format(Date())
        setProperty("archivesBaseName", "Habitica-WearOS-${formattedDate}${versionCode}")
    }

    signingConfigs {
        create("release")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        jvmToolchain(11)
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            extensions.extraProperties["enableCrashlytics"] = false
            extensions.extraProperties["alwaysUpdateBuildId"] = false
            resValue("string", "app_name", "Habitica Debug")
        }

        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "app_name", "Habitica")
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    flavorDimensions += "buildType"

    productFlavors {
        create("dev") {
            dimension = "buildType"
        }

        create("staff") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"staff\"")
            resValue("string", "app_name", "Habitica Staff")
            versionCode = app_version_code + 9
        }

        create("partners") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"partners\"")
            resValue("string", "app_name", "Habitica")
            versionCode = app_version_code + 7
        }

        create("alpha") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"alpha\"")
            resValue("string", "app_name", "Habitica Alpha")
            versionCode = app_version_code + 5
        }

        create("beta") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"beta\"")
            versionCode = app_version_code + 3
        }

        create("prod") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"production\"")
            versionCode = app_version_code + 1
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "../common/libs")))

    implementation("androidx.core:core-ktx:$core_ktx_version")
    implementation("com.google.android.gms:play-services-wearable:$play_wearables_version")
    implementation("androidx.recyclerview:recyclerview:$recyclerview_version")
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear:wear-input:1.1.0")

    // Networking
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")

    // REST API handling
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version") {
        exclude(module = "okhttp")
    }
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshi_version")

    implementation(platform("com.google.firebase:firebase-bom:$firebase_bom"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")
    implementation("androidx.preference:preference-ktx:$preferences_version")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

    implementation("com.google.android.gms:play-services-auth:$play_auth_version")

    implementation(project(":common"))
    implementation(project(":shared"))
    implementation("androidx.appcompat:appcompat:$appcompat_version")

    implementation("com.google.dagger:hilt-android:$daggerhilt_version")
    kapt("com.google.dagger:hilt-compiler:$daggerhilt_version")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")

    implementation("androidx.core:core-splashscreen:1.1.0-rc01")

    testImplementation("io.mockk:mockk:$mockk_version")
    testImplementation("io.mockk:mockk-android:$mockk_version")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-framework-datatest:$kotest_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
    testImplementation("app.cash.turbine:turbine:0.12.1")
}

repositories {
    mavenCentral()
}

val HRPG_PROPS_FILE = file("../habitica.properties")
if (HRPG_PROPS_FILE.canRead()) {
    val hrpgProps = Properties()
    FileInputStream(HRPG_PROPS_FILE).use { hrpgProps.load(it) }

    android.buildTypes.configureEach {
        hrpgProps.forEach { property ->
            buildConfigField("String", property.key as String, "\"${property.value}\"")
        }
    }
} else {
    error("habitica.properties not found")
}

val signingProps = Properties()
val signingPropFile = file("signingrelease.properties")
if (signingPropFile.canRead()) {
    FileInputStream(signingPropFile).use { signingProps.load(it) }

    if (
        signingProps.containsKey("STORE_FILE") &&
        signingProps.containsKey("STORE_PASSWORD") &&
        signingProps.containsKey("KEY_ALIAS") &&
        signingProps.containsKey("KEY_PASSWORD")
    ) {
        android.signingConfigs.getByName("release").storeFile = file(signingProps["STORE_FILE"].toString())
        android.signingConfigs.getByName("release").storePassword = signingProps["STORE_PASSWORD"].toString()
        android.signingConfigs.getByName("release").keyAlias = signingProps["KEY_ALIAS"].toString()
        android.signingConfigs.getByName("release").keyPassword = signingProps["KEY_PASSWORD"].toString()
    } else {
        println("signing.properties found but some entries are missing")
        android.buildTypes.getByName("release").signingConfig = null
    }
} else {
    println("signing.properties not found")
    android.buildTypes.getByName("release").signingConfig = null
}
