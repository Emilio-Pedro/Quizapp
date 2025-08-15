package com.example.quizzapp.data.repository

import com.example.quizzapp.data.model.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.let { firebaseUser ->
            User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName,
                email = firebaseUser.email
            )
        }
    }

    override suspend fun login(email: String, pass: String): User {
        try {
            // Usa a função await() das coroutines para esperar pelo resultado assíncrono
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            val firebaseUser =
                authResult.user ?: throw Exception("Utilizador não encontrado após o login.")

            return User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName,
                email = firebaseUser.email
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun register(email: String, pass: String): User {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            val firebaseUser = authResult.user ?: throw Exception("Falha ao criar o utilizador.")

            return User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName,
                email = firebaseUser.email
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}