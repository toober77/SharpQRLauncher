package com.github.toober77.sharpqrlauncher

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class LaunchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            startActivity(Intent(Intent.ACTION_MAIN)
                .setComponent(ComponentName(TARGET_PKG, TARGET_CLS))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION))
        } catch (e: Exception) {
            Toast.makeText(applicationContext, R.string.not_found, Toast.LENGTH_SHORT).show()
        }
        finishAndRemoveTask()
        @Suppress("DEPRECATION") overridePendingTransition(0, 0)
    }
    companion object {
        const val TARGET_PKG = "jp.co.sharp.android.qrreader"
        const val TARGET_CLS = "jp.co.sharp.android.qrreader.MainActivity"
    }
}
