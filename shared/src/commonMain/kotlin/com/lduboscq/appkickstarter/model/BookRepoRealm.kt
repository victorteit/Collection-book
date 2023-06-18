package com.lduboscq.appkickstarter.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

abstract class BookRepoRealm : BookRepository {
    lateinit var realm: Realm
    private var currentJob: Job? = null

    abstract suspend fun setupRealmSync()

    private fun cancelCurrentJob() {
        currentJob?.cancel()
        currentJob = null
}
    suspend fun convertToBookData(book: Book?): BookData? {
        if (!this::realm.isInitialized) {
            setupRealmSync()
        }

        var bookData: BookData? = null
        realm.write {
            if (book != null) {
                bookData = BookData(
                    id = findLatest(book)!!._id,
                    nameBook = findLatest(book)!!.nameBook,
                    authorBook = findLatest(book)!!.authorBook,
                    coverBook = findLatest(book)!!.coverBook,
                    yearPublished = findLatest(book)!!.yearPublished,
                    book = book
                )
            }
        }
        return bookData
    }

    override suspend fun getBook(nameBook: String): BookData? {
        if (!this::realm.isInitialized) {
            setupRealmSync()
        }
        cancelCurrentJob()
        // Search equality on the primary key field name
        val book: Book? = realm.query<Book>(Book::class, "nameBook = \"$nameBook\"").first().find()
        return convertToBookData(book)
    }



    override suspend fun addBook(bookData: BookData): BookData? {
        if (!this::realm.isInitialized){
            setupRealmSync()
        }

        var book2: Book? = null
        realm.write {
            book2 = this.copyToRealm(Book().apply {
                _id = bookData.id ?: RealmUUID.random().toString()
                nameBook = bookData.nameBook
                authorBook = bookData.authorBook
                coverBook = bookData.coverBook
                yearPublished = bookData.yearPublished
            })
        }
        return convertToBookData(book2)
    }

    override suspend fun getAllBooksList(bookName: String?): List<BookData>
    {
        if (!this::realm.isInitialized) {
            setupRealmSync()
        }
        cancelCurrentJob()
        val books: List<Book> =
            if (bookName == null)
                realm.query<Book>(Book::class).find()
            else
                realm.query<Book>(Book::class, "nameBook = \"$bookName\"").find()

        val bookDataList = books.map { book ->
            BookData(
                id = book._id,
                nameBook = book.nameBook,
                authorBook = book.authorBook,
                coverBook = book.coverBook,
                yearPublished = book.yearPublished,
                book = book
            )
        }
        return bookDataList
    }

    override suspend fun getAllBooks(bookName: String?): MutableState<List<BookData>> {
        if (!this::realm.isInitialized) {
            setupRealmSync()
        }
        val booksState: MutableState<List<BookData>> = mutableStateOf(emptyList())
        cancelCurrentJob()
        currentJob = CoroutineScope(Dispatchers.Default).launch {
            val bookFlow: Flow<ResultsChange<Book>> =
                if (bookName == null)
                    realm.query<Book>(Book::class).find().asFlow()
                else
                    realm.query<Book>(Book::class, "name = \"$bookName\"").find().asFlow()

            bookFlow.collect { resultsChange: ResultsChange<Book> ->
                val books = resultsChange.list
                val bookDataFlow = books.map { book ->
                    BookData(
                        id = book._id,
                        nameBook = book.nameBook,
                        authorBook = book.authorBook,
                        coverBook = book.coverBook,
                        yearPublished = book.yearPublished,
                        book = book
                    )
                }
                booksState.value = bookDataFlow
            }
        }

        return booksState
    }


    override suspend fun getAllBooksFlow(nameBook: String?): Flow<List<BookData>> {
        if (!this::realm.isInitialized) {
            setupRealmSync()
        }
        var booksState: Flow<List<BookData>> = flowOf(emptyList())
        cancelCurrentJob()

        currentJob = CoroutineScope(Dispatchers.Default).launch {
            val bookFlow: Flow<ResultsChange<Book>> =
                if (nameBook == null)
                    realm.query<Book>(Book::class).find().asFlow()
                else
                    realm.query<Book>(Book::class, "name = \"$nameBook\"").find().asFlow()

            bookFlow.collect { resultsChange: ResultsChange<Book> ->
                val books = resultsChange.list
                val bookDataFlow = books.map { book ->
                    BookData(
                        id = book._id,
                        nameBook = book.nameBook,
                        authorBook = book.authorBook,
                        coverBook = book.coverBook,
                        yearPublished = book.yearPublished,
                        book = book
                    )
                }
                booksState = flowOf(bookDataFlow)
            }
        }

        return booksState
    }


    /** Updates the frog that is the first match to the given name
     *   Placeholder operation:  Just adds to the name in fixed manner
     *   TODO: Make this accept variety of parameters reflecting the user's desired changes
     *   Returns an updated FrogData if a match is found, null otherwise.
     */
    override suspend fun updateBook(nameBook: String): BookData? {
        if (!this::realm.isInitialized) {
            setupRealmSync()
        }
        var bookData: BookData? = null
        try {
            // Search equality on the primary key field name
            val book: Book? =
                realm.query<Book>(Book::class, "nameBook = \"$nameBook\"").first().find()

            realm.write {
                if (book != null) {
                    findLatest(book)!!.authorBook = findLatest(book)!!.authorBook
                }
            }
            bookData = convertToBookData(book)
        } catch (e: Exception) {
            println(e.message)
        }

        return bookData
    }

    override suspend fun deleteBook(bookData: String): BookData? {
        if (!this::realm.isInitialized) {
            setupRealmSync()
        }
        var book2: BookData? = null
        try {
            val book: Book? =
                realm.query<Book>(Book::class, "nameBook = \"$bookData\"").first().find()

            realm.write {
                if (book != null) {
                    book2 = BookData(
                        id = book._id,
                        nameBook = book.nameBook,
                        authorBook = book.authorBook,
                        coverBook = book.coverBook,
                        yearPublished = book.yearPublished,
                        book = book
                    )
                    findLatest(book)
                        ?.also { delete(it) }
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }

        return book2
    }
}


