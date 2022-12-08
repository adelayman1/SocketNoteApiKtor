package com.example.domain.usecases

import com.example.domain.models.NoteModel
import com.example.domain.repositories.NoteRepository
import com.example.domain.repositories.SocketRepository
import com.example.domain.repositories.UserRepository
import com.example.routes.note.requestsModels.AddNoteParams

class UpdateNoteDetailsUseCase constructor(
    var noteRepository: NoteRepository,
    var userRepository: UserRepository,
    var socketRepository: SocketRepository
) {
    suspend operator fun invoke(
        userToken: String,
        note: AddNoteParams
    ) {
        return try {
            if (note.id.isNullOrEmpty())
                throw Exception("note ID is empty")
            if (userToken.isEmpty())
                throw Exception("Not Authorized")
            if (!userRepository.isUserTokenExist(userToken)) throw Exception("Authorization key is not exist")
            val getNoteDetailsResult =
                noteRepository.getNoteDetails(note.id!!.toInt()) ?: throw Exception("note not found")
            if (getNoteDetailsResult.userToken != userToken) throw Exception("Not Authorized")
            var result: NoteModel? = null
            if (!note.title.isNullOrEmpty()) {
                val updateNoteTitleResult = noteRepository.updateNoteTitle(note.id!!.toInt(), note.title?:"")
                result = updateNoteTitleResult
            }
            if (!note.description.isNullOrEmpty()) {
                val updateNoteDescriptionResult =
                    noteRepository.updateNoteDescription(note.id!!.toInt(), note.description?:"")
                result = updateNoteDescriptionResult
            }
            if (!note.subtitle.isNullOrEmpty()) {
                val updateNoteCategoryResult = noteRepository.updateNoteSubtitle(note.id!!.toInt(), note.subtitle?:"")
                result = updateNoteCategoryResult
            }
            val updateNoteWeblinkResult = noteRepository.updateNoteWebLink(note.id!!.toInt(), note.webLink)
            result = updateNoteWeblinkResult
//            var imageLink:String?=null
//            if(note.image!=null){
//                saveImageUseCase.invoke(Base64.getDecoder().decode(note.image))
//            }
            val updateNoteImageResult = noteRepository.updateNoteImage(note.id!!.toInt(), note.image)
            result = updateNoteImageResult
            if(note.color!=null){
                val updateNoteColorResult = noteRepository.updateNoteColor(note.id!!.toInt(), note.color!!)
                result = updateNoteColorResult
            }
            result ?: throw Exception("please fill fields first")
            socketRepository.sendNote(userToken, result)
        } catch (e: Exception) {
            throw Exception("unknown error ${e.message}")
        }
    }
}