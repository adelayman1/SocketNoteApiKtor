package com.example.data.repositories

import com.example.data.models.Users
import com.example.data.models.resultRowToUser
import com.example.data.source.dao.userDao.UserDAO
import com.example.domain.models.UserModel
import com.example.domain.repositories.UserRepository
import java.util.*

class UserRepositoryImpl constructor(var userDAO: UserDAO) : UserRepository {
    override suspend fun register(email: String, password: String, name: String): UserModel? {
        return userDAO.insetUser(
            userAuthToken = UUID.randomUUID().toString(),
            userEmail = email,
            userName = name,
            userPassword = password
        )?.let(::resultRowToUser)
    }

    override suspend fun login(email: String, password: String): UserModel? {
        // get user with given data
        val user = userDAO.getUserByEmail(email)
            ?: (throw Exception("Email is not exist"))
        // check is hashed password equal given password
        val isPasswordValid = password == user[Users.password]
        if (!isPasswordValid)
            throw Exception("Password is not valid")
        return user.let(::resultRowToUser)
    }

    override suspend fun isEmailExist(email: String): Boolean {
        return userDAO.getUserByEmail(email = email) != null
    }

    override suspend fun isUserTokenExist(userAuthKey: String): Boolean {
        return userDAO.getUserByToken(userAuthKey) != null
    }
}