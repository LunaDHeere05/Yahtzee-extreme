# **Yahtzee Extreme**

For my **Java Advanced** class, I decided to recreate a childhood favorite game of mine, Yahtzee! This project served as an opportunity to challenge myself by adapting quickly to a new programming language (**Kotlin**) and development environment (**Android Studio**). Additionally, it also challenged me to use different technologies and libraries within this new environment.

##Table of Contents
1. [What is Yahtzee?](#what-is-yahtzee)
   - [Yahtzee Combinations](#yahtzee-combinations)
2. [Extreme Rules](#extreme-rules)
3. [Technologies and Libraries](#technologies-and-libraries)
   - [Android Studio](#android-studio)
   - [Jetpack Libraries](#jetpack-libraries)
   - [Kotlin Utilities](#kotlin-utilities)
   - [Android APIs](#android-apis)
   - [Hardware Sensor APIs](#hardware-sensor-apis)

## **What is Yahtzee?**
Yahtzee is a classic dice game that challenges players to roll combinations for the highest score.

### **General Rules:**
- Players roll five dice up to three times per turn.
- After rolling, players can choose to keep (lock) dice and re-roll the rest.
- The goal is to achieve specific combinations like three-of-a-kind, full house, or the elusive Yahtzee (five-of-a-kind).
- Each combination can be scored only once during the game.
- The player with the highest total score at the end wins.

### **Yahtzee combinations:**

The specific combinations a user can try to achieve are:
- Three of a kind, Four of a kind
   - The user needs to throw three or four dice that have the same value
- Full house
   - The user should throw 3 of the same dice and then another 2 of the same dice
- Small Straight
   - The user needs to throw 4 sequential numbers
- Large Straight
   - The user needs to throw 5 sequential numbers
- Yahtzee
   - The user should throw 5 of the same numbers

To make this game more unique I added some extreme rules to add a twist to the gameplay. These Rules are triggered randomly during gameplay, with a maximum of three rules per game.

### **Extreme Rules:**

1. Positive Bonus Effects
- **Wizard's Favour**: Gain 7 points for rolling all even numbers.
- **Santa's Gift**: Receive a 15-point bonus as a surprise gift.
- **Ifrit's Blessing**: Doubles your current score.

2. Challenging Penalties:
- **Chocobo's Theft**: Lose 20 points due to a mischievous thief.
- **Potion of Chaos**: Randomly shuffles all dice, creating unexpected outcomes.
- **Shiva's Freeze**: Freezes all dice, preventing further rolls for the round.

## **Technologies and Libraries**

### **Android Studio**
- Used as IDE for this project.

### **Jetpack Libraries**
- **LiveData**: Used for observing and managing UI-related data.
    - `androidx.lifecycle.LiveData`
    - `androidx.lifecycle.MutableLiveData`
- **ViewModel**: lifecycle-conscious data management (used together with LiveData) to make sure data survives configuration changes.
    - `androidx.lifecycle.ViewModel`
- **Data Binding**: To simplify UI development.
    - `com.example.yahtzeeextreme.databinding.ActivityMainBinding`
 
### **Kotlin Utilities**
- **Randomization**: For generating random numbers. In the game it's used for the dice rolling.
    - `kotlin.random.Random`

### **Android APIs**
- **User Notifications**: For displaying messages. In the game it's used to show explanations of extreme rules.
    - `android.widget.Toast`
 
### **Hardware Sensor APIs**
- To detect changes in motion of the phone. In this game it's used to be able to roll dice by shaking the phone.
    - `android.hardware.Sensor`
    - `android.hardware.SensorEvent`
    - `android.hardware.SensorEventListener`
    - `android.hardware.SensorManager`
