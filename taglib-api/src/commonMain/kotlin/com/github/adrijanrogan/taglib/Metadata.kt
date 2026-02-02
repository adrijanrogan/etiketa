package com.github.adrijanrogan.taglib

/**
 * Represents the metadata of an audio file.
 *
 * @property tags Key-value pairs of metadata (e.g. "ARTIST" -> "Muse").
 * @property properties Optional audio properties of the file.
 * @property readOnly True if the audio file is read-only.
 */
class AudioMetadata(
    val tags: Map<String, String>,
    val properties: AudioProperties?,
    val readOnly: Boolean,
)

/**
 * Holds common audio properties of an audio file.
 *
 * @property bitrate Audio bitrate in kbps. For variable bitrate formats this is the average bitrate.
 * @property channels Number of audio channels.
 * @property lengthInMilliseconds Total duration of the audio in milliseconds.
 * @property sampleRate Sample rate in Hz.
 */
class AudioProperties(
    val bitrate: Int,
    val channels: Int,
    val lengthInMilliseconds: Int,
    val sampleRate: Int,
)

/**
 * Platform-specific handle for an audio file.
 *
 * This abstracts the underlying file representation (file path, URI, etc.).
 */
expect class AudioFileHandle

/**
 * Provides read and write access to audio metadata.
 * Implementations are platform-specific.
 */
interface AudioMetadataAccessor {

    /**
     * Reads metadata from the given audio file handle.
     *
     * @param handle Reference to the audio file.
     * @return [AudioMetadata] if the metadata could be read, or null if reading fails.
     */
    suspend fun read(handle: AudioFileHandle): AudioMetadata?

    /**
     * Writes the provided metadata tags to the given audio file handle.
     *
     * @param handle Reference to the audio file.
     * @param tags Key-value pairs to write to the file's metadata.
     */
    suspend fun write(handle: AudioFileHandle, tags: Map<String, String>)
}
