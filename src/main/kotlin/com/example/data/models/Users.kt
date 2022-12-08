package com.example.data.models

import com.example.domain.models.UserModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Users : Table() {
    var userID = integer("userId").autoIncrement()
    var userToken = varchar("userToken", 128)
    var email = varchar("email", 64)
    var password = varchar("password", 64)
    var name = varchar("name", 32)
    override val primaryKey: PrimaryKey = PrimaryKey(userID)
}

fun resultRowToUser(row: ResultRow) = UserModel(
    userName = row[Users.name],
    userID = row[Users.userID].toString(),
    userToken = row[Users.userToken],
    email = row[Users.email]
)