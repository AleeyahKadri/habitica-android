import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}
apply(plugin = "kotlin-android")

val rootExtra = rootProject.extra
val target_sdk: Int by rootExtra
val wearos_target_sdk: Int by rootExtra
val min_sdk: Int by rootExtra
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
val daggerhilt_version: String by rootExtra
val kotlin_version: String by rootExtra
val mockk_version: String by rootExtra
val kotest_version: String by rootExtra
val appcompat_version: String by rootExtra

android {
    compileSdk = target_sdk

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }

    defaultConfig {
        applicationId = "com.habitrpg.android.habitica"
        minSdk = 26
        targetSdk = wearos_target_sdk
        compileSdk = target_sdk
        versionCode = app_version_code + 1
        versionName = "${app_version_name}w"
        buildConfigField("String", "TESTING_LEVEL", "\"production\"")

        val formattedDate = java.text.SimpleDateFormat("yyMMdd").format(java.util.Date())
        setProperty("archivesBaseName", "Habitica-WearOS-$formattedDate$versionCode")
    }

    signingConfigs {
        create("release")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            extra.set("enableCrashlytics", false)
            extra.set("alwaysUpdateBuildId", false)
            resValue("string", "app_name", "Habitica Debug")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "app_name", "Habitica")
        }
    }

    bundle {
        language {
            // Specifies that the app bundle should not support
            // configuration APKs for language resources. These
            // resources are instead packaged with each base and
            // dynamic feature APK.
            enableSplit = false
        }
    }

    flavorDimensions.add("buildType")

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
            buildConfigField("String", "TESTING_LEVEL", "\"beta\"")
            dimension = "buildType"
            versionCode = app_version_code + 3
        }

        create("prod") {
            buildConfigField("String", "TESTING_LEVEL", "\"production\"")
            dimension = "buildType"
            versionCode = app_version_code + 1
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    namespace = "com.habitrpg.android.habitica"
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "../common/libs")))

    implementation("androidx.core:core-ktx:$core_ktx_version")
    implementation("com.google.android.gms:play-services-wearable:$play_wearables_version")
    implementation("androidx.recyclerview:recyclerview:$recyclerview_version")
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear:wear-input:1.1.0")

    //Networking
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")

    //REST API handling
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

android.testOptions {
    unitTests.all {
        it.useJUnitPlatform()
    }
}

val HRPG_PROPS_FILE = File(projectDir.absolutePath + "/../habitica.properties")
if (HRPG_PROPS_FILE.canRead()) {
    val HRPG_PROPS = Properties()
    HRPG_PROPS.load(FileInputStream(HRPG_PROPS_FILE))

    android.buildTypes.configureEach {
        HRPG_PROPS.forEach { property ->
            buildConfigField("String", property.key as String, "\"${property.value}\"")
        }
    }
} else {
    throw MissingResourceException("habitica.properties not found", "", "")
}

val props = Properties()
val propFile = File("signingrelease.properties")
if (propFile.canRead()) {
    props.load(FileInputStream(propFile))

    if (props.containsKey("STORE_FILE") && props.containsKey("STORE_PASSWORD") &&
            props.containsKey("KEY_ALIAS") && props.containsKey("KEY_PASSWORD")) {
        android.signingConfigs.getByName("release").storeFile = file(props["STORE_FILE"] as String)
        android.signingConfigs.getByName("release").storePassword = props["STORE_PASSWORD"] as String
        android.signingConfigs.getByName("release").keyAlias = props["KEY_ALIAS"] as String
        android.signingConfigs.getByName("release").keyPassword = props["KEY_PASSWORD"] as String
    } else {
        println("signing.properties found but some entries are missing")
        android.buildTypes.getByName("release").signingConfig = null
    }
} else {
    println("signing.properties not found")
    android.buildTypes.getByName("release").signingConfig = null
}
