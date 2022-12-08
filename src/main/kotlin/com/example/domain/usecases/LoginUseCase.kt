package com.example.domain.usecases

import com.example.domain.models.BaseResponse
import com.example.domain.models.UserModel
import com.example.domain.repositories.UserRepository

class LoginUseCase constructor(var userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): BaseResponse<UserModel> {
        return try {
            // check is email valid
            if (email.isBlank() || email.length < 5)
                return BaseResponse.ErrorResponse(message = "Email is too short") as BaseResponse<UserModel>
            // check is password valid
            if (password.isNullOrEmpty() || password.length < 5)
                return BaseResponse.ErrorResponse(message = "Password is too short") as BaseResponse<UserModel>
            // get user with given data
            val loginResult = userRepository.login(email, password)
            return BaseResponse.SuccessResponse(message = "Login done successfully", data = loginResult)
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}") as BaseResponse<UserModel>
        }
    }
}