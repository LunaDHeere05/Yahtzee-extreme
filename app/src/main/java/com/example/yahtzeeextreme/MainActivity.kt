package com.example.yahtzeeextreme

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.yahtzeeextreme.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : ComponentActivity() {


    private val gameViewModel: YahtzeeGameViewModel by viewModels()

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
    private val scoreCategories = mapOf( //dit nodig om mijn scorebord up te daten
        "ones" to R.id.Ones,
        "twos" to R.id.Twos,
        "threes" to R.id.Threes,
        "fours" to R.id.Fours,
        "fives" to R.id.Fives,
        "sixes" to R.id.Sixes
    )

    private var diceValues = IntArray(5) { 1 }
    private var attemptsleft = 3
    private var lockedDice = BooleanArray(5) { false }

    private val currentDiceValues = IntArray(5)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Observe the scores LiveData to update the scoreboard
        gameViewModel.scores.observe(this) { scores ->
            updateScoreboard(scores) // Pass the scores to the updateScoreboard function
        }

        updateDiceImages()
        updateRollInfo()

        binding.Ones.setOnClickListener {
            println("button not clicked yet")
            gameViewModel.doScore("ones", currentDiceValues) // Pass the updated currentDiceValues
            println("button clicked")
        }
        binding.Twos.setOnClickListener {
            println("button not clicked yet")
            gameViewModel.doScore("twos", currentDiceValues) // Pass the updated currentDiceValues
            println("button clicked")
        }


        binding.shakeToRollButton.setOnClickListener { //momenteel click listener wnt moet nog uitzoeken hoe ik da een shake ding maak
            if (attemptsleft > 0) {
                rollDice()
                attemptsleft--
                updateRollInfo()
            }
        }

        binding.diceTopLeft.setOnClickListener { toggleDiceLock(0, binding.diceTopLeft) }
        binding.diceTopRight.setOnClickListener { toggleDiceLock(1, binding.diceTopRight) }
        binding.diceMiddle.setOnClickListener { toggleDiceLock(2, binding.diceMiddle) }
        binding.diceBottomLeft.setOnClickListener { toggleDiceLock(3, binding.diceBottomLeft) }
        binding.diceBottomRight.setOnClickListener { toggleDiceLock(4, binding.diceBottomRight) }

    }


    private fun rollDice() {
        for (i in diceValues.indices) {
            if (!lockedDice[i]) {
                diceValues[i] = Random.nextInt(1, 7)
            }
            currentDiceValues[i] = diceValues[i]
        }
        lockedDice.fill(false); //dit zorgt ervoor dat telkens als er gerollt wordt dat het ni meer lockt is.
                                        //bij elke roll moet je dus opnieuw locked dice aangeven.
        updateDiceImages()
    }

    private fun updateDiceImages() {
        binding.diceTopLeft.setImageResource(diceImages[diceValues[0] - 1])
        binding.diceTopRight.setImageResource(diceImages[diceValues[1] - 1])
        binding.diceMiddle.setImageResource(diceImages[diceValues[2] - 1])
        binding.diceBottomLeft.setImageResource(diceImages[diceValues[3] - 1])
        binding.diceBottomRight.setImageResource(diceImages[diceValues[4] - 1])
    }

    private fun updateRollInfo() {
        // Update the roll info text
        binding.rollInfo.text = "You have $attemptsleft rolling attempts left!"
        binding.shakeToRollButton.isEnabled = attemptsleft > 0
    }

    private fun toggleDiceLock(index: Int, diceImageView: ImageView) {
        lockedDice[index] = !lockedDice[index]
        if (lockedDice[index]) {
            diceImageView.setImageResource(diceImages[diceValues[index] - 1 + 6])
        } else {
            diceImageView.setImageResource(diceImages[diceValues[index] - 1])
        }
    }

    private fun updateScoreboard(scores: Map<String, Int?>) {
        // Update the OnesScore TextView with the current score for "ones"
        val onesScore = scores["ones"] ?: 0
        binding.OnesScore.text = onesScore.toString()

        // Update the TwosScore TextView with the current score for "twos"
        val twosScore = scores["twos"] ?: 0
        binding.TwosScore.text = twosScore.toString()

        // Add additional fields as necessary
    }
    //scorebord


}

