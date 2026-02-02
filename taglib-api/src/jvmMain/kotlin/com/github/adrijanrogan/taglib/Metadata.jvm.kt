package com.github.adrijanrogan.taglib

/**
 * Represents a handle to an audio file on JVM.
 *
 * Wraps a file system path pointing to the audio file.
 */
actual class AudioFileHandle(val path: String)
