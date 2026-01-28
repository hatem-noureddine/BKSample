package com.hatem.noureddine.bank

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Main entry point for the iOS application.
 * Returns a [ComposeUIViewController] hosting the content of [App].
 */
@Suppress("FunctionNaming")
fun MainViewController() = ComposeUIViewController { App() }
