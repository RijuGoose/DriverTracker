plugins {
    id("com.android.library")
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.riju.repositoryimpl"
    compileSdk = 34

    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":repository"))
    implementation(project(":remotedatasource"))
    implementation(project(":localdatasource"))
    implementation(project(":domain"))

    implementation(libs.play.services.location)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
