package com.example.domain.usecases

import com.example.domain.models.BaseResponse
import com.example.domain.repositories.UserRepository
import io.ktor.http.content.*
import java.io.File
import java.util.*

class SaveImageUseCase constructor(var userRepository: UserRepository){
    suspend operator fun invoke(userToken:String?,image: PartData.FileItem): BaseResponse<String> {
        return try {
            if (userToken.isNullOrEmpty())
                throw Exception("Not Authorized")
            if (!userRepository.isUserTokenExist(userToken))
                throw Exception("Not Authorized")
            val path = "build/images/"
            val fileBytes = image.streamProvider().readBytes()
            val fileExtension = image.originalFileName?.takeLastWhile { it != '.' }
            val fileName = UUID.randomUUID().toString() + "." + fileExtension
            val folder = File(path)
            folder.mkdir()
            File("$path$fileName").writeBytes(fileBytes)
            return BaseResponse.SuccessResponse(data = "http://192.168.1.6:4040/$path$fileName", message = "image uploaded successfully")
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}") as BaseResponse<String>
        }
    }
}