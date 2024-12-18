package com.example.yahtzeeextreme.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("highscores")
    fun getHighScores(): Call<List<HighScore>>

    @POST("highscores")
    fun submitHighScore(@Body highScore: HighScore): Call<HighScore>
}
