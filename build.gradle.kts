buildscript {
    val kotlin_version by extra("1.9.0")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.google.gms:google-services:4.3.14") // For Firebase
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
