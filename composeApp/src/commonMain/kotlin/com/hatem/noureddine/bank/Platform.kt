package com.hatem.noureddine.bank

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform