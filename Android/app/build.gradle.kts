plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.a9p"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.a9p"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        jniLibs {
            useLegacyPackaging = true
        }
        resources {
            excludes += "/META-INF/INDEX.LIST"
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.core.ktx)
    implementation("androidx.preference:preference:1.2.1")
    implementation ("com.google.android.material:material:1.9.0")

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))

    // Firebase
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-messaging")

    // Retrofit for AWS API communication
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // HttpLoggingInterceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // tensorflow-lite
    implementation("org.tensorflow:tensorflow-lite:2.17.0")

    // pytorch
    implementation ("org.pytorch:pytorch_android_lite:2.1.0")
    implementation ("org.pytorch:pytorch_android_torchvision_lite:2.1.0")

    // ONNX Runtime for Android
    implementation("com.microsoft.onnxruntime:onnxruntime-android:1.20.0")
    implementation(libs.recyclerview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}