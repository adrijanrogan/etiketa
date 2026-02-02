package com.github.adrijanrogan.taglib.android

import android.net.Uri
import android.util.Log
import com.github.adrijanrogan.taglib.AudioFileHandle
import com.github.adrijanrogan.taglib.AudioMetadata
import com.github.adrijanrogan.taglib.AudioMetadataAccessor
import com.github.adrijanrogan.taglib.AudioProperties
import com.github.adrijanrogan.taglib.android.TagLibInitializer.Companion.applicationContext
import java.io.FileNotFoundException
import java.io.IOException


/**
 * Android implementation of [AudioMetadataAccessor] using TagLib via JNI.
 */
object AndroidAudioMetadataAccessor : AudioMetadataAccessor {

    // Load the native library at startup
    init {
        System.loadLibrary("taglib_android")
    }

    override suspend fun read(handle: AudioFileHandle): AudioMetadata? {
        // Attempt to open the file in read-write mode first
        var readOnly = false
        var parcelFd = try {
            applicationContext.contentResolver.openFileDescriptor(handle.uri, "rw")
        } catch (err: IOException) {
            Log.w("TagLibAndroid", err)
            null
        } catch (err: IllegalArgumentException) {
            // Thrown by some providers if rw is not allowed
            Log.w("TagLibAndroid", err)
            null
        }

        // Fallback: try read-only if RW failed
        if (parcelFd == null) {
            parcelFd = try {
                applicationContext.contentResolver.openFileDescriptor(handle.uri, "r")
            } catch (err: IOException) {
                Log.w("TagLibAndroid", err)
                return null
            }
            readOnly = (parcelFd != null)
        }

        if (parcelFd == null) {
            Log.w("TagLibAndroid", "parcelFd is null")
            return null
        }

        // Duplicate and detach the file descriptor:
        // - Duplicate: content resolver expects close() on the returned file descriptor
        // - Detach: native code takes ownership and is responsible for closing
        val fd = parcelFd.dup().detachFd()

        // Prepare the map to be filled by native code (avoids instantiating in JNI)
        val tags = HashMap<String, String>()
        val properties = readNative(fd, tags)

        // Close the ParcelFileDescriptor to tell the provider that we're done
        parcelFd.close()

        return AudioMetadata(tags, properties, readOnly)
    }

    override suspend fun write(handle: AudioFileHandle, tags: Map<String, String>) {
        // Attempts read-write access and silently returns if the file cannot be opened
        val parcelFd = try {
            applicationContext.contentResolver.openFileDescriptor(handle.uri, "rw")
        } catch (err: FileNotFoundException) {
            Log.w("TagLibAndroid", err)
            return
        } catch (err: IllegalArgumentException) {
            Log.w("TagLibAndroid", err)
            return
        }

        if (parcelFd == null) {
            Log.w("TagLibAndroid", "parcelFd is null")
            return
        }

        // Duplicate and detach the file descriptor:
        // - Duplicate: content resolver expects close() on the returned file descriptor
        // - Detach: native code takes ownership and is responsible for closing
        val fd = parcelFd.dup().detachFd()
        writeNative(fd, HashMap(tags))

        // Close the ParcelFileDescriptor to tell the provider that we're done
        parcelFd.close()
    }

    // Native functions implemented in C++ via JNI
    private external fun readNative(fd: Int, tags: HashMap<String, String>): AudioProperties?
    private external fun writeNative(fd: Int, tags: HashMap<String, String>)
}

/**
 * Convenience extension to read metadata from a Uri.
 */
suspend fun Uri.readAudioMetadata(): AudioMetadata? {
    val handle = AudioFileHandle(this)
    return AndroidAudioMetadataAccessor.read(handle)
}

/**
 * Convenience extension to write metadata tags to a Uri.
 */
suspend fun Uri.writeAudioMetadata(tags: Map<String, String>) {
    val handle = AudioFileHandle(this)
    return AndroidAudioMetadataAccessor.write(handle, tags)
}
