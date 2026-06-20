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

        // we only show the hard/easy button when the user chooses the arrow mode
        gameModeSelector.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_arrows) {
                difficultyGroup.visibility = View.VISIBLE
            } else {
                difficultyGroup.visibility = View.GONE
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
            // to change :
            Toast.makeText(this, "Leaderboard screen coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}