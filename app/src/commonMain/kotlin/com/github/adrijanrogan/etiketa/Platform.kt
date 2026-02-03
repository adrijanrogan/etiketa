package com.github.adrijanrogan.etiketa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
