# Huawei Glide Billentyűzet

Teljes glide/swipe billentyűzet rendszer Python prototípussal és Android alkalmazással.

## 📱 Rövid leírás

Ez egy **telepíthető Android billentyűzet alkalmazás**, ami lehetővé teszi a gyors szóírást ujjad áthúzásával a billentyűkön (glide/swipe típus). A rendszer automatikusan felismeri és előrejelzi a szavakat az ujj mozgása alapján.

## ✨ Főbb funkciók

- **Glide/Swipe gesztusok**: Húzd végig az ujjad a betűkön szavak írásához
- **Valós idejű szópredikció**: 3 szójavaslat megjelenítése írás közben
- **QWERTY elrendezés**: Standard billentyűzet kiosztás
- **Szimbólumok támogatása**: Számok és speciális karakterek
- **Billentyű előnézet**: Vizuális visszajelzés gomb nyomáskor
- **Testreszabható**: Glide be/ki, szójavaslatok be/ki

## 🚀 Gyors kezdés - APK beszerzése

### 🎯 Opció 1: GitHub Actions (LEGEGYSZERŰBB - AJÁNLOTT! 🌟)

**Nincs szükség semmilyen telepítésre! Az APK már készen vár!**

```bash
1. Menj a GitHub repository Actions fülére
2. Válaszd ki a legutóbbi sikeres build-et (zöld pipa ✅)
3. Görgess le az "Artifacts" részhez
4. Töltsd le az "app-debug" fájlt
5. Csomagold ki a ZIP-et → app-debug.apk
6. Telepítsd az eszközödre!
```

**👉 [Részletes útmutató: GITHUB_BUILD.md](GITHUB_BUILD.md)**

### 💻 Opció 2: Saját gépen build-elés

#### 2a. Android Studio (Kezdőknek ajánlott)

**Szükséges:** Android Studio + Android SDK

```bash
1. Telepítsd az Android Studio-t: https://developer.android.com/studio
2. Nyisd meg a projektet Android Studio-ban
3. Build → Build Bundle(s) / APK(s) → Build APK(s)
4. APK helye: app/build/outputs/apk/debug/app-debug.apk
```

#### 2b. Parancssor (Haladóknak)

**Szükséges:** Java JDK + Android SDK

```bash
# 1. Állítsd be az Android SDK-t
export ANDROID_HOME=$HOME/Android/Sdk  # Linux/macOS

# 2. Építsd meg az APK-t
./build.sh  # Linux/macOS
# vagy
gradlew.bat assembleDebug  # Windows

# 3. APK helye
app/build/outputs/apk/debug/app-debug.apk
```

## 📖 Részletes dokumentáció

- **[GITHUB_BUILD.md](GITHUB_BUILD.md)** 🇭🇺 - **APK letöltése GitHub-ról (ajánlott!)**
- **[HOW_TO_BUILD_APK.md](HOW_TO_BUILD_APK.md)** 🇭🇺 - Magyar nyelvű gyors útmutató APK építéshez
- **[BUILD_GUIDE.md](BUILD_GUIDE.md)** 🇬🇧 - Teljes körű build útmutató (angolul)
- **[ANDROID_README.md](ANDROID_README.md)** 🇬🇧 - Android alkalmazás dokumentáció (angolul)

## 📁 Projekt struktúra

```
huawei-glide-keyboard/
├── 📱 Android Alkalmazás
│   ├── app/
│   │   ├── src/main/java/com/huawei/glidekeyboard/
│   │   │   ├── GlideKeyboardService.java      # Fő billentyűzet szolgáltatás
│   │   │   ├── GlidePathDetector.java         # Ujjmozgás érzékelő
│   │   │   ├── WordPredictor.java             # Szópredikció motor
│   │   │   └── SettingsActivity.java          # Beállítások UI
│   │   └── src/main/res/                      # Android erőforrások
│   ├── build.gradle                            # Build konfiguráció
│   └── proguard-rules.pro                      # ProGuard szabályok
│
├── 🐍 Python Prototípus
│   ├── glide_keyboard.py                       # Fő implementáció
│   ├── example.py                              # Használati példák
│   └── test_glide_keyboard.py                  # Unit tesztek
│
├── 🔧 Build Eszközök
│   ├── gradlew / gradlew.bat                   # Gradle wrapper
│   ├── build.sh                                # Automatikus build script
│   └── gradle/                                 # Gradle konfiguráció
│
└── 📚 Dokumentáció
    ├── HOW_TO_BUILD_APK.md                     # 🇭🇺 APK build útmutató
    ├── BUILD_GUIDE.md                          # 🇬🇧 Részletes build guide
    ├── ANDROID_README.md                       # 🇬🇧 Android dokumentáció
    └── README.md                               # 🇬🇧 Projekt áttekintés
```

## 💻 Technikai részletek

### Android Alkalmazás

- **Minimum Android**: 5.0 (API 21)
- **Cél Android**: 14 (API 34)
- **Nyelv**: Java
- **Architektúra**: Input Method Service (IME)
- **Build rendszer**: Gradle 8.2
- **Méret**: ~2-5 MB

### Komponensek

1. **GlideKeyboardService** - Android IME szolgáltatás
   - Billentyűzet életciklus kezelése
   - Bemenet feldolgozás
   - Szójavaslatok kezelése

2. **GlidePathDetector** - Gesztus érzékelő
   - Touch event követés
   - Ujj útvonal normalizálása
   - Billentyű szekvencia generálás

3. **WordPredictor** - Predikciós motor
   - Szótár kezelés (~100 szó, bővíthető)
   - Karakterszekvencia egyeztetés
   - Szó pontozás és rangsorolás

4. **SettingsActivity** - Beállítások
   - Billentyűzet engedélyezés
   - Funkciók be/kikapcsolása
   - Felhasználói útmutatás

## 📲 Telepítés az APK után

1. **Engedélyezd a billentyűzetet**
   ```
   Beállítások → Rendszer → Nyelvek és bevitel →
   Képernyő billentyűzetek → Glide Keyboard (BE)
   ```

2. **Válaszd ki alapértelmezett billentyűzetnek**
   ```
   Koppints egy szövegmezőre →
   Billentyűzet váltó ikon (⌨️) →
   Válaszd: Glide Keyboard
   ```

3. **Használd a glide funkciót**
   - Helyezd az ujjad az első betűre
   - Húzd végig a szó betűin
   - Engedd el az utolsó betűnél
   - A szó automatikusan beíródik!

## 🎯 Használat

### Glide/Swipe mód

```
1. Tedd az ujjad az első betűre (pl. 'h')
2. Húzd végig: h → e → l → l → o
3. Engedd el az ujjad
4. Eredmény: "hello " (szóközzel együtt)
```

### Normál (tap) mód

- Koppints az egyes betűkre külön-külön
- Működik mint egy hagyományos billentyűzet

### Szójavaslatok

- Írás közben 3 szó jelenik meg felül
- Koppints az egyik szóra, és beíródik
- Folyamatosan frissül gépeléssel

### Speciális billentyűk

- **Shift (⇧)**: Nagy betű
- **123**: Számok és szimbólumok
- **ABC**: Vissza betűkhöz
- **Backspace (⌫)**: Törlés
- **Enter (✓)**: Küldés/új sor
- **Space**: Szóköz

## 🛠️ Testreszabás

### Szavak hozzáadása a szótárhoz

Szerkeszd: `app/src/main/java/com/huawei/glidekeyboard/WordPredictor.java`

```java
String[] commonWords = {
    "hello", "world",
    "sajat", "szavaid", "itt",  // ← Add hozzá itt
    ...
};
```

### Színek megváltoztatása

Szerkeszd: `app/src/main/res/values/colors.xml`

```xml
<color name="keyboard_background">#FF2C2C2C</color>  <!-- sötét szürke -->
<color name="key_background">#FF424242</color>       <!-- világosabb szürke -->
<color name="key_text">#FFFFFFFF</color>              <!-- fehér -->
```

### Billentyűzet kiosztás módosítása

Szerkeszd: `app/src/main/res/xml/qwerty.xml` vagy `symbols.xml`

## ❓ Gyakori problémák

### "Nem találom a billentyűzetet a listában"

**Megoldás:**
1. Telepítsd újra az APK-t
2. Indítsd újra a telefont
3. Ellenőrizd a beállításokban

### "Nem tudok APK-t építeni"

**Megoldás:** Lásd [HOW_TO_BUILD_APK.md](HOW_TO_BUILD_APK.md) részletes útmutatót

### "A predikció nem működik jól"

**Megoldás:**
- Adj hozzá több szót a szótárhoz
- Húzd pontosabban végig az ujjad a betűkön
- Ellenőrizd, hogy a "Szójavaslatok" be van-e kapcsolva

## 🔮 Jövőbeli fejlesztések

- [ ] Nagyobb szótár betöltése fájlból
- [ ] Magyar nyelv támogatás (áéíóöőúüű)
- [ ] Tanulás felhasználói szokásokból
- [ ] Automatikus helyesírás javítás
- [ ] Több billentyűzet téma
- [ ] Emoji billentyűzet
- [ ] Hang bemenet integráció
- [ ] Gesztus gyorsbillentyűk

## 📄 Licenc

MIT License - szabadon használható és módosítható

## 🤝 Közreműködés

Pull request-ek és issue-k várásával!

Lehetséges területek:
- Nagyobb szótár integrálása
- Új nyelvek támogatása
- UI/UX fejlesztések
- Algoritmus optimalizálás
- Tesztek írása

## 📞 Támogatás

Ha problémád van:

1. Olvasd el a [HOW_TO_BUILD_APK.md](HOW_TO_BUILD_APK.md)-t
2. Nézd meg a [BUILD_GUIDE.md](BUILD_GUIDE.md) hibaelhárítás szekciót
3. Futtasd részletes logokkal:
   ```bash
   ./gradlew assembleDebug --info --stacktrace
   ```

## 🌟 Köszönet

A projekt demonstrálja a swipe billentyűzet technológiát és Android Input Method fejlesztést.

---

**Fontos megjegyzés:** Az APK építése Android SDK-t igényel, ami ebben az online környezetben nem elérhető. Kérlek, kövesd a [HOW_TO_BUILD_APK.md](HOW_TO_BUILD_APK.md) útmutatót az APK saját gépen történő elkészítéséhez.

## 🚦 Gyors parancsok

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Telepítés USB-n keresztül
adb install app/build/outputs/apk/debug/app-debug.apk

# Projekt tisztítása
./gradlew clean

# Összes task listázása
./gradlew tasks
```
