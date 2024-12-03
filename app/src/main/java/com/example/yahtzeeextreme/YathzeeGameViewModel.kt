package com.example.yahtzeeextreme

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
            "ones" to 0,
            "twos" to 0,
            "threes" to 0,
            "fours" to 0,
            "fives" to 0,
            "sixes" to 0,
            "threeOfKind" to 0,
            "fourOfKind" to 0,
            "fullHouse" to 0,
            "smallStraight" to 0,
            "largeStraight" to 0,
            "yahtzee" to 0,
            "chance" to 0
        )
    }
    fun roll() {
        val newDice = _dice.value?.mapIndexed { index, value ->
            if (_locked.value?.get(index) == true) value else Random.nextInt(1, 7)
        }
        _dice.value = newDice
        _attemptsLeft.value = _attemptsLeft.value?.minus(1)
        if (_attemptsLeft.value == 0) {
            _locked.value =
                List(5) { true } // als al uw beurten om zijn blijven de dobbelstenen hoe ze nu zijn
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
    fun doScore(ruleName: String) {
        val currentScores = _scoreboard.value?.toMutableMap()
        val score = when (ruleName) {
            "ones" -> scoreOnes()
            "twos" -> scoreTwos()
            "threes" -> scoreThrees()
            "fours" -> scoreFours()
            "fives" -> scoreFives()
            "sixes" -> scoreSixes()
            "threeOfKind" -> scoreThreeOfAKind()
            "fourOfKind" -> scoreFourOfAKind()
            "fullHouse" -> scoreFullHouse()
            "smallStraight" -> scoreSmallStraight()
            "largeStraight" -> scoreLargeStraight()
            "yahtzee" -> scoreYahtzee()
            "chance" -> scoreChance()
            else -> null
        }
        currentScores?.set(ruleName, score)
        _scoreboard.value = currentScores
        resetGameState()
    }

    //scoring rules
    private fun scoreOnes() = scoreSpecificNumber(1)
    private fun scoreTwos() = scoreSpecificNumber(2)
    private fun scoreThrees() = scoreSpecificNumber(3)
    private fun scoreFours() = scoreSpecificNumber(4)
    private fun scoreFives() = scoreSpecificNumber(5)
    private fun scoreSixes() = scoreSpecificNumber(6)

    private fun scoreSpecificNumber(number: Int): Int {
        return _dice.value?.count { it == number }?.times(number) ?: 0
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