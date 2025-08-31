/*
 * SPDX-FileCopyrightText: 2025 Woolly Wink
 *
 * SPDX-License-Identifier: GPL-2.0-only OR GPL-3.0-only OR LicenseRef-KDE-Accepted-GPL
*/

package org.kde.kdeconnect.Plugins.AudioSinkPlugin

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.zorinos.zorin_connect.R
import org.kde.kdeconnect.NetworkPacket
import org.kde.kdeconnect.Plugins.Plugin
import org.kde.kdeconnect.Plugins.PluginFactory
import org.kde.kdeconnect.UserInterface.PluginSettingsFragment

@PluginFactory.LoadablePlugin
class AudioSinkPlugin : Plugin() {

    private val audioDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val audioData = intent.getByteArrayExtra("data")
            if (audioData != null) {
                val np = NetworkPacket("kdeconnect.audiosink.data")
                np.payload = NetworkPacket.Payload(audioData)
                device.sendPacket(np)
            }
        }
    }

    override val displayName: String
        get() = context.getString(R.string.pref_plugin_audiosink)

    override val description: String
        get() = context.getString(R.string.pref_plugin_audiosink_desc)

    override val icon: Int
        get() = R.drawable.ic_baseline_speaker_24

    override fun hasSettings(): Boolean {
        return true
    }

    override fun getSettingsFragment(activity: Activity): PluginSettingsFragment? {
        return PluginSettingsFragment.newInstance(pluginKey, R.xml.audiosink_preferences)
    }

    override fun onCreate(): Boolean {
        super.onCreate()
        LocalBroadcastManager.getInstance(context).registerReceiver(audioDataReceiver, IntentFilter("AudioData"))
        return true
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(audioDataReceiver)
        super.onDestroy()
    }

    override val supportedPacketTypes: Array<String>
        get() = arrayOf()

    override val outgoingPacketTypes: Array<String>
        get() = arrayOf("kdeconnect.audiosink.data")

    override fun displayAsButton(context: Context): Boolean {
        return true
    }

    override fun startMainActivity(parentActivity: Activity) {
        val intent = Intent(parentActivity, AudioSinkSettingsActivity::class.java)
        intent.putExtra("deviceId", device.deviceId)
        parentActivity.startActivity(intent)
    }
}

