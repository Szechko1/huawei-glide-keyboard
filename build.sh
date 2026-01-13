#!/bin/bash

# Build script for Huawei Glide Keyboard Android app
# This script checks requirements and builds the APK

set -e  # Exit on error

echo "=========================================="
echo "Huawei Glide Keyboard - Build Script"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check Java
echo -n "Checking Java... "
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo -e "${GREEN}✓${NC} Found Java $JAVA_VERSION"
else
    echo -e "${RED}✗${NC} Java not found"
    echo "Please install JDK 8 or later"
    exit 1
fi

# Check Android SDK
echo -n "Checking Android SDK... "
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    echo -e "${RED}✗${NC} Android SDK not found"
    echo ""
    echo "Please set ANDROID_HOME or ANDROID_SDK_ROOT environment variable"
    echo "Example: export ANDROID_HOME=/path/to/android-sdk"
    echo ""
    echo "You can download Android SDK from:"
    echo "  https://developer.android.com/studio#command-tools"
    exit 1
else
    SDK_PATH="${ANDROID_HOME:-$ANDROID_SDK_ROOT}"
    echo -e "${GREEN}✓${NC} Found at $SDK_PATH"
fi

# Check for required SDK components
echo -n "Checking SDK components... "
if [ -d "$SDK_PATH/platforms/android-34" ]; then
    echo -e "${GREEN}✓${NC} Android SDK 34 found"
else
    echo -e "${YELLOW}!${NC} Android SDK 34 not found"
    echo "Installing SDK Platform 34..."
    "$SDK_PATH/cmdline-tools/latest/bin/sdkmanager" "platforms;android-34" || {
        echo -e "${RED}Failed to install SDK Platform 34${NC}"
        echo "Please install manually: sdkmanager \"platforms;android-34\""
        exit 1
    }
fi

# Check build tools
echo -n "Checking build tools... "
if [ -d "$SDK_PATH/build-tools" ] && [ "$(ls -A $SDK_PATH/build-tools)" ]; then
    BUILD_TOOLS_VERSION=$(ls "$SDK_PATH/build-tools" | sort -V | tail -1)
    echo -e "${GREEN}✓${NC} Found version $BUILD_TOOLS_VERSION"
else
    echo -e "${YELLOW}!${NC} Build tools not found, will be downloaded automatically"
fi

echo ""
echo "All requirements met! Starting build..."
echo ""

# Clean previous builds
echo "Cleaning previous builds..."
./gradlew clean

# Build debug APK
echo ""
echo "Building debug APK..."
./gradlew assembleDebug

# Check if APK was created
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ -f "$APK_PATH" ]; then
    echo ""
    echo "=========================================="
    echo -e "${GREEN}Build successful!${NC}"
    echo "=========================================="
    echo ""
    echo "APK location: $APK_PATH"
    echo "APK size: $(du -h "$APK_PATH" | cut -f1)"
    echo ""
    echo "To install on connected device:"
    echo "  adb install $APK_PATH"
    echo ""
    echo "Or copy the APK to your device and install manually."
else
    echo ""
    echo -e "${RED}Build failed - APK not found${NC}"
    exit 1
fi
