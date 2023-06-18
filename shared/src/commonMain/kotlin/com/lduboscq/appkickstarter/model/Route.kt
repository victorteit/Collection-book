package com.lduboscq.appkickstarter.model

import cafe.adriel.voyager.core.screen.Screen

sealed class Route {
    data class Detail(val book: BookData?) : Route()
}

fun screenRouter(screen: Route) : Screen {
    return when (screen) {
        is Route.Detail -> {
            BookDetailedScreenContent(book = screen.book)
        }
    }
}