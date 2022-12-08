package com.example.domain.usecases

import com.example.domain.models.BaseResponse
import com.example.domain.models.NoteModel
import com.example.domain.repositories.NoteRepository
import com.example.domain.repositories.UserRepository

class GetNoteDetailsUseCase constructor(
    var noteRepository: NoteRepository,
    var userRepository: UserRepository
) {
    suspend operator fun invoke(userToken: String?, noteID: String?): BaseResponse<NoteModel> {
        return try {

            if (noteID.isNullOrEmpty())
                return BaseResponse.ErrorResponse(message = "note ID is empty") as BaseResponse<NoteModel>
            if (userToken.isNullOrEmpty())
                return BaseResponse.ErrorResponse(message = "Not Authorized") as BaseResponse<NoteModel>
            if (!userRepository.isUserTokenExist(userToken))
                return BaseResponse.ErrorResponse(message = "Authorization key is not exist") as BaseResponse<NoteModel>
            val getNoteDetailsResult = noteRepository.getNoteDetails(noteID.toInt())
                ?: return BaseResponse.ErrorResponse(message = "Note not found") as BaseResponse<NoteModel>
            if (getNoteDetailsResult.userToken != userToken)
                return BaseResponse.ErrorResponse(message = "Not Authorized") as BaseResponse<NoteModel>
            return BaseResponse.SuccessResponse(
                message = "Note details has got successfully",
                data = getNoteDetailsResult
            )
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = "unknown error ${e.message}") as BaseResponse<NoteModel>
        }
    }
}