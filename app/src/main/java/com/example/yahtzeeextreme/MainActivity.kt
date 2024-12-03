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
        R.drawable.dobbelsteen6
    )
}
