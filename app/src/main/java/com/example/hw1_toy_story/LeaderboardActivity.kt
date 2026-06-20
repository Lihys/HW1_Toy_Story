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
    private lateinit var webView: WebView // declared here globally bc we reference inside row clicks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val tableLayout = findViewById<TableLayout>(R.id.table_leaderboard)
        val btnBack = findViewById<Button>(R.id.btn_back_to_menu)
        webView = findViewById<WebView>(R.id.map_webview)

        val manager = LeaderboardManager(this)
        topScores = manager.getTopEntries()

        // Configure WebView Options
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        loadMapPins(webView)

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
            val entry = if (hasData) {
                topScores[i]
            } else {
                null
            }

            // score col
            val txtScore = TextView(this).apply {
                text = entry?.score?.toString() ?: "-"
                setTextColor(Color.parseColor("#333333"))
                gravity = Gravity.CENTER
                textSize = 15f
            }

            // distance col
            val txtDistance = TextView(this).apply {
                text = if (hasData) {
                    "${entry?.distance}m"
                } else {
                    "-"
                }
                setTextColor(Color.parseColor("#333333"))
                gravity = Gravity.CENTER
                textSize = 15f
            }

            // total score col
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

            if (entry != null) {
                row.setBackgroundResource(android.R.color.transparent)

                row.setOnClickListener {
                    webView.evaluateJavascript(
                        "if(window.mapReady) { focusOnMarker(${entry.mapY}, ${entry.mapX}); }"
                    ) { null }
                }
            }

            tableLayout.addView(row)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    // helper
    private fun createMarkersJavaScript(): String {
        return topScores
            .mapIndexed { index, entry ->
                """
            // this puts a marker on the x y coordinates on the map
            L.marker([${entry.mapY}, ${entry.mapX}])
                .addTo(map)
                // this makes it popup when we tap it
                .bindPopup('<b>Rank #${index + 1}</b><br>Total Score: ${entry.absoluteTotal}');
            """.trimIndent()
            }
            .joinToString("\n")
    }

    private fun loadMapPins(webView: WebView) {

        val centerY = if (topScores.isNotEmpty()) {
            topScores[0].mapY
        } else {
            0
        }

        val centerX = if (topScores.isNotEmpty()) {
            topScores[0].mapX
        } else {
            0
        }

        val markersJavaScript = createMarkersJavaScript()

        val mapHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <link rel="stylesheet"
                  href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />

            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

            <style>
                html, body, #map {
                    margin: 0;
                    padding: 0;
                    width: 100%;
                    height: 100%;
                }
            </style>
        </head>

        <body>
            <div id="map"></div>

            <script>
                var map = L.map('map').setView([$centerY, $centerX], 12);

                // downlowds map images from this site and displays it
                L.tileLayer(
                    'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                    {
                        attribution: '&copy; OpenStreetMap contributors'
                    }
                ).addTo(map);

                $markersJavaScript

                function focusOnMarker(lat, lng) {
                    map.setView([lat, lng], 14, {
                        animate: true,
                        duration: 1.5
                    });
                }

                map.whenReady(function() {
                    window.mapReady = true;
                });
            </script>
        </body>
        </html>
    """.trimIndent()

        webView.loadDataWithBaseURL(
            "about:blank",
            mapHtml,
            "text/html",
            "UTF-8",
            null
        )
    }
}