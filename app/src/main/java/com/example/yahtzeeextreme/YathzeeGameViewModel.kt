package com.example.yahtzeeextreme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class YahtzeeGameViewModel : ViewModel() {

    private val _dice = MutableLiveData<List<Int>>()
    val dice: LiveData<List<Int>> get() = _dice

    private val _locked =
        MutableLiveData<List<Boolean>>() //dit is dus om de dobbelstenen te laten hoe ze zijn als alle beurten om zijn om te rerollen
    val locked: LiveData<List<Boolean>> get() = _locked

    private val _attemptsLeft = MutableLiveData<Int>()
    val rollsLeft: LiveData<Int> get() = _attemptsLeft //om dus te zien hoe vaak er nog gerolt mag worde

    private val _scoreboard = MutableLiveData<Map<String, Int?>>()
    val scores: LiveData<Map<String, Int?>> get() = _scoreboard // dus voor op de score op te tellen
    // hiervoor moet ik nog plaats voorzien op de ui
    //LiveData is blijkbaar voor te zien of er aanpassingen zijn in de staat van de data (state management)
    //map wnt als ik da print voor ui wil ik zien waarover het gaat en dan de score -> gwn uit gemak lol


    init {
        _dice.value = List(5) { 1 }
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
    fun roll() {
        val newDice = _dice.value?.mapIndexed { index, value ->
            if (_locked.value?.get(index) == true) value else Random.nextInt(1, 7)
        }
        _dice.value = newDice
        _attemptsLeft.value = _attemptsLeft.value?.minus(1)
        if (_attemptsLeft.value == 0) {
            _locked.value = List(5) { true } // Lock all dice if no attempts left
        }
    }

    fun toggleLocked(idx: Int) {
        val newLocked = _locked.value?.toMutableList()
        newLocked?.set(idx, !(newLocked[idx]))
        _locked.value = newLocked
    }

    fun doScore(ruleName: String, ruleFn: (List<Int>) -> Int?) {
        val currentScores = _scoreboard.value?.toMutableMap()
        currentScores?.set(ruleName, ruleFn(_dice.value!!))
        _scoreboard.value = currentScores
        resetGameState()
    }

    private fun resetGameState() {
        _attemptsLeft.value = 3
        _locked.value = List(5) { false }
    }

    fun anyUnlocked(): Boolean {
        return _locked.value?.contains(false) == true
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
            else -> null
        }
        currentScores?.set(ruleName, score)
        _scoreboard.value = currentScores
        resetGameState()
    }

    //scoring rules
    fun scoreOnes(currentDiceValues: IntArray): Int? {
        if (_scoreboard.value?.get("ones") == null) { // Check if the score for "ones" is already filled
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 1) // Calculate the score for ones
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["ones"] = scoreNeeded // Update the score for ones
            _scoreboard.value = currentScores // This should trigger the observer
            Log.d("YahtzeeGameViewModel", "Score for ones updated to: $scoreNeeded")
            return scoreNeeded // Ensure you return the score
        } else {
            return null // Return null if already filled
        }
    }
    // Helper function to calculate the score for a specific number
    private fun scoreSpecificNumber(currentDiceValues: IntArray, number: Int): Int {
        return currentDiceValues.count { it == number } * number // Count occurrences of the number and multiply by the number
    }
    fun scoreTwos(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("twos") == null) { // Check if the score for "ones" is already filled
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 2) // Calculate the score for ones
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["twos"] = scoreNeeded // Update the score for ones
            _scoreboard.value = currentScores // This should trigger the observer
            Log.d("YahtzeeGameViewModel", "Score for ones updated to: $scoreNeeded")
            return scoreNeeded // Ensure you return the score
        } else {
            return null // Return null if already filled
        }
    }
    fun scoreThrees(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("threes") == null) { // Check if the score for "ones" is already filled
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 3) // Calculate the score for ones
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["threes"] = scoreNeeded // Update the score for ones
            _scoreboard.value = currentScores // This should trigger the observer
            Log.d("YahtzeeGameViewModel", "Score for ones updated to: $scoreNeeded")
            return scoreNeeded // Ensure you return the score
        } else {
            return null // Return null if already filled
        }
    }
    fun scoreFours(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("fours") == null) { // Check if the score for "ones" is already filled
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 4) // Calculate the score for ones
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["fours"] = scoreNeeded // Update the score for ones
            _scoreboard.value = currentScores // This should trigger the observer
            Log.d("YahtzeeGameViewModel", "Score for ones updated to: $scoreNeeded")
            return scoreNeeded // Ensure you return the score
        } else {
            return null // Return null if already filled
        }
    }
    fun scoreFives(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("fives") == null) { // Check if the score for "ones" is already filled
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 5) // Calculate the score for ones
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["fives"] = scoreNeeded // Update the score for ones
            _scoreboard.value = currentScores // This should trigger the observer
            Log.d("YahtzeeGameViewModel", "Score for ones updated to: $scoreNeeded")
            return scoreNeeded // Ensure you return the score
        } else {
            return null // Return null if already filled
        }
    }
    fun scoreSixes(currentDiceValues: IntArray): Int?{
        if (_scoreboard.value?.get("sixes") == null) { // Check if the score for "ones" is already filled
            val scoreNeeded = scoreSpecificNumber(currentDiceValues, 6) // Calculate the score for ones
            val currentScores = _scoreboard.value?.toMutableMap() ?: mutableMapOf()
            currentScores["sixes"] = scoreNeeded // Update the score for ones
            _scoreboard.value = currentScores // This should trigger the observer
            Log.d("YahtzeeGameViewModel", "Score for ones updated to: $scoreNeeded")
            return scoreNeeded // Ensure you return the score
        } else {
            return null // Return null if already filled
        }
    }

    private fun updateScore(ruleName: String, score: Int?) {
        val currentScores = _scoreboard.value?.toMutableMap()
        currentScores?.set(ruleName, score)
        _scoreboard.value = currentScores
    }

    private fun scoreSpecificNumber(number: Int): Int {
        val count = _dice.value?.count { it == number } ?: 0
        Log.d("YahtzeeGameViewModel", "Count of $number in dice: $count")
        return count * number
    }
    private fun scoreThreeOfAKind(): Int? {
        return if (hasNOfAKind(3)) _dice.value?.sum() else 0
    }

    private fun scoreFourOfAKind(): Int? {
        return if (hasNOfAKind(4)) _dice.value?.sum() else 0
    }

    private fun scoreFullHouse(): Int? {
        //als er 3 van 1 soort cijfers gegooid worden en 2 van een andere soort
        val counts = _dice.value?.groupingBy { it }?.eachCount() ?: return 0

        return if (counts.size == 2) {
            when {
                counts.any { it.value == 3 } && counts.any { it.value == 2 } -> 25
                else -> 0
            }
        } else {
            0
        }
    }
    private fun scoreSmallStraight(): Int? {
        //als er 4 opvolgende cijfers gegooid worden
        val uniqueValues = _dice.value?.distinct() ?: return 0
        return if ((uniqueValues.contains(1) && uniqueValues.contains(2) && uniqueValues.contains(3) && uniqueValues.contains(4)) ||
            (uniqueValues.contains(2) && uniqueValues.contains(3) && uniqueValues.contains(4) && uniqueValues.contains(5)) ||
            (uniqueValues.contains(3) && uniqueValues.contains(4) && uniqueValues.contains(5) && uniqueValues.contains(6))) {
            30
        } else {
            0
        }
    }

    private fun scoreLargeStraight(): Int? {
        // als er 5 opvolgende cijfers gegooid worden
        val uniqueValues = _dice.value?.distinct() ?: return 0
        return if ((uniqueValues.contains(1) && uniqueValues.contains(2) && uniqueValues.contains(3) && uniqueValues.contains(4) && uniqueValues.contains(5)) ||
            (uniqueValues.contains(2) && uniqueValues.contains(3) && uniqueValues.contains(4) && uniqueValues.contains(5) && uniqueValues.contains(6))) {
            40
        } else {
            0
        }
    }

    private fun scoreYahtzee(): Int? {
        //als er 5 dezelfde nummers gegooid worden
        return if (hasNOfAKind(5)) {
            50
        } else {
            0
        }
    }

    private fun scoreChance(): Int {
        //som van alle cijfers
        return _dice.value?.sum() ?: 0
    }
    private fun hasNOfAKind(n: Int): Boolean {
        val counts = _dice.value?.groupingBy { it }?.eachCount() ?: return false
        return counts.any { it.value >= n }
    }

}