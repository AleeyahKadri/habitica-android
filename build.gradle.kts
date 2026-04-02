import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.GradleBuild
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

buildscript {
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
        set("daggerhilt_version", "2.51.1")
        set("firebase_bom", "31.3.0")
        set("kotest_version", "5.6.2")
        set("kotlin_version", "2.0.20")
        set("ktlint_version", "1.2.1")
        set("lifecycle_version", "2.8.4")
        set("markwon_version", "4.6.2")
        set("mockk_version", "1.13.4")
        set("moshi_version", "1.15.0")
        set("navigation_version", "2.7.7")
        set("okhttp_version", "4.12.0")
        set("paging_version", "3.3.0")
        set("play_wearables_version", "18.2.0")
        set("play_auth_version", "21.2.0")
        set("preferences_version", "1.2.1")
        set("realm_version", "1.9.1")
        set("retrofit_version", "2.9.0")
        set("recyclerview_version", "1.3.2")
    }

    repositories {
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        mavenCentral()
    }

    val realm_version: String by extra
    val kotlin_version: String by extra
    val navigation_version: String by extra
    val daggerhilt_version: String by extra

    dependencies {
        classpath("com.android.tools.build:gradle:8.7.2")
        classpath("com.neenbedankt.gradle.plugins:android-apt:1.8")
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
        classpath("io.realm:realm-gradle-plugin:10.19.0")
        classpath("io.realm.kotlin:gradle-plugin:$realm_version")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigation_version")
        classpath("com.google.firebase:perf-plugin:1.4.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$daggerhilt_version")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:11.3.1")
        classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:$kotlin_version")
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
        }

        outputs.upToDateWhen { false }

        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) = Unit
            override fun beforeTest(testDescriptor: TestDescriptor) = Unit
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) = Unit

            override fun afterSuite(desc: TestDescriptor, result: TestResult) {
                if (desc.parent == null) {
                    val output = "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)"
                    val startItem = "|  "
                    val endItem = "  |"
                    val repeatLength = startItem.length + output.length + endItem.length
                    println("\n" + "-".repeat(repeatLength) + "\n" + startItem + output + endItem + "\n" + "-".repeat(repeatLength))
                }
            }
        })
    }
}

val props = Properties()
val propFile = file("version.properties")
if (propFile.canRead()) {
    FileInputStream(propFile).use { props.load(it) }

    if (props.containsKey("NAME") && props.containsKey("CODE")) {
        extra["app_version_name"] = props["NAME"].toString()
        extra["app_version_code"] = props["CODE"].toString().toInt()
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
        exclude { entry -> entry.file.toString().contains("generated") }
    }
}

tasks.named<io.gitlab.arturbosch.detekt.Detekt>("detekt").configure {
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
    setTasks(listOf(
        ":Habitica:testProdDebugUnitTest",
        ":wearos:testProdDebugUnitTest",
        ":common:testProdDebugUnitTest"
    ))
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "11"
    }
}
