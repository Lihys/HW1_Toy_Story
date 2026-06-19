package com.example.hw1_toy_story

class GameLogic(private val model: GameModel) {

    fun movePlayer(direction: Int) {
        model.playerCol = (model.playerCol + direction).coerceIn(0, GameConfig.NUM_COLS - 1)
    }

    // like when we advance the obstacles. each step in the "animation" we create
    fun gameStep() {
        model.hitObstacle = false
        model.obstacleRow ++

        if (model.obstacleRow >= GameConfig.NUM_ROWS) {
            generateObstacle()
        }

        checkCrash()
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

}