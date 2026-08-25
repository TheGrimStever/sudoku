package com.ai.baca

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform