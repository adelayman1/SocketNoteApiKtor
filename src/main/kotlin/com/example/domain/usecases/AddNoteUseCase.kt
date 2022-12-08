package com.example.domain.usecases

import com.example.domain.repositories.NoteRepository
import com.example.domain.repositories.SocketRepository
import com.example.domain.repositories.UserRepository
import com.example.routes.note.requestsModels.AddNoteParams
import java.text.SimpleDateFormat
import java.util.*

class AddNoteUseCase constructor(
    var noteRepository: NoteRepository,
    var userRepository: UserRepository,
    var socketRepository: SocketRepository
) {
    suspend operator fun invoke(userToken: String, note: AddNoteParams) {
        if (note.title.isNullOrBlank() || note.title!!.length < 3)
            throw Exception("title is not valid")
        if (note.description.isNullOrBlank() || note.description!!.length < 3)
            throw Exception("description is not valid")
        if (note.subtitle.isNullOrBlank())
            throw Exception("subtitle is not valid")
        if (userToken.isBlank())
            throw Exception("Authorization key not found")
        if (!userRepository.isUserTokenExist(userToken))
            throw Exception("Not Authorized")
//        var imageLink:String? = null
//        if(note.image!=null)
//            imageLink=saveImageUseCase(Base64.getDecoder().decode(note.image))

        try {
                val addNoteResult = noteRepository.addNote(
                    userToken = userToken,
                    title = note.title!!,
                    description = note.description!!,
                    subtitle = note.subtitle!!,
                    image = note.image,
                    date = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", Locale.getDefault()).format(Date()),
                    webLink = note.webLink,
                    color = note.color!!
                )
                socketRepository.sendNote(userToken, addNoteResult!!)
        } catch (e: Exception) {
            throw Exception("unknown error ${e.message}")
        }
    }
}