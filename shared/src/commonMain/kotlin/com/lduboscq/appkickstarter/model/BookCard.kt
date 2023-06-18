@file:OptIn(ExperimentalMaterialApi::class)

package com.lduboscq.appkickstarter.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lduboscq.appkickstarter.ui.RemoteImage
import com.lduboscq.appkickstarter.model.BookRepoRealmSync


@Composable
fun BookCard(bookData: BookData?, bookScreenModel: BookScreenModel) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .size(width = 280.dp, height = 200.dp),
        elevation = 4.dp,
    ) {
        if (bookData != null) {
            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
                    ){
                Text(
                    bookData.nameBook,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                RemoteImage(
                    url = bookData.coverBook,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clickable (onClick = {navigator.push(screenRouter(Route.Detail(bookData)))})
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { }) {
                        Text("Update")
                    }
                    Button(onClick = { bookScreenModel.deleteBook(bookData.nameBook)
                    }) {
                        Text("Delete")
                    }
                }
            }
        } else {
            Text("No book result")
            Spacer(Modifier.height(8.dp))
        }
    }
}

