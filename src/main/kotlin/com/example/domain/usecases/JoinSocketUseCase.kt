package com.example.domain.usecases

import com.example.domain.repositories.SocketRepository
import io.ktor.websocket.*

class JoinSocketUseCase constructor(var socketRepository: SocketRepository) {
    operator fun invoke(userToken: String, socket: WebSocketSession) {
        try {
            socketRepository.joinSocket(
                userToken = userToken,
                socket = socket
            )
        } catch (e: Exception) {
            throw Exception("unknown error ${e.message}")
        }
    }
}