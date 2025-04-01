plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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

    sourceDirectories.setFrom(files("${project.projectDir}/src/main/java"))
    classDirectories.setFrom(files("${buildDir}/intermediates/javac/debug/classes"))
    doLast {
        val deployDir = System.getenv("BITRISE_DEPLOY_DIR") ?: "$buildDir/deploy"
        val destination = File(deployDir, "coverage")

        println("🔍 Moving Combined Coverage Report to: $destination")

        destination.mkdirs()
        file("${buildDir}/reports/jacoco/combinedCoverageReport").copyRecursively(destination, overwrite = true)

        println("✅ Coverage Report moved to $destination")
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
