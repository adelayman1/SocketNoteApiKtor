package com.example.data.models

import com.example.domain.models.NoteModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Notes : Table() {
    val noteId = integer("noteID").autoIncrement()
    var userToken = varchar("userToken", 128)
    var title = varchar("title", 128)
    var subtitle = varchar("subtitle", 64)
    var description = varchar("description", 1024)
    var date = varchar("date", 64)
    var color = integer("color")
    var image = varchar("image", 1024).nullable()
    var webLink = varchar("link", 1024).nullable()
    override val primaryKey: PrimaryKey = PrimaryKey(noteId)
}

fun toNoteModel(row: ResultRow) = NoteModel(
    noteId = row[Notes.noteId].toString(),
    userToken = row[Notes.userToken],
    title = row[Notes.title],
    description = row[Notes.description],
    subtitle = row[Notes.subtitle],
    date = row[Notes.date].toString(),
    image = row[Notes.image],
    webLink = row[Notes.webLink],
    color = row[Notes.color]
)

fun toLocalDateTime(localDateTime: String): LocalDateTime {
    return LocalDateTime.parse(localDateTime)
}