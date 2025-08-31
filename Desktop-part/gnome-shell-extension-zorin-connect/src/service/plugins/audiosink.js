// SPDX-FileCopyrightText: 2025 Woolly Wink
//
// SPDX-License-Identifier: GPL-2.0-or-later

import Gio from 'gi://Gio';
import GLib from 'gi://GLib';
import GObject from 'gi://GObject';

import Plugin from '../plugin.js';

export const Metadata = {
    label: _('Audio Receiver'),
    description: _('Use your computer as a speaker for this device'),
    id: 'org.gnome.Shell.Extensions.ZorinConnect.Plugin.AudioSink',
    incomingCapabilities: ['kdeconnect.audiosink.data'],
    outgoingCapabilities: [],
};

const AudioSinkPlugin = GObject.registerClass({
    GTypeName: 'ZorinConnectAudioSinkPlugin',
}, class AudioSinkPlugin extends Plugin {

    _init(device) {
        super._init(device, 'audiosink');
        this._sinkModuleId = null;
        this._paplayProcess = null;
        this._stdinStream = null;

        this._createSink();
    }

    async _createSink() {
        try {
            const [res, out, err, status] = await this.device.executeCommand([
                'pactl',
                'load-module',
                'module-null-sink',
                'sink_name=zorin-connect-sink',
                'sink_properties=device.description=ZorinConnectAudioSink'
            ]);

            if (status === 0) {
                this._sinkModuleId = out.trim();
                this._startPlayer();
            }
        } catch (e) {
            logError(e, this.device.name);
        }
    }

    _startPlayer() {
        try {
            const [pid, stdin, stdout, stderr] = GLib.spawn_async_with_pipes(
                null, // working directory
                [
                    'paplay',
                    '--device=zorin-connect-sink',
                    '--raw',
                    '--format=s16le',
                    '--rate=44100',
                    '--channels=1'
                ],
                null, // envp
                GLib.SpawnFlags.DO_NOT_REAP_CHILD,
                null // child_setup
            );

            this._paplayProcess = Gio.Subprocess.new(pid, 0);
            this._stdinStream = new Gio.DataOutputStream({
                base_stream: new Gio.UnixOutputStream({ fd: stdin, close_fd: true }),
                close_base_stream: true
            });

        } catch (e) {
            logError(e, this.device.name);
        }
    }

    handlePacket(packet) {
        if (this._stdinStream && packet.payload) {
            this._stdinStream.write_bytes(packet.payload, null);
        }
    }

    destroy() {
        if (this._stdinStream) {
            this._stdinStream.close(null);
            this._stdinStream = null;
        }

        if (this._paplayProcess) {
            this._paplayProcess.force_exit();
            this._paplayProcess = null;
        }

        if (this._sinkModuleId) {
            this.device.executeCommand(['pactl', 'unload-module', this._sinkModuleId]);
            this._sinkModuleId = null;
        }

        super.destroy();
    }
});

export default AudioSinkPlugin;
