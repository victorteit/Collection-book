@file:OptIn(ExperimentalMaterial3Api::class)

package com.lduboscq.appkickstarter.model

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

class BookScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel() { BookScreenModel(BookRepoRealmLocal()) }
        val state by screenModel.state.collectAsState()
        val bookDatasState by screenModel.bookDatasState.collectAsState()

        var nameBook by remember { mutableStateOf("") }
        var authorBook by remember { mutableStateOf("") }
        var yearPublished by remember { mutableStateOf(0) }
        var coverBook by remember { mutableStateOf("") }

        val currentBook by screenModel.currentBook

        val showSnackbar = remember { mutableStateOf(false) }

        if (showSnackbar.value) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { showSnackbar.value = false }) {
                        Text(text = "DISMISS")
                    }
                }
            ) {
                Text("Book name and Author name cannot be empty.")
            }
        }

        LaunchedEffect(currentBook) {
            if (currentBook != null) {
                nameBook = currentBook!!.nameBook ?: ""
                authorBook = currentBook!!.authorBook ?: ""
                coverBook = currentBook!!.coverBook ?: ""
                yearPublished = currentBook!!.yearPublished
            } else {
                nameBook = ""
                authorBook = ""
                coverBook = ""
                yearPublished = 0
            }
        }


        Scaffold(
            topBar = { CenterAlignedTopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text = "My Book Collection",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF3700B3),
                    titleContentColor = Color.White
                )
            )
        }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    label = { androidx.compose.material3.Text(text = "Book name") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 4.dp),
                    value = nameBook,
                    onValueChange = { nameBook = it },
                    textStyle = TextStyle(fontWeight = FontWeight.Bold)
                )

                OutlinedTextField(
                    label = { androidx.compose.material3.Text(text = "Author name") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 4.dp),
                    value = authorBook,
                    onValueChange = { authorBook = it },
                    textStyle = TextStyle(fontWeight = FontWeight.Bold)
                )

                OutlinedTextField(
                    label = { androidx.compose.material3.Text(text = "Published year") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 4.dp),
                    value = yearPublished.toString(),
                    onValueChange = { value -> yearPublished = value.toIntOrNull() ?: 0 },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontWeight = FontWeight.Bold)
                )
                OutlinedTextField(
                    label = { androidx.compose.material3.Text(text = "Put the link for cover") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 4.dp),
                    value = coverBook,
                    onValueChange = { coverBook = it },
                    textStyle = TextStyle(fontWeight = FontWeight.Bold)
                )
                Row {
                    Button(onClick = {
                        if (screenModel.currentBook.value != null) {
                            screenModel.updateBook(nameBook, authorBook, coverBook, yearPublished)
                        } else {
                            screenModel.addBook(nameBook, authorBook, coverBook, yearPublished)
                        }
                    }) {
                        Text(if (screenModel.currentBook.value != null) "Update Book" else "Add Book")
                    }
                    Spacer(Modifier.width(25.dp))
                    Button(onClick = {
                        screenModel.getBook(nameBook)
                    })
                    {
                        Text("Find Book")
                    }
                    Spacer(Modifier.width(25.dp))
                    Button(onClick = { screenModel.clearFields() }) {
                        Text("Clear")
                    }
                }

                Divider(color = Color.Black)

                OutlinedButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        screenModel.getAllBooks(null)
                    }) {
                    androidx.compose.material3.Text(text = "Show list of books")
                }

                Divider(color = Color.Black)

                if (state is BookScreenModel.State.Result.SingleResult) {
                    if ((state as BookScreenModel.State.Result.SingleResult).bookData == null) {
                        Text("Not found.  Please try again.")
                    } else {
                        Text("The results of the action are:")
                        BookCard(
                            bookData = (state as BookScreenModel.State.Result.SingleResult).bookData,
                            bookScreenModel = screenModel
                        )
                    }
                } else if (state is BookScreenModel.State.Result.MultipleResult) {
                    if ((state as BookScreenModel.State.Result.MultipleResult).bookDatas == null) {
                        Text("Not found.  Please try again.")
                    } else {
                        Text("The results of the action are:")
                        // Access the fetched frogs from the state
                        val bookDatas =
                            (state as BookScreenModel.State.Result.MultipleResult).bookDatas

                        BookCardList(bookDatas.value, bookScreenModel = screenModel)
                    }
                } else if (state is BookScreenModel.State.Result.MultipleResultList) {
                    if ((state as BookScreenModel.State.Result.MultipleResultList).bookDatas == null) {
                        Text("Not found.  Please try again.")
                    } else {
                        Text("The results of the action are:")
                        val bookDatas =
                            (state as BookScreenModel.State.Result.MultipleResultList).bookDatas

                        BookCardList(bookDatas, bookScreenModel = screenModel)
                    }
                }
                BookCardList(bookDatas = bookDatasState, bookScreenModel = screenModel)
            }
        }
    }
}