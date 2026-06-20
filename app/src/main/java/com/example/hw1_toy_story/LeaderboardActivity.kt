package com.example.hw1_toy_story

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var topScores: List<LeaderboardEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val tableLayout = findViewById<TableLayout>(R.id.table_leaderboard)
        val btnBack = findViewById<Button>(R.id.btn_back_to_menu)
        val webView = findViewById<WebView>(R.id.map_webview)

        val manager = LeaderboardManager(this)
        topScores = manager.getTopEntries()

        // Populate Table Matrix View
        for (i in 0 until 10) {
            val row = TableRow(this).apply { setPadding(0, 6, 0, 6) }

            val txtRank = TextView(this).apply {
                text = "${i + 1}"
                setTextColor(Color.parseColor("#1976D2"))
                textSize = 15f
                setPadding(4, 4, 4, 4)
            }

            val hasData = (i < topScores.size)
            val entry = if (hasData) topScores[i] else null

            val txtScore = TextView(this).apply {
                text = entry?.score?.toString() ?: "-"
                setTextColor(Color.parseColor("#333333"))
                gravity = Gravity.CENTER
                textSize = 15f
            }

            val txtDistance = TextView(this).apply {
                text = if (hasData) "${entry?.distance}m" else "-"
                setTextColor(Color.parseColor("#333333"))
                gravity = Gravity.CENTER
                textSize = 15f
            }

            val txtTotal = TextView(this).apply {
                text = entry?.absoluteTotal?.toString() ?: "-"
                setTextColor(Color.parseColor("#013d7b"))
                gravity = Gravity.CENTER
                textSize = 15f
            }

            row.addView(txtRank)
            row.addView(txtScore)
            row.addView(txtDistance)
            row.addView(txtTotal)
            tableLayout.addView(row)
        }

        // Configure WebView Options
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        loadMapPins(webView)

        btnBack.setOnClickListener { finish() }
    }

    private fun loadMapPins(webView: WebView) {
        // Fallback default coordinates if data entries are empty
        val defaultLat = 32.15
        val defaultLng = 34.88

        val centerLat = if (topScores.isNotEmpty()) topScores[0].mapY else defaultLat
        val centerLng = if (topScores.isNotEmpty()) topScores[0].mapX else defaultLng

        // Build markers arrays dynamically matching entry indices
        val jsMarkers = StringBuilder()
        topScores.forEachIndexed { i, entry ->
            jsMarkers.append("L.marker([${entry.mapY}, ${entry.mapX}]).addTo(map)")
                .append(".bindPopup('<b>Rank #${i + 1}</b><br>Total Score: ${entry.absoluteTotal}');\n")
        }

        // Inject simple Leaflet HTML document frame
        val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>html, body, #map { margin:0; padding:0; width:100%; height:100%; }</style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    var map = L.map('map').setView([$centerLat, $centerLng], 12);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: '&copy; OpenStreetMap contributors'
                    }).addTo(map);
                    $jsMarkers
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }
}