# ⚽ FIFA World Cup 2026 — Bracket & Standings Viewer

An Android application that displays **live FIFA World Cup 2026 knockout brackets** and **group standings** using real-time data from the [worldcup26.ir](https://worldcup26.ir) API.

Built with **[TournamentBracketLib](https://github.com/Emil333/TournamentBracketLib)** — a custom Android library for rendering tournament brackets with smooth animations and transitions.

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🏆 **Knockout Brackets** | Full tournament tree from Round of 32 to the Final, rendered with connectors and animations |
| 📊 **Group Standings** | All 12 groups with team stats (MP, W, D, L, GF, GA, GD, PTS), sorted by points |
| 🏳️ **Country Flags** | Team flags loaded from CDN with caching and placeholders |
| 🔄 **Pull-to-Refresh** | Swipe down on standings to reload live data |
| 📡 **Live API Data** | No hardcoded values — all data fetched dynamically from the World Cup API |
| 🏷️ **Match Names** | Each bracket cell shows the round and match number |
| 📱 **Dark Theme** | Clean dark UI matching the bracket library's styling |

---

## 📸 Screenshots

| Home | Brackets | Group Standings |
|------|----------|-----------------|
| ![Home](https://via.placeholder.com/250x500/1c222e/fff?text=Home) | ![Brackets](https://via.placeholder.com/250x500/1c222e/fff?text=Brackets) | ![Standings](https://via.placeholder.com/250x500/1c222e/fff?text=Standings) |

> *Replace these placeholders with actual screenshots from your device.*

---

## 🏗️ Architecture

```
app/
├── activity/
│   ├── HomeActivity.java             — Landing screen with navigation
│   ├── MainActivity.java             — Knockout brackets display
│   └── GroupStandingsActivity.java   — Group tables with pull-to-refresh
├── adapter/
│   └── GroupStandingsAdapter.java    — RecyclerView adapter with DiffUtil
├── model/
│   ├── WorldCupGame.java             — Match model (knockout/group games)
│   ├── Group.java / GroupTeamEntry.java — Group standings models
│   ├── Team.java                     — Team info (name, flag URL)
│   └── GroupTeamDisplayItem.java     — Merged display model
└── network/
    ├── RetrofitClient.java           — Singleton Retrofit setup
    └── WorldCupApi.java              — API endpoint definitions
```

---

## 🌟 TournamentBracketLib — The Highlight

This app is powered by **[TournamentBracketLib](https://github.com/Emil333/TournamentBracketLib)** — an open-source Android library for generating tournament brackets.

### Library Features
- 🎯 Smooth bracket transitions and animations
- 🎨 Customizable colors (background, bracket lines, text)
- 🖼️ Competitor images (team flags/logos)
- 🏷️ Match names on each bracket cell
- 📐 Auto-sizing based on screen dimensions

### Usage in this app

```java
// Set up competitor with flag image
CompetitorData home = new CompetitorData("Brazil", "2");
home.setImageUrl("https://flagcdn.com/w80/br.png");

CompetitorData away = new CompetitorData("Argentina", "1");
away.setImageUrl("https://flagcdn.com/w80/ar.png");

// Create match with name
MatchData match = new MatchData(home, away);
match.setMatchName("QF - Match 97");

// Group matches into columns (rounds)
ColomnData quarterFinals = new ColomnData(Arrays.asList(match1, match2, match3, match4));
ColomnData semiFinals = new ColomnData(Arrays.asList(sf1, sf2));
ColomnData finals = new ColomnData(Arrays.asList(finalMatch));

// Display
bracketsView.setBracketsData(Arrays.asList(quarterFinals, semiFinals, finals));
```

### XML Configuration

```xml
<com.ventura.bracketslib.BracketsView
    android:id="@+id/bracket_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:bracketBackgroundColor="#1c222e"
    app:bracketColor="#262e40"
    app:bracketTextColor="#fff" />
```

---

## 📡 API Endpoints

All data is fetched from [worldcup26.ir](https://worldcup26.ir) (no authentication required):

| Endpoint | Description |
|----------|-------------|
| `GET /get/games` | All 104 matches (group + knockout) |
| `GET /get/groups` | 12 group standings with team stats |
| `GET /get/teams` | 48 teams with names, flags, and FIFA codes |
| `GET /get/stadiums` | 16 stadium details |

---

## 📋 Requirements

| Requirement | Version |
|-------------|---------|
| Android Min SDK | **24** (Android 7.0 Nougat) |
| Android Target SDK | **34** (Android 14) |
| Java | **8+** |
| Gradle | **8.10** |
| Android Gradle Plugin | **8.1.4** |
| JDK (for building) | **17** |

---

## 🔧 Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| [TournamentBracketLib](https://github.com/Emil333/TournamentBracketLib) | 1.1.4 | Bracket rendering with animations |
| [Retrofit](https://github.com/square/retrofit) | 2.9.0 | HTTP API client |
| [Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) | 2.9.0 | JSON deserialization |
| [OkHttp Logging](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor) | 4.11.0 | Network request logging |
| [Glide](https://github.com/bumptech/glide) | 4.15.1 | Image loading and caching |
| [SwipeRefreshLayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout) | 1.1.0 | Pull-to-refresh |
| Material Components | 1.9.0 | UI components |

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/emil-ep/TournamentBrackets.git
cd TournamentBrackets
```

### 2. Open in Android Studio

Open the project in Android Studio (Hedgehog or newer recommended).

### 3. Sync & Build

The project uses JitPack for the bracket library. Ensure your `build.gradle` includes:

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### 4. Run

Connect a device or start an emulator (API 24+) and run the app.

---

## 📁 Project Structure

```
TournamentBrackets/
├── app/                          — Main application module
├── build.gradle                  — Top-level build config (AGP 8.1.4)
├── gradle.properties             — JVM and AndroidX settings
├── gradle/wrapper/               — Gradle 8.10 wrapper
└── README.md
```

---

## ⭐ Find this project useful?

Support it by giving a **⭐️ star** on the upper right of this page!

Your stars help others discover this project and motivate continued development. 🙏

[![GitHub stars](https://img.shields.io/github/stars/emil-ep/TournamentBrackets?style=social)](https://github.com/emil-ep/TournamentBrackets/stargazers)

---

## 📄 License

This project is open-source. See the [LICENSE](LICENSE.md) file for details.
