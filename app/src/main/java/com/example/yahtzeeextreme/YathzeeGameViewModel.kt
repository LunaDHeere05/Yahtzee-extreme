package com.example.yahtzeeextreme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class YahtzeeGameViewModel : ViewModel() {

    private val _dice = MutableLiveData<List<Int>>()
    val dice: LiveData<List<Int>> get() = _dice

    private val _locked = MutableLiveData<List<Boolean>>() //dit is dus om de dobbelstenen te laten hoe ze zijn als alle beurten om zijn om te rerollen
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
            "fours" to null,
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
            _locked.value = List(5) { true } // als al uw beurten om zijn blijven de dobbelstenen hoe ze nu zijn
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
}