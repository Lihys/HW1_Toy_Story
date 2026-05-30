package com.example.hw1_toy_story

data class GameModel(
    var playerCol: Int = 1,
    var obstacleRow: Int = 0,
    var obstacleCol: Int = (0..2).random(),
    var lives: Int = 3,
    var isGameOver: Boolean = false,
    var hitObstacle: Boolean = false,
    var obstacleDrawable: Int = R.drawable.obstacle1
)
