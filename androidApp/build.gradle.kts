plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = Constants.NameSpace
    compileSdk = Constants.CompileSdk

    defaultConfig {
        minSdk = Constants.MinSdk
        targetSdk = Constants.TargetSdk
        versionCode = Constants.VersionCode
        versionName = Constants.VersionName
        testInstrumentationRunner = "${Constants.NameSpace}.TestInstrumentationRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("demo") {
            applicationId = Constants.NameSpace
        }
        create("beta") {
            applicationId = "${Constants.NameSpace}.beta"
        }
        create("store") {
            applicationId = Constants.NameSpace
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        // Support for java.time on API 25 and older
        isCoreLibraryDesugaringEnabled = true
    }

    packagingOptions.resources.excludes += "META-INF/versions/9/previous-compilation-data.bin"
}

kotlin {
    jvmToolchain(Constants.JavaVersion)
}

dependencies {
    implementation(project(":shared"))

    coreLibraryDesugaring(libs.android.desugar)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.junit.ktx)
    androidTestImplementation(platform(libs.koin.bom))
    androidTestImplementation(libs.koin.test)

    implementation(libs.activity.compose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}