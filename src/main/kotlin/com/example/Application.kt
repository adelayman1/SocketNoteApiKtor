package com.example

import com.example.data.source.DatabaseFactory
import com.example.di.modules.mainModule
import com.example.plugins.configureMonitoring
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.routes.note.noteRoute
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 4040, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin){
        modules(mainModule)
    }
    val databaseFactory by inject<DatabaseFactory>()
    databaseFactory.init()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    noteRoute()
}
