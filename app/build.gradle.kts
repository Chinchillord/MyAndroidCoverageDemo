plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("jacoco")
}

android {
    namespace = "com.example.counter"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.counter"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf("**/R.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*")
    val kotlinClasses = fileTree("${layout.buildDirectory}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    classDirectories.setFrom(kotlinClasses)
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(fileTree(layout.buildDirectory).include("**/debugUnitTest/testDebugUnitTest.exec"))
}

tasks.register<JacocoReport>("combinedCoverageReport") {
    reports {
        html.required.set(true)
    }

    val coverageSourceDirs = listOf(
        "src/main/java",
        "src/main/kotlin"
    )

    val unitTestCoverage = fileTree("/bitrise/src/app/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    val androidTestCoverage = fileTree("/bitrise/src/app/build/outputs/code_coverage/debugAndroidTest/connected/emulator(AVD) - 11/coverage.ec")

    executionData.setFrom(files(unitTestCoverage, androidTestCoverage))

    sourceDirectories.setFrom(coverageSourceDirs)
    classDirectories.setFrom(files("${layout.buildDirectory}/intermediates/javac/debug/classes"))
    doLast {
        println("🔍 Combined Coverage Report Generated at:")
        println("  📄 XML Report: ${reports.xml.outputLocation.get().asFile.absolutePath}")
        println("  📂 HTML Report: ${reports.html.outputLocation.get().asFile.absolutePath}")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.compose.livedata)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
