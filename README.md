# 🎸 Guitar Hero - Java Edition

A rhythm-based music game built with Java Swing, featuring customizable controls, dynamic difficulty, and a persistent leaderboard system.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

## 📋 Table of Contents

- [Features](#-features)
- [Getting Started](#-getting-started)
- [How to Play](#-how-to-play)
- [Game Controls](#-game-controls)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)
- [Building](#-building)
- [CI/CD](#-cicd)

## ✨ Features

### Core Gameplay
- 🎵 **4-Lane Rhythm Gameplay** - Hit notes as they fall down the screen
- 🎯 **Combo System** - Build combos for higher scores
- ❤️ **Lives System** - Configurable starting lives (miss notes = lose lives)
- 📈 **Dynamic Difficulty** - Game speed increases over time
- 💯 **Precision Scoring** - Better timing = more points (50-150 per note)

### Customization
- ⌨️ **Custom Key Bindings** - Remap all 4 lanes to your preferred keys
- ⚙️ **Speed Settings** - Adjust base speed, speed increment, and max speed
- 💾 **Import/Export** - Save and share your settings as JSON files
- 🔄 **Persistent Settings** - Automatically loads your preferences

### Game States
- 🏠 **Main Menu** - Enter your player name
- 🎮 **Play State** - Active gameplay
- ⏸️ **Pause State** - Press 'P' to pause
- ⚙️ **Settings State** - Press 'ESC' to access settings
- 💀 **Game Over** - View your score and leaderboard

### Leaderboard
- 🏆 **Top 10 Tracking** - Persistent high score system
- 📊 **Player Rankings** - Sorted by score with timestamps
- 💾 **Serialized Storage** - Scores saved to disk

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher (configured for JavaSE-21)
- **JDK** with Swing support

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/guitar-hero.git
   cd guitar-hero
   ```

2. **Compile the project**
   ```bash
   mkdir -p out/production
   find src -name "*.java" > sources.txt
   javac -d out/production @sources.txt
   ```

3. **Copy resources**
   ```bash
   cp -r resources out/production/
   ```

4. **Run the game**
   ```bash
   java -cp out/production cmd.Main
   ```

### Quick Start with JAR

```bash
cd out/production
jar cvfe ../guitar-hero.jar cmd.Main .
java -jar ../guitar-hero.jar
```

## 🎮 How to Play

1. **Start Screen**: Enter your player name (2-20 characters)
2. **Press ENTER** to start the game
3. **Hit the notes** as they reach the pink hit line at the bottom
4. **Press the correct key** for each lane when the note is in the hit zone
5. **Build combos** by hitting consecutive notes without missing
6. **Don't let notes pass** the hit line - you'll lose a life!
7. **Game Over** when you run out of lives

### Scoring System

| Timing Precision | Points |
|-----------------|--------|
| Perfect (< 5px) | 150    |
| Good (< 20px)   | 120    |
| OK (< 30px)     | 100    |

## 🎹 Game Controls

### In-Game
| Key | Action |
|-----|--------|
| `Q`, `W`, `D`, `F` | Default lane keys (customizable) |
| `P` | Pause/Resume game |
| `ESC` | Open settings (from menu/pause) |

### Menu Navigation
| Key | Action |
|-----|--------|
| `ENTER` | Start game / Confirm |
| `ESC` | Access settings |

### Game Over
| Key | Action |
|-----|--------|
| `ENTER` | Return to main menu |

## 🏗️ Architecture

The project follows a **Model-View-Controller (MVC)** pattern with additional design patterns:

### Design Patterns Used

1. **State Pattern** - Game states (Menu, Play, Pause, GameOver, Settings)
2. **Singleton Pattern** - Services (SettingsService, LeaderboardService)
3. **MVC Pattern** - Separation of concerns
4. **Command Pattern** - CommandExecutor (prepared for future use)
5. **Observer Pattern** - GameDispatcher for note spawning

### Key Components

```
┌─────────────┐
│ MainController│
└──────┬──────┘
       │
       ├─────► GameModel (Logic)
       │
       ├─────► GameController (Input)
       │
       ├─────► GameView (Rendering)
       │
       └─────► GameState (State Machine)
```

## 📁 Project Structure

```
GuitarHero/
├── src/
│   ├── cmd/
│   │   └── Main.java                    # Entry point
│   ├── controller/
│   │   ├── CommandExecutor.java         # Command pattern base
│   │   ├── GameController.java          # Input handling
│   │   ├── InputHandler.java            # Key validation
│   │   └── MainController.java          # Main game controller
│   ├── dispatcher/
│   │   └── GameDispatcher.java          # Note spawning timer
│   ├── leaderboard/
│   │   ├── Leaderboard.java             # Leaderboard data structure
│   │   └── PlayerScore.java             # Score entry
│   ├── model/
│   │   ├── GameModel.java               # Core game logic
│   │   ├── GameSettings.java            # Configuration model
│   │   ├── Lane.java                    # Lane logic
│   │   ├── Note.java                    # Note entity
│   │   └── Score.java                   # Scoring system
│   ├── services/
│   │   ├── LeaderboardService.java      # Leaderboard persistence
│   │   └── SettingsService.java         # Settings persistence
│   ├── state/
│   │   ├── GameState.java               # State interface
│   │   ├── MenuState.java               # Menu state
│   │   ├── PlayState.java               # Gameplay state
│   │   ├── PauseState.java              # Pause state
│   │   ├── GameOverState.java           # Game over state
│   │   └── SettingsState.java           # Settings state
│   ├── util/
│   │   └── TimerUtils.java              # Time utilities
│   └── view/
│       ├── GameView.java                # Main game view
│       ├── MenuView.java                # Menu screen
│       ├── GameOverView.java            # Game over screen
│       ├── GameSettingsView.java        # Settings screen
│       └── LeaderboardView.java         # Leaderboard display
├── resources/
│   ├── settings.json                    # Game settings
│   └── leaderboard.dat                  # Serialized scores
└── .github/workflows/
    └── gradle.yml                       # CI/CD pipeline
```

## ⚙️ Configuration

### Settings File (`resources/settings.json`)

```json
{
  "keyLane1": "Q",
  "keyLane2": "W",
  "keyLane3": "D",
  "keyLane4": "F",
  "baseSpeed": 5.0,
  "speedIncrement": 2.0,
  "maxSpeed": 10.0,
  "startLives": 1
}
```

### Configuration Options

| Setting | Type | Description | Default |
|---------|------|-------------|---------|
| `keyLane1-4` | char | Lane key bindings | Q, W, D, F |
| `baseSpeed` | double | Starting note speed | 5.0 |
| `speedIncrement` | double | Speed increase rate | 2.0 |
| `maxSpeed` | double | Maximum note speed | 10.0 |
| `startLives` | int | Initial lives | 1 |

### Customizing Settings

1. **In-Game**: Press `ESC` → Modify settings → Save
2. **Manual Edit**: Edit `resources/settings.json` directly
3. **Import/Export**: Use the settings menu to share configurations

## 🔨 Building

### Eclipse IDE
1. Import as existing Java project
2. Set JRE to JavaSE-21
3. Run `cmd.Main`

### Command Line

```bash
# Compile
mkdir -p out/production
find src -name "*.java" > sources.txt
javac -d out/production @sources.txt

# Create JAR
cd out/production
jar cvfe ../jar/guitar-hero.jar cmd.Main .

# Run
java -jar out/jar/guitar-hero.jar
```

### Using Make (optional)

Create a `Makefile`:

```makefile
.PHONY: compile run clean

compile:
	mkdir -p out/production
	find src -name "*.java" > sources.txt
	javac -d out/production @sources.txt
	cp -r resources out/production/

run: compile
	java -cp out/production cmd.Main

clean:
	rm -rf out sources.txt
```

Then simply run: `make run`

## 🔄 CI/CD

GitHub Actions workflow automatically:
- ✅ Compiles the project on push/PR
- 📦 Creates a JAR artifact
- ☁️ Uploads the build for 30 days
- 🧪 Runs tests (if present)

### Workflow Configuration

The workflow runs on:
- Push to `main` or `master`
- Pull requests to `main` or `master`

Build artifacts are available under the "Actions" tab.

## 🎯 Future Improvements

### Potential Enhancements
- 🎵 **Audio Support** - Add background music and sound effects
- 📊 **Statistics** - Track accuracy, perfect hits, etc.
- 🎨 **Themes** - Customizable color schemes
- 📝 **Song Editor** - Create custom note patterns
- 🌐 **Online Leaderboard** - Global rankings
- 🎮 **Multiple Difficulties** - Easy, Medium, Hard modes
- 📱 **Mobile Version** - Android/iOS port

### Code Improvements
- 🧪 **Unit Tests** - Add JUnit test coverage
- 🔌 **Dependency Injection** - Replace singletons
- 📡 **Observer Pattern** - Better view updates
- 🎯 **Interface Segregation** - Common view interfaces
- 📦 **Plugin System** - Extensible architecture

## 📝 License

This project is licensed under the MIT License.

## 👤 Author

**Silas**
- © 2025

## 🙏 Acknowledgments

- Built with Java Swing
- Inspired by the classic Guitar Hero series
- Thanks to the Java gaming community

---

**Enjoy the game! Rock on! 🎸🎵**
