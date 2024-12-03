package com.example.yahtzeeextreme

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private lateinit var rollInfo: TextView
    private lateinit var diceTopLeft: ImageView
    private lateinit var diceTopRight: ImageView
    private lateinit var diceMiddle: ImageView
    private lateinit var diceBottomLeft: ImageView
    private lateinit var diceBottomRight: ImageView
    private lateinit var shakeToRollButton: Button

    private val diceImages = arrayOf(
        R.drawable.dobbelsteen1,
        R.drawable.dobbelsteen2,
        R.drawable.dobbelsteen3,
        R.drawable.dobbelsteen4,
        R.drawable.dobbelsteen5,
        R.drawable.dobbelsteen6,
        R.drawable.dobbelsteenlocked1,
        R.drawable.dobbelsteenlocked2,
        R.drawable.dobbelsteenlocked3,
        R.drawable.dobbelsteenlocked4,
        R.drawable.dobbelsteenlocked5,
        R.drawable.dobbelsteenlocked6,
    )

    private var diceValues = IntArray(5) { 1 }
    private var attemptsleft = 3
    private var lockedDice = BooleanArray(5) { false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rollInfo = findViewById(R.id.rollInfo)
        diceTopLeft = findViewById(R.id.diceTopLeft)
        diceTopRight = findViewById(R.id.diceTopRight)
        diceMiddle = findViewById(R.id.diceMiddle)
        diceBottomLeft = findViewById(R.id.diceBottomLeft)
        diceBottomRight = findViewById(R.id.diceBottomRight)
        shakeToRollButton = findViewById(R.id.shakeToRollButton)

        updateDiceImages()
        updateRollInfo()

        shakeToRollButton.setOnClickListener { //momenteel click listener wnt moet nog uitzoeken hoe ik da een shake ding maak
            if (attemptsleft > 0) {
                rollDice()
                attemptsleft--
                updateRollInfo()
            }
        }

        diceTopLeft.setOnClickListener { toggleDiceLock(0, diceTopLeft) }
        diceTopRight.setOnClickListener { toggleDiceLock(1, diceTopRight) }
        diceMiddle.setOnClickListener { toggleDiceLock(2, diceMiddle) }
        diceBottomLeft.setOnClickListener { toggleDiceLock(3, diceBottomLeft) }
        diceBottomRight.setOnClickListener { toggleDiceLock(4, diceBottomRight) }
    }


    private fun rollDice() {
        for (i in diceValues.indices) {
            if (!lockedDice[i]) {
                diceValues[i] = Random.nextInt(1, 7)
            }
        }
        updateDiceImages()
    }

    private fun updateDiceImages() {
        diceTopLeft.setImageResource(diceImages[diceValues[0] - 1])
        diceTopRight.setImageResource(diceImages[diceValues[1] - 1])
        diceMiddle.setImageResource(diceImages[diceValues[2] - 1])
        diceBottomLeft.setImageResource(diceImages[diceValues[3] - 1])
        diceBottomRight.setImageResource(diceImages[diceValues[4] - 1])
    }

    private fun updateRollInfo() {
        // Update the roll info text
        rollInfo.text = "You have $attemptsleft rolling attempts left!"
        shakeToRollButton.isEnabled = attemptsleft > 0
    }

    private fun toggleDiceLock(index: Int, diceImageView: ImageView) {
        lockedDice[index] = !lockedDice[index]
        if (lockedDice[index]) {
            diceImageView.setImageResource(diceImages[diceValues[index] - 1 + 6])
        } else {
            diceImageView.setImageResource(diceImages[diceValues[index] - 1])
        }
    }
}

