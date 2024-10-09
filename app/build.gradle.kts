plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.jetbrainsKotlinKapt)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.project.tubocare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.tubocare"
        minSdk = 24
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
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

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.googleid)
    implementation(libs.firebase.storage)
    implementation(libs.places)
    implementation(libs.androidx.hilt.common)

    // junit
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Splash Api
    implementation(libs.androidx.core.splashscreen)

    // firebase
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.constraintlayout.compose)

    //Accompanist
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    //Datastore -> untuk menyimpan preferences
    implementation(libs.androidx.datastore.preferences)

    // coil -> untuk memuat gambar
    implementation(libs.coil.compose)

    // glide -> untuk mendownload gambar
    implementation(libs.glide)
    kapt(libs.compiler)

    // coroutines -> untuk asyncronus proces

    // room -> untuk database lokal
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

    // retrofit -> untuk connect api
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // serialization
    implementation(libs.kotlinx.serialization.json)

    // gson/json data formats
    implementation(libs.gson)
    implementation(libs.jackson.module.kotlin)

    // dagger hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // chart
    implementation("com.himanshoe:kalendar:1.3.2")
    implementation("com.himanshoe:kalendar-endlos:1.3.2")

    implementation("io.github.boguszpawlowski.composecalendar:composecalendar:1.2.0")
    implementation("io.github.boguszpawlowski.composecalendar:kotlinx-datetime:1.2.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    // work
    implementation(libs.androidx.work.runtime.ktx)

    // Other dependencies
    coreLibraryDesugaring(libs.tools.desugar.jdk.libs)

    // paging
    /*    implementation("androidx.paging:paging-runtime:3.2.1")
        implementation("androidx.paging:paging-compose:3.2.1")*/

    // extended icons
//    implementation("androidx.compose.material:material-icons-extended:1.6.4")

    // system ui controller
//    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

}
