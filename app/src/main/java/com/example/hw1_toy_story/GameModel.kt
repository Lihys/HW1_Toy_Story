package com.example.hw1_toy_story

// Centralized grid settings
object GameConfig {
    const val NUM_ROWS = 7
    const val NUM_COLS = 5
    const val PLAYER_ROW = NUM_ROWS - 1
}

data class GameModel(
    //the player starts in the middle column
    var playerCol: Int = GameConfig.NUM_COLS / 2,
    var obstacleRow: Int = 0,
    var obstacleCol: Int = (0 until GameConfig.NUM_COLS).random(),
    var lives: Int = 3,
    var isGameOver: Boolean = false,
    var hitObstacle: Boolean = false,
    var obstacleDrawable: Int = R.drawable.obstacle1
)