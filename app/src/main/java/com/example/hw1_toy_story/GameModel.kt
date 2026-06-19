package com.example.hw1_toy_story

// Centralized grid settings
object GameConfig {
    const val NUM_ROWS = 7
    const val NUM_COLS = 5
    const val PLAYER_ROW = NUM_ROWS - 1
}

data class GameModel(
    var playerCol: Int = GameConfig.NUM_COLS / 2, //starting in the middle column
    var lives: Int = 3,
    var isGameOver: Boolean = false,

    // obstacles:
    var obstacleRow: Int = 0,
    var obstacleCol: Int = (0 until GameConfig.NUM_COLS).random(),
    var hitObstacle: Boolean = false,
    var obstacleDrawable: Int = R.drawable.obstacle1,

    // coins:
    var coinRow: Int = -1,
    var coinCol: Int = 0,
    var scored: Int = 0,
    var coinDrawable: Int = R.drawable.coin


)