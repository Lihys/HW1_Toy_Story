package com.example.hw1_toy_story

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var gameLogic: GameLogic
    private lateinit var tiltSensor: TiltSensor
    private val model = GameModel()

    private var mediaPlayer: android.media.MediaPlayer? = null

    private lateinit var leaderboardManager: LeaderboardManager
    private lateinit var textScore: TextView
    private lateinit var textDistance: TextView

    private val handler = Handler(Looper.getMainLooper())//so it runs with delay
    private var deltaTime = 600L//the loop
    private var useTiltMode = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // the user's choices from the menu -
        useTiltMode = intent.getBooleanExtra("USE_TILT", false)
        deltaTime = intent.getLongExtra("GAME_SPEED", 600L)

        //building the grid
        val grid = Array(GameConfig.NUM_ROWS) { row ->
            Array(GameConfig.NUM_COLS) { col ->
                val resId = resources.getIdentifier(
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
        leaderboardManager = LeaderboardManager(this)

        textScore = findViewById(R.id.txt_score)
        textDistance = findViewById(R.id.txt_distance)

        //--Buttons--
        //left moves left and right moves the player right
        val btnLeft = findViewById<Button>(R.id.btn_left)
        val btnRight = findViewById<Button>(R.id.btn_right)

        // if tilt mode we don't show the arrows
        if (useTiltMode)
        {
            btnLeft.visibility = android.view.View.GONE
            btnRight.visibility = android.view.View.GONE

            // Only initialize the hardware sensor if we actually need it
            tiltSensor = TiltSensor(
                context = this,
                movePlayerLeft = {
                    gameLogic.movePlayer(-1)
                    gameView.updateView(model)
                },
                movePlayerRight = {
                    gameLogic.movePlayer(1)
                    gameView.updateView(model)
                }
            )
            tiltSensor.start()
        }
        else // show it
        {
            btnLeft.visibility = android.view.View.VISIBLE
            btnRight.visibility = android.view.View.VISIBLE

            btnLeft.setOnClickListener {
                gameLogic.movePlayer(-1)
                gameView.updateView(model)
            }
            btnRight.setOnClickListener {
                gameLogic.movePlayer(1)
                gameView.updateView(model)
            }
        }

        //start loop
        handler.postDelayed(gameLoop, deltaTime)
    }

    private val gameLoop = object : Runnable {
        //run() gets called every delta time
        override fun run() {
            if (!model.isGameOver){

                gameLogic.gameStep()

                // updating the score and distance
                textScore.text = "Score: ${model.scored}"
                textDistance.text = "Distance: ${model.distance}m"

                //boom
                if (model.hitObstacle) {

                    vibrate()
                    playCrashSound()

                    if (model.lives > 0) {
                        Toast.makeText(
                            this@MainActivity,
                            "${model.lives} lives left",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                if (model.isGameOver) {

                    // save the final score to our leaderboard
                    leaderboardManager.saveScore(
                        score = model.scored,
                        distance = model.distance,
                        location = "Hardcoded for testing" // default
                    )

                    Toast.makeText(
                        this@MainActivity,
                        "GAME OVER!",
                        Toast.LENGTH_LONG
                    ).show()

                }

                gameView.updateView(model)
                handler.postDelayed(this, deltaTime)
            }
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }

    private fun playCrashSound() {
        // if there's already an audio, release it, make it stop
        mediaPlayer?.release()

        mediaPlayer = android.media.MediaPlayer.create(this, R.raw.crash_sound)
        mediaPlayer?.start()
    }

    override fun onResume() {
        super.onResume()
        if (useTiltMode)
        {
            tiltSensor.start()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)

        mediaPlayer?.release()
        mediaPlayer = null

        if (useTiltMode)
        {
            tiltSensor.stop()
        }
    }
}