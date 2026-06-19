package com.example.hw1_toy_story

import kotlin.random.Random

class GameLogic(private val model: GameModel) {

    fun movePlayer(direction: Int) {
        model.playerCol = (model.playerCol + direction).coerceIn(0, GameConfig.NUM_COLS - 1)
        checkCoinCollect()
    }

    // like when we advance the obstacles. each step in the "animation" we create
    fun gameStep() {

        // obstacles:
        model.hitObstacle = false
        model.obstacleRow ++

        if (model.obstacleRow >= GameConfig.NUM_ROWS) {
            generateObstacle()
        }

        // coins:

        if (model.coinRow != -1) {
            model.coinRow++
        }

        // if we don't have a coin on the screen:
        if (model.coinRow >= GameConfig.NUM_ROWS || model.coinRow == -1) {
            if (Random.nextBoolean()){
                generateCoin()
            }
            else
            {
                model.coinRow = -1
            }
        }

        checkCrash()
        checkCoinCollect()
    }

    // generating a random obstacle
    private fun generateObstacle() {
        model.obstacleRow = 0
        model.obstacleCol = (0 until GameConfig.NUM_COLS).random()
        model.obstacleDrawable = listOf(
            R.drawable.obstacle1,
            R.drawable.obstacle2,
            R.drawable.obstacle3,
            R.drawable.obstacle4,
            R.drawable.obstacle5,
            R.drawable.obstacle6,
            R.drawable.obstacle7,
            R.drawable.obstacle8,
            R.drawable.obstacle9,
        ).random()
    }

    private fun generateCoin() {
        model.coinRow = 0
        var randomCol = (0 until GameConfig.NUM_COLS).random()

        // we don't want the obstacle and the coin to be in the same arr index
        if ( (model.obstacleRow == 0) && (randomCol == model.obstacleCol) ) {
            randomCol = (randomCol + 1) % GameConfig.NUM_COLS // % so it will stay in our bounds
        }

        model.coinCol = randomCol
    }

    // the player hits the obstacle
    private fun checkCrash() {
        //if both are in the same place
        if ( (model.obstacleRow == GameConfig.PLAYER_ROW) && (model.obstacleCol == model.playerCol) ) {
            onCrash()
        }
    }

    private fun onCrash() {
        model.hitObstacle = true
        model.lives--
        generateObstacle()
        if (model.lives <= 0) {
            model.isGameOver = true
        }
    }

    private fun checkCoinCollect() {
        if ((model.coinRow == GameConfig.PLAYER_ROW) && (model.coinCol == model.playerCol)) {
            model.scored += 10 //Reward 10 points
            model.coinRow = -1 //bye coin
        }
    }

}