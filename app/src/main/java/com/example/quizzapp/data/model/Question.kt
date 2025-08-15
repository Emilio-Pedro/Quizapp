package com.example.quizzapp.data.model

data class Question(
    val id: String = "",
    val statement: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0
)
