package com.example.plugins

import com.example.domain.models.NoteSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {
    install(Sessions){
        cookie<NoteSession>("noteSession")
    }
    intercept(ApplicationCallPipeline.Plugins){
        if(call.sessions.get<NoteSession>() ==null){
            val userToken = call.parameters["userToken"]
            call.sessions.set(NoteSession(userToken?:"NF"))
        }
    }
//    authentication {
//            jwt {
//                val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
//                realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
//                verifier(
//                    JWT
//                        .require(Algorithm.HMAC256("secret"))
//                        .withAudience(jwtAudience)
//                        .withIssuer(this@configureSecurity.environment.config.property("jwt.domain").getString())
//                        .build()
//                )
//                validate { credential ->
//                    if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
//                }
//            }
//        }

}
