# **Yahtzee Extreme**

## **Technologies and Libraries**
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
