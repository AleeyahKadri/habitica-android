import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.MissingResourceException
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

val targetSdk: Int by rootExtra
val wearosTargetSdk: Int by rootExtra
val minSdk: Int by rootExtra
val coreKtxVersion: String by rootExtra
val playWearablesVersion: String by rootExtra
val recyclerviewVersion: String by rootExtra
val okhttpVersion: String by rootExtra
val retrofitVersion: String by rootExtra
val moshiVersion: String by rootExtra
val firebaseBom: String by rootExtra
val lifecycleVersion: String by rootExtra
val coroutinesVersion: String by rootExtra
val preferencesVersion: String by rootExtra
val playAuthVersion: String by rootExtra
val daggerhiltVersion: String by rootExtra
val kotlinVersion: String by rootExtra
val mockkVersion: String by rootExtra
val kotestVersion: String by rootExtra

android {
    compileSdk = targetSdk

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }

    defaultConfig {
        applicationId = "com.habitrpg.android.habitica"
        minSdk = 26
        targetSdk = wearosTargetSdk
        compileSdk = targetSdk
        versionCode = (rootExtra["app_version_code"] as Int) + 1
        versionName = "${rootExtra["app_version_name"]}w"
        buildConfigField("String", "TESTING_LEVEL", "\"production\"")

        val formattedDate = SimpleDateFormat("yyMMdd").format(Date())
        setProperty("archivesBaseName", "Habitica-WearOS-${formattedDate}${versionCode}")
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
            extra["enableCrashlytics"] = false
            extra["alwaysUpdateBuildId"] = false
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
            versionCode = (rootExtra["app_version_code"] as Int) + 9
        }

        create("partners") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"partners\"")
            resValue("string", "app_name", "Habitica")
            versionCode = (rootExtra["app_version_code"] as Int) + 7
        }

        create("alpha") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"alpha\"")
            resValue("string", "app_name", "Habitica Alpha")
            versionCode = (rootExtra["app_version_code"] as Int) + 5
        }

        create("beta") {
            buildConfigField("String", "TESTING_LEVEL", "\"beta\"")
            dimension = "buildType"
            versionCode = (rootExtra["app_version_code"] as Int) + 3
        }

        create("prod") {
            buildConfigField("String", "TESTING_LEVEL", "\"production\"")
            dimension = "buildType"
            versionCode = (rootExtra["app_version_code"] as Int) + 1
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

    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("com.google.android.gms:play-services-wearable:$playWearablesVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerviewVersion")
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear:wear-input:1.1.0")

    //Networking
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    //REST API handling
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion") {
        exclude(module = "okhttp")
    }
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    implementation(platform("com.google.firebase:firebase-bom:$firebaseBom"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.preference:preference-ktx:$preferencesVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

    implementation("com.google.android.gms:play-services-auth:$playAuthVersion")

    implementation(project(":common"))
    implementation(project(":shared"))
    implementation("androidx.appcompat:appcompat:1.7.0")

    implementation("com.google.dagger:hilt-android:$daggerhiltVersion")
    kapt("com.google.dagger:hilt-compiler:$daggerhiltVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    implementation("androidx.core:core-splashscreen:1.1.0-rc01")

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
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

val HRPG_PROPS_FILE = file(projectDir.absolutePath + "/../habitica.properties")
if (HRPG_PROPS_FILE.canRead()) {
    val HRPG_PROPS = Properties()
    HRPG_PROPS.load(FileInputStream(HRPG_PROPS_FILE))

    android.buildTypes.configureEach {
        HRPG_PROPS.forEach { property ->
            buildConfigField("String", property.key as String, "\"${property.value}\"")
        }
    }
} else {
    throw MissingResourceException("habitica.properties not found")
}

val props = Properties()
val propFile = file("signingrelease.properties")
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
