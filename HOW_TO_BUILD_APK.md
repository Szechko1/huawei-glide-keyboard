# Hogyan építsd meg az APK-t

## Rövid összefoglaló

Az Android APK építéséhez **Android SDK** szükséges, ami ebben az online környezetben nem elérhető.

**Két lehetőséged van:**

### 1. Építsd meg a saját gépeden (Ajánlott)

#### Gyors kezdés (ha van Android Studio-d):

```bash
# 1. Klónozd/töltsd le a projektet
git clone <repository-url>
cd huawei-glide-keyboard

# 2. Nyisd meg Android Studio-ban
# File → Open → Válaszd ki a huawei-glide-keyboard mappát

# 3. Várj a Gradle sync-re

# 4. Build → Build Bundle(s) / APK(s) → Build APK(s)

# 5. APK helye: app/build/outputs/apk/debug/app-debug.apk
```

#### Gyors kezdés (parancssorból):

```bash
# 1. Töltsd le és telepítsd az Android SDK-t
# https://developer.android.com/studio

# 2. Állítsd be az ANDROID_HOME környezeti változót
export ANDROID_HOME=$HOME/Android/Sdk  # Linux/macOS
# vagy Windows-on: C:\Users\YourName\AppData\Local\Android\Sdk

# 3. Klónozd a projektet
git clone <repository-url>
cd huawei-glide-keyboard

# 4. Építsd meg
./build.sh  # Linux/macOS
# vagy
gradlew.bat assembleDebug  # Windows

# 5. APK helye: app/build/outputs/apk/debug/app-debug.apk
```

### 2. Használj online build szolgáltatást

Ha nincs lehetőséged helyben build-elni, próbálhatsz online szolgáltatásokat:

- **GitHub Actions** (ingyenes, ajánlott)
- **Bitrise**
- **CircleCI**
- **AppCenter**

## Részletes útmutató

👉 **Nézd meg a [BUILD_GUIDE.md](BUILD_GUIDE.md) fájlt**, ami részletesen leírja:

1. Szükséges eszközök telepítése (JDK, Android SDK)
2. Környezet beállítása
3. APK építése (debug és release)
4. APK telepítése eszközre
5. Hibaelhárítás
6. CI/CD konfiguráció

## Lépések képernyőképekkel (Android Studio)

### 1. Projekt megnyitása

![Open Project](docs/images/open-project.png)

1. Indítsd el az Android Studio-t
2. Kattints "Open"
3. Válaszd ki a `huawei-glide-keyboard` mappát
4. Kattints "OK"

### 2. Gradle Sync

![Gradle Sync](docs/images/gradle-sync.png)

- Android Studio automatikusan szinkronizálja a projektet
- Várj, amíg befejeződik (lásd alsó státuszsor)
- Ha hibát ír, ellenőrizd az SDK telepítését

### 3. APK építése

![Build APK](docs/images/build-apk.png)

1. Menü: **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. Várj a build befejezésére
3. Kattints a "locate" linkre
4. APK helye: `app/build/outputs/apk/debug/app-debug.apk`

### 4. APK telepítése

**Módszer A: USB-vel**

1. Engedélyezd az USB hibakeresést az eszközödön
2. Csatlakoztasd USB kábellel
3. Parancssor:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

**Módszer B: Fájl átvitel**

1. Másold át az APK-t az eszközödre
2. Nyisd meg fájlkezelővel
3. Koppints az APK-ra
4. Engedélyezd az "Ismeretlen forrás" telepítését
5. Koppints "Telepítés"

## Gyors build ellenőrzés (parancssor)

Ha már van telepített Android SDK-d, futtasd ezeket a parancsokat:

```bash
# Ellenőrizd a Java-t
java -version

# Ellenőrizd az Android SDK-t
echo $ANDROID_HOME
ls $ANDROID_HOME/platforms

# Tisztítsd a projektet
./gradlew clean

# Építsd az APK-t
./gradlew assembleDebug

# Ellenőrizd az eredményt
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

## Gyakori problémák

### "SDK location not found"

**Megoldás:** Hozz létre `local.properties` fájlt:
```properties
sdk.dir=/path/to/your/android-sdk
```

### "Could not find com.android.tools.build:gradle"

**Megoldás:** Ellenőrizd az internet kapcsolatot:
```bash
./gradlew clean build --refresh-dependencies
```

### "Android SDK Platform 34 not found"

**Megoldás:** Telepítsd az SDK-t:
```bash
sdkmanager "platforms;android-34"
```

## Build parancsok referencia

```bash
# Tisztítás
./gradlew clean

# Debug APK (gyors, teszteléshez)
./gradlew assembleDebug

# Release APK (optimalizált, publikáláshoz)
./gradlew assembleRelease

# Telepítés csatlakoztatott eszközre
./gradlew installDebug

# Összes elérhető task listázása
./gradlew tasks

# Build részletes kimenettel
./gradlew assembleDebug --info

# Build offline módban (internet nélkül)
./gradlew assembleDebug --offline
```

## Fájl méretek

- **app-debug.apk**: ~2-5 MB (teszteléshez)
- **app-release.apk**: ~1-3 MB (optimalizált, publisholható)

## Rendszerkövetelmények

- **JDK**: 8 vagy újabb (11 vagy 17 ajánlott)
- **Android SDK**: API 21+ (minimum), API 34 (target)
- **RAM**: minimum 4 GB (8 GB ajánlott)
- **Tárhely**: minimum 5 GB (SDK + projekt + build cache)
- **Internet**: első build-hez függőségek letöltéséhez

## Következő lépések a build után

1. **Telepítsd az APK-t** az eszközödre
2. **Engedélyezd a billentyűzetet**: Beállítások → Rendszer → Nyelvek és bevitel → Képernyő billentyűzetek
3. **Válaszd ki billentyűzetként**: Koppints egy szövegmezőre → Billentyűzet váltó ikon
4. **Használd a glide funkciót**: Húzd végig az ujjad a betűkön

## További segítség

- **Részletes útmutató**: [BUILD_GUIDE.md](BUILD_GUIDE.md)
- **Android dokumentáció**: [ANDROID_README.md](ANDROID_README.md)
- **Projekt README**: [README.md](README.md)
- **Android Developer**: https://developer.android.com/studio/build

## Kérdések?

Ha problémád van:

1. Olvasd el a [BUILD_GUIDE.md](BUILD_GUIDE.md) hibaelhárítás szekciót
2. Futtasd a build-et `--stacktrace` opcióval részletes hibákért:
   ```bash
   ./gradlew assembleDebug --stacktrace
   ```
3. Ellenőrizd az Android Studio "Build" paneljét
4. Nézd meg a Gradle konzol kimenetét

---

**Fontos**: Az APK építéséhez mindenképpen szükséged van egy gépre, ahol telepítve van az Android SDK. Ez egy online/cloud környezetben (ahol ez a fájl készült) nem elérhető, ezért a build-et a saját gépeden kell elvégezned a fenti útmutatók szerint.
