package com.example.hw1_toy_story

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LeaderboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val tableLayout = findViewById<TableLayout>(R.id.table_leaderboard)
        val btnBack = findViewById<Button>(R.id.btn_back_to_menu)

        val manager = LeaderboardManager(this)
        val topScores = manager.getTopEntries()

        // top 10
        for (i in 0 until 10) {
            val row = TableRow(this)
            row.setPadding(0, 6, 0, 6)

            // rank numbers
            val txtRank = TextView(this).apply {
                text = "${i + 1}"
                setTextColor(Color.parseColor("#1976D2"))
                textSize = 15f
                setPadding(4, 4, 4, 4)
            }

            // Check if we have saved record for this loop index
            val hasData = (i < topScores.size)
            val entry= if (hasData){
                topScores[i]
            }
            else
                {null}

            // score col
            val txtScore = TextView(this).apply {
                // default "-"
                text = entry?.score?.toString() ?: "-"

                setTextColor(Color.parseColor("#333333"))
                gravity = Gravity.CENTER
                textSize = 15f
            }

            // distance col
            val txtDistance = TextView(this).apply {
                text = if (hasData){
                    "${entry?.distance}m"
                }
                else {"-"}

                setTextColor(Color.parseColor("#333333"))

                gravity = Gravity.CENTER
                textSize = 15f
            }

            // total score col
            val txtTotal = TextView(this).apply {
                text = entry?.absoluteTotal?.toString() ?: "-"

                setTextColor(Color.parseColor("#E53935"))
                gravity = Gravity.CENTER
                textSize = 15f
            }

            row.addView(txtRank)
            row.addView(txtScore)
            row.addView(txtDistance)
            row.addView(txtTotal)

            tableLayout.addView(row)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}