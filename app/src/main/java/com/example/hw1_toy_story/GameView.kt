package com.example.hw1_toy_story
import android.widget.ImageView

class GameView(
    private val grid: Array<Array<ImageView>>,
    private val hearts: Array<ImageView>,
) {

    //how we update the UI
    fun updateView(model: GameModel) {
        clearGrid()
        drawObstacle(model)
        drawPlayer(model)
        updateHearts(model)
    }

    private fun clearGrid() {
        for (row in 0..6) {
            for (col in 0..2){
                grid[row][col].visibility = ImageView.INVISIBLE
            }
        }
    }

    private fun drawObstacle(model: GameModel) {
        if (model.obstacleRow in 0..6)
            grid[model.obstacleRow][model.obstacleCol].apply {
                setImageResource(model.obstacleDrawable)
                visibility = ImageView.VISIBLE
            }
    }

    private fun drawPlayer(model: GameModel) {
        /*grid[6][model.playerCol].apply {
            setImageResource(R.drawable.woody_player)
            visibility = ImageView.VISIBLE*/
        grid[6][model.playerCol].apply {
        //scaleType = ImageView.ScaleType.FIT_CENTER
        scaleX = 1.1f // making it bigger slightly
        scaleY = 1.1f
        setImageResource(R.drawable.woody_player)
       visibility = ImageView.VISIBLE
        }
    }

    private fun updateHearts(model: GameModel) {
        when (model.lives) {
            2 -> hearts[2].visibility = ImageView.INVISIBLE
            1 -> hearts[1].visibility = ImageView.INVISIBLE
            0 -> hearts[0].visibility = ImageView.INVISIBLE
        }
    }

}