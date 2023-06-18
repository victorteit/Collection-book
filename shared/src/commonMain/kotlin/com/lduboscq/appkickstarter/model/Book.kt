package com.lduboscq.appkickstarter.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class Book: RealmObject {
    @PrimaryKey
    var _id: String = RealmUUID.random().toString()
    var nameBook: String = ""
    var authorBook: String = ""
    var coverBook: String = ""
    var yearPublished: Int = 0
}

data class BookData(
    var id: String? = null,
    var nameBook: String = "",
    var authorBook: String = "",
    var coverBook: String = "",
    var yearPublished: Int = 0,
    var book : Book?)
