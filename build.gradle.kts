// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //required to make ksp work
    id("com.google.devtools.ksp") version "2.3.11" apply false
//    The Java Toolchain plugins for automatically handling code smells and minro errors of various types
//    I'm using all the jdk v25 compatible versions
    id("net.ltgt.errorprone") version "5.1.1" apply false
    id("com.github.spotbugs") version "6.5.11" apply false
//    Detekt, because it's actually a Kotlin language plugin
    id("dev.detekt") version("2.0.0-alpha.6") apply false

//    Rooms plugin, which for some reason wasn't necessary untli I wanted to add migrations
    id("androidx.room3") version "3.0.2" apply false
}