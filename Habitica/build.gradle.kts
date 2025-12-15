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
    id("androidx.navigation.safeargs")
    id("com.google.firebase.firebase-perf")

    id("jacoco-report-aggregation")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")
}

apply(plugin = "kotlin-android")
apply(plugin = "realm-android")

repositories {
    mavenLocal()
    mavenCentral()
    google()

    maven { url = uri("https://jitpack.io") }
}

val rootExtra = rootProject.extra

val okhttpVersion: String by rootExtra
val retrofitVersion: String by rootExtra
val daggerhiltVersion: String by rootExtra
val appcompatVersion: String by rootExtra
val recyclerviewVersion: String by rootExtra
val preferencesVersion: String by rootExtra
val coilVersion: String by rootExtra
val amplitudeVersion: String by rootExtra
val mockkVersion: String by rootExtra
val kotestVersion: String by rootExtra
val kotlinVersion: String by rootExtra
val firebaseBom: String by rootExtra
val playAuthVersion: String by rootExtra
val playWearablesVersion: String by rootExtra
val coreKtxVersion: String by rootExtra
val lifecycleVersion: String by rootExtra
val navigationVersion: String by rootExtra
val coroutinesVersion: String by rootExtra
val pagingVersion: String by rootExtra
val composeVersion: String by rootExtra
val accompanistVersion: String by rootExtra
val targetSdk: Int by rootExtra
val minSdk: Int by rootExtra

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "../common/libs")))
    //Networking
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    //REST API handling
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion") {
        exclude(module = "okhttp")
    }
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    //Dependency Injection
    implementation("com.google.dagger:hilt-android:$daggerhiltVersion")
    kapt("com.google.dagger:hilt-compiler:$daggerhiltVersion")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    //App Compatibility and Material Design
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.recyclerview:recyclerview:$recyclerviewVersion")
    implementation("androidx.preference:preference-ktx:$preferencesVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation("com.jaredrummler:android-device-names:2.1.1")

    // IAP Handling / Verification
    implementation("com.android.billingclient:billing-ktx:7.0.0")
    implementation("fr.avianey.com.viewpagerindicator:library:2.4.1@aar")

    implementation("io.coil-kt:coil-compose:$coilVersion")

    //Analytics
    implementation("com.amplitude:analytics-android:$amplitudeVersion")

    //Tests
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    androidTestImplementation("com.kaspersky.android-components:kaspresso:1.5.1") {
        exclude(module = "protobuf-lite")
    }
    androidTestImplementation("com.kaspersky.android-components:kaspresso-compose-support:1.5.1")

    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    debugImplementation("androidx.fragment:fragment-testing:1.8.2")
    androidTestImplementation("androidx.test:core-ktx:1.6.1")
    debugImplementation("androidx.test:monitor:1.7.2")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.2.1")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("io.mockk:mockk-agent:$mockkVersion")
    androidTestImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    androidTestUtil("androidx.test:orchestrator:1.5.0")

    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //Leak Detection
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
    //Push Notifications
    implementation(platform("com.google.firebase:firebase-bom:$firebaseBom"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.android.gms:play-services-auth:$playAuthVersion")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.android.gms:play-services-wearable:$playWearablesVersion")

    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.fragment:fragment-ktx:1.8.2")
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-compose:$pagingVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")

    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")

    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.animation:animation:$composeVersion")
    implementation("androidx.compose.ui:ui-text-google-fonts:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

    implementation(project(":common"))
    implementation(project(":shared"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")

    implementation("com.gu.android:toolargetool:0.3.0")
}

android {
    compileSdk = targetSdk
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }

    namespace = "com.habitrpg.android.habitica"

    defaultConfig {
        minSdk = minSdk
        compileSdk = targetSdk
        applicationId = "com.habitrpg.android.habitica"
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "STORE", "\"google\"")
        buildConfigField("String", "TESTING_LEVEL", "\"production\"")
        resourceConfigurations += listOf("en", "bg", "de", "en-rGB", "es", "fr", "hr-rHR", "in", "it", "iw", "ja", "ko", "lt", "nl", "pl", "pt-rBR", "pt-rPT", "ru", "tr", "uk", "zh", "zh-rTW")

        versionCode = rootExtra["app_version_code"] as Int
        versionName = rootExtra["app_version_name"] as String

        targetSdk = targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        val formattedDate = SimpleDateFormat("yyMMdd").format(Date())
        setProperty("archivesBaseName", "Habitica-${formattedDate}${versionCode}")
    }

    buildFeatures {
        viewBinding = true
        compose = true
        renderScript = true
        buildConfig = true
        aidl = true
    }

    signingConfigs {
        create("release")
    }
    
    flavorDimensions.add("buildType")

    buildTypes {
        debug {
            //applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            // Disable fabric build ID generation for debug builds
            extra["enableCrashlytics"] = false
            extra["alwaysUpdateBuildId"] = false
            //resValue("string", "content_provider", "com.habitrpg.android.habitica.debug.fileprovider")
            resValue("string", "content_provider", "com.habitrpg.android.habitica.fileprovider")
            resValue("string", "app_name", "Habitica Debug")
            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
        }
        create("debugIAP") {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            // Disable fabric build ID generation for debug builds
            extra["enableCrashlytics"] = false
            extra["alwaysUpdateBuildId"] = false
            resValue("string", "content_provider", "com.habitrpg.android.habitica.fileprovider")
            resValue("string", "app_name", "Habitica Debug")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            resValue("string", "content_provider", "com.habitrpg.android.habitica.fileprovider")
            resValue("string", "app_name", "Habitica")
        }
    }

    productFlavors {
        create("dev") {
            dimension = "buildType"
        }

        create("staff") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"staff\"")
            resValue("string", "app_name", "Habitica Staff")
            versionCode = (rootExtra["app_version_code"] as Int) + 8
        }

        create("partners") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"partners\"")
            resValue("string", "app_name", "Habitica")
            versionCode = (rootExtra["app_version_code"] as Int) + 6
        }

        create("alpha") {
            dimension = "buildType"
            buildConfigField("String", "TESTING_LEVEL", "\"alpha\"")
            resValue("string", "app_name", "Habitica Alpha")
            versionCode = (rootExtra["app_version_code"] as Int) + 4
        }

        create("beta") {
            buildConfigField("String", "TESTING_LEVEL", "\"beta\"")
            dimension = "buildType"
            versionCode = (rootExtra["app_version_code"] as Int) + 2
        }

        create("prod") {
            buildConfigField("String", "TESTING_LEVEL", "\"production\"")
            dimension = "buildType"
            versionCode = rootExtra["app_version_code"] as Int
        }
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src/main/java")
            resources.srcDirs("src/main/java")
            aidl.srcDirs("src/main/java")
            renderscript.srcDirs("src/main/java")
            res.srcDirs("res")
            assets.srcDirs("assets")
        }
        getByName("test") {
            java.srcDir("src/test/java")
        }
        create("debugIAP") {
            java.srcDirs("src/debug/java")
        }
        getByName("release") {
            java.srcDirs("src/release/java")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
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
    
    lint {
        abortOnError = false
        disable += listOf("MissingTranslation", "InvalidPackage")
        enable += listOf("LogConditional", "IconExpectedSize", "MissingRegistered", "TypographyQuotes")
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

android.testOptions {
    unitTests.all {
        it.useJUnitPlatform()
    }
    unitTests.isReturnDefaultValues = true
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

// Add Habitica Properties to buildConfigField
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

// Add Habitica Resources to resources
val HRPG_RES_FILE = file(projectDir.absolutePath + "/../habitica.resources")
if (HRPG_RES_FILE.canRead()) {
    val HRPG_RES = Properties()
    HRPG_RES.load(FileInputStream(HRPG_RES_FILE))

    android.buildTypes.configureEach {
        HRPG_RES.forEach { property ->
            resValue("string", property.key as String, "\"${property.value}\"")
        }
    }
} else {
    throw MissingResourceException("habitica.resources not found")
}

tasks.configureEach {
    if (name == "lint") {
        enabled = false
    }
}

gradle.projectsEvaluated {
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "500"))
    }
}

apply(plugin = "com.google.gms.google-services")
