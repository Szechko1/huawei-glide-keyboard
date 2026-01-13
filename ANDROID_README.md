# Huawei Glide Keyboard - Android Application

An installable Android keyboard application with glide/swipe input functionality.

## Project Structure

```
huawei-glide-keyboard/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/huawei/glidekeyboard/
│   │       │   ├── GlideKeyboardService.java    # Main keyboard service
│   │       │   ├── GlidePathDetector.java       # Glide gesture detection
│   │       │   ├── WordPredictor.java           # Word prediction logic
│   │       │   └── SettingsActivity.java        # Settings UI
│   │       ├── res/
│   │       │   ├── layout/                      # UI layouts
│   │       │   ├── xml/                         # Keyboard layouts
│   │       │   ├── values/                      # Strings, colors
│   │       │   └── drawable/                    # Graphics resources
│   │       └── AndroidManifest.xml
│   ├── build.gradle                             # App-level build config
│   └── proguard-rules.pro                       # ProGuard rules
├── build.gradle                                 # Project-level build config
├── settings.gradle                              # Project settings
└── gradle.properties                            # Gradle properties
```

## Features

### Core Functionality
- **Glide/Swipe Input**: Type by swiping across letters
- **QWERTY Layout**: Standard keyboard layout
- **Symbols Layout**: Numbers and special characters
- **Word Prediction**: Real-time word suggestions based on glide path
- **Suggestion Strip**: Shows top 3 predicted words
- **Key Preview**: Visual feedback when keys are pressed

### Implementation Details

#### GlideKeyboardService
- Extends `InputMethodService`
- Manages keyboard view and input processing
- Handles key presses and glide gestures
- Updates suggestion strip with predictions

#### GlidePathDetector
- Tracks touch events to detect glide gestures
- Converts screen coordinates to keyboard keys
- Filters out noise and short taps
- Sends completed paths to word predictor

#### WordPredictor
- Implements word matching algorithm
- Maintains dictionary of common English words
- Scores candidate words based on:
  - Letter sequence matching
  - Path length similarity
  - Character accuracy
- Returns ranked predictions

## Building the Application

### Prerequisites

1. **Android Studio** (Arctic Fox or later)
   - Download from: https://developer.android.com/studio

2. **Android SDK**
   - Minimum SDK: API 21 (Android 5.0)
   - Target SDK: API 34 (Android 14)
   - Compile SDK: API 34

3. **Java Development Kit (JDK)**
   - JDK 8 or later

### Build Steps

#### Option 1: Using Android Studio

1. **Open Project**
   ```bash
   # Launch Android Studio
   # File → Open → Select the huawei-glide-keyboard directory
   ```

2. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

3. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

4. **Locate APK**
   - APK will be in: `app/build/outputs/apk/debug/app-debug.apk`

#### Option 2: Using Command Line

1. **Navigate to Project Directory**
   ```bash
   cd huawei-glide-keyboard
   ```

2. **Make Gradle Wrapper Executable** (Linux/Mac)
   ```bash
   chmod +x gradlew
   ```

3. **Build Debug APK**
   ```bash
   # Linux/Mac
   ./gradlew assembleDebug

   # Windows
   gradlew.bat assembleDebug
   ```

4. **Build Release APK** (Signed)
   ```bash
   ./gradlew assembleRelease
   ```

5. **Install Directly to Connected Device**
   ```bash
   ./gradlew installDebug
   ```

### Output Files

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release-unsigned.apk`

## Installation

### Method 1: Install via ADB

```bash
# Enable USB debugging on your Android device
# Connect device via USB

adb install app/build/outputs/apk/debug/app-debug.apk
```

### Method 2: Install from Device

1. Copy APK file to your Android device
2. Open file manager on device
3. Tap the APK file
4. Allow "Install from Unknown Sources" if prompted
5. Tap "Install"

### Method 3: Install via Android Studio

1. Connect Android device or start emulator
2. Click "Run" button (green triangle) in Android Studio
3. Select target device
4. App will build and install automatically

## Enabling the Keyboard

After installation:

1. **Open the App**
   - Find "Huawei Glide Keyboard" in app drawer
   - Tap to open settings

2. **Enable Keyboard**
   - Tap "Enable Keyboard" button
   - Or go to: Settings → System → Languages & Input → On-screen keyboard
   - Enable "Glide Keyboard"

3. **Select Keyboard**
   - Tap "Select Keyboard" button in app
   - Or tap any text field
   - Tap keyboard switcher icon (⌨️) in navigation bar
   - Select "Glide Keyboard"

## Using the Keyboard

### Glide Input
1. Place finger on first letter
2. Swipe through each letter of the word
3. Lift finger on last letter
4. Keyboard predicts and inserts the word

### Tap to Type
- Tap individual keys to type normally
- Works like a standard keyboard

### Word Suggestions
- Suggestions appear in strip above keyboard
- Tap suggestion to insert word
- Updates as you type

### Switch Layouts
- Tap "123" to switch to symbols
- Tap "ABC" to return to letters

### Special Keys
- **Shift**: Capitalize next letter
- **Backspace**: Delete character
- **Enter**: Submit text
- **Space**: Insert space

## Customization

### Adding Words to Dictionary

Edit `WordPredictor.java` and add words to the `commonWords` array:

```java
String[] commonWords = {
    "hello", "world", "custom", "words", ...
};
```

### Changing Keyboard Colors

Edit `app/src/main/res/values/colors.xml`:

```xml
<color name="keyboard_background">#FF2C2C2C</color>
<color name="key_background">#FF424242</color>
<color name="key_text">#FFFFFFFF</color>
```

### Adjusting Keyboard Layout

Edit layout files in `app/src/main/res/xml/`:
- `qwerty.xml` - Letter layout
- `symbols.xml` - Number/symbol layout

## Troubleshooting

### Build Errors

**Problem**: Gradle sync failed
```
Solution: Update gradle.properties with correct SDK path
```

**Problem**: Cannot find SDK
```
Solution:
# Create/update local.properties
sdk.dir=/path/to/Android/Sdk
```

**Problem**: Dependencies not downloading
```
Solution: Check internet connection and try:
./gradlew clean build --refresh-dependencies
```

### Installation Issues

**Problem**: "App not installed"
```
Solution: Uninstall any previous version first
```

**Problem**: "Unknown sources blocked"
```
Solution: Settings → Security → Allow Unknown Sources
```

### Keyboard Not Appearing

**Problem**: Keyboard not in list
```
Solution:
1. Reinstall app
2. Restart device
3. Check if keyboard is enabled in system settings
```

**Problem**: Can't enable keyboard
```
Solution: Check if app has necessary permissions
Settings → Apps → Glide Keyboard → Permissions
```

## Development

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumentation tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

### Debugging

1. Build and install debug version
2. In Android Studio: Run → Debug
3. Use logcat to view logs:
   ```bash
   adb logcat | grep GlideKeyboard
   ```

### Code Structure

- **Service Layer**: `GlideKeyboardService` handles Android IME lifecycle
- **Detection Layer**: `GlidePathDetector` processes touch events
- **Prediction Layer**: `WordPredictor` ranks word candidates
- **UI Layer**: Layouts and resources for keyboard appearance

## Future Enhancements

- [ ] Load larger word dictionaries from files
- [ ] Implement user learning (personalized predictions)
- [ ] Add multi-language support
- [ ] Implement auto-correction
- [ ] Add themes and customization options
- [ ] Voice input integration
- [ ] Emoji keyboard layout
- [ ] Gesture shortcuts (swipe for delete, etc.)

## License

MIT License

## Credits

Based on the Python glide keyboard implementation, ported to Android with enhanced features and Android-specific optimizations.

## Support

For issues or questions:
- Check the troubleshooting section above
- Review Android Input Method documentation
- Check Android Studio build output for specific errors

## Technical Requirements

- **Minimum Android Version**: 5.0 (API 21)
- **Target Android Version**: 14 (API 34)
- **Required Permissions**: BIND_INPUT_METHOD (automatic for IME)
- **Storage**: ~5 MB installed
- **RAM**: Minimal (<10 MB runtime)
