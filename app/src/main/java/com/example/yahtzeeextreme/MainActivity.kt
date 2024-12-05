package com.example.yahtzeeextreme

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.yahtzeeextreme.databinding.ActivityMainBinding
import kotlin.random.Random
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class MainActivity : ComponentActivity(), SensorEventListener {


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
    private var diceValues = IntArray(5) { Random.nextInt(1,7) }
    private var attemptsleft = 3
    private var lockedDice = BooleanArray(5) { false }

    private val currentDiceValues = IntArray(5)
    private lateinit var binding: ActivityMainBinding

    //shake to roll

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lastUpdate: Long = 0
    private var last_x: Float = 0f
    private var last_y: Float = 0f
    private var last_z: Float = 0f
    private val SHAKE_THRESHOLD = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        updateDiceImages()
        updateRollInfo()

        binding.Ones.setOnClickListener {
            gameViewModel.doScore("ones", currentDiceValues)
        }
        binding.Twos.setOnClickListener {
            gameViewModel.doScore("twos", currentDiceValues)
        }
        binding.Threes.setOnClickListener {
            gameViewModel.doScore("threes", currentDiceValues)
        }
        binding.Fours.setOnClickListener {
            gameViewModel.doScore("fours", currentDiceValues)
        }
        binding.Fives.setOnClickListener {
            gameViewModel.doScore("fives", currentDiceValues)
        }
        binding.Sixes.setOnClickListener {
            gameViewModel.doScore("sixes", currentDiceValues)
        }
        binding.ThreeOfKind.setOnClickListener{
            gameViewModel.doScore("threeOfKind", currentDiceValues)
        }
        binding.FourOfKind.setOnClickListener{
            gameViewModel.doScore("fourOfKind", currentDiceValues)
        }

        binding.FullHouse.setOnClickListener{
            gameViewModel.doScore("fullHouse", currentDiceValues)
        }

        binding.SmallStraight.setOnClickListener{
            gameViewModel.doScore("smallStraight", currentDiceValues)
        }

        binding.LargeStraight.setOnClickListener{
            gameViewModel.doScore("largeStraight", currentDiceValues)
        }

        binding.Chance.setOnClickListener{
            gameViewModel.doScore("chance", currentDiceValues)
        }

        binding.Yahtzee.setOnClickListener{
            gameViewModel.doScore("yahtzee", currentDiceValues)
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

    //shake to roll
    override fun onResume() {
        super.onResume()
        // Register the sensor listener
        accelerometer?.let { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener
        sensorManager.unregisterListener(this)
    }



    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val curTime = System.currentTimeMillis()

            // Only allow one update every 100ms
            if ((curTime - lastUpdate) > 100) {
                val diffTime = (curTime - lastUpdate)
                lastUpdate = curTime

                val speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000

                if (speed > SHAKE_THRESHOLD) {
                    // Shake detected, roll the dice
                    if (attemptsleft > 0) {
                        rollDice()
                        attemptsleft--
                        updateRollInfo()
                    }
                }

                last_x = x
                last_y = y
                last_z = z
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //functie om dice te rollen

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
        val onesScore = scores["ones"] ?: 0
        binding.OnesScore.text = onesScore.toString()

        val twosScore = scores["twos"] ?: 0
        binding.TwosScore.text = twosScore.toString()

        val threeScore = scores["threes"] ?: 0
        binding.ThreesScore.text = threeScore.toString()

        val fourScore = scores["fours"] ?: 0
        binding.FoursScore.text = fourScore.toString()

        val fiveScore = scores["fives"] ?: 0
        binding.FivesScore.text = fiveScore.toString()

        val sixScore = scores["sixes"] ?: 0
        binding.SixesScore.text = sixScore.toString()

        val threeOfKindScore = scores["threeOfKind"] ?: 0
        binding.ThreeOfKindScore.text = threeOfKindScore.toString()

        val fourOfKindScore = scores["fourOfKind"] ?: 0
        binding.FourOfKindScore.text = fourOfKindScore.toString()

        val fullHouseScore = scores["fullHouse"] ?: 0
        binding.FullHouseScore.text = fullHouseScore.toString()

        val smallStraight = scores["smallStraight"] ?: 0
        binding.SmallStraightScore.text = smallStraight.toString()

        val largeStraight = scores["largeStraight"] ?: 0
        binding.LargeStraightScore.text = largeStraight.toString()

        val chance = scores["chance"] ?: 0
        binding.ChanceScore.text = chance.toString()

        val yahtzee = scores["yahtzee"] ?: 0
        binding.YahtzeeScore.text = yahtzee.toString()


    }


}

