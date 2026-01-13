# GitHub Actions - Automatikus APK Build

Ez a projekt **GitHub Actions**-t használ az APK automatikus buildeléséhez a GitHub szerverein.

## 🎯 Előnyök

✅ **Nincs szükség lokális Android SDK-ra**
✅ **Automatikus build minden push-ra**
✅ **Ingyenes GitHub Actions quota**
✅ **APK letölthető azonnal**
✅ **Több verzió kezelése**

## 🚀 Hogyan működik

### 1. Automatikus Build (minden push után)

Amikor kódot push-olsz a repository-ba, automatikusan elindul a build:

```bash
git push origin claude/implement-feature-mkcrq8s3c4espoz5-FVlRC
```

A GitHub Actions:
1. ✅ Beállítja a Java 17 környezetet
2. ✅ Telepíti az Android SDK-t
3. ✅ Build-eli az APK-t
4. ✅ Feltölti artifact-ként

### 2. APK letöltése a GitHub-ról

#### Lépések:

1. **Menj a repository GitHub oldalára**
   ```
   https://github.com/[username]/huawei-glide-keyboard
   ```

2. **Kattints az "Actions" fülre** (felül)

3. **Válaszd ki a legutóbbi workflow futást**
   - Zöld pipa = sikeres build ✅
   - Piros X = sikertelen build ❌
   - Sárga kör = még fut ⏳

4. **Görgetj le az "Artifacts" szekcióhoz**
   - Itt találod: `app-debug.apk`

5. **Kattints a letre, és töltsd le**
   - Kicsomagolás után: `app-debug.apk`

6. **Telepítsd az eszközödre**

## 📦 Workflow-k

### Build APK (`build-apk.yml`)

**Mikor fut:**
- Push bármely branch-re (main, master, develop, claude/**)
- Pull request main/master-re
- Manuális trigger (workflow_dispatch)

**Mit csinál:**
```yaml
1. Checkout kód
2. JDK 17 telepítése
3. Android SDK telepítése
4. gradlew futtatása (assembleDebug)
5. APK feltöltése artifact-ként
```

**Kimenet:**
- `app-debug.apk` (használható APK fájl)
- Build summary a futás végén
- PR komment (ha pull request)

### Release (`release.yml`)

**Mikor fut:**
- Version tag push-olva (pl. `v1.0`, `v1.1`)

**Mit csinál:**
```yaml
1. Build APK
2. APK átnevezése verzióval (huawei-glide-keyboard-1.0.apk)
3. Checksums generálása (SHA256, MD5)
4. GitHub Release létrehozása
5. APK csatolása a release-hez
```

**Kimenet:**
- Új Release a GitHub Releases oldalon
- Letölthető APK a release-ben
- Részletes leírás és telepítési útmutató

## 🎮 Használat

### Automatikus build indítása

**Egyszerűen push-olj:**
```bash
git add .
git commit -m "Update feature"
git push
```

✨ A GitHub Actions automatikusan buildet!

### Manuális build indítása

1. Menj: **Actions** → **Build Android APK**
2. Kattints: **Run workflow**
3. Válaszd ki a branch-et
4. Kattints: **Run workflow** (zöld gomb)

### Release készítése

```bash
# 1. Tag létrehozása
git tag -a v1.0 -m "Release version 1.0"

# 2. Tag push-olása
git push origin v1.0

# 3. GitHub automatikusan készít egy Release-t az APK-val
```

A Release megjelenik a **Releases** oldalon:
```
https://github.com/[username]/huawei-glide-keyboard/releases
```

## 📥 APK letöltése lépésről-lépésre

### GitHub Actions Artifacts-ból

1. 🌐 **Nyisd meg a repository-t GitHub-on**

2. 🎬 **Kattints "Actions"** (felső menü)

3. ✅ **Válaszd ki az utolsó sikeres build-et**
   - Zöld pipa jelöli
   - Általában a legelső a listában

4. 📦 **Görgess le az "Artifacts" részhez**
   - Alul, a workflow részletek alatt

5. ⬇️ **Kattints az "app-debug" linkre**
   - Letöltődik egy ZIP fájl

6. 📂 **Csomagold ki a ZIP-et**
   - Benne: `app-debug.apk`

7. 📲 **Másold az eszközödre és telepítsd**

### GitHub Releases-ből (ha van release)

1. 🌐 **Menj a Releases oldalra**
   ```
   https://github.com/[username]/huawei-glide-keyboard/releases
   ```

2. 📋 **Válaszd ki a kívánt verziót**
   - Pl: v1.0, v1.1, stb.

3. 📥 **Kattints az APK fájlra**
   - Pl: `huawei-glide-keyboard-1.0.apk`
   - Közvetlenül letöltődik (nem kell kicsomagolni)

4. 📲 **Telepítsd az eszközödre**

## 🔍 Build státusz ellenőrzése

### GitHub badge hozzáadása README-hez

Adj hozzá egy badge-et, ami mutatja a build státuszt:

```markdown
![Build Status](https://github.com/[username]/huawei-glide-keyboard/workflows/Build%20Android%20APK/badge.svg)
```

### Build napló megtekintése

1. Actions → Workflow futás kiválasztása
2. "Build APK" job
3. Részletes logok láthatók minden lépéshez

## ⚙️ Beállítások

### GitHub Actions engedélyezése

Ha a repository-d új, lehet hogy engedélyezned kell az Actions-t:

1. **Settings** → **Actions** → **General**
2. **Allow all actions** kiválasztása
3. **Save**

### Secrets beállítása (opcionális)

Ha signed APK-t szeretnél:

1. **Settings** → **Secrets and variables** → **Actions**
2. **New repository secret**
3. Add meg:
   - `KEYSTORE_FILE` - Base64 encoded keystore
   - `KEYSTORE_PASSWORD` - Keystore jelszó
   - `KEY_ALIAS` - Key alias
   - `KEY_PASSWORD` - Key jelszó

## 📊 GitHub Actions Limits

**Free tier:**
- ✅ 2000 perc/hó (public repos esetén KORLÁTLAN)
- ✅ 500 MB artifact storage
- ✅ 1 GB cache storage

**Az APK build:**
- ⏱️ ~5-8 perc buildenként
- 💾 ~3-5 MB APK méret
- 📦 Artifacts 30 napig megmaradnak

## 🐛 Hibaelhárítás

### "Build failed" - mi a probléma?

1. **Kattints a hibás workflow-ra**
2. **Nyisd meg a "Build APK" job-ot**
3. **Nézd meg a piros X-szel jelölt lépést**
4. **Olvasd el a hibákat a logban**

### Gyakori hibák:

**"Gradle build failed"**
```
Megoldás: Ellenőrizd a build.gradle fájlokat
```

**"SDK not found"**
```
Megoldás: A workflow már tartalmaz SDK telepítést,
újra futtatás általában megoldja
```

**"Permission denied: gradlew"**
```
Megoldás: chmod +x gradlew (már be van állítva)
```

### Workflow újrafuttatása

Ha egy build elhasalt:

1. Menj a sikertelen workflow-hoz
2. Kattints: **Re-run jobs** → **Re-run all jobs**
3. Várj az új build-re

## 🎨 Testreszabás

### Build több branch-re

Szerkeszd `.github/workflows/build-apk.yml`:

```yaml
on:
  push:
    branches: [ main, master, develop, 'feature/**' ]  # ← add hozzá
```

### Build csak bizonyos fájlok változásakor

```yaml
on:
  push:
    paths:
      - 'app/**'
      - '*.gradle'
      - 'gradle/**'
```

### Notification email-ről

GitHub automatikusan küld email-t sikertelen build-ről.

Kikapcsolás: **Settings** → **Notifications** → Actions

## 📚 További források

- [GitHub Actions dokumentáció](https://docs.github.com/en/actions)
- [Android GitHub Actions](https://github.com/android-actions)
- [Setup Android Action](https://github.com/android-actions/setup-android)

## 💡 Tippek

1. **Push gyakran** - minden push buildet indít, azonnal látod a hibákat
2. **Használd a PR-eket** - automatikus build és teszt merge előtt
3. **Készíts release-eket** - verziókövetés és changelog
4. **Nézd a logokat** - tanulj a build folyamatról

## 🎯 Quick Start Checklist

- [ ] Repository létrehozva GitHub-on
- [ ] Kód push-olva
- [ ] Actions fül megnyitva
- [ ] Első build lefutott (zöld pipa)
- [ ] Artifacts letöltve
- [ ] APK kicsomagolva
- [ ] APK telepítve eszközre
- [ ] Billentyűzet engedélyezve
- [ ] Működik! 🎉

---

**Most már tudsz APK-t építeni GitHub-on, Android SDK telepítése nélkül!** 🚀
