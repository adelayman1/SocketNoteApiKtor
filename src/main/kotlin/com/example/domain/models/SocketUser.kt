package com.example.domain.models

import io.ktor.websocket.*

data class SocketUser(val userToken:String,val socket:WebSocketSession)