package com.example.domain.repositories

import com.example.domain.models.UserModel

interface UserRepository {
    suspend fun register(email: String, password: String, name: String): UserModel?
    suspend fun isEmailExist(email: String): Boolean
    suspend fun isUserTokenExist(userAuthKey: String): Boolean
    suspend fun login(email: String, password: String): UserModel?
}