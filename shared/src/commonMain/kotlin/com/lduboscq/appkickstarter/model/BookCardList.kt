package com.lduboscq.appkickstarter.model

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BookCardList(bookDatas : List<BookData>, bookScreenModel: BookScreenModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        for (bookData in bookDatas) {
            BookCard(bookData, bookScreenModel)
        }
    }
}