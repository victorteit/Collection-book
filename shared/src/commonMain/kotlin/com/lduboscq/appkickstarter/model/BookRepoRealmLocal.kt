package com.lduboscq.appkickstarter.model

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class BookRepoRealmLocal() : BookRepoRealm(), BookRepository {
    override suspend fun setupRealmSync() {
        val config = RealmConfiguration.Builder(setOf(Book::class))
//            .inMemory()
            .build()
        realm = Realm.open(config)
    }

}
