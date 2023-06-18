package com.lduboscq.appkickstarter.model

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBook(bookData : String): BookData?
    suspend fun addBook(bookData: BookData): BookData?
    suspend fun updateBook(bookData: String ): BookData?
    suspend fun deleteBook(bookData: String): BookData?
    suspend fun getAllBooksList(bookName: String?): List<BookData>
    suspend fun getAllBooks(bookName: String?): MutableState<List<BookData>>
    suspend fun getAllBooksFlow(bookName: String?): Flow<List<BookData>>
}
