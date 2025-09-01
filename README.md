# Woolly-Wink Connect (Android App)

This project is a fork of Zorin Connect, which itself is a fork of the powerful and versatile [KDE Connect](https://kdeconnect.kde.org/). It allows for deep integration between your Android device and your desktop computer.

This app enables your devices to communicate securely over your local network, providing a seamless experience for managing your digital life across platforms. All communication is encrypted using RSA.

## Features

Woolly-Wink Connect brings a host of features to integrate your phone and computer:

*   **Shared Clipboard:** Copy text on one device and paste it on the other.
*   **Notification Sync:** See your phone's notifications directly on your desktop and reply to messages.
*   **File & URL Sharing:** Easily send files and links between your phone and computer.
*   **Remote Input:** Use your phone's screen as a touchpad and keyboard for your computer.
*   **Media Control:** Manage music and videos playing on your computer from your phone. Playback can automatically pause for phone calls.
*   **SMS Management:** Read and write SMS messages from the comfort of your desktop.
*   **Find My Phone:** Trigger an audible alarm on your phone to find it.
*   **Battery Status:** Quickly check your phone's battery level from your desktop.
*   **Remote Presentation Control:** Use your phone to navigate through presentation slides.
*   **Run Commands:** Execute predefined commands on your computer with a tap on your phone.
*   **Photo Browser:** Browse your phone's photo gallery from your desktop.

## Building From Source

To build this application yourself, you will need the following:

*   Java 17 JDK
*   Android SDK
    *   You can use the `sdkmanager` command-line tool to install the necessary components.

### Setup Instructions

1.  **Clone the repository:**
    ```sh
    git clone <your-repository-url>
    cd zorin-connect-android
    ```

2.  **Install Android SDK Components:**
    Ensure you have the required SDK Platform and Build Tools. This project is configured for:
    *   **Platform:** `platforms;android-34`
    *   **Build Tools:** `build-tools;34.0.0`

    You can install them using `sdkmanager`:
    ```sh
    sdkmanager "platforms;android-34" "build-tools;34.0.0"
    ```

3.  **Build the App:**
    Use the included Gradle wrapper to build the project. This will download the correct Gradle version and all dependencies.
    ```sh
    ./gradlew build
    ```

4.  **Install the APK:**
    After a successful build, the debug APK will be located in `build/outputs/apk/debug/`. The filename will look something like `zorin-connect-android-debug-*.apk`.

    You can install it on a connected Android device using the Android Debug Bridge (`adb`):
    ```sh
    adb install build/outputs/apk/debug/<apk-filename>.apk
    ```

## Usage

To use the app, you need to have the corresponding client running on your desktop computer (like Zorin Connect on Zorin OS, or KDE Connect on other Linux distributions, Windows, or macOS).

1.  Install the Woolly-Wink Connect app on your Android device.
2.  Install the client on your desktop.
3.  Ensure both devices are connected to the same local network (e.g., the same Wi-Fi).
4.  Open the app on your phone and the client on your desktop.
5.  Your devices should automatically discover each other.
6.  From your phone, select your computer from the list of available devices and click "Request pairing".
7.  Accept the pairing request on your desktop.

Once paired, you can access the various features through the app on your phone or the desktop client. You can configure which features are enabled and manage paired devices.