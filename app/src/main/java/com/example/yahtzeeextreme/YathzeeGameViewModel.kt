package com.example.yahtzeeextreme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class YahtzeeGameViewModel : ViewModel() {


    private val _highScore = MutableLiveData<Int>()
    val highScore: LiveData<Int> get() = _highScore

    private val _locked =
        MutableLiveData<List<Boolean>>() //dit is dus om de dobbelstenen te laten hoe ze zijn als alle beurten om zijn om te rerollen
    val locked: LiveData<List<Boolean>> get() = _locked

    private val _attemptsLeft = MutableLiveData<Int>() //om dus te zien hoe vaak er nog gerolt mag worde

    private val _scoreboard = MutableLiveData<Map<String, Int?>>()
    val scores: LiveData<Map<String, Int?>> get() = _scoreboard // dus voor op de score op te tellen
    // hiervoor moet ik nog plaats voorzien op de ui
    //LiveData is blijkbaar voor te zien of er aanpassingen zijn in de staat van de data (state management)
    //map wnt als ik da print voor ui wil ik zien waarover het gaat en dan de score -> gwn uit gemak lol


    init {
        _locked.value = List(5) { false }
        _attemptsLeft.value = 3
        _scoreboard.value = mapOf( //dit is om scores bij te houden zie hierbove
            "ones" to null,
            "twos" to null,
            "threes" to null,
            "fives" to null,
            "sixes" to null,
            "threeOfKind" to null,
            "fourOfKind" to null,
            "fullHouse" to null,
            "smallStraight" to null,
            "largeStraight" to null,
            "yahtzee" to null,
            "chance" to null
        )
    }

    //scoring
    fun doScore(ruleName: String, currentDiceValues: IntArray) {
        val currentScores = _scoreboard.value?.toMutableMap()
        val score = when (ruleName) {
            "ones" -> scoreOnes(currentDiceValues)
            "twos" -> scoreTwos(currentDiceValues)
            "threes" -> scoreThrees(currentDiceValues)

            "fours" -> scoreFours(currentDiceValues)
            "fives" -> scoreFives(currentDiceValues)
            "sixes" -> scoreSixes(currentDiceValues)
            "threeOfKind" -> scoreThreeOfAKind(currentDiceValues)
            "fourOfKind" -> scoreFourOfAKind(currentDiceValues)
            "fullHouse" -> scoreFullHouse(currentDiceValues)
            "smallStraight" -> scoreSmallStraight(currentDiceValues)
            "largeStraight" -> scoreLargeStraight(currentDiceValues)
            "chance" -> scoreChance(currentDiceValues)
            "yahtzee" -> scoreYahtzee(currentDiceValues)
            else -> null
        }
        currentScores?.set(ruleName, score)
        _scoreboard.value = currentScores
        resetGameState()
    }

    //scoring rules

    private fun scoreSpecificNumber(currentDiceValues: IntArray, number: Int): Int {
        return currentDiceValues.count { it == number } * number
    }

    fun resetGameState() {
        _attemptsLeft.value = 3
        _locked.value = List(5) { false }
    }

    fun scoreOnes(currentDiceValues: IntArray): Int? {
        if (_scoreboard.value?.get("ones") == null) {
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 1)
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["ones"] = scoreNeeded
            _scoreboard.value = currentScores
            Log.d("YahtzeeGameViewModel", "Score for ones updated to: $scoreNeeded")
            return scoreNeeded
        } else {
            return 0
        }
    }

    fun scoreTwos(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("twos") == null) {
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 2)
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["twos"] = scoreNeeded
            _scoreboard.value = currentScores
            return scoreNeeded
        } else {
            return 0
        }
    }
    fun scoreThrees(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("threes") == null) {
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 3)
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["threes"] = scoreNeeded
            _scoreboard.value = currentScores
            return scoreNeeded
        } else {
            return 0
        }
    }
    fun scoreFours(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("fours") == null) {
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 4)
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["fours"] = scoreNeeded
            _scoreboard.value = currentScores
            return scoreNeeded
        } else {
            return 0
        }
    }
    fun scoreFives(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("fives") == null) {
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 5)
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["fives"] = scoreNeeded
            _scoreboard.value = currentScores
            return scoreNeeded
        } else {
            return 0
        }
    }
    fun scoreSixes(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("sixes") == null) {
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 6)
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["sixes"] = scoreNeeded
            _scoreboard.value = currentScores
            return scoreNeeded
        } else {
            return 0
        }
    }

//    private fun scoreSpecificNumber(currentDiceValues: IntArray, number: Int): Int {
//        return currentDiceValues.count { it == number } * number
//    }

    private fun hasNOfAKind(currentDiceValues: IntArray, n: Int): Boolean {
        val counts = currentDiceValues.toList().groupingBy{ it }.eachCount()
        return counts.any { it.value >= n }
    }


    private fun scoreThreeOfAKind(currentDiceValues: IntArray): Int? {
        // Check if the score for "ThreeOfKind" is already set
        if (_scoreboard.value?.get("ThreeOfKind") == null) {
            // Check if the dice roll has three of a kind
            if (hasNOfAKind(currentDiceValues, 3)) {
                // Group dice by value and count occurrences
                val counts = currentDiceValues.toList().groupingBy { it }.eachCount()

                // Find the value with at least three occurrences and calculate the sum
                val threeOfKindValue = counts.filter { it.value >= 3 }.keys.firstOrNull()
                val scoreRolled = threeOfKindValue?.let { it * 3 } ?: 0

                // Update the scoreboard
                val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
                currentScores["ThreeOfKind"] = scoreRolled
                _scoreboard.value = currentScores

                return scoreRolled
            }
        }
        return 0
    }

private fun scoreFourOfAKind(currentDiceValues: IntArray): Int? {
        // Check if the score for "ThreeOfKind" is already set
        if (_scoreboard.value?.get("ThreeOfKind") == null) {
            // Check if the dice roll has three of a kind
            if (hasNOfAKind(currentDiceValues, 4)) {
                // Group dice by value and count occurrences
                val counts = currentDiceValues.toList().groupingBy { it }.eachCount()

                // Find the value with at least three occurrences and calculate the sum
                val threeOfKindValue = counts.filter { it.value >= 4 }.keys.firstOrNull()
                val scoreRolled = threeOfKindValue?.let { it * 4 } ?: 0

                // Update the scoreboard
                val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
                currentScores["ThreeOfKind"] = scoreRolled
                _scoreboard.value = currentScores

                return scoreRolled
            }
        }
        return 0
    }
    private fun scoreYahtzee(currentDiceValues: IntArray): Int? {
        //als er 5 dezelfde nummers gegooid worden
        return if (hasNOfAKind(currentDiceValues, 5)) {
            50
        } else {
            0
        }
    }

    private fun scoreFullHouse(currentDiceValues: IntArray): Int? {
        // Group dice values by occurrence and count each
        val counts = currentDiceValues.toList().groupingBy { it }.eachCount()

        // Check if there are exactly two different values with counts 3 and 2
        return if (counts.size == 2) {
            when {
                counts.any { it.value == 3 } && counts.any { it.value == 2 } -> 25 // Full house score
                else -> 0
            }
        } else {
            0
        }
    }

    private fun scoreSmallStraight(currentDiceValues: IntArray): Int? {

        val uniqueValues = currentDiceValues

        return if ((1 in uniqueValues && 2 in uniqueValues && 3 in uniqueValues && 4 in uniqueValues) ||
            (2 in uniqueValues && 3 in uniqueValues && 4 in uniqueValues && 5 in uniqueValues) ||
            (3 in uniqueValues && 4 in uniqueValues && 5 in uniqueValues && 6 in uniqueValues)) {
            30
        } else {
            0
        }
    }


    private fun scoreLargeStraight(currentDiceValues: IntArray): Int? {
        val uniqueValues = currentDiceValues
        return if ((1 in uniqueValues && 2 in uniqueValues && 3 in uniqueValues && 4 in uniqueValues && 5 in uniqueValues) ||
            (2 in uniqueValues && 3 in uniqueValues && 4 in uniqueValues && 5 in uniqueValues && 6 in uniqueValues)) {
            40
        } else {
            0
        }
    }

    private fun scoreChance(currentDiceValues: IntArray): Int {
        // Check if the score for "ThreeOfKind" is not already set
        if (_scoreboard.value?.get("chance") == null) {
            return currentDiceValues.sum() // Return the sum of all dice
        } else {
            return 0 // Return 0 if "ThreeOfKind" is already scored
        }
    }



    //extreme rules

    //gain points

    //lose points


}