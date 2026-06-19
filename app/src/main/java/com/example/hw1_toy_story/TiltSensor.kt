package com.example.hw1_toy_story

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class TiltSensor(
    context: Context,
    private val movePlayerLeft: () -> Unit,
    private val movePlayerRight: () -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val tiltSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val TILT_THRESHOLD = 3.0f //chnages how sensitive the tilt is
    private var lastMoveTime = 0L
    private val COOLDOWN_MS = 400L //cooldown between moves

    fun start() {
        // ? bc it works only if we have the sensing option
        tiltSensor?.let {
            sensorData ->
            sensorManager.registerListener(this, // we send the data to this class
                sensorData, // which sensor
                SensorManager.SENSOR_DELAY_GAME //speed
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    // the tilt logic
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorData ->

            val horizontalTilt = sensorData.values[0]
            val currentTime = System.currentTimeMillis()

            // if cooldown time hasn't passed yet
            if (currentTime - lastMoveTime < COOLDOWN_MS)
            {
                return;
            }

            // tilt left
            if (horizontalTilt > TILT_THRESHOLD) {
                movePlayerLeft()
                lastMoveTime = currentTime

            }
            // tilt right
            else if (horizontalTilt < TILT_THRESHOLD*-1) {
                movePlayerRight()
                lastMoveTime = currentTime
            }
        }
    }

    // no need to implement so empty
   override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
   }
}