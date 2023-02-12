package com.sunman.feature.main.ui.animations

import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews


internal fun View.enable() {
    if (this is ViewGroup) {
        allViews.forEach { it.isEnabled = true }
    }

    isEnabled = true
}

internal fun View.disable() {
    if (this is ViewGroup) {
        allViews.forEach { it.isEnabled = false }
    }

    isEnabled = false
}
