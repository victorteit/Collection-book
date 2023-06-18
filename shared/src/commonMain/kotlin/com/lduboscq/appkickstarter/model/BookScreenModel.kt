package com.lduboscq.appkickstarter.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import cafe.adriel.voyager.core.model.StateScreenModel


class BookScreenModel (private val repository: BookRepository)
    : StateScreenModel<BookScreenModel.State>(State.Init) {
    private val _bookList = MutableStateFlow<List<BookData>>(emptyList())
    val bookDatasState: StateFlow<List<BookData>> = _bookList.asStateFlow()
    val currentBook: MutableState<BookData?> = mutableStateOf(null)


    init {
        coroutineScope.launch {
            val bookDatas: Flow<List<BookData>> = repository.getAllBooksFlow(null)
            bookDatas.collect { books ->
                _bookList.value = books
            }
        }
    }

    fun clearFields() {
        currentBook.value = null
    }

    sealed class State {
        object Init : State()
        object Loading : State()
        sealed class Result: State() {
            class SingleResult(val bookData: BookData?) : Result()
            class MultipleResult(val bookDatas: MutableState<List<BookData>>) : Result()
            class MultipleResultList(val bookDatas: List<BookData>) : Result()
        }
    }
    fun getBook(bookName : String) {
        coroutineScope.launch {
        mutableState.value = State.Loading
        val book = repository.getBook(bookName)
        mutableState.value = State.Result.SingleResult(book)
        currentBook.value = book  // Update the currentBook state when a book is fetched
         }
    }
    fun getAllBooks(bookName : String?) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value = State.Result.MultipleResult(repository.getAllBooks(bookName))
        }
    }

    fun getAllBooksList(bookName: String?) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value = State.Result.MultipleResultList(repository.getAllBooksList(bookName))
        }
    }

    fun addBook(bookName: String, authorBook: String, coverBook: String, yearPublished: Int) {
        coroutineScope.launch {
            println("bookName = $bookName, authorBook = $authorBook, coverBook = $coverBook, yearPublished = $yearPublished")
            mutableState.value = BookScreenModel.State.Loading
            mutableState.value = BookScreenModel.State.Result.SingleResult(
                repository.addBook(
                BookData(
                    nameBook = bookName,
                    authorBook = authorBook,
                    coverBook = coverBook,
                    yearPublished = yearPublished,
                    book = null)
            ))
        }
    }

    fun updateBook(bookName: String, authorBook: String, coverBook: String, yearPublished: Int) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value = State.Result.SingleResult(repository.updateBook(bookName))
        }
    }

    fun deleteBook(bookName: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value = State.Result.SingleResult(repository.deleteBook(bookName))
        }
    }

}