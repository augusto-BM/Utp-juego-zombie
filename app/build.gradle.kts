plugins {
    //alias(libs.plugins.androidApplication)

    id("com.android.application")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.utp.zombiegame"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.utp.zombiegame"
        minSdk = 23
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.25")

    //_____________________________   CONECTAR CON FIREBASE   _______________________________
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation ("com.google.firebase:firebase-storage:21.0.0")
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    //______________________________________________________________________________________

    //-------------------- DEPENDENCIAS PARA LA BASE DE DATOS ---------------------------
    implementation ("com.google.android.material:material:1.11.0")       /*DISEÑO*/
    implementation ("com.google.firebase:firebase-auth:22.3.1")         /*AUTENTICACIÓN*/
    implementation ("com.google.firebase:firebase-database:20.3.1")     /*BASE DATOS*/
    //-----------------------------------------------------------------------------------
    implementation ("com.airbnb.android:lottie:3.0.1")      /*PARA HACER ANIMACIONES 3.0.1*/
    implementation ("com.squareup.picasso:picasso:2.71828") /*GESTIONAR IMAGEN*/
    implementation ("de.hdodenhof:circleimageview:3.0.1")   /*IMAGEN CIRCULAR*/
    //-----------------------------------------------------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("com.github.bumptech.glide:okhttp3-integration:4.12.0")


}