plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.sovereign"
    compileSdk = 34

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
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packagingOptions{
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
}

dependencies {
    // Android libraries
    implementation(libs.appcompat) // AppCompat for backward compatibility
    implementation(libs.material) // Material design components
    implementation(libs.activity) // Activity support
    implementation(libs.constraintlayout) // ConstraintLayout for advanced layouts

    implementation(libs.gson)
    implementation(libs.android.mail)
    implementation(libs.android.activation)

    // Firebase libraries
    implementation(libs.firebase.firestore) // Firestore database support
    implementation(platform(libs.firebase.firestore)) // Firebase platform BOM
    implementation(libs.firebase.database) // Realtime Database support
    implementation(libs.firebase.storage) // Firebase Storage for uploading files


    // Glide dependencies
    implementation(libs.glide.v4150)
    testImplementation(libs.junit)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)


}

fun kapt(s: String){
    TODO("Not yet Implemented")
}