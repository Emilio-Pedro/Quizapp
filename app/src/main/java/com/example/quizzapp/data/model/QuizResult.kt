package com.example.quizzapp.data.model

data class QuizResult(
    val userId: String = "",
    val date: Long = 0L,
    val score: Int = 0,
    val totalQuestions: Int = 0
)