//////////////////////////////////////////////////////////////////////
// Listing 1: shared-Abhängigkeit in der Android-Build-Gradle-Datei //
//////////////////////////////////////////////////////////////////////

implementation(project(":shared"))


/////////////////////////////////////////////////////////////
// Listing 2: Ausschnitt aus der Shared-Build-Gradle-Datei //
/////////////////////////////////////////////////////////////

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    ...

    cocoapods {
		 ...
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        ...
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            ...
        }
}
...


///////////////////////////////////////////////////
// Listing 3: Inhalt der Expect-Klasse-Plattform //
///////////////////////////////////////////////////

expect class Platform() {
    val platform: String
}


///////////////////////////////////////////////////////////////
// Listing 4: iOS-Implementierung der getDeviceName-Funktion //
///////////////////////////////////////////////////////////////

actual fun getDeviceName(): String {
    return UIDevice.currentDevice.name
}


///////////////////////////////////////////////////////////////////
// Listing 5: Android-Implementierung der getDeviceName-Funktion //
///////////////////////////////////////////////////////////////////

actual fun getDeviceName(): String {
    return android.os.Build.MODEL
}


////////////////////////////////////////////////////////////
// Listing 6: Implementierung der geteilten Businesslogik //
////////////////////////////////////////////////////////////

fun helloDevice(): String {
    val platform = Platform()

    return "Hello my Name is ${platform.getDeviceName()}"
}


///////////////////////////////////////////////////////////
// Listing 7: Android-Aufruf der geteilten Businesslogik //
///////////////////////////////////////////////////////////

val greeting = Greeting()
tv.text = greeting.helloDevice()