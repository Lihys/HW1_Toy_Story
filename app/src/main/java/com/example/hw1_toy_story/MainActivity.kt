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

    private lateinit var textScore: TextView
    private lateinit var textDistance: TextView

    private val handler = Handler(Looper.getMainLooper())//so it runs with delay
    private var deltaTime = 600L//the loop
    private var useTiltMode = false

    // location
    private lateinit var leaderboardManager: LeaderboardManager
    private lateinit var fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient
    private var liveMapX = 0.0
    private var liveMapY = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init managers
        leaderboardManager = LeaderboardManager(this)
        fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this)

        // request GPS permissions
        val locationPermissionRequest = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false)) {

                fetchLiveLocation()
            }
        }

        locationPermissionRequest.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ))

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
                    if (model.lives > 0) {
                        Toast.makeText(
                            this@MainActivity,
                            "${model.lives} lives left",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                if (model.isGameOver) {
                    // when the game is over we save the score locally
                    leaderboardManager.saveScore(
                        score = model.scored,
                        distance = model.distance,
                        mapX = liveMapX,
                        mapY = liveMapY
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

    private fun fetchLiveLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
                if (location != null) {
                    liveMapX = location.longitude
                    liveMapY = location.latitude
                }
            }
        } catch (e: SecurityException) {
            // because i had a problem here
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
        )
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
        if (useTiltMode)
        {
            tiltSensor.stop()
        }
    }
}