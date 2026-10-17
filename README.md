# ConnectTag System Bridge (Android)

[![](https://jitpack.io/v/connecttag/system-bridge-android.svg)](https://jitpack.io/#connecttag/system-bridge-android)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A lightweight, clean Android utility library that bridges OS system interactions, soft-keyboard observation, clipboard transactions, and external app intents with optional Dagger/Hilt integration.

## Features

- 📋 **`IClipboardManager`**: Safe clipboard manipulation with optional toast notifications and Android 13+ compatibility.
- ⌨️ **`IKeyboardManager`**: Lifecycle-safe software keyboard dismissal detection via global layout observers.
- 🚀 **`SystemInteractionLauncher`**: Clean, mockable facade for launching system intents:
  - Browser URLs (`openWeb`)
  - App settings (`openApplicationDetailsSettings`)
  - Email clients (`sendEmail`)
  - Phone calls & Contacts (`makeCall`, `addContact`)
  - Social media share targets (`shareWithWhatsapp`, `shareWithTelegram`)
- 💉 **Dependency Injection Ready**: Ready-made `SystemBridgeModule` providing singleton bindings for Hilt / Dagger.

## Installation

Add JitPack repository to your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
    }
}
```

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.connecttag:system-bridge-android:1.0.0")
}
```

## Quick Start

### 1. Using Injected Facade

```kotlin
@Inject
lateinit var systemLauncher: SystemInteractionLauncher

// Open URLs or system settings cleanly
systemLauncher.openWeb("https://connecttag.app")
systemLauncher.shareText("Check out ConnectTag!")
```

### 2. Observing Keyboard State

```kotlin
val keyboardManager = KeyboardManagerImpl()
keyboardManager.attachDismissListener(activity) {
    println("Soft keyboard was dismissed")
}
```

## License

```
Copyright 2026 Connect Tag

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```
