package com.baatsen.venuesearch

import android.view.View

fun View.setVisible(visible: Boolean) {
    if (visible) this.visibility = View.VISIBLE else
        this.visibility = View.GONE
}