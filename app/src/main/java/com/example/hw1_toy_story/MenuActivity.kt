package com.example.hw1_toy_story

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButtonToggleGroup

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val gameModeSelector = findViewById<RadioGroup>(R.id.mode_radio_group)
        val difficultyGroup =
            findViewById<MaterialButtonToggleGroup>(R.id.difficulty_group)
        val btnStart = findViewById<Button>(R.id.btn_start_game)
        val btnLeaderboard = findViewById<Button>(R.id.btn_leaderboard)

        val btnEasy = findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_easy)
        val btnHard = findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_hard)

        val radioTilt = findViewById<android.widget.RadioButton>(R.id.radio_tilt)
        val radioArrows = findViewById<android.widget.RadioButton>(R.id.radio_arrows)

        //  clear the other check when clicked
        radioTilt.setOnClickListener {
            gameModeSelector.check(R.id.radio_tilt)
        }

        radioArrows.setOnClickListener {
            gameModeSelector.check(R.id.radio_arrows)
        }

        // to visually show the toggle is pressed / unpressed:
        fun updateToggleVisuals(checkedId: Int) {
            if (checkedId == R.id.btn_easy) {
                // Easy is selected:
                btnEasy.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1E88E5"))
                btnEasy.setTextColor(android.graphics.Color.WHITE)

                // Hard is unselected:
                btnHard.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFFDF5"))
                btnHard.setTextColor(android.graphics.Color.parseColor("#E53935"))
            } else if (checkedId == R.id.btn_hard) {
                // Hard is selected:
                btnHard.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E53935"))
                btnHard.setTextColor(android.graphics.Color.WHITE)

                // Easy is unselected:
                btnEasy.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFFDF5"))
                btnEasy.setTextColor(android.graphics.Color.parseColor("#1E88E5"))
            }
        }

// Color them correctly right when the screen opens up
        updateToggleVisuals(difficultyGroup.checkedButtonId)

// Listen for clicks to instantly update the pressed/unpressed states dynamically
        difficultyGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                updateToggleVisuals(checkedId)
            }
        }

        btnStart.setOnClickListener {
            //set the settings based on the user's choice
            val selectedModeId = gameModeSelector.checkedRadioButtonId

            var useTilt = false
            var gameSpeed = 600L // default/slow
            val isHardMode = difficultyGroup.checkedButtonId == R.id.btn_hard


            when (selectedModeId) {
                R.id.radio_tilt -> {
                    useTilt = true
                    gameSpeed = 600L
                }
                R.id.radio_arrows -> {
                    useTilt = false
                    // If the switch is checked, it's hard mode (fast). Otherwise, it's easy mode (slow).
                    if (isHardMode) {
                        gameSpeed = 300L // Hard mode speed
                    } else {
                        gameSpeed = 600L // Easy mode speed
                    }
                }
            }

            // launching Main Activity and passing our settings
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("USE_TILT", useTilt)
                putExtra("GAME_SPEED", gameSpeed)
            }
            startActivity(intent)
        }

        btnLeaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }
}