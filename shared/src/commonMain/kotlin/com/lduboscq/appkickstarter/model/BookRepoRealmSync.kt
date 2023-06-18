package com.lduboscq.appkickstarter.model

import io.realm.kotlin.Realm
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.SyncException
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.mongodb.sync.SyncSession

abstract class BookRepoRealmSync() : BookRepoRealm() {

    private val appServiceInstance by lazy {
        val configuration =
            AppConfiguration.Builder("application-0-xduvy").log(LogLevel.ALL)
                .build()
        App.create(configuration)
    }


    override suspend fun setupRealmSync() {
        val user =
            appServiceInstance.login(Credentials.apiKey("qEiQdMFGi7kPzRtgdK3ZpGARWyhu15HIDDQfg9tC3iAWopz1J9HfeQ9wjaqpfEu4"))

        println("Got Here")
        val config = SyncConfiguration.Builder(user, setOf(Book::class))
            .initialSubscriptions { realm ->
                add(
                    realm.query<Book>(
                        Book::class,
                        "_id == $0",
                        user.id
                    ),
                    name = "BookSub",
                    updateExisting = true
                )
            }
            .errorHandler { session: SyncSession, error: SyncException ->
                println("*************\n" + error)
            }
            .build()
        realm = Realm.open(config)
    }
}