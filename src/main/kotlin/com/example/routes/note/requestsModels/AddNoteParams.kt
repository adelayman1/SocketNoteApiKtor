package com.example.routes.note.requestsModels

data class AddNoteParams(
    var id:String?=null,
    var title: String?,
    var subtitle: String?,
    var description: String?,
    var color:Int?,
    var image: String?,
    var webLink: String?
)