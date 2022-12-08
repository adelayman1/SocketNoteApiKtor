package com.example.domain.models

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserModel(
    var userID: String = "-1",
    var userToken: String,
    var userName: String,
    var email: String
)
//
//fun toHashedPassword(password: String): String {
//    return BCrypt.withDefaults().hashToString(12, password.toCharArray())
//}
//
//fun verifyHashedPassword(password: String, hashedPassword: String): Boolean {
//    return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
//}