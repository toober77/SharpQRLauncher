package com.github.toober77.sharpqrlauncher

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast

class LaunchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("p", Context.MODE_PRIVATE)
        when {
            Settings.canDrawOverlays(this) -> launchWithCurtain()
            !prefs.getBoolean("asked", false) -> {
                prefs.edit().putBoolean("asked", true).apply()
                showSetupDialog()
            }
            else -> launchDirect()
        }
    }

    private fun showSetupDialog() {
        AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
            .setTitle(R.string.setup_title)
            .setMessage(R.string.setup_msg)
            .setPositiveButton(R.string.go_settings) { _, _ ->
                try {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                } catch (_: Exception) {}
                done()
            }
            .setNeutralButton(R.string.add_shortcut) { _, _ -> requestPin(this); done() }
            .setNegativeButton(R.string.open_now) { _, _ -> launchDirect() }
            .setOnCancelListener { done() }
            .show()
    }

    private fun startTarget(): Boolean = try {
        startActivity(targetIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        true
    } catch (e: Exception) {
        Toast.makeText(applicationContext, R.string.not_found, Toast.LENGTH_SHORT).show()
        false
    }

    private fun launchDirect() { startTarget(); done() }

    private fun launchWithCurtain() {
        val wm = applicationContext.getSystemService(WindowManager::class.java)
        val curtain = View(applicationContext).apply { setBackgroundColor(Color.BLACK) }
        val lp = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.OPAQUE)
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        val added = try { wm.addView(curtain, lp); true } catch (_: Exception) { false }
        startTarget()
        done()
        if (added) Handler(Looper.getMainLooper()).postDelayed({
            try { wm.removeView(curtain) } catch (_: Exception) {}
        }, CURTAIN_MS)
    }

    private fun done() {
        finishAndRemoveTask()
        @Suppress("DEPRECATION") overridePendingTransition(0, 0)
    }

    companion object {
        const val TARGET_PKG = "jp.co.sharp.android.qrreader"
        const val TARGET_CLS = "jp.co.sharp.android.qrreader.MainActivity"
        const val CURTAIN_MS = 300L

        fun targetIntent(): Intent = Intent(Intent.ACTION_MAIN)
            .setComponent(ComponentName(TARGET_PKG, TARGET_CLS))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        fun requestPin(ctx: Context) {
            val sm = ctx.getSystemService(ShortcutManager::class.java)
            if (sm == null || !sm.isRequestPinShortcutSupported) {
                Toast.makeText(ctx.applicationContext, R.string.pin_unsupported, Toast.LENGTH_SHORT).show(); return
            }
            val info = ShortcutInfo.Builder(ctx, "sharp_qr_direct")
                .setShortLabel(ctx.getString(R.string.shortcut_label))
                .setIcon(Icon.createWithResource(ctx, R.mipmap.ic_launcher))
                .setIntent(targetIntent())
                .build()
            sm.requestPinShortcut(info, null)
        }
    }
}
