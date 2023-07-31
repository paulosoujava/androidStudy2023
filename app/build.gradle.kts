plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.8.0-1.0.8"
}

android {
    namespace = "com.example.stydyandroid2023"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.stydyandroid2023"
        minSdk = 24
        targetSdk = 33
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets.configureEach {
        kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
    }

}

dependencies {

    val composeVersion = "1.2.1"
    val coreKtxVersion = "1.9.0"
    val lifecycleKtxVersion = "2.5.1"
    val activityComposeVersion = "1.6.0"
    val androidxLifecycleVersion = "2.5.1"
    val navigationComposeVersion = "2.5.2"

    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKtxVersion")
    implementation("androidx.activity:activity-compose:$activityComposeVersion")

    implementation("com.squareup.retrofit2:retrofit:2.5.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")


    implementation("com.github.yanneckreiss.kconmapper:annotations:1.0.0-alpha06")
    ksp("com.github.yanneckreiss.kconmapper:ksp:1.0.0-alpha06")


}

