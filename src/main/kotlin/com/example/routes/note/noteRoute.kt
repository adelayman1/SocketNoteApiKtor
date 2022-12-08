package com.example.routes.note

import com.example.domain.models.NoteSession
import com.example.domain.usecases.*
import com.example.routes.note.requestsModels.AddNoteParams
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.koin.ktor.ext.inject
import java.time.Duration

fun Application.noteRoute() {
    val joinSocketUseCase by inject<JoinSocketUseCase>()
    val addNoteUseCase by inject<AddNoteUseCase>()
    val updateNoteDetailsUseCase by inject<UpdateNoteDetailsUseCase>()
    val saveImageUseCase by inject<SaveImageUseCase>()
    val getAllUserNotesUseCase by inject<GetAllUserNotesUseCase>()
    val getNoteDetailsUseCase by inject<GetNoteDetailsUseCase>()
    val searchNoteUseCase by inject<SearchNoteUseCase>()
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/notes-socket") {
            val session = call.sessions.get<NoteSession>()
            if (session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Note Session Not Found"))
                return@webSocket
            }
            try {
                joinSocketUseCase(session.userToken, this)
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    var addNoteParams: AddNoteParams = jacksonObjectMapper().readValue(frame.readText())
                    if (addNoteParams.id == null) {
                        // add new note
                        addNoteUseCase(
                            session.userToken,
                            note = addNoteParams
                        )
                    } else {
                        // edit note
                        updateNoteDetailsUseCase(session.userToken, note = addNoteParams)
                    }

                }
            } catch (e: Exception) {
                println("xxxxxxxxxxxxxx${e.message}")
                call.respond(HttpStatusCode.BadGateway)
            } finally {
            }
        }
        route(path = "/notes") {
            get {
                val limit = call.request.queryParameters["limit"]?.toInt()
                val offset = call.request.queryParameters["offset"]?.toInt()
                val getNotesResult = getAllUserNotesUseCase(
                    userToken = call.request.header("Authorization"),
                    limit = limit,
                    offset = offset
                )
                call.respond(message = getNotesResult, status = getNotesResult.statuesCode)
            }
            get("/{note_id}") {
                val getNoteDetailsResult = getNoteDetailsUseCase(
                    userToken = call.request.header("Authorization"), noteID = call.parameters["note_id"]
                )
                call.respond(message = getNoteDetailsResult, status = getNoteDetailsResult.statuesCode)
            }
            get("/search") {
                val limit = call.request.queryParameters["limit"]?.toInt()
                val offset = call.request.queryParameters["offset"]?.toInt()
                val getSearchResult = searchNoteUseCase(
                    userToken = call.request.header("Authorization"),
                    searchWord = call.parameters["search_word"],
                    limit = limit,
                    offset = offset
                )
                call.respond(message = getSearchResult, status = getSearchResult.statuesCode)
            }
            post("/image") {
                val multipart = call.receiveMultipart()
                multipart.forEachPart { partData ->
                    when (partData) {
                        is PartData.FormItem -> Unit
                        is PartData.FileItem -> {
                            var saveImage = saveImageUseCase(call.request.header("Authorization"), partData)
                            call.respond(saveImage.statuesCode, saveImage)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}