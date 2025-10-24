# Guitar Hero - Java Edition

A rhythm-based music game built with Java Swing, featuring a clean MVC architecture with Observer and Command patterns.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)
[![GitHub](https://img.shields.io/badge/GitHub-Silas2004/Guitar--Hero-181717?style=flat-square&logo=github)](https://github.com/Silas2004/Guitar-Hero)
## Overview

Guitar Hero is a professional Java implementation of a rhythm game where players hit notes as they fall down the screen. The project demonstrates clean software architecture principles including MVC, Observer Pattern, and Command Pattern.

## Features

### Core Gameplay
- **4-Lane System** - Four lanes with customizable key bindings
- **Dynamic Difficulty** - Speed gradually increases during gameplay
- **Combo System** - Chain hits together for higher scores
- **Lives System** - Configurable starting lives with miss penalties
- **Precision Scoring** - Timing-based point system (100-150 points per note)

### User Interface
- **Main Menu** - Player name entry with validation
- **Gameplay View** - Real-time score, combo, and lives display
- **Pause System** - Pause and resume functionality
- **Settings Panel** - Configure controls and difficulty
- **Game Over Screen** - Final score and leaderboard display
- **Persistent Leaderboard** - Top 10 scores saved to disk

### Configuration
- **Custom Key Bindings** - Remap all four lane keys
- **Speed Tuning** - Adjust base speed, increment, and maximum speed
- **Lives Configuration** - Set starting lives (1-10)
- **Import/Export** - Save and share settings as JSON files

## Architecture

### Design Patterns

**Observer Pattern**
- `GameModel` extends `GameObservable` to notify observers of state changes
- `GameView` implements `GameObserver` for automatic UI updates
- Decouples model from view for better maintainability

**Command Pattern**
- `Command` interface for game actions
- `CommandExecutor` for centralized command execution
- `HitLaneCommand`, `PauseCommand`, `OpenSettingsCommand` implementations

**State Pattern**
- `GameState` interface for game states
- Separate classes for Menu, Play, Pause, GameOver, and Settings states
- Clean state transitions through `MainController`

**Singleton Pattern**
- `SettingsService` for configuration management
- `LeaderboardService` for score persistence

**MVC Architecture**
```
Model (model/*)        - Game logic, scoring, lanes, notes
View (view/*)          - UI components and rendering
Controller (controller/*) - Input handling and game flow
```

### Project Structure

```
src/
├── cmd/
│   └── Main.java                    # Application entry point
├── controller/
│   ├── Command.java                 # Command pattern interfaces
│   ├── CommandExecutor.java         # Command execution
│   ├── GameController.java          # Input controller
│   ├── InputHandler.java            # Key validation
│   └── MainController.java          # Main application controller
├── dispatcher/
│   └── GameDispatcher.java          # Note spawning timer
├── leaderboard/
│   ├── Leaderboard.java             # Leaderboard data structure
│   └── PlayerScore.java             # Score entry model
├── model/
│   ├── GameModel.java               # Core game logic
│   ├── GameSettings.java            # Configuration model
│   ├── Lane.java                    # Lane logic
│   ├── Note.java                    # Note entity
│   └── Score.java                   # Scoring system
├── observer/
│   ├── GameObservable.java          # Observable base class
│   └── GameObserver.java            # Observer interface
├── services/
│   ├── LeaderboardService.java      # Leaderboard persistence
│   └── SettingsService.java         # Settings management
├── state/
│   ├── GameState.java               # State interface
│   ├── MenuState.java               # Menu state
│   ├── PlayState.java               # Gameplay state
│   ├── PauseState.java              # Pause state
│   ├── GameOverState.java           # Game over state
│   └── SettingsState.java           # Settings state
├── util/
│   └── TimerUtils.java              # Time utilities
└── view/
    ├── GameView.java                # Main game view
    ├── MenuView.java                # Menu screen
    ├── GameOverView.java            # Game over screen
    ├── GameSettingsView.java        # Settings screen
    └── LeaderboardView.java         # Leaderboard display
```

## Requirements

- **Java Development Kit (JDK) 21** or higher
- **Swing** (included in JDK)

## Installation

### Clone the Repository

```bash
git clone https://github.com/yourusername/guitar-hero.git
cd guitar-hero
```

### Build from Source

```bash
# Create output directory
mkdir -p out/production

# Generate source file list
find src -name "*.java" > sources.txt

# Compile
javac -d out/production @sources.txt

# Copy resources
cp -r resources out/production/
```

### Run the Game

```bash
java -cp out/production cmd.Main
```

### Create JAR (Optional)

```bash
cd out/production
jar cvfe ../guitar-hero.jar cmd.Main .
cd ../..
java -jar out/guitar-hero.jar
```

## Configuration

### Settings File

Settings are stored in `resources/settings.json`:

```json
{
  "keyLane1": "A",
  "keyLane2": "S",
  "keyLane3": "D",
  "keyLane4": "F",
  "baseSpeed": 2.0,
  "speedIncrement": 0.5,
  "maxSpeed": 10.0,
  "startLives": 3
}
```

### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `keyLane1-4` | char | A,S,D,F | Key bindings for each lane |
| `baseSpeed` | double | 2.0 | Initial note falling speed |
| `speedIncrement` | double | 0.5 | Speed increase rate |
| `maxSpeed` | double | 10.0 | Maximum note speed |
| `startLives` | int | 3 | Starting number of lives |

### Modifying Settings

1. **In-Game**: Press `ESC` → Modify values → Click "Save Settings"
2. **Manual Edit**: Edit `resources/settings.json` directly
3. **Import/Export**: Use the Settings menu buttons

## Game Controls

### Gameplay

| Key | Action |
|-----|--------|
| `A`, `S`, `D`, `F` | Lane keys (customizable) |
| `P` | Pause/Resume |
| `ESC` | Open settings |

### Menu Navigation

| Key | Action |
|-----|--------|
| `ENTER` | Start game / Continue |
| `ESC` | Open settings |

## Scoring System

Points are awarded based on timing precision:

| Timing | Distance | Points |
|--------|----------|--------|
| Perfect | < 5px | 150 |
| Great | < 15px | 130 |
| Good | < 25px | 120 |
| OK | < 40px | 100 |

**Combo System**: Hit consecutive notes without missing to build your combo multiplier.

## Development

### IDE Setup (Eclipse)

1. Import as existing Java project
2. Configure JDK 21 as project JRE
3. Build path should include `src/` directory
4. Run `cmd.Main` as Java Application

### IDE Setup (IntelliJ IDEA)

1. Open project directory
2. Set Project SDK to Java 21
3. Mark `src/` as Sources Root
4. Run `cmd.Main`

### Build Automation

The project includes a GitHub Actions workflow (`.github/workflows/gradle.yml`) that automatically:
- Compiles the project on push/PR
- Creates JAR artifacts
- Uploads builds with 30-day retention

## Testing

### Performance Benchmarks

- **Target FPS**: 60 FPS
- **Input Lag**: < 20ms
- **Frame Time**: 16-17ms average

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style

- Follow Java naming conventions
- Use meaningful variable names
- Add Javadoc comments for public methods
- Keep methods focused and concise
- Maintain existing architecture patterns

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Built with Java Swing
- Inspired by the Guitar Hero game series
- Architecture patterns from Gang of Four Design Patterns

---

**© 2025 Silas**  
Built with ☕ and 🎸