package com.example.yahtzeeextreme

import android.util.Log
import android.widget.Toast
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

    fun resetScore(){
            _scoreboard.value = mapOf(
                "ones" to null,
                "twos" to null,
                "threes" to null,
                "fours" to null,
                "fives" to null,
                "sixes" to null,
                "threeOfKind" to null,
                "fourOfKind" to null,
                "fullHouse" to null,
                "smallStraight" to null,
                "largeStraight" to null,
                "chance" to null,
                "yahtzee" to null
            )
    }

    fun scoreOnes(currentDiceValues: IntArray): Int {
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

    private fun scoreTwos(currentDiceValues: IntArray): Int {
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
    private fun scoreThrees(currentDiceValues: IntArray): Int {
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
    private fun scoreFours(currentDiceValues: IntArray): Int {
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
    private fun scoreFives(currentDiceValues: IntArray): Int {
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
    private fun scoreSixes(currentDiceValues: IntArray): Int {
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


    private fun hasNOfAKind(currentDiceValues: IntArray, n: Int): Boolean {
        val counts = currentDiceValues.toList().groupingBy{ it }.eachCount()
        return counts.any { it.value >= n }
    }


    private fun scoreThreeOfAKind(currentDiceValues: IntArray): Int {
        if (_scoreboard.value?.get("ThreeOfKind") == null) {
            if (hasNOfAKind(currentDiceValues, 3)) {
                val counts = currentDiceValues.toList().groupingBy { it }.eachCount()

                val threeOfKindValue = counts.filter { it.value >= 3 }.keys.firstOrNull()
                val scoreRolled = threeOfKindValue?.let { it * 3 } ?: 0

                val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
                currentScores["ThreeOfKind"] = scoreRolled
                _scoreboard.value = currentScores

                return scoreRolled
            }
        }
        return 0
    }

private fun scoreFourOfAKind(currentDiceValues: IntArray): Int {
        if (_scoreboard.value?.get("ThreeOfKind") == null) {
            if (hasNOfAKind(currentDiceValues, 4)) {
                val counts = currentDiceValues.toList().groupingBy { it }.eachCount()

                val threeOfKindValue = counts.filter { it.value >= 4 }.keys.firstOrNull()
                val scoreRolled = threeOfKindValue?.let { it * 4 } ?: 0

                val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
                currentScores["ThreeOfKind"] = scoreRolled
                _scoreboard.value = currentScores

                return scoreRolled
            }
        }
        return 0
    }
    private fun scoreYahtzee(currentDiceValues: IntArray): Int {
        return if (hasNOfAKind(currentDiceValues, 5) &&  _scoreboard.value?.get("Yahtzee") == null){
            50
        } else {
            0
        }
    }

    private fun scoreFullHouse(currentDiceValues: IntArray): Int {
        val counts = currentDiceValues.toList().groupingBy { it }.eachCount()
        return if (counts.size == 2) {
            if (counts.any { it.value == 3 } && counts.any { it.value == 2 }) {
                25
            } else {
                0
            }
        } else {
            0
        }
    }

    private fun scoreSmallStraight(currentDiceValues: IntArray): Int {
        return if ((1 in currentDiceValues && 2 in currentDiceValues && 3 in currentDiceValues && 4 in currentDiceValues) ||
            (2 in currentDiceValues && 3 in currentDiceValues && 4 in currentDiceValues && 5 in currentDiceValues) ||
            (3 in currentDiceValues && 4 in currentDiceValues && 5 in currentDiceValues && 6 in currentDiceValues)) {
            30
        } else {
            0
        }
    }


    private fun scoreLargeStraight(currentDiceValues: IntArray): Int {
        return if ((1 in currentDiceValues && 2 in currentDiceValues && 3 in currentDiceValues && 4 in currentDiceValues && 5 in currentDiceValues) ||
            (2 in currentDiceValues && 3 in currentDiceValues && 4 in currentDiceValues && 5 in currentDiceValues && 6 in currentDiceValues)) {
            40
        } else {
            0
        }
    }

    private fun scoreChance(currentDiceValues: IntArray): Int {
        if (_scoreboard.value?.get("chance") == null) {
            return currentDiceValues.sum()
        } else {
            return 0
        }
    }


}