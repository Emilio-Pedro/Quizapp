package com.example.quizzapp.data.repository


import com.example.quizzapp.data.model.User


interface AuthRepository {

    fun getCurrentUser(): User?

    suspend fun login(email: String, pass: String): User
    suspend fun register(email: String, pass: String): User
    fun logout()
}