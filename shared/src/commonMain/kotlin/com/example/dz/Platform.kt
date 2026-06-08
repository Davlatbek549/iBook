package com.example.dz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform