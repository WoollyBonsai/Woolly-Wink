/*
 * SPDX-FileCopyrightText: 2025 Woolly Wink
 *
 * SPDX-License-Identifier: GPL-2.0-only OR GPL-3.0-only OR LicenseRef-KDE-Accepted-GPL
 */

package org.kde.kdeconnect.Plugins.AudioSinkPlugin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.zorinos.zorin_connect.R
import org.kde.kdeconnect.BackgroundService

class AudioSinkSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var mediaProjectionManager: MediaProjectionManager
        private val screenCaptureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = Intent(requireContext(), BackgroundService::class.java).apply {
                    action = "START_AUDIO_CAPTURE"
                    putExtra("resultCode", result.resultCode)
                    putExtra("data", result.data)
                }
                requireContext().startService(intent)
            }
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.audiosink_preferences, rootKey)

            mediaProjectionManager = requireContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

            val isEnabledPreference = findPreference<SwitchPreferenceCompat>("is_enabled")
            isEnabledPreference?.setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    screenCaptureLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
                } else {
                    val intent = Intent(requireContext(), BackgroundService::class.java).apply {
                        action = "STOP_AUDIO_CAPTURE"
                    }
                    requireContext().startService(intent)
                }
                true
            }
        }
    }
}
