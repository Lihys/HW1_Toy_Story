package com.example.hw1_toy_story
import android.widget.ImageView

class GameView(
    private val grid: Array<Array<ImageView>>,
    private val hearts: Array<ImageView>,
    ){

    //how we update the UI
    fun updateView(model: GameModel) {
        clearGrid()
        drawPlayer(model)
        drawCoin(model)
        drawObstacle(model)
        updateHearts(model)
    }

    private fun clearGrid() {
        for (row in grid.indices) {
            for (col in grid[row].indices){
                grid[row][col].visibility = ImageView.INVISIBLE
            }
        }
    }

    private fun drawObstacle(model: GameModel) {
        if ( (model.obstacleRow in grid.indices) && (model.obstacleCol in grid[0].indices) )
            grid[model.obstacleRow][model.obstacleCol].apply {
                scaleX = 1.1f
                scaleY = 1.1f
                setImageResource(model.obstacleDrawable)
                visibility = ImageView.VISIBLE
            }
    }

    private fun drawPlayer(model: GameModel) {
        grid[GameConfig.PLAYER_ROW][model.playerCol].apply {
            scaleX = 1.1f
            scaleY = 1.1f
            setImageResource(R.drawable.woody_player)
            visibility = ImageView.VISIBLE
        }
    }

    private fun drawCoin(model: GameModel) {
        if (model.coinRow in grid.indices && model.coinCol in grid[0].indices)
        {
            grid[model.coinRow][model.coinCol].apply {
                setImageResource(model.coinDrawable)

                //editing the size
                scaleX = 0.55f
                scaleY = 0.55f

                visibility = ImageView.VISIBLE
            }
        }
    }

    private fun updateHearts(model: GameModel) {
        when (model.lives) {
            3 -> hearts.forEach { it.visibility = ImageView.VISIBLE }
            2 -> hearts[2].visibility = ImageView.INVISIBLE
            1 -> hearts[1].visibility = ImageView.INVISIBLE
            0 -> hearts[0].visibility = ImageView.INVISIBLE
        }
    }

}