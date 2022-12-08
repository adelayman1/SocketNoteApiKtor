package com.example.domain.repositories

import com.example.domain.models.NoteModel
import io.ktor.websocket.*

interface SocketRepository {
    fun joinSocket(userToken: String, socket: WebSocketSession)
    suspend fun sendNote(userToken:String,note: NoteModel)
}