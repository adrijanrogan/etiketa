package com.github.adrijanrogan.etiketa

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Etiketa",
    ) {
        // TODO JVM app
    }
}
