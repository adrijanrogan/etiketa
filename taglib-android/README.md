**This is a pure Kotlin Android library module, *not* Kotlin Multiplatform.** 
This way we can use `externalNativeBuild` for building the JNI library.

From the Kotlin Multiplatform plugin migration page:

- Native build support
    - The new plugin focuses on producing a standard AAR for the Android target. Native code integration in Kotlin Multiplatform is handled directly by KMP's own native targets (such as androidNativeArm64 and androidNativeX86) and its C-interop capabilities. If you need to include native C/C++ code, you should define it as part of a common or native source set and configure the C-interop within the kotlin block, rather than using the Android-specific externalNativeBuild mechanism. 
    - Alternatively, if you need native build support via externalNativeBuild, our recommendation is to create a separate standalone com.android.library module where you can integrate native code, and consume that standalone library from your Kotlin Multiplatform library project's androidMain source set. For more details, see Create an Android library and Add C and C++ code to your project.
