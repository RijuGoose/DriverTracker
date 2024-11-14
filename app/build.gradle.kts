plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    kotlin("plugin.serialization").version("2.0.10")
}

android {
    namespace = "com.riju.drivertracker"
    compileSdk = 34

    secrets {
        propertiesFileName = "secrets.properties"
        defaultPropertiesFileName = "local.defaults.properties"
        ignoreList.add("keyToIgnore")
        ignoreList.add("sdk.*")
    }

    defaultConfig {
        applicationId = "com.riju.drivertracker"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":repository"))
    implementation(project(":repositoryimpl"))
    implementation(project(":localdatasource"))
    implementation(project(":remotedatasource"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.maps.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.material.icons.extended)
    ksp(libs.hilt.compiler)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
