package com.example.dz.data.mapper

import com.example.dz.data.remote.dto.gutendex.GutendexBookDto
import com.example.dz.data.remote.dto.openlibrary.OpenLibraryBookDto
import com.example.dz.data.remote.dto.openlibrary.OpenLibraryWorkDto
import com.example.dz.domain.model.Author
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

object BookMapper {
    fun fromOpenLibraryBook(dto: OpenLibraryBookDto): Book {
        val workId = dto.key.toOpenLibraryWorkId()

        return Book(
            id = workId.toOpenLibraryDomainId(),
            title = dto.title.orEmpty(),
            authors = dto.authorNames.mapIndexed { index, authorName ->
                Author(
                    id = dto.authorKeys.getOrNull(index) ?: authorName.toStableId(),
                    name = authorName
                )
            },
            coverUrl = dto.coverId?.let(::openLibraryCoverUrl),
            categories = dto.subjects.take(MAX_CATEGORIES).map(::categoryFromName),
            rating = dto.rating,
            reviewCount = dto.reviewCount,
            firstPublishYear = dto.firstPublishYear,
            pageCount = dto.pageCount,
            language = dto.languages.firstOrNull(),
            isFree = true
        )
    }

    fun fromOpenLibraryWork(dto: OpenLibraryWorkDto): Book {
        val workId = dto.key.toOpenLibraryWorkId()

        return Book(
            id = workId.toOpenLibraryDomainId(),
            title = dto.title.orEmpty(),
            coverUrl = dto.covers.firstOrNull()?.let(::openLibraryCoverUrl),
            description = dto.description.toPlainText(),
            categories = dto.subjects.take(MAX_CATEGORIES).map(::categoryFromName),
            firstPublishYear = dto.firstPublishDate?.firstFourDigitYear(),
            isFree = true
        )
    }

    fun fromGutendexBook(dto: GutendexBookDto): Book =
        Book(
            id = dto.id?.let { "gutenberg-$it" }.orEmpty(),
            title = dto.title.orEmpty(),
            authors = dto.authors.map { author ->
                Author(
                    id = author.name.orEmpty().toStableId(),
                    name = author.name.orEmpty()
                )
            },
            coverUrl = dto.formats.entries.firstOrNull { (type, _) ->
                type.startsWith("image/")
            }?.value,
            description = dto.summaries.firstOrNull(),
            categories = dto.subjects.take(MAX_CATEGORIES).map(::categoryFromName),
            language = dto.languages.firstOrNull(),
            isFree = true
        )

    fun openLibraryWorkIdFromDomainId(bookId: String): String =
        bookId.removePrefix(OPEN_LIBRARY_PREFIX)

    private fun String?.toOpenLibraryWorkId(): String =
        this?.removePrefix("/works/").orEmpty()

    private fun String.toOpenLibraryDomainId(): String =
        if (isBlank()) "" else "$OPEN_LIBRARY_PREFIX$this"

    private fun openLibraryCoverUrl(coverId: Int): String =
        "https://covers.openlibrary.org/b/id/$coverId-L.jpg"

    private fun categoryFromName(name: String): Category =
        Category(
            id = name.toStableId(),
            name = name
        )

    private fun JsonElement?.toPlainText(): String? =
        when (this) {
            is JsonPrimitive -> contentOrNull
            is JsonObject -> this["value"]?.jsonPrimitive?.contentOrNull
            else -> null
        }

    private fun String.firstFourDigitYear(): Int? =
        Regex("\\d{4}").find(this)?.value?.toIntOrNull()

    private fun String.toStableId(): String =
        trim()
            .lowercase()
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .ifBlank { "unknown" }

    private const val MAX_CATEGORIES = 6
    private const val OPEN_LIBRARY_PREFIX = "openlibrary-"
}
