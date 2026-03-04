import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val realmVersion by extra("1.9.1")
    val kotlinVersion by extra("2.0.20")
    val navigationVersion by extra("2.7.7")
    val daggerhiltVersion by extra("2.51.1")

    extra.apply {
        set("min_sdk", 21)
        set("target_sdk", 34)
        set("wearos_target_sdk", 33)
        set("app_version_name", "")
        set("app_version_code", 0)

        set("accompanist_version", "0.30.0")
        set("amplitude_version", "1.6.1")
        set("appcompat_version", "1.7.0")
        set("coil_version", "2.4.0")
        set("compose_version", "1.6.8")
        set("compose_compiler", "1.5.14")
        set("core_ktx_version", "1.13.1")
        set("coroutines_version", "1.8.0")
        set("daggerhilt_version", daggerhiltVersion)
        set("firebase_bom", "31.3.0")
        set("kotest_version", "5.6.2")
        set("kotlin_version", kotlinVersion)
        set("ktlint_version", "1.2.1")
        set("lifecycle_version", "2.8.4")
        set("markwon_version", "4.6.2")
        set("mockk_version", "1.13.4")
        set("moshi_version", "1.15.0")
        set("navigation_version", navigationVersion)
        set("okhttp_version", "4.12.0")
        set("paging_version", "3.3.0")
        set("play_wearables_version", "18.2.0")
        set("play_auth_version", "21.2.0")
        set("preferences_version", "1.2.1")
        set("realm_version", realmVersion)
        set("retrofit_version", "2.9.0")
        set("recyclerview_version", "1.3.2")
    }

    repositories {
        google()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.2")
        classpath("com.neenbedankt.gradle.plugins:android-apt:1.8")
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
        classpath("io.realm:realm-gradle-plugin:10.19.0")
        classpath("io.realm.kotlin:gradle-plugin:$realmVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion")
        classpath("com.google.firebase:perf-plugin:1.4.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$daggerhiltVersion")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:11.3.1")
        classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:$kotlinVersion")
    }
}

apply(plugin = "io.gitlab.arturbosch.detekt")

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    repositories {
        google()
        mavenCentral()
    }

    tasks.withType<Test>().configureEach {
        testLogging {
            events("passed", "skipped", "failed", "standardError")
            showStandardStreams = false
        }
        outputs.upToDateWhen { false }
    }
}

val props = Properties()
val propFile = File("version.properties")
if (propFile.canRead()) {
    props.load(FileInputStream(propFile))

    if (props.containsKey("NAME") && props.containsKey("CODE")) {
        extra.set("app_version_name", props["NAME"])
        extra.set("app_version_code", props["CODE"].toString().toInt())
    } else {
        println("version.properties found but some entries are missing")
    }
} else {
    println("version.properties not found")
}

configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    source.setFrom(files("Habitica/src/main/java"))
    config.setFrom(files("detekt.yml"))
    baseline = file("${rootProject.projectDir}/detekt_baseline.xml")
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    filter {
        exclude { entry ->
            entry.file.toString().contains("generated")
        }
    }
}

tasks.named("detekt").configure {
    reports {
        xml.required.set(false)
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
        txt.required.set(false)
        sarif.required.set(true)
        sarif.outputLocation.set(file("build/reports/detekt.sarif"))
    }
}

tasks.register<GradleBuild>("allUnitTests") {
    tasks = listOf(":Habitica:testProdDebugUnitTest", ":wearos:testProdDebugUnitTest", ":common:testProdDebugUnitTest")
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "11"
    }
}
