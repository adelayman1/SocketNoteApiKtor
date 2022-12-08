package com.example.data.source.dao.userDao

import org.jetbrains.exposed.sql.ResultRow

interface UserDAO {
    suspend fun insetUser(userAuthToken:String,userEmail: String,userPassword:String,userName:String): ResultRow?
    suspend fun getUserByEmail(email: String): ResultRow?
    suspend fun getUserByToken(userAuthorizationKey: String): ResultRow?
}