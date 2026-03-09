# Internet Lie Detector

AI-powered communication analyzer for Android. Paste any message and get a truth probability score, deception signals, hidden meaning predictions, and shareable results.

## Features

- **Message Analyzer** — Paste text, get truth probability with animated scanning effect
- **Truth Gauge** — Animated circular gauge (green/yellow/red) with percentage
- **Analysis Report** — Interpretations, linguistic signals, hidden meaning, confidence level
- **Screenshot Analyzer** — Upload chat screenshots (WhatsApp, SMS, Telegram, Instagram, iMessage) with ML Kit OCR
- **History** — All past analyses stored locally with Room database
- **Lie Ranking** — Global leaderboard of most suspicious excuses
- **Honesty Profile** — Analyze multiple messages from one person to build a behavioral profile
- **Lie Detector Game** — Write a statement, let the AI judge, challenge friends
- **Share** — Native Android share to TikTok, Instagram, WhatsApp, Twitter

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Room Database (local storage)
- Google ML Kit (OCR for screenshot analysis)
- Navigation Compose

## Design

- Dark cyber-analysis theme (#0F172A)
- Green (#22C55E) / Yellow (#F59E0B) / Red (#EF4444) truth scoring
- Monospace fonts for data display
- Smooth Compose animations

## Build

Open in Android Studio, sync Gradle, and run:

```bash
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Min SDK: Android 8.0 (API 26)
