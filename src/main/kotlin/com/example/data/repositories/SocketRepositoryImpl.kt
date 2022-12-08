package com.example.data.repositories

import com.example.domain.models.NoteModel
import com.example.domain.models.SocketUser
import com.example.domain.repositories.SocketRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class SocketRepositoryImpl :SocketRepository{
    private val joinedUsers = ConcurrentHashMap<String, SocketUser>()
    override fun joinSocket(userToken: String, socket: WebSocketSession) {
        try {
            joinedUsers[userToken] = SocketUser(
                userToken = userToken,
                socket = socket
            )
        } catch (e: Exception) {
            throw Exception("unknown error ${e.message}")
        }
    }

    override suspend fun sendNote(userToken:String,note:NoteModel){
        joinedUsers[userToken]!!.socket.send(Frame.Text(jacksonObjectMapper().writeValueAsString(note)))
    }
}