
# Android Application - Toy Story themed game

A Toy Story themed Android game developed in Kotlin. In the game, you can play as Woody and try to avoid falling obstacles, and collect coins. You get points based on how long you survived, and how many coins you collected.

## Game modes:
The game has two control modes:
1. Arrow mode, with two difficulty levels available
2. Tilt mode, you move the player by tilting your phone
The mode is selected in the menu.

## Features:
- Vibration and sound effects when hitting obstacles
- Lives remaining (starting with 3 hearts)
- Arrow and tilt controls
- Coin collection
- Local leaderboard
- GPS location saved and showcased for the top 10 leading scores

## Leaderboard:
- In the leaderboard you can see the top 10 leading scores. this data saves locally.
- In addition there is a map that shows the saved location of each score. When pressing a leaderboard entry, the map zoom in to show that entry's location.

## Project Structure:
- **MainActivity** manages the game loop, player movement, scores, and lives.
- **MenuActivity** manages the game mode, selections and navigation
- **LeaderboardActivity** displays the leaderboard and map
-**LeaderboardManager** saves and loads local scores
- **GameModel** stores the game state
- **GameLogic** manages movement, crashes, score, and game rules
- **GameView** updates the visuals of the game
- **TiltSensor** handles tilt-based movements

## Used in the project:
- WebView and Leaflet, OpenStreetMap 
- Local data saving
- GPS location services, internet access, Gson
