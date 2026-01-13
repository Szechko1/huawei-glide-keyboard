# APK Build Guide - Huawei Glide Keyboard

Complete guide to building the APK from source code.

## Prerequisites

### 1. Java Development Kit (JDK)

**Required**: JDK 8 or later (JDK 11 or 17 recommended)

#### Installation:

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**macOS:**
```bash
brew install openjdk@17
```

**Windows:**
- Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/)
- Install and add to PATH

**Verify installation:**
```bash
java -version
javac -version
```

### 2. Android SDK

You have two options: Android Studio (easier) or Command Line Tools (lighter).

#### Option A: Android Studio (Recommended for beginners)

1. **Download Android Studio**
   - Visit: https://developer.android.com/studio
   - Download for your OS

2. **Install Android Studio**
   - Follow installation wizard
   - Install recommended SDK components

3. **Configure SDK**
   - Open Android Studio
   - Go to: Tools → SDK Manager
   - Ensure these are installed:
     - Android SDK Platform 34 (Android 14)
     - Android SDK Build-Tools (latest)
     - Android SDK Platform-Tools

4. **Set Environment Variable**

   **Linux/macOS** (add to `~/.bashrc` or `~/.zshrc`):
   ```bash
   export ANDROID_HOME=$HOME/Android/Sdk
   export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   ```

   **Windows** (System Environment Variables):
   ```
   ANDROID_HOME=C:\Users\YourName\AppData\Local\Android\Sdk
   PATH=%PATH%;%ANDROID_HOME%\cmdline-tools\latest\bin
   PATH=%PATH%;%ANDROID_HOME%\platform-tools
   ```

#### Option B: Android Command Line Tools (For advanced users)

1. **Download Command Line Tools**
   - Visit: https://developer.android.com/studio#command-tools
   - Download for your OS

2. **Extract and Setup**
   ```bash
   # Linux/macOS
   mkdir -p $HOME/android-sdk/cmdline-tools
   unzip commandlinetools-*.zip -d $HOME/android-sdk/cmdline-tools
   mv $HOME/android-sdk/cmdline-tools/cmdline-tools $HOME/android-sdk/cmdline-tools/latest

   # Set environment variable
   export ANDROID_HOME=$HOME/android-sdk
   export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
   ```

3. **Install SDK Components**
   ```bash
   sdkmanager "platforms;android-34"
   sdkmanager "build-tools;34.0.0"
   sdkmanager "platform-tools"
   ```

4. **Accept Licenses**
   ```bash
   sdkmanager --licenses
   ```

### 3. Verify Setup

Run these commands to verify everything is installed:

```bash
# Check Java
java -version

# Check Android SDK
echo $ANDROID_HOME
ls $ANDROID_HOME/platforms

# Check SDK manager
sdkmanager --list | grep "platforms;android-34"
```

## Building the APK

### Method 1: Using the Build Script (Easiest)

We provide a build script that checks requirements and builds automatically:

**Linux/macOS:**
```bash
./build.sh
```

**Windows:**
```bash
gradlew.bat assembleDebug
```

### Method 2: Using Gradle Directly

#### Build Debug APK (unsigned, for testing)

```bash
# Linux/macOS
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

#### Build Release APK (signed, for distribution)

1. **Create a keystore** (first time only):
   ```bash
   keytool -genkey -v -keystore my-release-key.keystore \
           -alias my-key-alias \
           -keyalg RSA -keysize 2048 \
           -validity 10000
   ```

2. **Create `keystore.properties`** in project root:
   ```properties
   storePassword=your_store_password
   keyPassword=your_key_password
   keyAlias=my-key-alias
   storeFile=../my-release-key.keystore
   ```

3. **Build release APK:**
   ```bash
   ./gradlew assembleRelease
   ```

### Method 3: Using Android Studio

1. Open project in Android Studio
2. Wait for Gradle sync to complete
3. Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
4. Click "locate" link when build completes

## Output Locations

After successful build, APKs are located at:

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`

## Installing the APK

### On Connected Device (USB)

1. **Enable USB Debugging** on your Android device:
   - Settings → About Phone → Tap "Build Number" 7 times
   - Settings → Developer Options → Enable USB Debugging

2. **Connect device via USB**

3. **Install APK:**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk

   # Or to reinstall/update:
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

### On Device Manually

1. **Copy APK to device**
   - Via USB cable
   - Via Bluetooth
   - Via cloud storage
   - Via email

2. **Install from device:**
   - Open file manager
   - Navigate to APK file
   - Tap to install
   - Allow "Install from Unknown Sources" if prompted

### On Emulator

```bash
# Start emulator
emulator -avd Pixel_4_API_34

# Install APK
adb -e install app/build/outputs/apk/debug/app-debug.apk
```

## Build Variants

### Debug Build
- Faster build time
- Includes debugging information
- Not optimized
- Larger APK size
- Auto-signed with debug key

```bash
./gradlew assembleDebug
```

### Release Build
- Optimized code
- Smaller APK size
- ProGuard/R8 enabled (if configured)
- Requires signing with your key

```bash
./gradlew assembleRelease
```

## Troubleshooting

### Problem: "SDK location not found"

**Solution:** Create `local.properties` file in project root:
```properties
sdk.dir=/path/to/your/android/sdk
```

Example paths:
- Linux: `sdk.dir=/home/username/Android/Sdk`
- macOS: `sdk.dir=/Users/username/Library/Android/sdk`
- Windows: `sdk.dir=C\:\\Users\\username\\AppData\\Local\\Android\\Sdk`

### Problem: "Build failed - Could not find com.android.tools.build:gradle:8.2.0"

**Solution:** Check internet connection and Gradle cache:
```bash
./gradlew clean build --refresh-dependencies
```

### Problem: "Execution failed for task ':app:processDebugResources'"

**Solution:** Install Android SDK Platform 34:
```bash
sdkmanager "platforms;android-34"
```

### Problem: "AAPT2 not found"

**Solution:** Install build tools:
```bash
sdkmanager "build-tools;34.0.0"
```

### Problem: Gradle daemon issues

**Solution:** Kill and restart Gradle daemon:
```bash
./gradlew --stop
./gradlew assembleDebug
```

### Problem: Out of memory during build

**Solution:** Increase Gradle heap size in `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### Problem: "Could not determine the dependencies"

**Solution:** Check repositories in `build.gradle`:
```gradle
repositories {
    google()
    mavenCentral()
}
```

## Build Configuration

### Changing Version

Edit `app/build.gradle`:
```gradle
android {
    defaultConfig {
        versionCode 2      // Increment for each release
        versionName "1.1"  // User-visible version
    }
}
```

### Changing Package Name

1. Update `app/build.gradle`:
   ```gradle
   android {
       namespace 'com.yourcompany.glidekeyboard'
       defaultConfig {
           applicationId "com.yourcompany.glidekeyboard"
       }
   }
   ```

2. Refactor package in Android Studio:
   - Right-click package → Refactor → Rename

### Changing App Name

Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Your App Name</string>
```

### Changing Minimum Android Version

Edit `app/build.gradle`:
```gradle
android {
    defaultConfig {
        minSdk 23  // Change from 21 to support newer features
    }
}
```

## Build Performance Tips

1. **Enable Gradle Daemon** (usually automatic)

2. **Enable Parallel Builds** in `gradle.properties`:
   ```properties
   org.gradle.parallel=true
   org.gradle.caching=true
   ```

3. **Use Configuration Cache**:
   ```bash
   ./gradlew assembleDebug --configuration-cache
   ```

4. **Increase Heap Size** in `gradle.properties`:
   ```properties
   org.gradle.jvmargs=-Xmx4096m
   ```

## Continuous Integration (CI/CD)

### GitHub Actions Example

Create `.github/workflows/build.yml`:

```yaml
name: Android Build

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build with Gradle
      run: ./gradlew assembleDebug

    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

## Additional Resources

- [Android Developer Guide](https://developer.android.com/studio/build)
- [Gradle Build Tool](https://gradle.org/)
- [Android SDK Manager](https://developer.android.com/studio/command-line/sdkmanager)
- [APK Signing](https://developer.android.com/studio/publish/app-signing)

## Support

If you encounter issues:

1. Check this troubleshooting guide
2. Review build output for specific errors
3. Check Android Studio's "Build" panel for details
4. Run with `--stacktrace` for more info:
   ```bash
   ./gradlew assembleDebug --stacktrace
   ```

## Quick Reference

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on device
./gradlew installDebug

# Run all checks
./gradlew check

# List all tasks
./gradlew tasks

# Build with verbose output
./gradlew assembleDebug --info

# Offline build (use cached dependencies)
./gradlew assembleDebug --offline
```
