//The necessary gradle support for errorprone to actually do anything
import net.ltgt.gradle.errorprone.errorprone
//Apparently this will allow us to force pmd to run, in a custom gradle task anyway
import org.gradle.api.plugins.quality.Pmd

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // Required to make ksp work
    id("com.google.devtools.ksp")
//    Java Toolchain Plugins
    pmd
    checkstyle

    id("net.ltgt.errorprone")
    id("com.github.spotbugs")
}

//The necessary enabler for Errorprone to run on gradle
tasks.withType<JavaCompile>().configureEach {
    options.errorprone.disableWarningsInGeneratedCode = true
}


pmd {
    isConsoleOutput = true
    toolVersion = "7.26.0"
    rulesMinimumPriority = 5
    ruleSets = listOf("category/java/errorprone.xml", "category/java/bestpractices.xml")
    ruleSetFiles = files("PmdRules.xml")
}

android {
    namespace = "com.example.project1_438"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.project1_438"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.navigation:navigation-compose:2.10.0")
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

//    Room Dependencies
    val roomVersion = "3.0.2"

    implementation("androidx.room3:room3-runtime:$roomVersion")
    ksp("androidx.room3:room3-compiler:$roomVersion")

//    Java Toolchain Dependencies
    errorprone("com.google.errorprone:error_prone_core:2.50.0")
//    I hate robots, so much right now
    pmd("net.sourceforge.pmd:pmd:7.26.0")
    pmd("net.sourceforge.pmd:pmd-ant:7.26.0")
    pmd("net.sourceforge.pmd:pmd-java:7.26.0")
    pmd("net.sourceforge.pmd:pmd-kotlin:7.26.0")

}

//Supplied by Mr. Gippity, because my bran is tired from trying to get this to work for 6 hours while the robot kept running me in circles
//I gotta just start reading documentation myself instead of asking the hallucination machines for help explaining things
/*
 * Creates a PMD analysis task for this Android/Kotlin module.
 *
 * The standard PMD task looks for Java source sets. This project stores
 * application code as Kotlin files, so this task explicitly tells PMD to
 * scan Kotlin files under app/src/main.
 *
 * The shared PMD settings, including the PMD version and rulesets, remain
 * in the pmd { } configuration above.
 */
tasks.register<Pmd>("pmdKotlin") {
    description = "Runs PMD analysis on the app's Kotlin source files."

    source(fileTree("src/main") {
        include("**/*.kt")
    })

//    // Supplies PMD and its built-in language/rule resources.
//    pmdClasspath = configurations["pmd"]
//    // Configure the custom ruleset for this user-defined task.
//    ruleSetFiles = files("PmdRule.xml")

    // These are the built-in Kotlin rules available to the task.
//    ruleSets = listOf(
//        "category/kotlin/bestpractices.xml",
//        "category/kotlin/errorprone.xml"
//    )
}