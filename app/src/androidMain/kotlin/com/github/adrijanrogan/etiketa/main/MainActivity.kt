package com.github.adrijanrogan.etiketa.main

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.composables.core.HorizontalSeparator
import com.composeunstyled.Text
import com.github.adrijanrogan.etiketa.ui.Button
import com.github.adrijanrogan.etiketa.ui.Theme
import com.github.adrijanrogan.taglib.android.readAudioMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private data class AudioItem(
    val title: String,
    val uri: Uri,
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
        )

        setContent {
            Theme {
                MainContent()
            }
        }
    }

    @Composable
    fun MainContent() {
        val media by produceState(initialValue = emptyList()) {
            value = loadAudioMedia()
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
            onResult = { uri ->
                if (uri == null) {
                    return@rememberLauncherForActivityResult
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    val data = uri.readAudioMetadata()
                    if (data != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, data.tags.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = media,
                    key = { it.uri },
                ) { item ->
                    Column {
                        Text(
                            text = item.title,
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                        HorizontalSeparator(color = Theme.colors.contentSecondary)
                    }
                }
            }

            Button(
                onClick = {
                    launcher.launch(arrayOf("audio/*"))
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .widthIn(max = Theme.ElementMaxWidth)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp)
                    .padding(WindowInsets.safeDrawing.asPaddingValues())
            ) {
                Text(
                    text = "Open file",
                )
            }
        }
    }

    private suspend fun loadAudioMedia(): List<AudioItem> = withContext(Dispatchers.IO) {
        val cursor = applicationContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null,
        ) ?: return@withContext emptyList()

        cursor.use { cursor ->
            val uriIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            buildList {
                while (cursor.moveToNext()) {
                    val uri = cursor.getStringOrNull(uriIndex)
                    val title = cursor.getStringOrNull(titleIndex)
                    if (uri != null && title != null) {
                        val item = AudioItem(
                            title = title,
                            uri = uri.toUri(),
                        )
                        add(item)
                    }
                }
            }
        }
    }
}
