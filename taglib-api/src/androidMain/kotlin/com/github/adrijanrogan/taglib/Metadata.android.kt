package com.github.adrijanrogan.taglib

import android.net.Uri

/**
 * Represents a handle to an audio file on Android.
 *
 * Wraps a [Uri] pointing to the audio file. This can be a file URI from private app storage
 * or a content URI from MediaStore / SAF.
 *
 */
actual class AudioFileHandle(val uri: Uri)
