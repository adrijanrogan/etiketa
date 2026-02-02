package com.github.adrijanrogan.taglib.android

import android.content.Context
import androidx.startup.Initializer

/**
 * Marker object for the AndroidX Startup initializer.
 * Thanks to the Kable library for the idea.
 */
object TagLibAndroid

/**
 * AndroidX Startup initializer for the TagLib Android library.
 * This runs automatically if AndroidManifest.xml is included as-is.
 */
class TagLibInitializer : Initializer<TagLibAndroid> {

    override fun create(context: Context): TagLibAndroid {
        applicationContext = context.applicationContext
        return TagLibAndroid
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // We do not depend on any other components
        return emptyList()
    }

    companion object {
        /**
         * Holds the global Android application context for use by [AndroidAudioMetadataAccessor].
         *
         * This is initialized once at app startup via [TagLibInitializer].
         */
        internal lateinit var applicationContext: Context
            private set
    }
}
