# Virasat — Namma Guide 🏛
### ವಿರಾಸತ್ — ನಮ್ಮ ಮಾರ್ಗದರ್ಶಿ
**Smart Heritage Tourism Guide for Karnataka, India**

---

## Prerequisites

| Tool | Version |
|------|---------|
| Android Studio | Hedgehog (2023.1.1) or newer |
| Kotlin | 1.9.23 |
| Gradle | 8.5 |
| Min Android SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |

---

## Setup Instructions

### 1. Open in Android Studio
1. Clone/copy this folder.
2. Open **Android Studio → File → Open** → select `VirasatNammaGuide/`
3. Let Gradle sync finish.

### 2. Add Noto Serif Font (Required)
Download **Noto Serif** from [Google Fonts](https://fonts.google.com/noto/specimen/Noto+Serif) and place these files in `app/src/main/res/font/`:
- `noto_serif_regular.ttf`
- `noto_serif_bold.ttf`
- `noto_serif_italic.ttf`

> **Alternative:** In Android Studio, right-click `res/font/` → New → Font Resource File → use the Downloadable Fonts option to auto-fetch Noto Serif.

### 3. Add Launcher Icons (Required)
Place launcher icons in the mipmap folders, or use Android Studio's **Image Asset Studio**:
- Right-click `res/` → New → Image Asset
- Icon Type: Launcher Icons (Adaptive and Legacy)
- Use a temple/heritage motif in maroon (#8B1A1A)

### 4. Audio Guide Files (Optional)
To add real audio narration:
1. Place MP3 files in `app/src/main/res/raw/`:
   - `audio_ka001.mp3` — Hampi Vittala Temple
   - `audio_ka002.mp3` — Belur Chennakeshava
   - `audio_ka003.mp3` — Badami Cave Temples
   - `audio_ka004.mp3` — Shravanabelagola
   - `audio_ka005.mp3` — Aihole Durga Temple
2. Update `SiteRepository.kt` — change `audioResId = 0` to `audioResId = R.raw.audio_kaXXX`

### 5. Run the App
```
./gradlew assembleDebug
# or press ▶ Run in Android Studio
```

---

## Testing QR Scanning

Generate a QR code containing the text `KA001` (or KA002–KA005) using any online QR generator:
- https://www.qr-code-generator.com/
- https://goqr.me/

Point the QR Scanner screen at the generated QR code — it will open the corresponding heritage site detail screen.

---

## Architecture

```
MVVM + Jetpack Navigation (Single Activity)
├── UI Layer       → Fragments (Discovery, Detail, QRScanner, Passport)
├── ViewModel      → SiteViewModel, CheckInViewModel  
├── Repository     → SiteRepository (single source of truth)
└── Data Layer     → Room DB (CheckInEntity, CheckInDao, VirasatDatabase)
```

## Package Structure

```
com.virasat.nammaguide/
├── MainActivity.kt
├── data/
│   ├── db/            CheckInEntity, CheckInDao, VirasatDatabase
│   ├── model/         HeritageSite
│   └── repository/    SiteRepository
├── ui/
│   ├── discovery/     SiteDiscoveryFragment, SiteCardAdapter
│   ├── detail/        SiteDetailFragment
│   ├── qr/            QRScannerFragment
│   └── passport/      PassportFragment, PassportAdapter
└── viewmodel/
    ├── SiteViewModel.kt
    └── CheckInViewModel.kt
```

---

## Key Implementation Notes

| Feature | Implementation |
|---------|---------------|
| Audio crash prevention | MediaPlayer released in `ViewModel.onCleared()` |
| QR camera freeze | `ImageProxy.close()` in `addOnCompleteListener`, NOT `onSuccessListener` |
| Duplicate navigation | Boolean `isNavigating` flag debounces rapid QR detections |
| Check-in persistence | Room DB with `Flow<List<CheckInEntity>>` → LiveData |
| Language toggle | EN/KN `strings.xml` in `values/` and `values-kn/` |

---

## Color Palette

| Role | Color | Hex |
|------|-------|-----|
| Primary (Temple Maroon) | ![#8B1A1A](https://via.placeholder.com/15/8B1A1A/000000?text=+) | `#8B1A1A` |
| Secondary (Sandstone Gold) | ![#C8860A](https://via.placeholder.com/15/C8860A/000000?text=+) | `#C8860A` |
| Background (Aged Parchment) | ![#FDF3E3](https://via.placeholder.com/15/FDF3E3/000000?text=+) | `#FDF3E3` |
| Surface (Warm Off-White) | ![#FFF8F0](https://via.placeholder.com/15/FFF8F0/000000?text=+) | `#FFF8F0` |

---

## License
Educational / Portfolio project. Heritage site information is in the public domain.
