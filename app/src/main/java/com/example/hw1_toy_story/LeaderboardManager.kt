package com.example.hw1_toy_story

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class LeaderboardEntry(
    val score: Int,
    val distance: Int,
    val location: String = "Unknown"
) {
    // the total value deciding the highest rank (score+distance)
    val absoluteTotal: Int get() = score + distance
}

class LeaderboardManager(context: Context) {
    private val prefs = context.getSharedPreferences("toy_story_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Fetches the top 10 and sorts by highest absolute total
    fun getTopEntries(): List<LeaderboardEntry> {
        val json = prefs.getString("leaderboard_data", null) ?: return emptyList()
        val type = object : TypeToken<List<LeaderboardEntry>>() {}.type
        val rawList: List<LeaderboardEntry> = gson.fromJson(json, type)
        return rawList.sortedByDescending { it.absoluteTotal }
    }

    // saves a new total score
    fun saveScore(score: Int, distance: Int, location: String = "Unknown") {
        val currentEntries = getTopEntries().toMutableList()
        currentEntries.add(LeaderboardEntry(score, distance, location))

        // only top 10 rows
        val topTen = currentEntries.sortedByDescending { it.absoluteTotal }.take(10)

        prefs.edit().putString("leaderboard_data", gson.toJson(topTen)).apply()
    }
}