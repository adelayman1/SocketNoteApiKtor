package com.example.plugins

import com.example.routes.user.userRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {

    routing {
        userRoute()
        static{
            files(".")
        }
    }
}
