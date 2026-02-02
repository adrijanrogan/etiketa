plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    androidLibrary {
        namespace = "com.github.adrijanrogan.taglib"
        compileSdk = 36
    }
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                // No extra dependencies
            }
        }

        androidMain {
            dependencies {
                // No extra dependencies
            }
        }

        jvmMain {
            dependencies {
                // No extra dependencies
            }
        }
    }
}
