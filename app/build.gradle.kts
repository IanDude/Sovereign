plugins {
    alias(libs.plugins.android.application) // Core Android application plugin
    alias(libs.plugins.google.gms.google.services) // Google Services plugin for Firebase

}

android {
    namespace = "com.example.sovereign" // Unique package namespace
    compileSdk = 34 // Compile SDK version

    defaultConfig {
        applicationId = "com.example.sovereign" // Unique application ID
        minSdk = 24 // Minimum supported SDK
        targetSdk = 34 // Targeted SDK version
        versionCode = 1 // Version code for updates
        versionName = "1.0" // Version name
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Test runner
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Disable code minification for release builds
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Default ProGuard rules
                "proguard-rules.pro" // Custom ProGuard rules
            )
        }
    }

    buildFeatures {
        viewBinding = true // Enable ViewBinding for easier UI management
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Java 8 compatibility
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Android libraries
    implementation(libs.appcompat) // AppCompat for backward compatibility
    implementation(libs.material) // Material design components
    implementation(libs.activity) // Activity support
    implementation(libs.constraintlayout) // ConstraintLayout for advanced layouts

    // Firebase libraries
    implementation(libs.firebase.firestore) // Firestore database support
    implementation(platform(libs.firebase.firestore)) // Firebase platform BOM
    implementation(libs.firebase.database) // Realtime Database support
    implementation(libs.firebase.storage) // Firebase Storage for uploading files

    // Glide dependencies
    implementation(libs.glide.v4150)
     

    // Testing libraries
    testImplementation(libs.junit) // Unit testing
    androidTestImplementation(libs.ext.junit) // Android-specific testing
    androidTestImplementation(libs.espresso.core) // UI testing with Espresso
}

fun kapt(s: String) {
    TODO("Not yet implemented")
}
