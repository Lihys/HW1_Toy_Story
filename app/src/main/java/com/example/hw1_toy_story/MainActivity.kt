package com.example.hw1_toy_story

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var gameLogic: GameLogic
    private val model = GameModel()

    private val handler = Handler(Looper.getMainLooper())//so it runs with delay
    private val DELTA_TIME = 600L//the loop

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //building the grid 3 cols and 7 rows
        val grid = Array(7) { row ->
            Array(3) { col ->
                val resId = resources.getIdentifier(
                    // cells 1_2 etc
                    "cell_${row}_${col}", "id", packageName
                )
                findViewById<ImageView>(resId)
            }
        }

        // builds the hearts array
        val hearts = arrayOf(
            findViewById<ImageView>(R.id.heart1),
            findViewById<ImageView>(R.id.heart2),
            findViewById<ImageView>(R.id.heart3)
        )

        //Init
        gameLogic = GameLogic(model)
        gameView  = GameView(grid, hearts)

        //--Buttons--
        //left moves left and right moves the player right
        findViewById<Button>(R.id.btn_left).setOnClickListener {
            gameLogic.movePlayer(-1)
            gameView.updateView(model)
        }
        findViewById<Button>(R.id.btn_right).setOnClickListener {
            gameLogic.movePlayer(1)
            gameView.updateView(model)
        }

        //start loop
        handler.postDelayed(gameLoop, DELTA_TIME)
    }

    private val gameLoop = object : Runnable {
        //run() gets called every delta time
        override fun run() {
            if (!model.isGameOver){

                gameLogic.gameStep()
                //boom
                if (model.hitObstacle) {
                    vibrate()
                    if (model.lives > 0) {
                        Toast.makeText(
                            this@MainActivity,
                            "${model.lives} lives left",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                if (model.isGameOver) {

                        Toast.makeText(
                            this@MainActivity,
                            "GAME OVER!",
                            Toast.LENGTH_LONG
                        ).show()

                }

                gameView.updateView(model)
                handler.postDelayed(this, DELTA_TIME)
            }
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }
}