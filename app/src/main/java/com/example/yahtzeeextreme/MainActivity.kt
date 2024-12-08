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
import android.widget.Toast

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
    private fun toggleScoreButtons(enable: Boolean) {
        binding.Ones.isEnabled = enable
        binding.Twos.isEnabled = enable
        binding.Threes.isEnabled = enable
        binding.Fours.isEnabled = enable
        binding.Fives.isEnabled = enable
        binding.Sixes.isEnabled = enable
        binding.ThreeOfKind.isEnabled = enable
        binding.FourOfKind.isEnabled = enable
        binding.FullHouse.isEnabled = enable
        binding.SmallStraight.isEnabled = enable
        binding.LargeStraight.isEnabled = enable
        binding.Chance.isEnabled = enable
        binding.Yahtzee.isEnabled = enable
    }

    private var diceValues = IntArray(5) { Random.nextInt(1,7) }
    private var attemptsleft = 3
    private var lockedDice = BooleanArray(5) { false }

    private var currentDiceValues = IntArray(5)
    private lateinit var binding: ActivityMainBinding

    //shake to roll

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lastUpdate: Long = 500
    private var last_x: Float = 0f
    private var last_y: Float = 0f
    private var last_z: Float = 0f
    private val SHAKE_THRESHOLD = 200

    private var currentRound = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        gameViewModel.scores.observe(this) { scores ->
            updateScoreboard(scores)
        }

        gameViewModel.highScore.observe(this) { highScore ->
            binding.highScore.text = highScore.toString()
        }


        updateDiceImages()
        updateRollInfo()

        binding.Ones.setOnClickListener {
            gameViewModel.doScore("ones", currentDiceValues)
            resetForNewTurn()
        }
        binding.Twos.setOnClickListener {
            gameViewModel.doScore("twos", currentDiceValues)
            resetForNewTurn()
        }
        binding.Threes.setOnClickListener {
            gameViewModel.doScore("threes", currentDiceValues)
            resetForNewTurn()
        }
        binding.Fours.setOnClickListener {
            gameViewModel.doScore("fours", currentDiceValues)
            resetForNewTurn()
        }
        binding.Fives.setOnClickListener {
            gameViewModel.doScore("fives", currentDiceValues)
            resetForNewTurn()
        }
        binding.Sixes.setOnClickListener {
            gameViewModel.doScore("sixes", currentDiceValues)
            resetForNewTurn()
        }
        binding.ThreeOfKind.setOnClickListener{
            gameViewModel.doScore("threeOfKind", currentDiceValues)
            resetForNewTurn()
        }
        binding.FourOfKind.setOnClickListener{
            gameViewModel.doScore("fourOfKind", currentDiceValues)
            resetForNewTurn()
        }

        binding.FullHouse.setOnClickListener{
            gameViewModel.doScore("fullHouse", currentDiceValues)
            resetForNewTurn()
        }

        binding.SmallStraight.setOnClickListener{
            gameViewModel.doScore("smallStraight", currentDiceValues)
            resetForNewTurn()
        }

        binding.LargeStraight.setOnClickListener{
            gameViewModel.doScore("largeStraight", currentDiceValues)
            resetForNewTurn()
        }

        binding.Chance.setOnClickListener{
            gameViewModel.doScore("chance", currentDiceValues)
            resetForNewTurn()
        }

        binding.Yahtzee.setOnClickListener{
            gameViewModel.doScore("yahtzee", currentDiceValues)
            resetForNewTurn()
        }

        binding.shakeToRollButton.setOnClickListener { //btn voor het vergemakkelijken van de tests
            if (attemptsleft > 0) {
                rollDice()
                attemptsleft--
                updateRollInfo()
            }
        }

        binding.resetGame.setOnClickListener {
            resetForNewTurn()
            currentRound = 0
            attemptsleft = 3
            binding.highScore.text = "0"
            gameViewModel.resetScore()

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
        accelerometer?.let { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }



    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val curTime = System.currentTimeMillis()
            if ((curTime - lastUpdate) > 500) {
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

        if (Random.nextBoolean()) {
            activateRandomRule()
        }
    }

    private fun updateDiceImages() {
        binding.diceTopLeft.setImageResource(diceImages[diceValues[0] - 1])
        binding.diceTopRight.setImageResource(diceImages[diceValues[1] - 1])
        binding.diceMiddle.setImageResource(diceImages[diceValues[2] - 1])
        binding.diceBottomLeft.setImageResource(diceImages[diceValues[3] - 1])
        binding.diceBottomRight.setImageResource(diceImages[diceValues[4] - 1])
    }

    private fun updateRollInfo() {
        binding.rollInfo.text = "You have $attemptsleft rolling attempts left!"
        binding.shakeToRollButton.isEnabled = attemptsleft > 0
        toggleScoreButtons(attemptsleft == 0)
    }

    private fun toggleDiceLock(index: Int, diceImageView: ImageView) {
        lockedDice[index] = !lockedDice[index]
        if (lockedDice[index]) {
            diceImageView.setImageResource(diceImages[diceValues[index] - 1 + 6])
        } else {
            diceImageView.setImageResource(diceImages[diceValues[index] - 1])
        }
    }
    private fun updateHighScore() {
        val scores = gameViewModel.scores.value?.values
        val newScore = scores?.filterNotNull()?.sum() ?: 0
        val highScore = binding.highScore.text.toString().toIntOrNull() ?: 0
        if (newScore > highScore) {
            binding.highScore.text = newScore.toString()
        }
    }

    fun adjustScore(points: Int) {
        val currentScore = binding.highScore.text.toString().toIntOrNull() ?:0
        val newScore = currentScore + points
        binding.highScore.text = newScore.toString()
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

    //als ik gescoord heb moet er een nieuwe ronde starten

    private fun resetForNewTurn() {
        currentRound++
        gameViewModel.resetGameState()
        attemptsleft = 3
        updateRollInfo()
        updateDiceImages()
        toggleScoreButtons(false)
        rollDice()
        updateHighScore()
    }


    //extreme rules

    //gain points
    private fun wizardsFavour() {
        if (currentDiceValues.all { it % 2 == 0 }) {
            val toast = Toast.makeText(
                this,
                "A wizard bestows you with 7 bonus points for your exceptional symmetry!",
                Toast.LENGTH_LONG
            )
            toast.show()
            adjustScore(7)
        }
    }


    private fun santasGift() {
        adjustScore(15) // Add 15 points
        Toast.makeText(
            this,
            "Santa has bestowed you a gift! You gain 15 points!",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun ifrit(currentScore: Int) {
        adjustScore(currentScore)
        Toast.makeText(
            this,
            "Ifrit blessed you with his presence and doubled your score !",
            Toast.LENGTH_LONG
        ).show()
    }


    //lose points

    private fun chocobo(){
        adjustScore(-20)
        Toast.makeText(
            this,
            "wow there! a chocobo just stole 20 points and ran away !",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun potion(){
        diceValues.shuffle()
        for(i in diceValues.indices){
            currentDiceValues[i] = diceValues[i]
        }
        updateDiceImages()

        Toast.makeText(
            this,
            "A witch's potion messed up your dice! They get all mixed up!",
            Toast.LENGTH_LONG
        ).show()
    }


    private fun shiva() {
        attemptsleft = 0
        lockedDice.fill(true)
        updateRollInfo()
        updateDiceImages()

        Toast.makeText(
            this,
            "Shiva's icy touch has frozen all your dice! No more rolls this round.",
            Toast.LENGTH_LONG
        ).show()
    }

    private val rules = listOf(
        ::wizardsFavour,
        ::santasGift,
        { ifrit(binding.highScore.text.toString().toIntOrNull() ?: 0) },
        ::chocobo,
        ::potion,
        ::shiva
    )
    private val usedRules = mutableSetOf<() -> Unit>()
    private var rulesActivated = 0
    private val maxRulesPerGame = 3

    private fun activateRandomRule() {
        if (rulesActivated >= maxRulesPerGame || currentRound <= 1) return

        val activationChance = 0.2
        if (Random.nextDouble() > activationChance) return

        val unusedRules = rules.filterNot { usedRules.contains(it) }
        if (unusedRules.isNotEmpty()) {
            val selectedRule = unusedRules.random()
            usedRules.add(selectedRule)
            rulesActivated++
            selectedRule()
        }
    }




}

