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
            publisher = dto.publisher.firstOrNull(),
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

    /**
     * Folds the search row's numbers into the canonical work record.
     *
     * Neither OpenLibrary endpoint is enough on its own: the work has the description and subjects
     * but no rating, length or publisher, and the search row has those but a thinner description.
     * Work wins wherever both speak.
     */
    fun mergeOpenLibrary(work: Book, searchRow: Book?): Book {
        if (searchRow == null) return work
        return work.copy(
            authors = work.authors.ifEmpty { searchRow.authors },
            coverUrl = work.coverUrl ?: searchRow.coverUrl,
            categories = work.categories.ifEmpty { searchRow.categories },
            rating = searchRow.rating,
            reviewCount = searchRow.reviewCount,
            firstPublishYear = work.firstPublishYear ?: searchRow.firstPublishYear,
            pageCount = searchRow.pageCount,
            language = work.language ?: searchRow.language,
            publisher = searchRow.publisher,
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
            downloadCount = dto.downloadCount,
            isFree = true,
            textUrl = dto.plainTextUrl()
        )

    /**
     * Picks a readable plain-text download from the Gutendex `formats` map, preferring UTF-8 and
     * ignoring archived (`.zip`) entries. Returns null when no plain-text format is available.
     */
    private fun GutendexBookDto.plainTextUrl(): String? =
        formats.entries
            .filter { (type, url) -> type.startsWith("text/plain") && !url.endsWith(".zip") }
            .minByOrNull { (type, _) -> if (type.contains("utf-8", ignoreCase = true)) 0 else 1 }
            ?.value

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
